package com.deploymentsys.tcp.protocol.request;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class FileTransferRequestPacket extends Packet {
	private String sourceFilePath;
	private String fileName;// 文件名
	private String targetDir;// 文件存放的目标文件夹
	private String md5;// 文件md5值
	private long startPos;// 开始位置
	private byte[] bytes;// 文件字节数组
	private long endPos;// 结尾位置
	/**
	 * 文件总长度
	 */
	private long fileLength;
	/**
	 * 文件md5值验证结果
	 */
	private boolean md5Validate;

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public long getStartPos() {
		return startPos;
	}

	public void setStartPos(long startPos) {
		this.startPos = startPos;
	}

	public long getEndPos() {
		return endPos;
	}

	public void setEndPos(long endPos) {
		this.endPos = endPos;
	}

	public boolean isMd5Validate() {
		return md5Validate;
	}

	public void setMd5Validate(boolean md5Validate) {
		this.md5Validate = md5Validate;
	}

	public long getFileLength() {
		return fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public Byte getCommand() {
		return Command.FILE_TRANSFER_REQUEST;
	}
}
