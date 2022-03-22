package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployTaskBean extends BaseBean {
	private int id;
	private int flowId;
	private int configId;
	private int appId;
	private String flowType;
	private int flowOrder;
	private String targetServerOrderType;
	private String batchNo;
	private String description;
	private String status;
	private String configName;
	private String appName;
	/**
	 * 部署申请人
	 */
	private String deploymentApplicant;
	/**
	 * 是否属于回滚任务
	 */
	private boolean rollback;

	public DeployTaskBean() {
	}

	public DeployTaskBean(int id, String configName, int appId, String description, String createDate, int creator,
			String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;

	}

	public boolean isRollback() {
		return rollback;
	}

	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}

	public String getDeploymentApplicant() {
		return deploymentApplicant;
	}

	public void setDeploymentApplicant(String deploymentApplicant) {
		this.deploymentApplicant = deploymentApplicant;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public int getFlowOrder() {
		return flowOrder;
	}

	public void setFlowOrder(int flowOrder) {
		this.flowOrder = flowOrder;
	}

	public String getTargetServerOrderType() {
		return targetServerOrderType;
	}

	public void setTargetServerOrderType(String targetServerOrderType) {
		this.targetServerOrderType = targetServerOrderType;
	}

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public int getConfigId() {
		return configId;
	}

	public void setConfigId(int configId) {
		this.configId = configId;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof DeployTaskBean) {
			DeployTaskBean task = (DeployTaskBean) object;
			return this.id == task.id && this.appId == task.appId && this.batchNo.equals(task.batchNo);
		} else {
			return false;
		}
	}

	@Override
	public final int hashCode() {
		int hashcode = 17;
		hashcode = hashcode * 31 + (int) this.id;
		return hashcode;
	}

}
