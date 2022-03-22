package com.deploymentsys.service.deploy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.manager.deploy.DeployTaskServerToDoManager;

@Service
public class DeployTaskServerToDoService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskServerToDoService.class);

	@Autowired
	private DeployTaskServerToDoManager taskServerToDoManager;

	public List<DeployTaskServerToDoBean> list(int pageStart, int size, int taskServerId) {
		return taskServerToDoManager.list(pageStart, size, taskServerId);
	}

	public int getListCount(int taskServerId) {
		return taskServerToDoManager.getListCount(taskServerId);
	}

}
