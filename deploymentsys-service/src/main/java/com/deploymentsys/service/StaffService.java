package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.manager.StaffManager;
import com.deploymentsys.manager.StaffRoleManager;

@Service
public class StaffService {

	@Autowired
	private StaffManager staffManager;

	@Autowired
	private StaffRoleManager staffRoleManager;

	public String getStaffPwd(int id) {
		return staffManager.getStaffPwd(id);
	}

	public List<StaffBean> list(int pageStart, int size, String loginName) {
		return staffManager.list(pageStart, size, loginName);
	}

	public int getListCount(String loginName) {
		return staffManager.getListCount(loginName);
	}

	public StaffBean getStaff(String loginName, String password) {
		return staffManager.getStaff(loginName, password);
	}

	public StaffBean getById(int id) {
		return staffManager.getById(id);
	}

	public StaffBean getStaffByLoginName(String loginName) {
		return staffManager.getStaffByLoginName(loginName);
	}

	public int delete(int[] ids) {
		return staffManager.delete(ids);
	}

	public int add(StaffBean bean) {
		return staffManager.add(bean);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		// 先删除人员角色之间多对多的关系数据
		for (int i = 0; i < ids.length; i++) {
			staffRoleManager.delete(ids[i]);
		}

		return staffManager.softDelete(ids);
	}

	public int changeStaffPwd(int id, String password, String modifyDate, int modifier, String modifyIp) {
		return staffManager.changeStaffPwd(id, password, modifyDate, modifier, modifyIp);
	}

	public int update(StaffBean bean) {
		return staffManager.update(bean);
	}

}
