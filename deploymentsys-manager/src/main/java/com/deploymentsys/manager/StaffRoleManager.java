package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.StaffRoleBean;
import com.deploymentsys.dao.StaffRoleMapper;

@Component
public class StaffRoleManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffRoleManager.class);

	@Autowired
	StaffRoleMapper staffRoleMapper;

	public List<Integer> getStaffRoleRelation(int staffId) {
		try {
			return staffRoleMapper.getStaffRoleRelation(staffId);
		} catch (Exception ex) {
			LOGGER.error("StaffRoleManager.getStaffRoleRelation catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public int add(StaffRoleBean bean) {
		return staffRoleMapper.add(bean);
	}

	public int addBatch(List<StaffRoleBean> list) {
		return staffRoleMapper.addBatch(list);
	}

	public int delete(int staffId) {
		return staffRoleMapper.delete(staffId);
	}

	public int deleteByRoleId(int roleId) {
		return staffRoleMapper.deleteByRoleId(roleId);
	}

}
