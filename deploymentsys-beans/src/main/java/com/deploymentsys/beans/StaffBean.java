package com.deploymentsys.beans;

public class StaffBean extends BaseBean {
	private int id;
	private String loginName;
	private String password;
	private String trueName;
	private String birthday;
	private String localPhotoPath;
	private String virtualPhotoUrl;

	public StaffBean() {
	}

	public StaffBean(int id, String loginName, String password, String createDate, int creator, String createIp,
			String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.loginName = loginName;
		this.password = password;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLocalPhotoPath() {
		return localPhotoPath;
	}

	public void setLocalPhotoPath(String localPhotoPath) {
		this.localPhotoPath = localPhotoPath;
	}

	public String getVirtualPhotoUrl() {
		return virtualPhotoUrl;
	}

	public void setVirtualPhotoUrl(String virtualPhotoUrl) {
		this.virtualPhotoUrl = virtualPhotoUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
