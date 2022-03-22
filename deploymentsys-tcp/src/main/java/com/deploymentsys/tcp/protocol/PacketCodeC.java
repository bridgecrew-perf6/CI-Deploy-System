package com.deploymentsys.tcp.protocol;

import java.util.HashMap;
import java.util.Map;

import com.deploymentsys.tcp.protocol.command.Command;
import com.deploymentsys.tcp.protocol.request.DeleteDirRequestPacket;
import com.deploymentsys.tcp.protocol.request.EmptyDirRequestPacket;
import com.deploymentsys.tcp.protocol.request.ExecShellRequestPacket;
import com.deploymentsys.tcp.protocol.request.FileCopyRequestPacket;
import com.deploymentsys.tcp.protocol.request.FileTransferRequestPacket;
import com.deploymentsys.tcp.protocol.response.DeleteDirResponsePacket;
import com.deploymentsys.tcp.protocol.response.EmptyDirResponsePacket;
import com.deploymentsys.tcp.protocol.response.ExecShellResponsePacket;
import com.deploymentsys.tcp.protocol.response.FileCopyResponsePacket;
import com.deploymentsys.tcp.protocol.response.FileTransferResponsePacket;
import com.deploymentsys.tcp.serialize.Serializer;
import com.deploymentsys.tcp.serialize.impl.JSONSerializer;

import io.netty.buffer.ByteBuf;

public class PacketCodeC {

	public static final int MAGIC_NUMBER = 0x16612802;
	public static final PacketCodeC INSTANCE = new PacketCodeC();

	private final Map<Byte, Class<? extends Packet>> packetTypeMap;
	private final Map<Byte, Serializer> serializerMap;

	private PacketCodeC() {
		packetTypeMap = new HashMap<>();

		packetTypeMap.put(Command.FILE_TRANSFER_REQUEST, FileTransferRequestPacket.class);
		packetTypeMap.put(Command.FILE_TRANSFER_RESPONSE, FileTransferResponsePacket.class);

		packetTypeMap.put(Command.FILE_COPY_REQUEST, FileCopyRequestPacket.class);
		packetTypeMap.put(Command.FILE_COPY_RESPONSE, FileCopyResponsePacket.class);

		packetTypeMap.put(Command.EMPTY_DIR_REQUEST, EmptyDirRequestPacket.class);
		packetTypeMap.put(Command.EMPTY_DIR_RESPONSE, EmptyDirResponsePacket.class);

		packetTypeMap.put(Command.DELETE_DIR_REQUEST, DeleteDirRequestPacket.class);
		packetTypeMap.put(Command.DELETE_DIR_RESPONSE, DeleteDirResponsePacket.class);

		packetTypeMap.put(Command.EXEC_SHELL_REQUEST, ExecShellRequestPacket.class);
		packetTypeMap.put(Command.EXEC_SHELL_RESPONSE, ExecShellResponsePacket.class);

		serializerMap = new HashMap<>();
		Serializer serializer = new JSONSerializer();
		serializerMap.put(serializer.getSerializerAlogrithm(), serializer);
	}

	public void encode(ByteBuf byteBuf, Packet packet) {
		// 1. 序列化 java 对象
		byte[] bytes = Serializer.DEFAULT.serialize(packet);

		// String tt=JSON.toJSONString(packet);

		// 2. 实际编码过程
		byteBuf.writeInt(MAGIC_NUMBER);
		byteBuf.writeByte(packet.getVersion());
		byteBuf.writeByte(Serializer.DEFAULT.getSerializerAlogrithm());

		// byte temp01=packet.getCommand();
		byteBuf.writeByte(packet.getCommand());

		// int temp02=bytes.length;

		byteBuf.writeInt(bytes.length);
		byteBuf.writeBytes(bytes);
	}

	public Packet decode(ByteBuf byteBuf) {
		// 跳过 magic number
		byteBuf.skipBytes(4);

		// 跳过版本号
		byteBuf.skipBytes(1);

		// 序列化算法
		byte serializeAlgorithm = byteBuf.readByte();

		// 指令
		byte command = byteBuf.readByte();

		// 数据包长度
		int length = byteBuf.readInt();

		byte[] bytes = new byte[length];
		byteBuf.readBytes(bytes);

		Class<? extends Packet> requestType = getRequestType(command);
		Serializer serializer = getSerializer(serializeAlgorithm);

		if (requestType != null && serializer != null) {
			return serializer.deserialize(requestType, bytes);
		}

		return null;
	}

	private Serializer getSerializer(byte serializeAlgorithm) {
		return serializerMap.get(serializeAlgorithm);
	}

	private Class<? extends Packet> getRequestType(byte command) {
		return packetTypeMap.get(command);
	}
}
