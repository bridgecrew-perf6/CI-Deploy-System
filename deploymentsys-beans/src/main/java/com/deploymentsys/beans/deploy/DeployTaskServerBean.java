package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployTaskServerBean extends BaseBean {
	private int id;
	private int deployTaskId;
	private int appId;
	private String batchNo;
	private int serverOrder;
	private int flowServerId;
	private int targetServerId;
	private String targetServerIp;
	private int targetServerPort;
	private String deployDir;
	private String status;

	public DeployTaskServerBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTargetServerPort() {
		return targetServerPort;
	}

	public void setTargetServerPort(int targetServerPort) {
		this.targetServerPort = targetServerPort;
	}

	public String getTargetServerIp() {
		return targetServerIp;
	}

	public void setTargetServerIp(String targetServerIp) {
		this.targetServerIp = targetServerIp;
	}

	public int getFlowServerId() {
		return flowServerId;
	}

	public void setFlowServerId(int flowServerId) {
		this.flowServerId = flowServerId;
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

	public int getDeployTaskId() {
		return deployTaskId;
	}

	public void setDeployTaskId(int deployTaskId) {
		this.deployTaskId = deployTaskId;
	}

	public int getServerOrder() {
		return serverOrder;
	}

	public void setServerOrder(int serverOrder) {
		this.serverOrder = serverOrder;
	}

	public int getTargetServerId() {
		return targetServerId;
	}

	public void setTargetServerId(int targetServerId) {
		this.targetServerId = targetServerId;
	}

	public String getDeployDir() {
		return deployDir;
	}

	public void setDeployDir(String deployDir) {
		this.deployDir = deployDir;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
