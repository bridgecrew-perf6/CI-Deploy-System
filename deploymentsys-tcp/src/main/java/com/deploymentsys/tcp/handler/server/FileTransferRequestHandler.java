package com.deploymentsys.tcp.handler.server;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.tcp.protocol.request.FileTransferRequestPacket;
import com.deploymentsys.tcp.protocol.response.FileTransferResponsePacket;
import com.deploymentsys.utils.FileUtil;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class FileTransferRequestHandler extends SimpleChannelInboundHandler<FileTransferRequestPacket> {
	public static final FileTransferRequestHandler INSTANCE = new FileTransferRequestHandler();
	private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferRequestHandler.class);

	private FileTransferRequestHandler() {

	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileTransferRequestPacket fileTransferRequestPacket)
			throws IOException, InterruptedException {
		long fileLength = fileTransferRequestPacket.getFileLength();
		String fileName = fileTransferRequestPacket.getFileName();// 文件名
		String targetDir = fileTransferRequestPacket.getTargetDir();// 文件传输过来后存放文件的目标目录

		FileTransferResponsePacket response = new FileTransferResponsePacket();
		BeanUtils.copyProperties(fileTransferRequestPacket, response);
		// 返回给发送端的数据包不需要包含文件字节
		response.setBytes(null);

		// 在这里面try catch一下吧，别再方面上面直接 throws IOException,
		// InterruptedException，免得客户端不知道这边发生的异常
		try {
			File targetDirFile = new File(targetDir);
			if (!targetDirFile.exists() || targetDirFile.isFile()) {
				targetDirFile.mkdirs();
			}

			String targetFilePath = targetDir + SysConstants.FILE_SEPARATOR + fileName;

			RandomAccessFile randomAccessFile = new RandomAccessFile(targetFilePath, "rw");// r: 只读模式； rw:读写模式

			if (fileLength == 0) {
				// 文件字节数为0，啥也不用干
				randomAccessFile.close();
				randomAccessFile = null;
				ctx.channel().writeAndFlush(response);// 向客户端发送消息
			} else {
				long startPos = fileTransferRequestPacket.getStartPos();
				byte[] bytes = fileTransferRequestPacket.getBytes();
				String md5 = fileTransferRequestPacket.getMd5();// 文件md5值

				randomAccessFile.seek(startPos);// 移动文件记录指针的位置,
				randomAccessFile.write(bytes);// 调用了seek（start）方法，是指把文件的记录指针定位到start字节的位置。也就是说程序将从start字节开始写数据

				long nextPos = startPos + bytes.length;
				response.setStartPos(nextPos);
				response.setEndPos(nextPos);

				randomAccessFile.close();
				randomAccessFile = null;
				bytes = null;
				// 判断是否需要验证md5值
				if (nextPos == fileLength) {
					LOGGER.info("文件 {} 接收完毕", fileName);
					boolean md5ValidateResult = md5.equals(FileUtil.getFileMd5(targetFilePath));
					LOGGER.info("文件 {} md5验证结果（接收端记录）：{}", fileName, md5ValidateResult ? "一致" : "不一致");

					response.setMd5Validate(md5ValidateResult);
				}

				ctx.channel().writeAndFlush(response);// 向客户端发送消息
			}
		} catch (Exception ex) {
			response.setResponseCode(1);
			response.setResponseMsg(ExceptionUtils.getStackTrace(ex));

			ctx.channel().writeAndFlush(response);// 向客户端发送消息
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// super.channelInactive(ctx);
		LOGGER.info("FileTransferRequestHandler.channelInactive 客户端通信连接已断开");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		LOGGER.error("FileTransferRequestHandler.exceptionCaught 抓到了异常:", cause);
	}

}
