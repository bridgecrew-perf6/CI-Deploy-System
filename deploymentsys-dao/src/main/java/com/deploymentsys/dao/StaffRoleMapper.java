package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.StaffRoleBean;

public interface StaffRoleMapper {

	List<Integer> getStaffRoleRelation(@Param("staffId") int staffId);

	int add(StaffRoleBean bean);

	int addBatch(List<StaffRoleBean> list);

	int delete(@Param("staffId") int staffId);

	int deleteByRoleId(@Param("roleId") int roleId);

}