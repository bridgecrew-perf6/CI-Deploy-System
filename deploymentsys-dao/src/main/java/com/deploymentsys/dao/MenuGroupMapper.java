package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.MenuGroupBean;

public interface MenuGroupMapper {

	int add(MenuGroupBean bean);

	MenuGroupBean getMenuGroup(@Param("id") int id);

	List<MenuGroupBean> getAllMenuGroups();

	/**
	 * 根据人员id获取该人员所拥有的功能组（权限）
	 * 
	 * @param staffId
	 * @return
	 */
	List<MenuGroupBean> getStaffMenuGroups(@Param("staffId") int staffId);

	List<MenuGroupBean> list(@Param("pageStart") int pageStart, @Param("size") int size, @Param("name") String name);

	int getListCount(@Param("name") String name);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int updateMenuGroup(MenuGroupBean bean);

}