package com.deploymentsys.tcp.handler.server;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.request.MessageRequestPacket;
import com.deploymentsys.tcp.protocol.response.MessageResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class MessageRequestHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
	public static final MessageRequestHandler INSTANCE = new MessageRequestHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(MessageRequestHandler.class);

	private static final ExecutorService DEPLOY_TASK_THREADPOOL = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	private MessageRequestHandler() {

	}

	private void processMsg(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket,
			MessageResponsePacket messageResponsePacket) {
		try {
			System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

//			// Thread.sleep(30 * 1000);
//			ctx.channel().writeAndFlush(new MessageResponsePacket("服务端繁忙中。。。111 userId: " + messageRequestPacket.getToUserId()));
//			// Thread.sleep(2 * 1000);
//			
//			messageResponsePacket.setMessage("服务端繁忙中。。。222");
//			ctx.channel().writeAndFlush(new MessageResponsePacket("服务端繁忙中。。。222 userId: " + messageRequestPacket.getToUserId()));
//			// Thread.sleep(2 * 1000);
//
//			messageResponsePacket.setMessage("服务端繁忙中。。。333");
//			ctx.channel().writeAndFlush(new MessageResponsePacket("服务端繁忙中。。。333 userId: " + messageRequestPacket.getToUserId()));
			// Thread.sleep(2 * 1000);

			if (messageRequestPacket.getToUserId() > 0) {
				int a = 1;
			} else {
				int a = 1 / 0;
			}

			messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
			ctx.channel().writeAndFlush(messageResponsePacket);
		} catch (Exception ex) {
			messageResponsePacket.setMessage("服务端回复 服务端出现异常【" + ExceptionUtils.getStackTrace(ex) + "】");
			ctx.channel().writeAndFlush(messageResponsePacket);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket messageRequestPacket) {
		MessageResponsePacket messageResponsePacket = new MessageResponsePacket();

		DEPLOY_TASK_THREADPOOL.execute(new Runnable() {
			public void run() {
				processMsg(ctx, messageRequestPacket, messageResponsePacket);
			}
		});

		// processMsg(ctx, messageRequestPacket, messageResponsePacket);

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// super.channelInactive(ctx);
		// System.out.println("进来了MessageRequestHandler.channelInactive");
		LOGGER.info("客户端通信连接已断开");

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		LOGGER.error("MessageRequestHandler.exceptionCaught抓到了异常:", cause);
	}

}
