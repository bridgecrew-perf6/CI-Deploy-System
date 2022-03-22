package com.deploymentsys.tcp.handler.server;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.request.EmptyDirRequestPacket;
import com.deploymentsys.tcp.protocol.response.EmptyDirResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class EmptyDirRequestHandler extends SimpleChannelInboundHandler<EmptyDirRequestPacket> {
	public static final EmptyDirRequestHandler INSTANCE = new EmptyDirRequestHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(EmptyDirRequestHandler.class);

	private final String className = this.getClass().getName();

	private EmptyDirRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, EmptyDirRequestPacket requestPacket)
			throws IOException, InterruptedException {
		String filePathForEmpty = requestPacket.getFilePathForEmpty();// 需要清空的目录

		String responseMsg = "";

		EmptyDirResponsePacket response = new EmptyDirResponsePacket();

		// 在这里面try catch一下吧，别再方面上面直接 throws IOException,
		// InterruptedException，免得客户端不知道这边发生的异常
		try {
			File filePathForEmptyFile = new File(filePathForEmpty);

			if (!filePathForEmptyFile.exists() || filePathForEmptyFile.isFile()) {
				responseMsg = MessageFormat.format("路径 {0} 不存在或是一个文件", filePathForEmpty);
				response.setResponseMsg(responseMsg);
				response.setDone(true);// 不存在也算删除成功

			} else {
				FileUtils.cleanDirectory(filePathForEmptyFile);

				responseMsg = MessageFormat.format("目录 {0} 清空成功", filePathForEmpty);
				response.setResponseMsg(responseMsg);
				response.setDone(true);
			}

			LOGGER.info(responseMsg);

		} catch (Exception ex) {
			LOGGER.error(className + ".channelRead0  发生异常 ", ex);
			response.setResponseCode(1);
			response.setResponseMsg(ExceptionUtils.getStackTrace(ex));
		}

		ctx.channel().writeAndFlush(response);// 向客户端发送消息
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// super.channelInactive(ctx);
		LOGGER.info(className + ".channelInactive 客户端通信连接已断开");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		LOGGER.error(className + ".exceptionCaught 抓到了异常:", cause);
	}

}
