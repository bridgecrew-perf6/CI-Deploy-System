package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployProjectVersionBean;

public interface DeployProjectVersionMapper {

	int add(DeployProjectVersionBean bean);

	DeployProjectVersionBean getById(@Param("id") int id);

	List<DeployProjectVersionBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("projectId") int projectId);

	List<DeployProjectVersionBean> listAllByProjectId(@Param("projectId") int projectId);

	int getListCount(@Param("projectId") int projectId);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int softDeleteByProjectId(int projectId);

	int update(DeployProjectVersionBean bean);

}