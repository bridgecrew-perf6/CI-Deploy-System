package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployFlowServerToDoBean;
import com.deploymentsys.manager.deploy.DeployFlowServerToDoManager;

@Service
public class DeployFlowServerToDoService {

	@Autowired
	private DeployFlowServerToDoManager deployFlowServerToDoManager;

	public List<DeployFlowServerToDoBean> list(int pageStart, int size, int flowServerId) {
		return deployFlowServerToDoManager.list(pageStart, size, flowServerId);
	}

	public int getListCount(int flowServerId) {
		return deployFlowServerToDoManager.getListCount(flowServerId);
	}

	public DeployFlowServerToDoBean getOneById(int id) {
		return deployFlowServerToDoManager.getOneById(id);
	}

	public int softDelete(int[] ids) {
		return deployFlowServerToDoManager.softDelete(ids);
	}

	public int add(DeployFlowServerToDoBean bean) {
		return deployFlowServerToDoManager.add(bean);
	}

	public int update(DeployFlowServerToDoBean bean) {
		return deployFlowServerToDoManager.update(bean);
	}

}