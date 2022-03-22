package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.deploy.DeployProjectBean;
import com.deploymentsys.manager.deploy.DeployAppManager;
import com.deploymentsys.manager.deploy.DeployProjectManager;
import com.deploymentsys.manager.deploy.DeployProjectVersionManager;

@Service
public class DeployProjectService {

	@Autowired
	private DeployProjectManager projectManager;
	@Autowired
	private DeployAppManager appManager;
	@Autowired
	private DeployProjectVersionManager projectVersionManager;

	public List<DeployProjectBean> list(int pageStart, int size, String projectName) {
		return projectManager.list(pageStart, size, projectName);
	}

	public DeployProjectBean getById(int id) {
		return projectManager.getById(id);
	}

	public int getListCount(String projectName) {
		return projectManager.getListCount(projectName);
	}

	/**
	 * 软删除项目，把下面的部署应用数据也删掉
	 * 
	 * @param ids
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		for (int projectId : ids) {
			// 软删除项目下面的应用数据
			appManager.softDeleteByProjectId(projectId);
			// 软删除项目下面的项目版本数据
			projectVersionManager.softDeleteByProjectId(projectId);
		}

		return projectManager.softDelete(ids);
	}

	public int add(DeployProjectBean bean) {
		return projectManager.add(bean);
	}

	public int update(DeployProjectBean bean) {
		return projectManager.update(bean);
	}
}
