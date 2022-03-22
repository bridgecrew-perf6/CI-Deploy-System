package com.deploymentsys.service.netty;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.service.netty.handler.client.DeleteDirResponseForWebHandler;
import com.deploymentsys.service.netty.handler.client.EmptyDirResponseForWebHandler;
import com.deploymentsys.service.netty.handler.client.ExecShellResponseForWebHandler;
import com.deploymentsys.service.netty.handler.client.FileCopyResponseForWebHandler;
import com.deploymentsys.service.netty.handler.client.FileTransferResponseForWebHandler;
import com.deploymentsys.tcp.codec.PacketDecoder;
import com.deploymentsys.tcp.codec.PacketEncoder;
import com.deploymentsys.tcp.codec.Spliter;
import com.deploymentsys.tcp.handler.DeployIdleStateHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * 部署系统netty通信客户端初始化辅助类
 * 
 * @author adminjack
 *
 */
public class NettyClientInit {
	private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientInit.class);

	/**
	 * 执行命令行
	 * 
	 * @param toDo
	 * @return
	 */
	public static Bootstrap initForExecShell(DeployTaskServerToDoBean toDo) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						// 空闲检测
						ch.pipeline().addLast(new DeployIdleStateHandler());
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(new ExecShellResponseForWebHandler(toDo));
						ch.pipeline().addLast(new PacketEncoder());
					}
				});

		return bootstrap;
	}

	/**
	 * 清空目录
	 * 
	 * @param toDo
	 * @return
	 */
	public static Bootstrap initForEmptyDir(DeployTaskServerToDoBean toDo) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						// 空闲检测
						ch.pipeline().addLast(new DeployIdleStateHandler());
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(new EmptyDirResponseForWebHandler(toDo));
						ch.pipeline().addLast(new PacketEncoder());
					}
				});

		return bootstrap;
	}

	/**
	 * 删除文件或目录
	 * 
	 * @param toDo
	 * @return
	 */
	public static Bootstrap initForDeleteDir(DeployTaskServerToDoBean toDo) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						// 空闲检测
						ch.pipeline().addLast(new DeployIdleStateHandler());
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(new DeleteDirResponseForWebHandler(toDo));
						ch.pipeline().addLast(new PacketEncoder());
					}
				});

		return bootstrap;
	}

	/**
	 * 文件拷贝
	 * 
	 * @param toDo
	 * @return
	 */
	public static Bootstrap initForFileCopy(DeployTaskServerToDoBean toDo) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						// 空闲检测
						ch.pipeline().addLast(new DeployIdleStateHandler());
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(new FileCopyResponseForWebHandler(toDo));
						ch.pipeline().addLast(new PacketEncoder());
					}
				});

		return bootstrap;
	}

	/**
	 * 文件传输
	 * 
	 * @param sourceFilePath
	 * @param targetDir
	 * @param toDo
	 * @return
	 */
	public static Bootstrap initForFileTransfer(List<DeployTaskFileBean> files, String sourceFilePathBase,
			String targetDirBase, DeployTaskServerToDoBean toDo) {
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();
		Bootstrap bootstrap = new Bootstrap();

		bootstrap.group(workerGroup).channel(NioSocketChannel.class).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
				.option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) {
						// 空闲检测
						ch.pipeline().addLast(new DeployIdleStateHandler());
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(new PacketDecoder());
						ch.pipeline().addLast(
								new FileTransferResponseForWebHandler(files, sourceFilePathBase, targetDirBase, toDo));
						// ch.pipeline().addLast(customChannelHandler);
						ch.pipeline().addLast(new PacketEncoder());
					}
				});

		return bootstrap;
	}

	public static Channel connect(Bootstrap bootstrap, String ip, int port) throws InterruptedException {
		return bootstrap.connect(ip, port).addListener(new GenericFutureListener<Future<? super Void>>() {
			@Override
			public void operationComplete(Future<? super Void> future) throws Exception {
				if (future.isSuccess()) {
					LOGGER.info("目标服务器连接成功，ip:{}, port:{}", ip, port);
				} else {
					LOGGER.error("目标服务器连接失败");
				}
			}

		}).sync().channel();
	}

}
