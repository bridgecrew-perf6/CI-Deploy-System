package com.deploymentsys.beans.constants;

/**
 * 流程配置的目标机器的任务执行类型
 * 
 * @author adminjack
 *
 */
public interface TargetServerToDoType {
	/**
	 * 文件传输
	 */
	String FILE_TRANSFER = "文件传输";
	/**
	 * 文件拷贝：如果sourceFilePath是一个文件，那就将文件拷贝到目标目录；如果sourceFilePath是一个文件夹，那就将此文件夹下面的东西拷贝到目标目录
	 */
	String FILE_COPY = "文件拷贝";
	/**
	 * 删除目录
	 */
	String DELETE_DIR = "删除目录";
	/**
	 * 清空目录
	 */
	String EMPTY_DIR = "清空目录";
	/**
	 * 执行命令行
	 */
	String REMOTE_EXEC_SHELL = "执行命令行";
}
