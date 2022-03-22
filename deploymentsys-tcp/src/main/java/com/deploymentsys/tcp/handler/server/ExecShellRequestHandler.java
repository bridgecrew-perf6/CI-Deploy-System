package com.deploymentsys.tcp.handler.server;

import java.io.IOException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.request.ExecShellRequestPacket;
import com.deploymentsys.tcp.protocol.response.ExecShellResponsePacket;
import com.deploymentsys.utils.ExecShellUtils;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ExecShellRequestHandler extends SimpleChannelInboundHandler<ExecShellRequestPacket> {
	public static final ExecShellRequestHandler INSTANCE = new ExecShellRequestHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecShellRequestHandler.class);

	private final String className = this.getClass().getName();

	private ExecShellRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ExecShellRequestPacket requestPacket)
			throws IOException, InterruptedException {
		String shell = requestPacket.getShell();// 需要执行的命令行

		String responseMsg = "";
		ExecShellResponsePacket response = new ExecShellResponsePacket();

		// 在这里面try catch一下吧，别再方面上面直接 throws IOException,
		// InterruptedException，免得客户端不知道这边发生的异常
		try {
			// responseMsg = ExecShellUtils.execShell(shell);

			Thread t = new Thread(new Runnable() {
				public void run() {
					try {
						ExecShellUtils.execShell(shell);
					} catch (Exception ex) {
						LOGGER.error("ExecShellUtils.execShell 发生异常 ", ex);
					}
				}
			});

			t.start();
			responseMsg = "远程执行命令行完毕";
			response.setResponseMsg(responseMsg);
			response.setDone(true);

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
