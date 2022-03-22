package com.deploymentsys.tcp.handler.server;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.protocol.Packet;
import com.deploymentsys.tcp.protocol.command.Command;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class DeployHandler extends SimpleChannelInboundHandler<Packet> {
	public static final DeployHandler INSTANCE = new DeployHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployHandler.class);

	private Map<Byte, SimpleChannelInboundHandler<? extends Packet>> handlerMap;

	private DeployHandler() {
		handlerMap = new HashMap<>();

		handlerMap.put(Command.FILE_TRANSFER_REQUEST, FileTransferRequestHandler.INSTANCE);
		handlerMap.put(Command.FILE_COPY_REQUEST, FileCopyRequestHandler.INSTANCE);
		handlerMap.put(Command.DELETE_DIR_REQUEST, DeleteDirRequestHandler.INSTANCE);
		handlerMap.put(Command.EMPTY_DIR_REQUEST, EmptyDirRequestHandler.INSTANCE);
		handlerMap.put(Command.EXEC_SHELL_REQUEST, ExecShellRequestHandler.INSTANCE);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
		handlerMap.get(packet.getCommand()).channelRead(ctx, packet);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// super.channelInactive(ctx);

		LOGGER.info("DeployHandler.channelInactive 客户端通信连接已断开");
		// 显式调用GC是强烈不被推荐使用的，其次很多生产环境甚至禁用了显式GC调用
		// System.gc();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		LOGGER.error("DeployHandler.exceptionCaught抓到了异常:", cause);
	}
}
