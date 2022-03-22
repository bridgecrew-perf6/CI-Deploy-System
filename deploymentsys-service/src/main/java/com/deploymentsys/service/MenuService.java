package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.MenuBean;
import com.deploymentsys.manager.MenuManager;
import com.deploymentsys.manager.RoleMenuManager;

@Service
public class MenuService {

	@Autowired
	private MenuManager menuManager;
	
	@Autowired
	private RoleMenuManager roleMenuManager;

	public List<MenuBean> getMenusByMenuGroupId(int menuGroupId) {
		return menuManager.getMenusByMenuGroupId(menuGroupId);
	}

	public List<MenuBean> getAllMenus() {
		return menuManager.getAllMenus();
	}

	public List<String> getStaffPermissions(int staffId) {
		return menuManager.getStaffPermissions(staffId);
	}

	public List<MenuBean> getStaffMenus(int staffId) {
		return menuManager.getStaffMenus(staffId);
	}

	public List<MenuBean> list(int pageStart, int size, String name, String url, Integer menuGroupId) {
		return menuManager.list(pageStart, size, name, url, menuGroupId);
	}

	public MenuBean getMenu(int id) {
		return menuManager.getMenu(id);
	}

	public int getListCount(String name, String url, Integer menuGroupId) {
		return menuManager.getListCount(name, url, menuGroupId);
	}

	/**
	 * 软删除菜单，把角色菜单（权限）中间表中的数据也删掉
	 * @param ids
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		for (int menuId : ids) {
			roleMenuManager.deleteByMenuId(menuId);
		}
		return menuManager.softDelete(ids);
	}

	public int add(MenuBean bean) {
		return menuManager.add(bean);
	}

	public int updateMenu(MenuBean bean) {
		return menuManager.updateMenu(bean);
	}
}
