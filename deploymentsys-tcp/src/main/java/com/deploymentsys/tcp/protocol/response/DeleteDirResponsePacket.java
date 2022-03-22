package com.deploymentsys.tcp.protocol.response;

import com.deploymentsys.tcp.protocol.command.Command;
import com.deploymentsys.tcp.protocol.request.DeleteDirRequestPacket;

public class DeleteDirResponsePacket extends DeleteDirRequestPacket {

	@Override
	public Byte getCommand() {
		return Command.DELETE_DIR_RESPONSE;
	}
}
