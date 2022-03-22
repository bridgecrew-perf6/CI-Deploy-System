package com.deploymentsys.tcp.codec;

import java.util.List;

import com.deploymentsys.tcp.protocol.PacketCodeC;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List out) {
		out.add(PacketCodeC.INSTANCE.decode(in));
	}

}
