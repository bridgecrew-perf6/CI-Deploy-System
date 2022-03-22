package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployAppBean;

public interface DeployAppMapper {

	int add(DeployAppBean bean);

	int updateApp(DeployAppBean bean);

	List<DeployAppBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("appName") String appName);

	int getListCount(@Param("appName") String appName);

	DeployAppBean getDeployApp(@Param("id") int id);

	DeployAppBean getDeployAppByName(@Param("appName") String appName);

	// int delete(int[] ids);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);

	int softDeleteByProjectId(int projectId);

}