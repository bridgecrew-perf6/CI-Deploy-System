package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployTaskFileBean extends BaseBean {
	private int id;
	private String relativePath;
	private String batchNo;
	private int appId;
	private String md5;

	public DeployTaskFileBean() {
	}

	public DeployTaskFileBean(int id, String relativePath, int appId, String batchNo, String md5, String createDate,
			int creator, String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.relativePath = relativePath;
		this.batchNo = batchNo;
		this.md5 = md5;
		this.appId = appId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

}
