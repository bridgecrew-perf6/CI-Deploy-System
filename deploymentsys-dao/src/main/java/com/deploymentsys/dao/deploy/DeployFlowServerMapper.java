package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployFlowServerBean;

public interface DeployFlowServerMapper {

	List<DeployFlowServerBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("deployFlowId") int deployFlowId);
	
	List<DeployFlowServerBean> getListByFlowId(@Param("flowId") int flowId);

	int getListCount(@Param("deployFlowId") int deployFlowId);

	DeployFlowServerBean getDeployFlowServer(@Param("id") int id);

	DeployFlowServerBean getDeployFlowServer2(@Param("flowId") int flowId, @Param("targetServerId") int targetServerId);

	int softDelete(int[] ids);
	
	int softDeleteByFlowId(@Param("flowId") int flowId);

	int update(DeployFlowServerBean bean);

	int add(DeployFlowServerBean bean);
}
