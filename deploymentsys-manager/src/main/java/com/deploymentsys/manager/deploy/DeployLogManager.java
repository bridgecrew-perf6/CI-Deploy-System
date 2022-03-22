package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployLogBean;
import com.deploymentsys.dao.deploy.DeployLogMapper;

@Component
public class DeployLogManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployLogManager.class);

	@Autowired
	DeployLogMapper deployLogMapper;

	public int add(DeployLogBean bean) {
		return deployLogMapper.add(bean);
	}

	public List<DeployLogBean> listByTaskId(int pageStart, int size, int taskId) {
		try {
			return deployLogMapper.listByTaskId(pageStart, size, taskId);
		} catch (Exception ex) {
			LOGGER.error("获取日志列表发生异常: {}, 参数taskId：{}", ex, taskId);
			return new ArrayList<>();
		}
	}

	public int getListCountByTaskId(int taskId) {
		try {
			return deployLogMapper.getListCountByTaskId(taskId);
		} catch (Exception ex) {
			LOGGER.error("获取日志列表总和发生异常：{}, 参数taskId：{}", ex, taskId);
			return 0;
		}
	}

	public List<DeployLogBean> listByTaskServerId(int pageStart, int size, int taskId, int taskServerId) {
		try {
			return deployLogMapper.listByTaskServerId(pageStart, size, taskId, taskServerId);
		} catch (Exception ex) {
			LOGGER.error("获取日志列表发生异常: {}, 参数taskId：{}, 参数taskServerId：{}", ex, taskId, taskServerId);
			return new ArrayList<>();
		}
	}

	public int getListCountByTaskServerId(int taskId, int taskServerId) {
		try {
			return deployLogMapper.getListCountByTaskServerId(taskId, taskServerId);
		} catch (Exception ex) {
			LOGGER.error("获取日志列表总和发生异常：{}, 参数taskId：{}, 参数taskServerId：{}", ex, taskId, taskServerId);
			return 0;
		}
	}

	public List<DeployLogBean> listByTaskServerToDoId(int pageStart, int size, int taskId, int taskServerId,
			int serverTodoId) {
		try {
			return deployLogMapper.listByTaskServerToDoId(pageStart, size, taskId, taskServerId, serverTodoId);
		} catch (Exception ex) {
			LOGGER.error("获取日志列表发生异常: {}, 参数taskId：{}, 参数taskServerId：{}, 参数serverTodoId：{}", ex, taskId, taskServerId,
					serverTodoId);
			return new ArrayList<>();
		}
	}

	public int getListCountByTaskServerToDoId(int taskId, int taskServerId, int serverTodoId) {
		try {
			return deployLogMapper.getListCountByTaskServerToDoId(taskId, taskServerId, serverTodoId);
		} catch (Exception ex) {
			LOGGER.error("获取日志列表总和发生异常：{}, 参数taskId：{}, 参数taskServerId：{}, 参数serverTodoId：{}", ex, taskId, taskServerId,
					serverTodoId);
			return 0;
		}
	}

}
