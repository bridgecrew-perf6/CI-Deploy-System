package com.deploymentsys.beans.constants;

/**
 * 部署任务状态常量
 * 
 * @author adminjack
 *
 */
public interface DeployTaskStatus {
	/**
	 * 未就绪
	 */
	String NOT_READY = "未就绪";

	/**
	 * 部署成功
	 */
	String SUCCESS = "部署成功";
	/**
	 * 部署失败
	 */
	String FAILURE = "部署失败";
	/**
	 * 等待部署
	 */
	String WAIT_FOR_DEPLOY = "等待部署";
	/**
	 * 等待执行部署
	 */
	String WAIT_FOR_EXEC_DEPLOY = "等待执行部署";
	/**
	 * 执行中
	 */
	String RUNNING = "执行中";

	/**
	 * 任务已终止
	 */
	String ABORT = "任务已终止";
}
