package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.StaffRoleBean;
import com.deploymentsys.manager.StaffRoleManager;

@Service
public class StaffRoleService {

	@Autowired
	private StaffRoleManager staffRoleManager;

	public List<Integer> getStaffRoleRelation(int staffId) {
		return staffRoleManager.getStaffRoleRelation(staffId);
	}

	public int delete(int staffId) {
		return staffRoleManager.delete(staffId);
	}

	public int add(StaffRoleBean bean) {
		return staffRoleManager.add(bean);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public void addNewStaffRoleRelation(int staffId, List<StaffRoleBean> beans) {
		// 先删除现有的所有关系
		delete(staffId);

		// 添加新关系
		// for (StaffRoleBean staffRoleBean : beans) {
		// add(staffRoleBean);
		// }
		if (beans.size() > 0) {
			staffRoleManager.addBatch(beans);
		}

	}

}
