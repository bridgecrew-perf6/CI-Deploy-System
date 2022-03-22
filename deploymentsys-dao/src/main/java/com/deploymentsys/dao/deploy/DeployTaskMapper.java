package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployTaskBean;

public interface DeployTaskMapper {

	List<DeployTaskBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("flowType") String flowType, @Param("appId") String appId, @Param("batchNo") String batchNo,
			@Param("deploymentApplicantId") int deploymentApplicantId, @Param("rollback") String rollback);

	int getListCount(@Param("flowType") String flowType, @Param("appId") String appId, @Param("batchNo") String batchNo,
			@Param("deploymentApplicantId") int deploymentApplicantId, @Param("rollback") String rollback);

	List<DeployTaskBean> listByStatus(@Param("status") String status, @Param("size") int size);

	List<DeployTaskBean> listByStatus2(@Param("status") String status);

	/**
	 * 根据appId和批次号获取所有部署任务
	 * 
	 * @param appId
	 * @param batchNo
	 * @return
	 */
	List<DeployTaskBean> listByAppIdAndBatchNo(@Param("appId") int appId, @Param("batchNo") String batchNo);

	int isExistByAppIdAndBatchNo(@Param("appId") int appId, @Param("batchNo") String batchNo);

	DeployTaskBean getById(@Param("id") int id);

	int addBatch(List<DeployTaskBean> list);

	int updateStatus(DeployTaskBean bean);
}