package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployProjectBean;

public interface DeployProjectMapper {

	int add(DeployProjectBean bean);

	DeployProjectBean getById(@Param("id") int id);

	List<DeployProjectBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("projectName") String projectName);

	int getListCount(@Param("projectName") String projectName);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int update(DeployProjectBean bean);

}