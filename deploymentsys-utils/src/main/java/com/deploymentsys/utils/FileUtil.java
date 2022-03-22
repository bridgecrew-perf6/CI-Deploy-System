package com.deploymentsys.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import com.deploymentsys.beans.constants.SysConstants;

public class FileUtil {

	/**
	 * 获取文件的md5值
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static String getFileMd5(String filePath) throws FileNotFoundException, IOException {
		FileInputStream fis = new FileInputStream(new File(filePath));
		String md5 = DigestUtils.md5Hex(fis);
		fis.close();
		return md5;
	}

	/**
	 * 文件路径的简单处理：把\替换成/；另外统一检查后面有没有带 / ，如果有则去掉最后的/
	 * 
	 * @return
	 */
	public static String filePathConvert(String filePath) {
		String result = filePath.replaceAll("\\\\", SysConstants.FILE_SEPARATOR);
		if (SysConstants.FILE_SEPARATOR.equals(String.valueOf(result.charAt(result.length() - 1)))) {
			result = result.substring(0, result.length() - 1);
		}
		return result;
	}

	/**
	 * 文件路径格式验证，windows、linux至少要符合一种格式；在这个系统里面，是先验证再替换分隔符 filePathConvert
	 * 验证windows的路径格式：^[a-zA-Z]:(((\\(?! )[^/:*?<>\""|\\]+)+\\?)|(\\)?)\s*$ 验证linux
	 * 的路径格式：^(/[^/ ]*)+/?$
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean filePathValidate(String filePath) {
		String patternWin1 = "^[a-zA-Z]:(((\\\\(?! )[^/:*?<>\\\"\"|\\\\]+)+\\\\?)|(\\\\)?)\\s*$";
		String patternWin2 = "^[a-zA-Z]:(/[^/ ]*)+/?$";
		String patternLinux = "^(/[^/ ]*)+/?$";

		boolean isMatch1 = Pattern.matches(patternWin1, filePath);
		boolean isMatch2 = Pattern.matches(patternWin2, filePath);
		boolean isMatch3 = Pattern.matches(patternLinux, filePath);
		return isMatch1 || isMatch2 || isMatch3;
	}

	public static void getFileList(String rootPath) {
		File file = new File(rootPath);
		if (!file.exists()) {
			System.out.println("目录不存在");
			return;
		}
		if (!file.isDirectory()) {
			System.out.println("配置的路径不是一个文件夹");
			return;
		}

		File[] fileList = file.listFiles();

		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isFile()) {
				String fileName = fileList[i].getName();
				String fileName2 = fileList[i].getAbsolutePath();
				String fileName3 = fileList[i].getPath();
				String fileName4 = filePathConvert(fileList[i].getAbsolutePath());
				System.out.println("文件：" + fileName + " == " + fileName2 + " == " + fileName3 + " == " + fileName4);
			}

			if (fileList[i].isDirectory()) {
				String fileName = fileList[i].getName();
				String fileName2 = fileList[i].getAbsolutePath();
				String fileName3 = fileList[i].getPath();
				System.out.println("目录：" + fileName + " == " + fileName2 + " == " + fileName3);
			}
		}
	}

	/**
	 * 递归获取指定文件目录的大小
	 * 
	 * @param file
	 * @return
	 */
	public static long getTotalSizeOfFilesInDir(File file) {
		if (file.isFile()) {
			return file.length();
		}

		File[] children = file.listFiles();
		long total = 0;
		if (children != null && children.length > 0) {
			for (File child : children) {
				total += getTotalSizeOfFilesInDir(child);
			}
		}

		return total;
	}

	/**
	 * 递归获取指定目录下的文件数量总和
	 * 
	 * @param file
	 * @return
	 */
	public static int getTotalFilesInDir(File file) {
		if (file.isFile()) {
			return 1;
		}

		File[] children = file.listFiles();
		int total = 0;
		if (children != null && children.length > 0) {
			for (File child : children) {
				total += getTotalFilesInDir(child);
			}
		}

		return total;
	}

	/**
	 * 递归获取指定目录下的文件数量总和（排除某些指定后缀的文件）
	 * 
	 * @param file
	 * @param suffixesForExclude
	 * @return
	 */
	public static int getTotalFilesInDirExcludeBySuffix(File file, String suffixesForExclude) {
		if (file.isFile()) {
			if (suffixesForExclude.indexOf(file.getName().substring(file.getName().lastIndexOf(".") + 1)) < 0) {
				return 1;
			} else {
				return 0;
			}
		}

		File[] children = file.listFiles();
		int total = 0;
		if (children != null && children.length > 0) {
			for (File child : children) {
				total += getTotalFilesInDirExcludeBySuffix(child, suffixesForExclude);
			}
		}

		return total;
	}

	public static void zipFiles(String[] fileAbsolutePaths, String zipFileAbsolutePath) throws IOException {
		byte[] buffer = new byte[1024];

		String strZipName = zipFileAbsolutePath;// 生成文件的目录及命令

		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(strZipName));

		File[] files = new File[fileAbsolutePaths.length];
		// 需要同时下载的多个文件，这些文件必须都存在，而且不能是文件夹
		for (int i = 0; i < fileAbsolutePaths.length; i++) {
			files[i] = new File(fileAbsolutePaths[i]);
		}

		for (int i = 0; i < files.length; i++) {
			FileInputStream fis = new FileInputStream(files[i]);
			out.putNextEntry(new ZipEntry(files[i].getName()));

			int len;
			while ((len = fis.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}

			out.closeEntry();
			fis.close();
		}

		out.close();
	}

	public static void main(String[] args) throws IOException {
		String targetDir = "D:/deploy-sys-temp/前端资源02/aaa";
		// String sourceDir="D:\\test\\www.aa.com";
		String sourceDir = "D:\\test\\3.xlsx";
		String sourceFile = "D:/test/www.aa.com/配置文件 - 副本.config";
		try {
			// 目录拷贝
			//FileUtils.copyDirectory(new File(sourceDir), new File(targetDir));

			// 把某个文件拷贝到某个目录下
			 //FileUtils.copyFileToDirectory(new File(sourceFile), new File(targetDir));
			 
			//清空目录
			 FileUtils.cleanDirectory(new File(sourceDir));
			 
			//删除文件或目录，需要做非空校验
			 //FileUtils.forceDelete(new File(sourceDir));
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

		String aa = "/新建文件夹/1111.docx";
		System.out.println(aa.substring(0, aa.lastIndexOf("/")));
		System.out.println(aa.substring(aa.lastIndexOf("/") + 1));

		System.out.println("测试： " + "".indexOf("xml"));

		String zipFileAbsolutePath = "D:\\test\\test.zip";
		String[] filePaths = { "D:/test/2.txt", "D:/test/1.exe", "D:/test/3.xlsx" };
		//zipFiles(filePaths, zipFileAbsolutePath);

		System.out.println("end");
	}
}
