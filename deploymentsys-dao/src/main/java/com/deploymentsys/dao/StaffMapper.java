package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.StaffBean;

public interface StaffMapper {

	int add(StaffBean bean);

	String getStaffPwd(int id);
	
	StaffBean getById(@Param("id") int id);

	StaffBean getStaff(@Param("loginName") String loginName, @Param("password") String password);
	
	StaffBean getStaffByLoginName(@Param("loginName") String loginName);

	List<StaffBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("loginName") String loginName);

	int getListCount(@Param("loginName") String loginName);

	int delete(int[] ids);
	
	int update(StaffBean bean);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int changeStaffPwd(@Param("id") int id, @Param("password") String password, @Param("modifyDate") String modifyDate,
			@Param("modifier") int modifier, @Param("modifyIp") String modifyIp);

}