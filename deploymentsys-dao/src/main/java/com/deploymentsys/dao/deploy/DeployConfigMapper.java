package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployConfigBean;

public interface DeployConfigMapper {

	List<DeployConfigBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("appId") int appId);
	
	List<DeployConfigBean> getListByAppId(@Param("appId") int appId);

	int getListCount(@Param("appId") int appId);
	
	DeployConfigBean getDeployConfig(@Param("id") int id);
	
	int softDeleteByAppId(@Param("appId") int appId);
	
	int softDelete(int[] ids);
	
	int add(DeployConfigBean bean);
	
	int update(DeployConfigBean bean);
}