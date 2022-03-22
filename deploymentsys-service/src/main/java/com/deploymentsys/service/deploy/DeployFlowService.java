package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.deploy.DeployFlowBean;
import com.deploymentsys.manager.deploy.DeployFlowManager;
import com.deploymentsys.manager.deploy.DeployFlowServerManager;

@Service
public class DeployFlowService {

	@Autowired
	private DeployFlowManager deployFlowManager;

	@Autowired
	private DeployFlowServerManager flowServerManager;

	public List<DeployFlowBean> list(int pageStart, int size, int deployConfigId) {
		return deployFlowManager.list(pageStart, size, deployConfigId);
	}

	public int getListCount(int deployConfigId) {
		return deployFlowManager.getListCount(deployConfigId);
	}

	public DeployFlowBean getDeployFlow(int id) {
		return deployFlowManager.getDeployFlow(id);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		for (int i : ids) {
			flowServerManager.softDeleteByFlowId(i);
		}
		return deployFlowManager.softDelete(ids);
	}

	public int add(DeployFlowBean bean) {
		return deployFlowManager.add(bean);
	}
	
	public int update(DeployFlowBean bean) {
		return deployFlowManager.update(bean);
	}

}
