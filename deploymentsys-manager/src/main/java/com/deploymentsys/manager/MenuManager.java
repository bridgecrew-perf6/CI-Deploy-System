package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.MenuBean;
import com.deploymentsys.dao.MenuMapper;

@Component
public class MenuManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuManager.class);

	@Autowired
	MenuMapper menuMapper;

	public List<MenuBean> getMenusByMenuGroupId(int menuGroupId) {
		try {
			return menuMapper.getMenusByMenuGroupId(menuGroupId);
		} catch (Exception ex) {
			LOGGER.error("MenuManager.getMenusByMenuGroupId catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<MenuBean> getAllMenus() {
		try {
			return menuMapper.getAllMenus();
		} catch (Exception ex) {
			LOGGER.error("MenuManager.getAllMenus catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<String> getStaffPermissions(int staffId) {
		try {
			return menuMapper.getStaffPermissions(staffId);
		} catch (Exception ex) {
			LOGGER.error("MenuManager.getStaffPermissions catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<MenuBean> getStaffMenus(int staffId) {
		try {
			return menuMapper.getStaffMenus(staffId);
		} catch (Exception ex) {
			LOGGER.error("MenuManager.getStaffMenus catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<MenuBean> list(int pageStart, int size, String name, String url, Integer menuGroupId) {
		try {
			return menuMapper.list(pageStart, size, name, url, menuGroupId);
		} catch (Exception ex) {
			LOGGER.error("分页获取功能列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public MenuBean getMenu(int id) {
		try {
			return menuMapper.getMenu(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取功能发生异常", ex);
			return new MenuBean();
		}
	}

	public int getListCount(String name, String url, Integer menuGroupId) {
		try {
			return menuMapper.getListCount(name, url, menuGroupId);
		} catch (Exception ex) {
			LOGGER.error("获取功能列表总和发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		return menuMapper.softDelete(ids);
	}

	public int add(MenuBean bean) {
		try {
			return menuMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加功能发生异常", ex);
			return 0;
		}
	}

	public int updateMenu(MenuBean bean) {
		try {
			return menuMapper.updateMenu(bean);
		} catch (Exception ex) {
			LOGGER.error("修改功能信息发生异常", ex);
			return 0;
		}
	}

}
