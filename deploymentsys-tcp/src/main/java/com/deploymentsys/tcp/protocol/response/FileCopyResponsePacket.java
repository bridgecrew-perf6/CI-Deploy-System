package com.deploymentsys.tcp.protocol.response;

import com.deploymentsys.tcp.protocol.command.Command;
import com.deploymentsys.tcp.protocol.request.FileCopyRequestPacket;

public class FileCopyResponsePacket extends FileCopyRequestPacket {

	@Override
	public Byte getCommand() {
		return Command.FILE_COPY_RESPONSE;
	}
}
