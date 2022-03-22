package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployProjectBean extends BaseBean {
	private int id;
	private String projectName;
	private String description;

	public DeployProjectBean() {
	}

	public DeployProjectBean(int id, String projectName, String description, String createDate, int creator,
			String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.projectName = projectName;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RoleBean [id=" + id + ", projectName=" + projectName + ", description=" + description + "]";
	}
}
