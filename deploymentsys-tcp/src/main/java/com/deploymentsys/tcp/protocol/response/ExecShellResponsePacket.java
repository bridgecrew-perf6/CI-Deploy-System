package com.deploymentsys.tcp.protocol.response;

import com.deploymentsys.tcp.protocol.command.Command;
import com.deploymentsys.tcp.protocol.request.ExecShellRequestPacket;

public class ExecShellResponsePacket extends ExecShellRequestPacket {

	@Override
	public Byte getCommand() {
		return Command.EXEC_SHELL_RESPONSE;
	}
}
