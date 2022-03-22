package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.RoleBean;
import com.deploymentsys.manager.RoleManager;
import com.deploymentsys.manager.RoleMenuManager;
import com.deploymentsys.manager.StaffRoleManager;

@Service
public class RoleService {

	@Autowired
	private RoleManager roleManager;
	@Autowired
	private RoleMenuManager roleMenuManager;
	@Autowired
	private StaffRoleManager staffRoleManager;

	public List<RoleBean> getAllRoles() {
		return roleManager.getAllRoles();
	}

	public List<RoleBean> list(int pageStart, int size, String name) {
		return roleManager.list(pageStart, size, name);
	}

	public RoleBean getRole(int id) {
		return roleManager.getRole(id);
	}

	public int getListCount(String name) {
		return roleManager.getListCount(name);
	}

	/**
	 * 软删除角色，把角色菜单（权限）中间表、角色人员中间表中的数据也删掉
	 * @param ids
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {		
		for (int roleId : ids) {
			roleMenuManager.delete(roleId);
			staffRoleManager.deleteByRoleId(roleId);
		}
		
		return roleManager.softDelete(ids);
	}

	public int add(RoleBean bean) {
		return roleManager.add(bean);
	}

	public int updateRole(int id, String name, String description, String modifyDate, int modifier, String modifyIp) {
		return roleManager.updateRole(id, name, description, modifyDate, modifier, modifyIp);
	}
}
