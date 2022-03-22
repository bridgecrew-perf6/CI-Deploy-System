package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.RoleMenuBean;
import com.deploymentsys.dao.RoleMenuMapper;

@Component
public class RoleMenuManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleMenuManager.class);

	@Autowired
	RoleMenuMapper roleMenuMapper;

	public List<Integer> getRoleMenuRelation(int roleId) {
		try {
			return roleMenuMapper.getRoleMenuRelation(roleId);
		} catch (Exception ex) {
			LOGGER.error("RoleMenuManager.getRoleMenuRelation catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public int add(RoleMenuBean bean) {
		// 如果要使用Spring的事务管理，则不可自己加上try catch
		return roleMenuMapper.add(bean);
	}

	public int addBatch(List<RoleMenuBean> list) {
		return roleMenuMapper.addBatch(list);
	}

	public int delete(int roleId) {
		return roleMenuMapper.delete(roleId);
	}

	public int deleteByMenuId(int menuId) {
		return roleMenuMapper.deleteByMenuId(menuId);
	}

}
