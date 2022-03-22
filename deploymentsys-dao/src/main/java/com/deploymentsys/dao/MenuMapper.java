package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.MenuBean;

public interface MenuMapper {

	int add(MenuBean bean);

	MenuBean getMenu(@Param("id") int id);

	/**
	 * 根据menuGroupId获取功能
	 * 
	 * @param menuGroupId
	 * @return
	 */
	List<MenuBean> getMenusByMenuGroupId(@Param("menuGroupId") int menuGroupId);

	List<MenuBean> getAllMenus();

	/**
	 * 根据staffId获取该人员的所有操作权限
	 * 
	 * @param staffId
	 * @return
	 */
	List<String> getStaffPermissions(@Param("staffId") int staffId);

	/**
	 * 根据人员id获取该人员所拥有的功能（权限）
	 * 
	 * @param staffId
	 * @return
	 */
	List<MenuBean> getStaffMenus(@Param("staffId") int staffId);

	List<MenuBean> list(@Param("pageStart") int pageStart, @Param("size") int size, @Param("name") String name,
			@Param("url") String url, @Param("menuGroupId") Integer menuGroupId);

	int getListCount(@Param("name") String name, @Param("url") String url, @Param("menuGroupId") Integer menuGroupId);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int updateMenu(MenuBean bean);

}