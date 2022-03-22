package com.deploymentsys.tcp.handler.client;

import java.io.RandomAccessFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.deploymentsys.tcp.protocol.request.FileTransferRequestPacket;
import com.deploymentsys.tcp.protocol.response.FileTransferResponsePacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;

public class FileTransferResponseHandler extends SimpleChannelInboundHandler<FileTransferResponsePacket> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferResponseHandler.class);

	private NioEventLoopGroup workerGroup;

	private String fileName;

	public FileTransferResponseHandler(NioEventLoopGroup workerGroup, String fileName) {
		this.workerGroup = workerGroup;
		this.fileName = fileName;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// super.channelActive(ctx);
		// System.out.println("进来了FileTransferResponseHandler.channelActive");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileTransferResponsePacket fileTransferResponsePacket) {
		String fileName = fileTransferResponsePacket.getFileName();// 文件名

		try {
			if (fileTransferResponsePacket.getResponseCode() != 0) {
				LOGGER.info("文件 {} 传输失败，接收端异常信息：{}", fileName, fileTransferResponsePacket.getResponseMsg());
				// 更新部署任务状态为失败

				closeActions(ctx);
			} else {
				long startPos = fileTransferResponsePacket.getStartPos();
				long lastEndPos = fileTransferResponsePacket.getEndPos();
				long fileLength = fileTransferResponsePacket.getFileLength();

				// 如果上一次文件传输的结尾位置等于文件长度，那么就认为文件传输完毕，检查md5验证结果
				if (lastEndPos == fileLength) {
					LOGGER.info("文件 {} 传输完毕", fileName);
					// 写入数据库日志表
					if (fileLength > 0) {
						boolean md5ValidateResult = fileTransferResponsePacket.isMd5Validate();
						LOGGER.info("文件 {} md5验证结果（发送端记录）：{}", fileName, md5ValidateResult ? "一致" : "不一致");
					} else {
						LOGGER.info("文件 {} md5验证结果（发送端记录）：{}", fileName, "文件长度为0，跳过验证");
					}

					closeActions(ctx);

					return;
				}

				LOGGER.info("=========文件 {} 已传输 {} 字节============", fileName, lastEndPos);
				RandomAccessFile randomAccessFile = new RandomAccessFile(fileTransferResponsePacket.getSourceFilePath(),
						"r");
				randomAccessFile.seek(startPos);

				long perByteLength = 5 * 1024 * 1024;// 每次传输的文件字节数
				long leftByteLength = fileLength - startPos;// 剩下的文件字节数
				long transferByteLength = leftByteLength > perByteLength ? perByteLength : leftByteLength;

				FileTransferRequestPacket request = new FileTransferRequestPacket();
				BeanUtils.copyProperties(fileTransferResponsePacket, request);

				byte[] bytes = new byte[(int) transferByteLength];
				randomAccessFile.read(bytes);

				request.setStartPos(startPos);
				request.setBytes(bytes);

				randomAccessFile.close();
				randomAccessFile = null;
				ctx.channel().writeAndFlush(request);
			}

		} catch (Exception ex) {
			LOGGER.error("FileTransferResponseHandler.channelRead0 捕获到了异常：", ex);

			try {
				closeActions(ctx);
			} catch (InterruptedException e) {
				LOGGER.error("FileTransferResponseHandler.closeActions 捕获到了异常：", ex);
			}

			// 更新部署任务状态为失败
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("FileTransferResponseHandler.exceptionCaught抓到了异常:", cause);
		LOGGER.info("关闭客户端工作线程组。。。");
		if (workerGroup != null) {
			workerGroup.shutdownNow();
		}
		// super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("FileTransferResponseHandler.channelInactive: 服务端通信连接已断开");
		// 判断文件是否传输失败
		// LOGGER.info("文件 {} 传输失败了", fileName);
		// LOGGER.info("关闭客户端工作线程组。。。");
		if (workerGroup != null) {
			LOGGER.info("客户端工作线程组workerGroup不为null，关闭workerGroup。。。");
			workerGroup.shutdownNow();
		}

		// super.channelInactive(ctx);
	}

	private void closeActions(ChannelHandlerContext ctx) throws InterruptedException {
		LOGGER.info("关闭ChannelHandlerContext和Channel。。。");
		ctx.channel().close().sync();
		ctx.close().sync();
		LOGGER.info("关闭客户端工作线程组。。。");
		if (workerGroup != null) {
			workerGroup.shutdownNow();
			workerGroup = null;
		}
	}

}
