package com.deploymentsys.beans;

public class StaffRoleBean {
	private int staffId;
	private int roleId;

	private String createDate;
	private int creator;
	private String createIp;

	public StaffRoleBean() {
	}

	public StaffRoleBean(int staffId, int roleId, String createDate, int creator, String createIp) {
		super();
		this.staffId = staffId;
		this.roleId = roleId;
		this.createDate = createDate;
		this.creator = creator;
		this.createIp = createIp;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateIp() {
		return createIp;
	}

	public void setCreateIp(String createIp) {
		this.createIp = createIp;
	}

	public int getStaffId() {
		return staffId;
	}

	public void setStaffId(int staffId) {
		this.staffId = staffId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}