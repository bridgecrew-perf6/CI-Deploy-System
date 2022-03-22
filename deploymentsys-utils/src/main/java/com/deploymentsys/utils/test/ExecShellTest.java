package com.deploymentsys.utils.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.UUID;

import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.MD5;

public class ExecShellTest {

	public static void main(String[] args) {
		// String shell = "D:\\apache-tomcat-9.0.0.M11\\bin\\startup.bat";
		// String shell = "netstat -aon|findstr \"8080\"";
		String shell = "d: && cd D:\\eclipse_workspace\\deploymentsys-root\\deploymentsys-tcp-server\\target\\deploymentsys-tcp-server-0.0.1-SNAPSHOT-bin\\deploymentsys-tcp-server-0.0.1-SNAPSHOT && startup-win-server.cmd";

		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					System.out.println(execShell(shell));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		t.start();

		System.out.println(666);
	}

	private static String execShell(String shell) throws Exception {
		String charsetName;
		String os = System.getProperty("os.name");

		// 简单判断，实际上并不只有这两种操作系统
		if (os.toLowerCase().startsWith("win")) {
			System.out.println("这个是windows系统");
			shell = MessageFormat.format("cmd /c \"{0}\"", shell);
			charsetName = "gbk";
		} else {
			System.out.println("这个是linux系统");
			shell = MessageFormat.format("/bin/sh -c \"{0}\"", shell);
			charsetName = "utf-8";
		}

		// boolean execResult = false;
		StringBuilder execResult = new StringBuilder();

		Process process;

		Runtime runtime = Runtime.getRuntime();

		System.out.println("开始执行。。。");
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
		// execResult = true;

		return execResult.toString();
	}
}
