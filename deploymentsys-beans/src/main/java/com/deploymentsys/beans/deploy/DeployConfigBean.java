package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployConfigBean extends BaseBean {
	private int id;
	private String configName;
	private String description;
	private int appId;
	private String appName;

	public DeployConfigBean() {
	}

	public DeployConfigBean(int id, String configName, int appId, String description, String createDate, int creator,
			String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.configName = configName;
		this.description = description;
		this.appId = appId;
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

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
