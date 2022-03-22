package com.deploymentsys.tcp.protocol.response;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class MessageResponsePacket extends Packet {

	private String message;

	public MessageResponsePacket() {

	}

	public MessageResponsePacket(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Byte getCommand() {

		return -1;
	}
}
