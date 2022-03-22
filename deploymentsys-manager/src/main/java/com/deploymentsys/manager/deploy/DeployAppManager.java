package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.dao.deploy.DeployAppMapper;

@Component
public class DeployAppManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployAppManager.class);

	@Autowired
	DeployAppMapper deployAppMapper;

	public List<DeployAppBean> list(int pageStart, int size, String appName) {
		try {
			return deployAppMapper.list(pageStart, size, appName);
		} catch (Exception ex) {
			LOGGER.error("分页获取应用列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String appName) {
		try {
			return deployAppMapper.getListCount(appName);
		} catch (Exception ex) {
			LOGGER.error("获取应用列表总和发生异常", ex);
			return 0;
		}
	}

	public DeployAppBean getDeployApp(int id) {
		try {
			return deployAppMapper.getDeployApp(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取部属应用发生异常", ex);
			return new DeployAppBean();
		}
	}

	public DeployAppBean getDeployAppByName(String appName) {
		try {
			return deployAppMapper.getDeployAppByName(appName);
		} catch (Exception ex) {
			LOGGER.error("根据appName获取部属应用发生异常", ex);
			return new DeployAppBean();
		}
	}

	public int add(DeployAppBean bean) {
		try {
			return deployAppMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加部署应用发生异常", ex);
			return 0;
		}
	}

	public int updateApp(DeployAppBean bean) {
		try {
			return deployAppMapper.updateApp(bean);
		} catch (Exception ex) {
			LOGGER.error("修改应用信息发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		return deployAppMapper.softDelete(ids);
	}

	public int softDeleteByProjectId(int projectId) {
		return deployAppMapper.softDeleteByProjectId(projectId);
	}

}
