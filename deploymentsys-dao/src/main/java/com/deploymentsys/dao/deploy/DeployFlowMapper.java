package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployFlowBean;

public interface DeployFlowMapper {

	List<DeployFlowBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("deployConfigId") int deployConfigId);
	
	List<DeployFlowBean> getListByConfigId(@Param("configId") int configId);

	int getListCount(@Param("deployConfigId") int deployConfigId);

	DeployFlowBean getDeployFlow(@Param("id") int id);
	
	int softDelete(int[] ids);
	
	int softDeleteByConfigId(@Param("configId") int configId);
	
	int add(DeployFlowBean bean);
	
	int update(DeployFlowBean bean);
}