package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployProjectVersionBean;
import com.deploymentsys.manager.deploy.DeployProjectVersionManager;

@Service
public class DeployProjectVersionService {

	@Autowired
	private DeployProjectVersionManager projectVersionManager;

	public List<DeployProjectVersionBean> list(int pageStart, int size, int projectId) {
		return projectVersionManager.list(pageStart, size, projectId);
	}

	public List<DeployProjectVersionBean> listAllByProjectId(int projectId) {
		return projectVersionManager.listAllByProjectId(projectId);
	}

	public DeployProjectVersionBean getById(int id) {
		return projectVersionManager.getById(id);
	}

	public int getListCount(int projectId) {
		return projectVersionManager.getListCount(projectId);
	}

	/**
	 * 软删除版本
	 * 
	 * @param ids
	 * @return
	 */
	public int softDelete(int[] ids) {
		return projectVersionManager.softDelete(ids);
	}

	public int add(DeployProjectVersionBean bean) {
		return projectVersionManager.add(bean);
	}

	public int update(DeployProjectVersionBean bean) {
		return projectVersionManager.update(bean);
	}
}
