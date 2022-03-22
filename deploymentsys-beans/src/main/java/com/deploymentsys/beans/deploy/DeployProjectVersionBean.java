package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployProjectVersionBean extends BaseBean {
	private int id;
	private int projectId;
	private String versionNumber;
	private String description;

	public DeployProjectVersionBean() {
	}

	public DeployProjectVersionBean(int id, int projectId, String versionNumber, String description, String createDate,
			int creator, String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.projectId = projectId;
		this.versionNumber = versionNumber;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RoleBean [id=" + id + ", projectId=" + projectId + ", versionNumber=" + versionNumber + ", description="
				+ description + "]";
	}
}
