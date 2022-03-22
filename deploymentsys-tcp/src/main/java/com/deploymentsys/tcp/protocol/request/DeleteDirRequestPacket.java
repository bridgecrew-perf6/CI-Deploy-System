package com.deploymentsys.tcp.protocol.request;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class DeleteDirRequestPacket extends Packet {
	/**
	 * 需要删除的文件路径，可以是文件或目录
	 */
	private String filePathForDelete;

	public String getFilePathForDelete() {
		return filePathForDelete;
	}

	public void setFilePathForDelete(String filePathForDelete) {
		this.filePathForDelete = filePathForDelete;
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
		return Command.DELETE_DIR_REQUEST;
	}
}
