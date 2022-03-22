package com.deploymentsys.tcp.protocol.request;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

public class MessageRequestPacket extends Packet {
	private int toUserId;
	private String message;

	public MessageRequestPacket() {

	}

	public MessageRequestPacket(int toUserId, String message) {
		super();
		this.toUserId = toUserId;
		this.message = message;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Byte getCommand() {
		return 0;
	}
}
