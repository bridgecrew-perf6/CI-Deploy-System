package com.deploymentsys.tcp.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.codec.PacketCodecHandler;
import com.deploymentsys.tcp.codec.Spliter;
import com.deploymentsys.tcp.handler.DeployIdleStateHandler;
import com.deploymentsys.tcp.handler.server.DeployHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;

public class NettyDeployTransferServerStarter {
	static {
		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties");

		// System.out.println(System.getProperty("user.dir"));
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyDeployTransferServerStarter.class);

	public static void main(String[] args) throws IOException {
		final ServerBootstrap serverBootstrap = new ServerBootstrap();
		NioEventLoopGroup boss = new NioEventLoopGroup(2, new DefaultThreadFactory("bossServer1", false));
		NioEventLoopGroup worker = new NioEventLoopGroup(4, new DefaultThreadFactory("workerServer1", false));

		serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
				.childOption(ChannelOption.SO_KEEPALIVE, true).childOption(ChannelOption.TCP_NODELAY, true)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					protected void initChannel(NioSocketChannel ch) {
						// 空闲检测
						ch.pipeline().addLast(new DeployIdleStateHandler());
						ch.pipeline().addLast(new Spliter());
						ch.pipeline().addLast(PacketCodecHandler.INSTANCE);
						// ch.pipeline().addLast(MessageRequestHandler.INSTANCE);
						// ch.pipeline().addLast(new FileTransferRequestHandler());
						ch.pipeline().addLast(DeployHandler.INSTANCE);
					}
				});

		try {
			FileInputStream in = new FileInputStream("config/DeployTransferServer.config");

			Properties properties = new Properties();
			properties.load(in);
			String ip = properties.getProperty("ip");
			int port = Integer.valueOf(properties.getProperty("port"));
			LOGGER.info("监听的ip是：{}，端口号是：{}", ip, port);

			bind(serverBootstrap, ip, port);
		} catch (Exception e) {
			LOGGER.error("NettyDeployTransferServer 启动异常", e);
		}

	}

	private static void bind(ServerBootstrap serverBootstrap, String ip, int port) throws InterruptedException {
		serverBootstrap.bind(ip, port).addListener(future -> {
			if (future.isSuccess()) {
				LOGGER.info("ip:{}, port: {} 绑定成功", ip, port);
				LOGGER.info("部署系统通信服务端启动成功。。。");
			} else {
				LOGGER.info("ip:{}, port: {}绑定失败", ip, port);
				LOGGER.info("部署系统通信服务端启动失败。。。");
			}
		});

	}
}
