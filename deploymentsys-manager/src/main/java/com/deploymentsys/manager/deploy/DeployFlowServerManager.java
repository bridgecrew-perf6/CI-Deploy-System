package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployFlowServerBean;
import com.deploymentsys.dao.deploy.DeployFlowServerMapper;

@Component
public class DeployFlowServerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowServerManager.class);

	@Autowired
	DeployFlowServerMapper deployFlowServerMapper;

	public List<DeployFlowServerBean> list(int pageStart, int size, int deployFlowId) {
		try {
			return deployFlowServerMapper.list(pageStart, size, deployFlowId);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署配置流程服务器列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public List<DeployFlowServerBean> getListByFlowId(int flowId) {
		return deployFlowServerMapper.getListByFlowId(flowId);
	}

	public int getListCount(int deployFlowId) {
		try {
			return deployFlowServerMapper.getListCount(deployFlowId);
		} catch (Exception ex) {
			LOGGER.error("获取部署配置流程服务器列表总和发生异常", ex);
			return 0;
		}
	}

	public DeployFlowServerBean getDeployFlowServer(int id) {
		try {
			return deployFlowServerMapper.getDeployFlowServer(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取部属配置流程服务器发生异常", ex);
			return new DeployFlowServerBean();
		}
	}

	public DeployFlowServerBean getDeployFlowServer(int flowId, int targetServerId) {
		try {
			return deployFlowServerMapper.getDeployFlowServer2(flowId, targetServerId);
		} catch (Exception ex) {
			LOGGER.error("根据flowId和targetServerId获取部属配置流程服务器发生异常", ex);
			return new DeployFlowServerBean();
		}
	}

	public int softDelete(int[] ids) {
		return deployFlowServerMapper.softDelete(ids);
	}

	public int softDeleteByFlowId(int flowId) {
		return deployFlowServerMapper.softDeleteByFlowId(flowId);
	}

	public int update(DeployFlowServerBean bean) {
		try {
			return deployFlowServerMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("修改部署流程服务器发生异常", ex);
			return 0;
		}
	}

	public int add(DeployFlowServerBean bean) {
		return deployFlowServerMapper.add(bean);
	}

}
