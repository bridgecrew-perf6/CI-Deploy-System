package com.deploymentsys.tcp.handler.client;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.response.MessageResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

public class MessageResponseHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageResponseHandler.class);
	
	private NioEventLoopGroup workerGroup;
	public MessageResponseHandler(NioEventLoopGroup workerGroup) {
		this.workerGroup=workerGroup;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket messageResponsePacket) throws InterruptedException {
		// int a=1/0;
		System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());

		System.out.println("客户端主动关闭连接");
		ctx.channel().close().sync();
		ctx.close().sync();
		
		workerGroup.shutdownNow();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("MessageResponseHandler.exceptionCaught抓到了异常:", cause);
		// super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("服务端通信连接已断开");
		// super.channelInactive(ctx);
	}

}
