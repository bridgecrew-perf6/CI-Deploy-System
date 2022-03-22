package com.deploymentsys.beans;

public class RoleMenuBean {
	private int menuId;
	private int roleId;

	private String createDate;
	private int creator;
	private String createIp;

	public RoleMenuBean() {
	}

	public RoleMenuBean(int menuId, int roleId, String createDate, int creator, String createIp) {
		super();
		this.menuId = menuId;
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

	public int getMenuId() {
		return menuId;
	}

	public void setStaffId(int menuId) {
		this.menuId = menuId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}