package com.deploymentsys.tcp.client;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.deploymentsys.tcp.codec.PacketDecoder;
import com.deploymentsys.tcp.codec.PacketEncoder;
import com.deploymentsys.tcp.codec.Spliter;
import com.deploymentsys.tcp.handler.DeployIdleStateHandler;
import com.deploymentsys.tcp.handler.client.FileTransferResponseHandler;
import com.deploymentsys.tcp.handler.client.MessageResponseHandler;
import com.deploymentsys.tcp.protocol.request.FileTransferRequestPacket;
import com.deploymentsys.tcp.protocol.request.MessageRequestPacket;
import com.deploymentsys.utils.FileUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

public class NettyDeployTransferClientStarter {
	static {
		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties");
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(NettyDeployTransferClientStarter.class);
	// static ExecutorService service = Executors.newFixedThreadPool(4);
	static ExecutorService service = Executors.newCachedThreadPool();

	public static void main(String[] args) {
		// String sourceFilePath = "D:/test/www.aa.com/配置文件 - 副本.config";
		String sourceFilePath = "D:/test/www.aa.com/1111";
		String targetDir = "D:/test/www.aa.com/targetD";

		try {
			File sourceFile = new File(sourceFilePath);
			if (sourceFile.isFile()) {
				System.out.println("isFile");
				FileUtils.copyFileToDirectory(sourceFile, new File(targetDir));
			} else {
				FileUtils.copyDirectory(sourceFile, new File(targetDir));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(666);

		// filesTransferTest01();
		// sendMsgTest01(ch);
	}

	private static void filesTransferTest01() {
		String ip = "192.168.1.100";
		int port = 8001;
		try {
			String sourceFilePath1 = "D:/BaiduYunDownload/Netty入门与实战：仿写微信 IM 即时通讯系统.rar";
			String sourceFilePath3 = "D:/installpackage/mysql-5.7.18-linux-glibc2.5-x86_64.tar.gz";
			String sourceFilePath4 = "D:/installpackage/vmwareworkstation_12_lite_chs/VMware-Workstation-12.1.1-3770994精简官方中文安装注册版.exe";
			List<String> sourceFilePaths = new ArrayList<>();
			// for (int i = 0; i < 1000; i++) {
			// sourceFilePaths.add(sourceFilePath1);
			// }

			sourceFilePaths.add(sourceFilePath1);
			sourceFilePaths.add(sourceFilePath3);
			sourceFilePaths.add(sourceFilePath4);

			String targetDir = "D:/test/www.aa.com/111";

			for (String path : sourceFilePaths) {
				// 在这就判断源文件是否存在
				File sourceFile = new File(path);
				if (!sourceFile.exists()) {
					LOGGER.info("文件 {} 不存在", path);
					continue;
				}

				service.submit(new Runnable() {
					@Override
					public void run() {
						NioEventLoopGroup workerGroup = new NioEventLoopGroup();

						try {
							Bootstrap bootstrap = init(workerGroup, path);
							Channel ch = connect(bootstrap, ip, port);

							fileTransfer(ch, path, targetDir, workerGroup);
						} catch (Exception e) {
							LOGGER.error("多线程出现异常：", e);
							LOGGER.info("关闭客户端工作线程组。。。");
							workerGroup.shutdownNow();
						}
					}
				});
			}

			service.shutdown();
			service = null;

		} catch (Exception e) {
			LOGGER.error("出现异常：", e);
			// workerGroup.shutdownNow();
		}
	}

	private static Bootstrap init(NioEventLoopGroup workerGroup, String fileName) {
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
						// ch.pipeline().addLast(new MessageResponseHandler(workerGroup));
						ch.pipeline().addLast(new FileTransferResponseHandler(workerGroup, fileName));
						ch.pipeline().addLast(new PacketEncoder());
					}
				});

		return bootstrap;
	}

	private static Channel connect(Bootstrap bootstrap, String ip, int port) throws InterruptedException {
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

	private static void sendMsgTest01(Channel channel) throws InterruptedException {
		if (null == channel) {
			LOGGER.error("TCP连接尚未开启！");
			return;
		}
		channel.writeAndFlush(new MessageRequestPacket(1, "我的测试消息 aaaaaa 12345@#$《》4444444444"));

		// channel.closeFuture().sync();
		// channel.writeAndFlush(new MessageRequestPacket(2, "我的测试消息 aaaaaa
		// 12345@#$《》555555555555"));
		// channel.writeAndFlush(new MessageRequestPacket(3, "我的测试消息 aaaaaa
		// 12345@#$《》666666"));
	}

	private static void fileTransfer(Channel channel, String sourceFilePath, String targetDir,
			NioEventLoopGroup workerGroup) throws Exception {
		if (null == channel) {
			LOGGER.error("TCP连接尚未开启！");
			return;
		}

		File sourceFile = new File(sourceFilePath);
		if (sourceFile.exists() && sourceFile.isFile()) {
			FileTransferRequestPacket request = new FileTransferRequestPacket();
			request.setSourceFilePath(sourceFilePath);
			request.setFileName(sourceFile.getName());
			request.setTargetDir(targetDir);

			request.setStartPos(0);

			int perByteLength = 5 * 1024 * 1024;// 每次传输的文件字节数
			RandomAccessFile randomAccessFile = new RandomAccessFile(sourceFile, "r");
			long fileLength = randomAccessFile.length();
			request.setFileLength(fileLength);

			if (fileLength > 0) {
				int transferByteLength = fileLength > perByteLength ? perByteLength : (int) fileLength;
				byte[] bytes = new byte[transferByteLength];
				randomAccessFile.seek(0);
				randomAccessFile.read(bytes);

				request.setMd5(FileUtil.getFileMd5(sourceFilePath));
				request.setBytes(bytes);
			} else {
				request.setBytes(null);
			}

			randomAccessFile.close();
			channel.writeAndFlush(request);
		} else {
			LOGGER.info("{}不存在或不是文件！", sourceFilePath);
			workerGroup.shutdownNow();
		}

	}

}
