package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployProjectVersionBean;
import com.deploymentsys.dao.deploy.DeployProjectVersionMapper;

@Component
public class DeployProjectVersionManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployProjectVersionManager.class);

	@Autowired
	DeployProjectVersionMapper projectVersionMapper;

	public List<DeployProjectVersionBean> list(int pageStart, int size, int projectId) {
		try {
			return projectVersionMapper.list(pageStart, size, projectId);
		} catch (Exception ex) {
			LOGGER.error("分页获取项目版本列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public List<DeployProjectVersionBean> listAllByProjectId(int projectId) {
		try {
			return projectVersionMapper.listAllByProjectId(projectId);
		} catch (Exception ex) {
			LOGGER.error("根据projectId获取项目版本列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public DeployProjectVersionBean getById(int id) {
		try {
			return projectVersionMapper.getById(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取项目版本发生异常", ex);
			return null;
		}
	}

	public int getListCount(int projectId) {
		try {
			return projectVersionMapper.getListCount(projectId);
		} catch (Exception ex) {
			LOGGER.error("获取项目版本列表总和发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		return projectVersionMapper.softDelete(ids);
	}

	public int softDeleteByProjectId(int projectId) {
		return projectVersionMapper.softDeleteByProjectId(projectId);
	}

	public int add(DeployProjectVersionBean bean) {
		try {
			return projectVersionMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加项目版本发生异常", ex);
			return 0;
		}
	}

	public int update(DeployProjectVersionBean bean) {
		try {
			return projectVersionMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("编辑项目版本发生异常", ex);
			return 0;
		}
	}

}
