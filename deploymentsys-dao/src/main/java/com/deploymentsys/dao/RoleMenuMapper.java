package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.RoleMenuBean;

public interface RoleMenuMapper {

	List<Integer> getRoleMenuRelation(@Param("roleId") int roleId);

	int add(RoleMenuBean bean);
	
	int addBatch(List<RoleMenuBean> list);

	int delete(@Param("roleId") int roleId);
	
	int deleteByMenuId(@Param("menuId") int menuId);

}