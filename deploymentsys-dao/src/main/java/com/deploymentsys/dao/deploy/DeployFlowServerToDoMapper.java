package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployFlowServerToDoBean;

public interface DeployFlowServerToDoMapper {

	List<DeployFlowServerToDoBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("flowServerId") int flowServerId);

	List<DeployFlowServerToDoBean> getListByFlowServerId(@Param("flowServerId") int flowServerId);

	int getListCount(@Param("flowServerId") int flowServerId);

	int softDelete(int[] ids);

	int softDeleteByFlowServerId(@Param("flowServerId") int flowServerId);

	int add(DeployFlowServerToDoBean bean);

	int update(DeployFlowServerToDoBean bean);

	DeployFlowServerToDoBean getOneById(@Param("id") int id);

}