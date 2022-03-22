package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployFlowServerBean extends BaseBean {
	private int id;
	private int serverOrder;
	private int flowId;
	private int targetServerId;
	private String deployDir;
	private String targetServerIp;
	private int targetServerPort;

	public DeployFlowServerBean() {
	}

	public String getDeployDir() {
		return deployDir;
	}

	public void setDeployDir(String deployDir) {
		this.deployDir = deployDir;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerOrder() {
		return serverOrder;
	}

	public void setServerOrder(int serverOrder) {
		this.serverOrder = serverOrder;
	}

	public int getFlowId() {
		return flowId;
	}

	public void setFlowId(int flowId) {
		this.flowId = flowId;
	}

	public int getTargetServerId() {
		return targetServerId;
	}

	public void setTargetServerId(int targetServerId) {
		this.targetServerId = targetServerId;
	}

	public String getTargetServerIp() {
		return targetServerIp;
	}

	public void setTargetServerIp(String targetServerIp) {
		this.targetServerIp = targetServerIp;
	}

	public int getTargetServerPort() {
		return targetServerPort;
	}

	public void setTargetServerPort(int targetServerPort) {
		this.targetServerPort = targetServerPort;
	}

}
