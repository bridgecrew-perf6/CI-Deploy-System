package com.deploymentsys.service.deploy;

import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.deploy.DeployLogBean;

public interface IDeployLogService {

	/**
	 * 按照taskId来记录日志
	 * 
	 * @param taskId
	 * @param logContent
	 * @param baseBean
	 * @return
	 */
	void log(int taskId, String logContent, BaseBean baseBean);

	/**
	 * 按照taskId、targetServerId来记录日志
	 * 
	 * @param taskId
	 * @param targetServerId
	 * @param logContent
	 * @param baseBean
	 * @return
	 */
	void log(int taskId, int targetServerId, String logContent, BaseBean baseBean);

	/**
	 * 按照taskId、targetServerId、serverTodoId来记录日志
	 * 
	 * @param taskId
	 * @param targetServerId
	 * @param serverTodoId
	 * @param logContent
	 * @param baseBean
	 * @return
	 */
	void log(int taskId, int targetServerId, int serverTodoId, String logContent, BaseBean baseBean);

	void log(DeployLogBean logBean);
}
