package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.MenuGroupBean;
import com.deploymentsys.dao.MenuGroupMapper;

@Component
public class MenuGroupManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuGroupManager.class);

	@Autowired
	MenuGroupMapper menuGroupMapper;
	
	public List<MenuGroupBean> getStaffMenuGroups(int staffId) {
		try {
			return menuGroupMapper.getStaffMenuGroups(staffId);
		} catch (Exception ex) {
			LOGGER.error("MenuGroupManager.getStaffMenuGroups catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<MenuGroupBean> getAllMenuGroups() {
		try {
			return menuGroupMapper.getAllMenuGroups();
		} catch (Exception ex) {
			LOGGER.error("MenuGroupManager.getAllMenuGroups catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<MenuGroupBean> list(int pageStart, int size, String name) {
		try {
			return menuGroupMapper.list(pageStart, size, name);
		} catch (Exception ex) {
			LOGGER.error("分页获取功能组列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public MenuGroupBean getMenuGroup(int id) {
		try {
			return menuGroupMapper.getMenuGroup(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取功能组发生异常", ex);
			return new MenuGroupBean();
		}
	}

	public int getListCount(String name) {
		try {
			return menuGroupMapper.getListCount(name);
		} catch (Exception ex) {
			LOGGER.error("获取功能组列表总和发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		try {
			return menuGroupMapper.softDelete(ids);
		} catch (Exception ex) {
			LOGGER.error("软删除功能组发生异常", ex);
			return 0;
		}
	}

	public int add(MenuGroupBean bean) {
		try {
			return menuGroupMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加功能组发生异常", ex);
			return 0;
		}
	}

	public int updateMenuGroup(MenuGroupBean bean) {
		try {
			return menuGroupMapper.updateMenuGroup(bean);
		} catch (Exception ex) {
			LOGGER.error("修改功能组信息发生异常", ex);
			return 0;
		}
	}

}
