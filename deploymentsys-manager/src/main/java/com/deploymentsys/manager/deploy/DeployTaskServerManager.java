package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployTaskServerBean;
import com.deploymentsys.dao.deploy.DeployTaskServerMapper;

@Component
public class DeployTaskServerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskServerManager.class);

	@Autowired
	DeployTaskServerMapper taskServerMapper;

	public List<DeployTaskServerBean> listByStatus(String status) {
		return taskServerMapper.listByStatus(status);
	}

	public List<DeployTaskServerBean> listAllByTaskId(int taskId) {
		return taskServerMapper.listAllByTaskId(taskId);
	}

	public DeployTaskServerBean getById(int id) {
		return taskServerMapper.getById(id);
	}

	public List<DeployTaskServerBean> list(int pageStart, int size, int taskId) {
		try {
			return taskServerMapper.list(pageStart, size, taskId);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署任务服务器列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(int taskId) {
		try {
			return taskServerMapper.getListCount(taskId);
		} catch (Exception ex) {
			LOGGER.error("获取部署任务服务器列表总和发生异常", ex);
			return 0;
		}
	}

	public int addBatch(List<DeployTaskServerBean> list) {
		return taskServerMapper.addBatch(list);
	}

	public int updateStatusByTaskId(DeployTaskServerBean bean) {
		return taskServerMapper.updateStatusByTaskId(bean);
	}

	public int updateStatus(DeployTaskServerBean bean) {
		return taskServerMapper.updateStatus(bean);
	}

}
