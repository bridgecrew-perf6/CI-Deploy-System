package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.RoleBean;

public interface RoleMapper {

	int add(RoleBean bean);

	RoleBean getRole(@Param("id") int id);

	List<RoleBean> list(@Param("pageStart") int pageStart, @Param("size") int size, @Param("name") String name);	

	int getListCount(@Param("name") String name);

	List<RoleBean> getAllRoles();

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int updateRole(@Param("id") int id, @Param("name") String name, @Param("description") String description,
			@Param("modifyDate") String modifyDate, @Param("modifier") int modifier,
			@Param("modifyIp") String modifyIp);

}