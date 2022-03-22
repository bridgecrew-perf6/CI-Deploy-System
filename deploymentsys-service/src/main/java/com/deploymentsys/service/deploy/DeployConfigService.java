package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.manager.deploy.DeployConfigManager;
import com.deploymentsys.manager.deploy.DeployFlowManager;

@Service
public class DeployConfigService {

	@Autowired
	private DeployConfigManager deployConfigManager;
	@Autowired
	private DeployFlowManager deployFlowManager;

	public List<DeployConfigBean> list(int pageStart, int size, int appId) {
		return deployConfigManager.list(pageStart, size, appId);
	}
	
	public List<DeployConfigBean> getListByAppId(int appId) {
		return deployConfigManager.getListByAppId(appId);
	}

	public int getListCount(int appId) {
		return deployConfigManager.getListCount(appId);
	}
	
	public DeployConfigBean getDeployConfig(int id) {
		return deployConfigManager.getDeployConfig(id);		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		for (int i : ids) {
			deployFlowManager.softDeleteByConfigId(i);
		}
		
		return deployConfigManager.softDelete(ids);
	}
	
	public int add(DeployConfigBean bean) {
		return deployConfigManager.add(bean);
	}
	
	public int update(DeployConfigBean bean) {
		return deployConfigManager.update(bean);
	}

}
