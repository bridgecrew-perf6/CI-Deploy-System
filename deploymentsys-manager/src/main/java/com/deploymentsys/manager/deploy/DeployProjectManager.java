package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployProjectBean;
import com.deploymentsys.dao.deploy.DeployProjectMapper;

@Component
public class DeployProjectManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployProjectManager.class);

	@Autowired
	DeployProjectMapper projectMapper;

	public List<DeployProjectBean> list(int pageStart, int size, String projectName) {
		try {
			return projectMapper.list(pageStart, size, projectName);
		} catch (Exception ex) {
			LOGGER.error("分页获取项目列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public DeployProjectBean getById(int id) {
		try {
			return projectMapper.getById(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取项目发生异常", ex);
			return new DeployProjectBean();
		}
	}

	public int getListCount(String projectName) {
		try {
			return projectMapper.getListCount(projectName);
		} catch (Exception ex) {
			LOGGER.error("获取项目列表总和发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		return projectMapper.softDelete(ids);
	}

	public int add(DeployProjectBean bean) {
		try {
			return projectMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加项目发生异常", ex);
			return 0;
		}
	}

	public int update(DeployProjectBean bean) {
		try {
			return projectMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("编辑项目发生异常", ex);
			return 0;
		}
	}

}
