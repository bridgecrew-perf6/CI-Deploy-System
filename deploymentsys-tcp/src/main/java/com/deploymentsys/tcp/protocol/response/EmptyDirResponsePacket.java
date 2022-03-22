package com.deploymentsys.tcp.protocol.response;

import com.deploymentsys.tcp.protocol.command.Command;
import com.deploymentsys.tcp.protocol.request.EmptyDirRequestPacket;

public class EmptyDirResponsePacket extends EmptyDirRequestPacket {

	@Override
	public Byte getCommand() {
		return Command.EMPTY_DIR_RESPONSE;
	}
}
