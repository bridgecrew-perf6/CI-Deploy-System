package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.RoleBean;
import com.deploymentsys.dao.RoleMapper;

@Component
public class RoleManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleManager.class);

	@Autowired
	RoleMapper roleMapper;

	public List<RoleBean> getAllRoles() {
		try {
			return roleMapper.getAllRoles();
		} catch (Exception ex) {
			LOGGER.error("RoleManager.getAllRoles catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<RoleBean> list(int pageStart, int size, String name) {
		try {
			return roleMapper.list(pageStart, size, name);
		} catch (Exception ex) {
			LOGGER.error("分页获取角色列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public RoleBean getRole(int id) {
		try {
			return roleMapper.getRole(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取角色发生异常", ex);
			return new RoleBean();
		}
	}

	public int getListCount(String name) {
		try {
			return roleMapper.getListCount(name);
		} catch (Exception ex) {
			LOGGER.error("获取角色列表总和发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		return roleMapper.softDelete(ids);
	}

	public int add(RoleBean bean) {
		try {
			return roleMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加角色发生异常", ex);
			return 0;
		}
	}

	public int updateRole(int id, String name, String description, String modifyDate, int modifier, String modifyIp) {
		try {
			return roleMapper.updateRole(id, name, description, modifyDate, modifier, modifyIp);
		} catch (Exception ex) {
			LOGGER.error("修改角色信息发生异常", ex);
			return 0;
		}
	}

}
