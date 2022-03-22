package com.deploymentsys.tcp.protocol.request;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class EmptyDirRequestPacket extends Packet {
	/**
	 * 需要删除的文件路径，可以是文件或目录
	 */
	private String filePathForEmpty;

	public String getFilePathForEmpty() {
		return filePathForEmpty;
	}

	public void setFilePathForEmpty(String filePathForEmpty) {
		this.filePathForEmpty = filePathForEmpty;
	}

	/**
	 * 操作结果
	 */
	private boolean done;

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	@Override
	public Byte getCommand() {
		return Command.EMPTY_DIR_REQUEST;
	}
}
