package com.deploymentsys.service.netty.handler.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.deploymentsys.beans.constants.DeployTaskStatus;
import com.deploymentsys.beans.deploy.DeployLogBean;
import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.service.deploy.DeployTaskService;
import com.deploymentsys.tcp.protocol.request.FileTransferRequestPacket;
import com.deploymentsys.tcp.protocol.response.FileTransferResponsePacket;
import com.deploymentsys.utils.FileUtil;
import com.deploymentsys.utils.WebUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileTransferResponseForWebHandler extends SimpleChannelInboundHandler<FileTransferResponsePacket> {
	private static final Logger LOGGER = LoggerFactory.getLogger(FileTransferResponseForWebHandler.class);

	private static final DeployTaskService deployTaskService = new DeployTaskService();
	private static final WebUtils webUtils = new WebUtils();

	// private NioEventLoopGroup workerGroup;

	private DeployTaskServerToDoBean toDo;
	private String sourceFilePathBase;
	private String targetDirBase;
	private List<DeployTaskFileBean> files;

	private String sourceFilePathCurrent = "";
	private String targetDirCurrent = "";
	private String relativePathCurrent = "";

	private String logMsg;

	private int currentFileIndex = 0;
	private DeployTaskFileBean currentTaskFile;

	private boolean isNewFile = false;

	public FileTransferResponseForWebHandler() {
	}

	public FileTransferResponseForWebHandler(List<DeployTaskFileBean> files, String sourceFilePathBase,
			String targetDirBase, DeployTaskServerToDoBean toDo) {
		this.sourceFilePathBase = sourceFilePathBase;
		this.targetDirBase = targetDirBase;
		this.toDo = toDo;
		this.files = files;
	}

	private void fileStartTransfer(DeployTaskFileBean currentFile, ChannelHandlerContext ctx)
			throws InterruptedException, FileNotFoundException, IOException {
		isNewFile = false;
		relativePathCurrent = currentFile.getRelativePath();
		sourceFilePathCurrent = sourceFilePathBase + relativePathCurrent;

		File tempFile = new File(sourceFilePathCurrent);
		if (!tempFile.exists() || !tempFile.isFile()) {
			logMsg = MessageFormat.format("文件：{0} 不存在或不是文件", sourceFilePathCurrent);

			LOGGER.info(logMsg);
			deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
					toDo.getId(), logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

			toDo.setStatus(DeployTaskStatus.FAILURE);
			deployTaskService.addToTaskServerToDoQueue(toDo);

			closeActions(ctx);
			return;
		}

		// 文件传输之前验证一次MD5
		String md5Current = FileUtil.getFileMd5(sourceFilePathCurrent);
		if (!md5Current.equals(currentFile.getMd5())) {
			logMsg = MessageFormat.format("文件：{0} md5验证不通过", sourceFilePathCurrent);

			LOGGER.info(logMsg);
			deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
					toDo.getId(), logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

			toDo.setStatus(DeployTaskStatus.FAILURE);
			deployTaskService.addToTaskServerToDoQueue(toDo);

			closeActions(ctx);
			return;
		}

		targetDirCurrent = targetDirBase + relativePathCurrent.substring(0, relativePathCurrent.lastIndexOf("/"));

		LOGGER.info("准备开始传输文件 {} ", tempFile.getName());
		FileTransferRequestPacket request = new FileTransferRequestPacket();
		request.setSourceFilePath(sourceFilePathCurrent);
		request.setFileName(tempFile.getName());
		request.setTargetDir(targetDirCurrent);

		request.setStartPos(0);

		int perByteLength = 5 * 1024 * 1024;// 每次传输的文件字节数
		RandomAccessFile randomAccessFile = new RandomAccessFile(sourceFilePathCurrent, "r");
		long fileLength = randomAccessFile.length();
		request.setFileLength(fileLength);

		if (fileLength > 0) {
			int transferByteLength = fileLength > perByteLength ? perByteLength : (int) fileLength;
			byte[] bytes = new byte[transferByteLength];
			randomAccessFile.seek(0);
			randomAccessFile.read(bytes);

			request.setMd5(md5Current);
			request.setBytes(bytes);
		} else {
			request.setBytes(null);
		}

		randomAccessFile.close();
		ctx.channel().writeAndFlush(request);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// super.channelActive(ctx);
		try {
			currentTaskFile = files.get(currentFileIndex);
			fileStartTransfer(currentTaskFile, ctx);

		} catch (Exception ex) {
			LOGGER.error("FileTransferResponseForWebHandler.channelActive", ex);

			try {
				deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
						toDo.getId(), ExceptionUtils.getStackTrace(ex), webUtils.getCurrentDateStr(), 1,
						webUtils.getLocalIp()));
			} catch (UnknownHostException e) {
				LOGGER.error("webUtils.getLocalIp出现异常", e);
			}

			toDo.setStatus(DeployTaskStatus.FAILURE);
			deployTaskService.addToTaskServerToDoQueue(toDo);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FileTransferResponsePacket fileTransferResponsePacket) {
		String fileName = fileTransferResponsePacket.getFileName();// 文件名

		try {
			if (fileTransferResponsePacket.getResponseCode() != 0) {
				logMsg = MessageFormat.format("文件 {0} 传输失败，接收端异常信息：{1}", fileName,
						fileTransferResponsePacket.getResponseMsg());
				LOGGER.info(logMsg);
				// 更新部署任务状态为失败
				toDo.setStatus(DeployTaskStatus.FAILURE);
				deployTaskService.addToTaskServerToDoQueue(toDo);

				deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
						toDo.getId(), logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

				closeActions(ctx);
			} else {
				long startPos = fileTransferResponsePacket.getStartPos();
				long lastEndPos = fileTransferResponsePacket.getEndPos();
				long fileLength = fileTransferResponsePacket.getFileLength();

				// 如果上一次文件传输的结尾位置等于文件长度，那么就认为文件传输完毕，检查md5验证结果
				if (lastEndPos == fileLength) {
					logMsg = MessageFormat.format("文件 {0} 传输完毕", fileName);
					LOGGER.info(logMsg);
					deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
							toDo.getId(), logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

					boolean md5ValidateResult = false;
					// 写入数据库日志表
					if (fileLength > 0) {
						md5ValidateResult = fileTransferResponsePacket.isMd5Validate();
						logMsg = MessageFormat.format("文件 {0} md5验证结果（发送端记录）：{1}", fileName,
								md5ValidateResult ? "一致" : "不一致");
						LOGGER.info(logMsg);

						if (!md5ValidateResult) {
							toDo.setStatus(DeployTaskStatus.FAILURE);

							deployTaskService.addToLogQueue(
									new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(), toDo.getId(),
											logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

							deployTaskService.addToTaskServerToDoQueue(toDo);
							closeActions(ctx);
							return;
						}
						// toDo.setStatus(md5ValidateResult ? DeployTaskStatus.SUCCESS :
						// DeployTaskStatus.FAILURE);
					} else {
						logMsg = MessageFormat.format("文件 {0} md5验证结果（发送端记录）：{1}", fileName, "文件长度为0，跳过验证");
						LOGGER.info(logMsg);

						md5ValidateResult = true;
					}

					deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
							toDo.getId(), logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

					if ((currentFileIndex + 1) == files.size()) {
						toDo.setStatus(md5ValidateResult ? DeployTaskStatus.SUCCESS : DeployTaskStatus.FAILURE);
						deployTaskService.addToTaskServerToDoQueue(toDo);
						closeActions(ctx);

						return;
					} else {
						currentFileIndex++;
						isNewFile = true;
					}
				}

				if (isNewFile) {
					currentTaskFile = files.get(currentFileIndex);
					fileStartTransfer(currentTaskFile, ctx);
				} else {
					LOGGER.info("=========文件 {} 已传输 {} 字节============", fileName, lastEndPos);
					RandomAccessFile randomAccessFile = new RandomAccessFile(
							fileTransferResponsePacket.getSourceFilePath(), "r");
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

			}

		} catch (Exception ex) {
			LOGGER.error("FileTransferResponseHandler.channelRead0 捕获到了异常：", ex);
			// 更新部署任务状态为失败
			toDo.setStatus(DeployTaskStatus.FAILURE);
			deployTaskService.addToTaskServerToDoQueue(toDo);

			try {
				deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
						toDo.getId(), ExceptionUtils.getStackTrace(ex), webUtils.getCurrentDateStr(), 1,
						webUtils.getLocalIp()));
			} catch (UnknownHostException e1) {
				LOGGER.error("webUtils.getLocalIp() ", e1);
			}

			try {
				closeActions(ctx);
			} catch (InterruptedException e) {
				LOGGER.error("FileTransferResponseHandler.closeActions 捕获到了异常：", ex);
			}

		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error("FileTransferResponseHandler.exceptionCaught抓到了异常:", cause);
		// LOGGER.info("关闭客户端工作线程组。。。");
		// super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info("FileTransferResponseHandler.channelInactive: 服务端通信连接已断开");
		// 判断文件是否传输失败
		// LOGGER.info("文件 {} 传输失败了", fileName);
		// LOGGER.info("关闭客户端工作线程组。。。");

		// super.channelInactive(ctx);
	}

	private void closeActions(ChannelHandlerContext ctx) throws InterruptedException {
		LOGGER.info("关闭ChannelHandlerContext和Channel。。。");
		ctx.channel().close().sync();
		ctx.close().sync();
	}

}
