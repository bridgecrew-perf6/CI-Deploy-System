package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.RoleMenuBean;
import com.deploymentsys.manager.RoleMenuManager;

@Service
public class RoleMenuService {

	@Autowired
	private RoleMenuManager roleMenuManager;

	public List<Integer> getRoleMenuRelation(int roleId) {
		return roleMenuManager.getRoleMenuRelation(roleId);
	}

	public int delete(int roleId) {
		return roleMenuManager.delete(roleId);
	}

	public int add(RoleMenuBean bean) {
		return roleMenuManager.add(bean);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public void addNewRoleMenuRelation(int roleId, List<RoleMenuBean> beans) {
		// 先删除现有的所有关系
		delete(roleId);

		// 添加新关系
		// for (RoleMenuBean roleMenuBean : beans) {
		// add(roleMenuBean);
		// }
		if (beans.size() > 0) {
			roleMenuManager.addBatch(beans);
		}

	}

}
