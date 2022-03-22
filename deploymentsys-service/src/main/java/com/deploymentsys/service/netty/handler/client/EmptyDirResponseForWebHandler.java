package com.deploymentsys.service.netty.handler.client;

import java.net.UnknownHostException;
import java.text.MessageFormat;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.deploymentsys.beans.constants.DeployTaskStatus;
import com.deploymentsys.beans.deploy.DeployLogBean;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.service.deploy.DeployTaskService;
import com.deploymentsys.tcp.protocol.request.EmptyDirRequestPacket;
import com.deploymentsys.tcp.protocol.response.EmptyDirResponsePacket;
import com.deploymentsys.utils.WebUtils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class EmptyDirResponseForWebHandler extends SimpleChannelInboundHandler<EmptyDirResponsePacket> {
	private static final Logger LOGGER = LoggerFactory.getLogger(EmptyDirResponseForWebHandler.class);

	private static final DeployTaskService deployTaskService = new DeployTaskService();
	private static final WebUtils webUtils = new WebUtils();

	private DeployTaskServerToDoBean toDo;

	private String logMsg;
	private final String className = this.getClass().getName();

	public EmptyDirResponseForWebHandler() {
	}

	public EmptyDirResponseForWebHandler(DeployTaskServerToDoBean toDo) {
		this.toDo = toDo;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		// super.channelActive(ctx);
		try {
			EmptyDirRequestPacket request = new EmptyDirRequestPacket();

			request.setFilePathForEmpty(toDo.getParam1());

			ctx.channel().writeAndFlush(request);
		} catch (Exception ex) {
			LOGGER.error(className + ".channelActive", ex);

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
	protected void channelRead0(ChannelHandlerContext ctx, EmptyDirResponsePacket emptyDirResponsePacket) {
		try {
			if (emptyDirResponsePacket.getResponseCode() != 0) {
				logMsg = MessageFormat.format("远程清空目录任务失败 {0}，接收端异常信息：{1}", JSON.toJSONString(toDo),
						emptyDirResponsePacket.getResponseMsg());
				// 更新部署任务状态为失败
				toDo.setStatus(DeployTaskStatus.FAILURE);
			} else {
				boolean done = emptyDirResponsePacket.isDone();

				logMsg = MessageFormat.format("远程清空目录任务{0} {1}, responseMsg: {2}", done ? "成功" : "失败",
						JSON.toJSONString(toDo), emptyDirResponsePacket.getResponseMsg());

				toDo.setStatus(done ? DeployTaskStatus.SUCCESS : DeployTaskStatus.FAILURE);
			}

			LOGGER.info(logMsg);
			deployTaskService.addToTaskServerToDoQueue(toDo);

			deployTaskService.addToLogQueue(new DeployLogBean(toDo.getTaskId(), toDo.getDeployTaskServerId(),
					toDo.getId(), logMsg, webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp()));

			closeActions(ctx);

		} catch (Exception ex) {
			LOGGER.error(className + ".channelRead0 捕获到了异常：", ex);
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
				LOGGER.error(className + ".closeActions 捕获到了异常：", ex);
			}
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error(className + ".exceptionCaught抓到了异常:", cause);
		// super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		LOGGER.info(className + ".channelInactive: 服务端通信连接已断开");
		// super.channelInactive(ctx);
	}

	private void closeActions(ChannelHandlerContext ctx) throws InterruptedException {
		LOGGER.info("关闭ChannelHandlerContext和Channel。。。");
		ctx.channel().close().sync();
		ctx.close().sync();
	}

}
