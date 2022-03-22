package com.deploymentsys.tcp.codec;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.PacketCodeC;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<Packet> {

	@Override
	protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) {
		PacketCodeC.INSTANCE.encode(out, packet);
	}

	// @Override
	// public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
	// throws Exception {
	// // TODO Auto-generated method stub
	// System.out.println("进来了PacketEncoder.exceptionCaught");
	// //super.exceptionCaught(ctx, cause);
	// }

}
