package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.dao.deploy.DeployTaskServerToDoMapper;

@Component
public class DeployTaskServerToDoManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskServerToDoManager.class);

	@Autowired
	DeployTaskServerToDoMapper taskServerToDoMapper;

	public List<DeployTaskServerToDoBean> listAllByTaskServerId(int taskServerId) {
		return taskServerToDoMapper.listAllByTaskServerId(taskServerId);
	}

	public List<DeployTaskServerToDoBean> list(int pageStart, int size, int taskServerId) {
		try {
			return taskServerToDoMapper.list(pageStart, size, taskServerId);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署任务服务器todo列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(int taskServerId) {
		try {
			return taskServerToDoMapper.getListCount(taskServerId);
		} catch (Exception ex) {
			LOGGER.error("获取部署任务服务器todo列表总和发生异常", ex);
			return 0;
		}
	}

	public List<DeployTaskServerToDoBean> listByStatus(String status) {
		return taskServerToDoMapper.listByStatus(status);
	}

	public int addBatch(List<DeployTaskServerToDoBean> list) {
		return taskServerToDoMapper.addBatch(list);
	}

	public int updateStatusByServerTaskId(DeployTaskServerToDoBean bean) {
		return taskServerToDoMapper.updateStatusByServerTaskId(bean);
	}

	public int updateStatus(DeployTaskServerToDoBean bean) {
		return taskServerToDoMapper.updateStatus(bean);
	}

	public DeployTaskServerToDoBean getById(int id) {
		return taskServerToDoMapper.getById(id);
	}

}
