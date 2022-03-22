package com.deploymentsys.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;

public class ExecShellUtils {

	public static String execShell(String shell) throws Exception {
		String charsetName;
		String os = System.getProperty("os.name");

		// 简单判断，实际上并不只有这两种操作系统
		if (os.toLowerCase().startsWith("win")) {
			shell = MessageFormat.format("cmd /c \"{0}\"", shell);
			charsetName = "gbk";
		} else if (os.toLowerCase().startsWith("linux")) {
			shell = MessageFormat.format("/bin/sh -c \"{0}\"", shell);
			charsetName = "utf-8";
		} else {
			throw new Exception("不支持的操作系统类型！");
		}

		StringBuilder execResult = new StringBuilder();

		Process process;
		Runtime runtime = Runtime.getRuntime();
		process = runtime.exec(shell);

		InputStream is;
		InputStreamReader isr;
		BufferedReader br;

		// 打印执行的输出结果
		is = process.getInputStream();
		isr = new InputStreamReader(is, charsetName); // windows下用gbk：解决输出乱码；linux下改用utf-8
		br = new BufferedReader(isr);
		String line;
		while ((line = br.readLine()) != null) {
			execResult.append(line + "\n");
		}

		return execResult.toString();
	}

}
