package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.deploy.DeployLogBean;
import com.deploymentsys.manager.deploy.DeployLogManager;

@Service
public class DeployLogServiceDbImpl implements IDeployLogService {

	@Autowired
	private DeployLogManager logManager;

	@Override
	public void log(int taskId, String logContent, BaseBean baseBean) {
		logManager.add(new DeployLogBean(taskId, 0, 0, logContent, baseBean.getCreateDate(), baseBean.getCreator(),
				baseBean.getCreateIp()));
		// return true;
	}

	@Override
	public void log(int taskId, int targetServerId, String logContent, BaseBean baseBean) {
		logManager.add(new DeployLogBean(taskId, targetServerId, 0, logContent, baseBean.getCreateDate(),
				baseBean.getCreator(), baseBean.getCreateIp()));
	}

	@Override
	public void log(int taskId, int targetServerId, int serverTodoId, String logContent, BaseBean baseBean) {
		logManager.add(new DeployLogBean(taskId, targetServerId, serverTodoId, logContent, baseBean.getCreateDate(),
				baseBean.getCreator(), baseBean.getCreateIp()));
	}

	@Override
	public void log(DeployLogBean logBean) {
		logManager.add(logBean);
	}

	public List<DeployLogBean> listByTaskId(int pageStart, int size, int taskId) {
		return logManager.listByTaskId(pageStart, size, taskId);
	}

	public int getListCountByTaskId(int taskId) {
		return logManager.getListCountByTaskId(taskId);
	}

	public List<DeployLogBean> listByTaskServerId(int pageStart, int size, int taskId, int taskServerId) {
		return logManager.listByTaskServerId(pageStart, size, taskId, taskServerId);
	}

	public int getListCountByTaskServerId(int taskId, int taskServerId) {
		return logManager.getListCountByTaskServerId(taskId, taskServerId);
	}

	public List<DeployLogBean> listByTaskServerToDoId(int pageStart, int size, int taskId, int taskServerId,
			int serverTodoId) {
		return logManager.listByTaskServerToDoId(pageStart, size, taskId, taskServerId, serverTodoId);
	}

	public int getListCountByTaskServerToDoId(int taskId, int taskServerId, int serverTodoId) {
		return logManager.getListCountByTaskServerToDoId(taskId, taskServerId, serverTodoId);
	}

}
