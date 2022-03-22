package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployFlowBean;
import com.deploymentsys.dao.deploy.DeployFlowMapper;

@Component
public class DeployFlowManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowManager.class);

	@Autowired
	DeployFlowMapper deployFlowMapper;

	public List<DeployFlowBean> list(int pageStart, int size, int deployConfigId) {
		try {
			return deployFlowMapper.list(pageStart, size, deployConfigId);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署配置流程列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public List<DeployFlowBean> getListByConfigId(int configId) {
		return deployFlowMapper.getListByConfigId(configId);
	}

	public int getListCount(int deployConfigId) {
		try {
			return deployFlowMapper.getListCount(deployConfigId);
		} catch (Exception ex) {
			LOGGER.error("获取部署配置流程列表总和发生异常", ex);
			return 0;
		}
	}

	public DeployFlowBean getDeployFlow(int id) {
		try {
			return deployFlowMapper.getDeployFlow(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取部属配置流程发生异常", ex);
			return new DeployFlowBean();
		}
	}

	public int softDelete(int[] ids) {
		return deployFlowMapper.softDelete(ids);
	}

	public int softDeleteByConfigId(int configId) {
		return deployFlowMapper.softDeleteByConfigId(configId);
	}

	public int add(DeployFlowBean bean) {
		try {
			return deployFlowMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加部署流程发生异常", ex);
			return 0;
		}
	}

	public int update(DeployFlowBean bean) {
		try {
			return deployFlowMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("修改部署流程发生异常", ex);
			return 0;
		}
	}

}
