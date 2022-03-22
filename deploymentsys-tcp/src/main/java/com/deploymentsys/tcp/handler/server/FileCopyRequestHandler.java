package com.deploymentsys.tcp.handler.server;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.request.FileCopyRequestPacket;
import com.deploymentsys.tcp.protocol.response.FileCopyResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class FileCopyRequestHandler extends SimpleChannelInboundHandler<FileCopyRequestPacket> {
	public static final FileCopyRequestHandler INSTANCE = new FileCopyRequestHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(FileCopyRequestHandler.class);

	private FileCopyRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileCopyRequestPacket fileCopyRequestPacket)
			throws IOException, InterruptedException {
		String sourceFilePath = fileCopyRequestPacket.getSourceFilePath();// 文件拷贝的源目录
		String targetDir = fileCopyRequestPacket.getTargetDir();// 文件拷贝的目标目录

		String responseMsg = "";

		FileCopyResponsePacket response = new FileCopyResponsePacket();
		// BeanUtils.copyProperties(fileTransferRequestPacket, response);

		// 在这里面try catch一下吧，别再方面上面直接 throws IOException,
		// InterruptedException，免得客户端不知道这边发生的异常
		try {
			File sourceFile = new File(sourceFilePath);
			if (!sourceFile.exists()) {
				responseMsg = MessageFormat.format("文件 {0} 不存在", sourceFilePath);
				response.setResponseMsg(responseMsg);
				response.setDone(false);

			} else {
				if (sourceFile.isFile()) {
					// 把某个文件拷贝到某个目录下
					FileUtils.copyFileToDirectory(sourceFile, new File(targetDir));
				} else {
					// 目录拷贝
					FileUtils.copyDirectory(sourceFile, new File(targetDir));
				}

				responseMsg = MessageFormat.format("文件 {0} 拷贝成功", sourceFilePath);
				response.setResponseMsg(responseMsg);
				response.setDone(true);
			}

			LOGGER.info(responseMsg);

		} catch (Exception ex) {
			LOGGER.error("FileCopyRequestHandler.channelRead0  发生异常 ", ex);
			response.setResponseCode(1);
			response.setResponseMsg(ExceptionUtils.getStackTrace(ex));
		}

		ctx.channel().writeAndFlush(response);// 向客户端发送消息
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// super.channelInactive(ctx);
		LOGGER.info("FileCopyRequestHandler.channelInactive 客户端通信连接已断开");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		LOGGER.error("FileCopyRequestHandler.exceptionCaught 抓到了异常:", cause);
	}

}
