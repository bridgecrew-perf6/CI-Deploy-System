package com.deploymentsys.tcp.handler.server;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.request.DeleteDirRequestPacket;
import com.deploymentsys.tcp.protocol.response.DeleteDirResponsePacket;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class DeleteDirRequestHandler extends SimpleChannelInboundHandler<DeleteDirRequestPacket> {
	public static final DeleteDirRequestHandler INSTANCE = new DeleteDirRequestHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDirRequestHandler.class);

	private DeleteDirRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DeleteDirRequestPacket requestPacket)
			throws IOException, InterruptedException {
		String filePathForDelete = requestPacket.getFilePathForDelete();// 需要删除的文件路径，可以是文件或目录

		String responseMsg = "";

		DeleteDirResponsePacket response = new DeleteDirResponsePacket();
		// BeanUtils.copyProperties(fileTransferRequestPacket, response);

		// 在这里面try catch一下吧，别再方面上面直接 throws IOException,
		// InterruptedException，免得客户端不知道这边发生的异常
		try {
			File filePathForDeleteFile = new File(filePathForDelete);

			if (!filePathForDeleteFile.exists()) {
				responseMsg = MessageFormat.format("路径 {0} 不存在", filePathForDelete);
				response.setResponseMsg(responseMsg);
				response.setDone(true);// 不存在也算删除成功

			} else {
				FileUtils.forceDelete(filePathForDeleteFile);

				responseMsg = MessageFormat.format("路径 {0} 删除成功", filePathForDelete);
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
