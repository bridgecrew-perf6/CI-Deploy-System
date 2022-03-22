package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployAppBean extends BaseBean {
	private int id;
	private int projectId;
	private String projectName;
	private String appName;
	private String fileDir;
	private String description;

	public DeployAppBean() {
	}

	public DeployAppBean(int id, String appName, String fileDir, String description, String createDate, int creator,
			String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.appName = appName;
		this.fileDir = fileDir;
		this.description = description;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getFileDir() {
		return fileDir;
	}

	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
