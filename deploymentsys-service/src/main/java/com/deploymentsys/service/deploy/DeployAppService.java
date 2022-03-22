package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.manager.deploy.DeployAppManager;
import com.deploymentsys.manager.deploy.DeployConfigManager;

@Service
public class DeployAppService {

	@Autowired
	private DeployAppManager deployAppManager;
	@Autowired
	private DeployConfigManager deployConfigManager;

	public List<DeployAppBean> list(int pageStart, int size, String appName) {
		return deployAppManager.list(pageStart, size, appName);
	}

	public int getListCount(String appName) {
		return deployAppManager.getListCount(appName);
	}

	public DeployAppBean getDeployApp(int id) {
		return deployAppManager.getDeployApp(id);		
	}
	
	public DeployAppBean getDeployAppByName(String appName) {
		return deployAppManager.getDeployAppByName(appName);		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		for (int i : ids) {
			deployConfigManager.softDeleteByAppId(i);
		}
		
		return deployAppManager.softDelete(ids);
	}
	
	public int add(DeployAppBean bean) {
		return deployAppManager.add(bean);
	}
	
	public int updateApp(DeployAppBean bean) {
		return deployAppManager.updateApp(bean);
	}
}
