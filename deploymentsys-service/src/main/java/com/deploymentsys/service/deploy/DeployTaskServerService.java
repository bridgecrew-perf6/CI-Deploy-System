package com.deploymentsys.service.deploy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployTaskServerBean;
import com.deploymentsys.manager.deploy.DeployTaskServerManager;

@Service
public class DeployTaskServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskServerService.class);

	@Autowired
	private DeployTaskServerManager taskServerManager;

	public List<DeployTaskServerBean> list(int pageStart, int size, int taskId) {
		return taskServerManager.list(pageStart, size, taskId);
	}

	public int getListCount(int taskId) {
		return taskServerManager.getListCount(taskId);
	}

}
