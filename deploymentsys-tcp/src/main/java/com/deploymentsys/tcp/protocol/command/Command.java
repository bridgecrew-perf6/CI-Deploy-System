package com.deploymentsys.tcp.protocol.command;

public interface Command {
	/**
	 * 文件传输请求
	 */
	Byte FILE_TRANSFER_REQUEST = 1;
	/**
	 * 文件传输响应
	 */
	Byte FILE_TRANSFER_RESPONSE = 2;

	/**
	 * 文件拷贝请求
	 */
	Byte FILE_COPY_REQUEST = 3;
	/**
	 * 文件拷贝响应
	 */
	Byte FILE_COPY_RESPONSE = 4;

	/**
	 * 文件/目录删除请求
	 */
	Byte DELETE_DIR_REQUEST = 5;
	/**
	 * 文件/目录删除响应
	 */
	Byte DELETE_DIR_RESPONSE = 6;

	/**
	 * 清空目录请求
	 */
	Byte EMPTY_DIR_REQUEST = 7;
	/**
	 * 清空目录响应
	 */
	Byte EMPTY_DIR_RESPONSE = 8;

	/**
	 * 执行命令行请求
	 */
	Byte EXEC_SHELL_REQUEST = 9;
	/**
	 * 执行命令行响应
	 */
	Byte EXEC_SHELL_RESPONSE = 10;
}
