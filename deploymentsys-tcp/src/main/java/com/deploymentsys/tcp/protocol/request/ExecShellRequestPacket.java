package com.deploymentsys.tcp.protocol.request;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class ExecShellRequestPacket extends Packet {
	/**
	 * 需要执行的命令行
	 */
	private String shell;

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
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
		return Command.EXEC_SHELL_REQUEST;
	}
}
