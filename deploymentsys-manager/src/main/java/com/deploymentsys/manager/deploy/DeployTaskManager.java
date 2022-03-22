package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployTaskBean;
import com.deploymentsys.dao.deploy.DeployTaskMapper;

@Component
public class DeployTaskManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskManager.class);

	@Autowired
	DeployTaskMapper deployTaskMapper;

	public DeployTaskBean getById(int id) {
		return deployTaskMapper.getById(id);
	}

	public List<DeployTaskBean> listByAppIdAndBatchNo(int appId, String batchNo) {
		return deployTaskMapper.listByAppIdAndBatchNo(appId, batchNo);
	}

	/**
	 * 根据状态获取部署任务列表
	 * 
	 * @param status
	 * @param size
	 * @return
	 */
	public List<DeployTaskBean> listByStatus(String status, int size) {
		try {
			return deployTaskMapper.listByStatus(status, size);
		} catch (Exception ex) {
			LOGGER.error("根据状态获取部署任务列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public List<DeployTaskBean> listByStatus2(String status) {
		return deployTaskMapper.listByStatus2(status);
	}

	public List<DeployTaskBean> list(int pageStart, int size, String flowType, String appId, String batchNo,
			int deploymentApplicantId, String rollback) {
		try {
			return deployTaskMapper.list(pageStart, size, flowType, appId, batchNo, deploymentApplicantId, rollback);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署任务列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String flowType, String appId, String batchNo, int deploymentApplicantId, String rollback) {
		try {
			return deployTaskMapper.getListCount(flowType, appId, batchNo, deploymentApplicantId, rollback);
		} catch (Exception ex) {
			LOGGER.error("获取部署任务列表总和发生异常", ex);
			return 0;
		}
	}

	public int addBatch(List<DeployTaskBean> list) {
		return deployTaskMapper.addBatch(list);
	}

	public int isExistByAppIdAndBatchNo(int appId, String batchNo) {
		return deployTaskMapper.isExistByAppIdAndBatchNo(appId, batchNo);
	}

	public int updateStatus(DeployTaskBean bean) {
		return deployTaskMapper.updateStatus(bean);
	}

}
