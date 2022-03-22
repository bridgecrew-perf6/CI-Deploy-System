package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;

public interface DeployTaskServerToDoMapper {

	List<DeployTaskServerToDoBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("taskServerId") int taskServerId);

	List<DeployTaskServerToDoBean> listAllByTaskServerId(@Param("taskServerId") int taskServerId);

	int getListCount(@Param("taskServerId") int taskServerId);

	List<DeployTaskServerToDoBean> listByStatus(@Param("status") String status);

	DeployTaskServerToDoBean getById(@Param("id") int id);

	int addBatch(List<DeployTaskServerToDoBean> list);

	int updateStatusByServerTaskId(DeployTaskServerToDoBean serverToDoBean);

	int updateStatus(DeployTaskServerToDoBean bean);
}