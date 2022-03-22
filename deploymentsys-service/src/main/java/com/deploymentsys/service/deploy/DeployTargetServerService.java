package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployTargetServerBean;
import com.deploymentsys.manager.deploy.DeployTargetServerManager;

@Service
public class DeployTargetServerService {

	@Autowired
	private DeployTargetServerManager deployTargetServerManager;

	public DeployTargetServerBean getDeployTargetServer(int id) {
		return deployTargetServerManager.getDeployTargetServer(id);
	}

	public List<DeployTargetServerBean> list(int pageStart, int size, String serverIp, String serverPort) {
		return deployTargetServerManager.list(pageStart, size, serverIp, serverPort);
	}

	public int getListCount(String serverIp, String serverPort) {
		return deployTargetServerManager.getListCount(serverIp, serverPort);
	}

	public int softDelete(int[] ids) {
		return deployTargetServerManager.softDelete(ids);
	}

	public int add(DeployTargetServerBean bean) {
		return deployTargetServerManager.add(bean);
	}

	public int update(DeployTargetServerBean bean) {
		return deployTargetServerManager.update(bean);
	}

}
