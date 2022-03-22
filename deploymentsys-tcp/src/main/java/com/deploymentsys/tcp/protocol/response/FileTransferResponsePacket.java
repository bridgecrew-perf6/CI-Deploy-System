package com.deploymentsys.tcp.protocol.response;

import com.deploymentsys.tcp.protocol.command.Command;
import com.deploymentsys.tcp.protocol.request.FileTransferRequestPacket;

public class FileTransferResponsePacket extends FileTransferRequestPacket {

	@Override
	public Byte getCommand() {
		return Command.FILE_TRANSFER_RESPONSE;
	}
}
