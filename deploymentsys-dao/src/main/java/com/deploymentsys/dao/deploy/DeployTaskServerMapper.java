package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployTaskServerBean;

public interface DeployTaskServerMapper {

	List<DeployTaskServerBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("taskId") int taskId);

	List<DeployTaskServerBean> listAllByTaskId(@Param("taskId") int taskId);

	int getListCount(@Param("taskId") int taskId);

	DeployTaskServerBean getById(@Param("id") int id);

	List<DeployTaskServerBean> listByStatus(@Param("status") String status);

	int addBatch(List<DeployTaskServerBean> list);

	int updateStatusByTaskId(DeployTaskServerBean taskServerBean);

	int updateStatus(DeployTaskServerBean bean);
}