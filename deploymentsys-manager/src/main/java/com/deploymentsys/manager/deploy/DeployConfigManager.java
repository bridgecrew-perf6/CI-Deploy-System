package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.dao.deploy.DeployConfigMapper;

@Component
public class DeployConfigManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployConfigManager.class);

	@Autowired
	DeployConfigMapper deployConfigMapper;

	public List<DeployConfigBean> list(int pageStart, int size, int appId) {
		try {
			return deployConfigMapper.list(pageStart, size, appId);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署配置列表发生异常", ex);
			return new ArrayList<>();
		}
	}
	
	public List<DeployConfigBean> getListByAppId(int appId) {
		try {
			return deployConfigMapper.getListByAppId(appId);
		} catch (Exception ex) {
			LOGGER.error("根据appId获取配置列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(int appId) {
		try {
			return deployConfigMapper.getListCount(appId);
		} catch (Exception ex) {
			LOGGER.error("获取部署配置列表总和发生异常", ex);
			return 0;
		}
	}

	public DeployConfigBean getDeployConfig(int id) {
		try {
			return deployConfigMapper.getDeployConfig(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取部属配置发生异常", ex);
			return new DeployConfigBean();
		}
	}

	public int softDeleteByAppId(int appId) {
		return deployConfigMapper.softDeleteByAppId(appId);
	}
	
	public int softDelete(int[] ids) {		
		return deployConfigMapper.softDelete(ids);
	}
	
	public int add(DeployConfigBean bean) {
		try {
			return deployConfigMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加部署配置发生异常", ex);
			return 0;
		}
	}
	
	public int update(DeployConfigBean bean) {
		try {
			return deployConfigMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("编辑部署配置发生异常", ex);
			return 0;
		}
	}

}
