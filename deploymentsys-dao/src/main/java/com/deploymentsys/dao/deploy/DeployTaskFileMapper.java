package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployTaskFileBean;

public interface DeployTaskFileMapper {

	List<DeployTaskFileBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("fileName") String fileName, @Param("appId") int appId, @Param("batchNo") String batchNo);

	List<DeployTaskFileBean> listAll(@Param("appId") int appId, @Param("batchNo") String batchNo);

	// List<DeployConfigBean> getListByAppId(@Param("appId") int appId);
	//
	int getListCount(@Param("fileName") String fileName, @Param("appId") int appId, @Param("batchNo") String batchNo);
	//
	// DeployConfigBean getDeployConfig(@Param("id") int id);
	//
	// int softDeleteByAppId(@Param("appId") int appId);
	//
	// int softDelete(int[] ids);

	int addBatch(List<DeployTaskFileBean> list);

}