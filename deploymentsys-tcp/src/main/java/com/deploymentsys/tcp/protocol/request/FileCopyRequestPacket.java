package com.deploymentsys.tcp.protocol.request;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class FileCopyRequestPacket extends Packet {
	private String sourceFilePath;
	private String targetDir;// 文件存放的目标文件夹

	/**
	 * 文件拷贝操作结果
	 */
	private boolean done;

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getSourceFilePath() {
		return sourceFilePath;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public String getTargetDir() {
		return targetDir;
	}

	public void setTargetDir(String targetDir) {
		this.targetDir = targetDir;
	}

	@Override
	public Byte getCommand() {
		return Command.FILE_COPY_REQUEST;
	}
}
