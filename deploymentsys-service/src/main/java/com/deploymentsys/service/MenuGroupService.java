package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.MenuGroupBean;
import com.deploymentsys.manager.MenuGroupManager;

@Service
public class MenuGroupService {

	@Autowired
	private MenuGroupManager menuGroupManager;

	public List<MenuGroupBean> getStaffMenuGroups(int staffId) {
		return menuGroupManager.getStaffMenuGroups(staffId);
	}

	public List<MenuGroupBean> getAllMenuGroups() {
		return menuGroupManager.getAllMenuGroups();
	}

	public List<MenuGroupBean> list(int pageStart, int size, String name) {
		return menuGroupManager.list(pageStart, size, name);
	}

	public MenuGroupBean getMenuGroup(int id) {
		return menuGroupManager.getMenuGroup(id);
	}

	public int getListCount(String name) {
		return menuGroupManager.getListCount(name);
	}

	public int softDelete(int[] ids) {
		return menuGroupManager.softDelete(ids);
	}

	public int add(MenuGroupBean bean) {
		return menuGroupManager.add(bean);
	}

	public int updateMenuGroup(MenuGroupBean bean) {
		return menuGroupManager.updateMenuGroup(bean);
	}
}
