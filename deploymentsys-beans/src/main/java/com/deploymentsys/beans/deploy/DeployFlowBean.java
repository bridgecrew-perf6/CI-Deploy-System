package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployFlowBean extends BaseBean {
	private int id;
	private String flowType;
	private int flowOrder;
	private String targetServerOrderType;
	private int deployConfigId;
	private String deployConfigName;
	private String deployAppName;

	public DeployFlowBean() {
	}

	public DeployFlowBean(int id, String configName, int appId, String description, String createDate, int creator,
			String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;

	}

	public String getDeployAppName() {
		return deployAppName;
	}

	public void setDeployAppName(String deployAppName) {
		this.deployAppName = deployAppName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getDeployConfigId() {
		return deployConfigId;
	}

	public void setDeployConfigId(int deployConfigId) {
		this.deployConfigId = deployConfigId;
	}

	public String getDeployConfigName() {
		return deployConfigName;
	}

	public void setDeployConfigName(String deployConfigName) {
		this.deployConfigName = deployConfigName;
	}

}
