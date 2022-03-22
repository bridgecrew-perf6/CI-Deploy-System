package com.deploymentsys.beans;

import com.alibaba.excel.annotation.ExcelIgnore;

public class BaseBean {
	@ExcelIgnore
	private String createDate;
	@ExcelIgnore
	private int creator;
	@ExcelIgnore
	private String createIp;
	@ExcelIgnore
	private String modifyDate;
	@ExcelIgnore
	private int modifier;
	@ExcelIgnore
	private String modifyIp;
	@ExcelIgnore
	private boolean delete;

	public BaseBean() {
	}

	public BaseBean(String createDate, int creator, String createIp, String modifyDate, int modifier, String modifyIp) {
		super();
		this.createDate = createDate;
		this.creator = creator;
		this.createIp = createIp;
		this.modifyDate = modifyDate;
		this.modifier = modifier;
		this.modifyIp = modifyIp;
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

	public String getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}

	public int getModifier() {
		return modifier;
	}

	public void setModifier(int modifier) {
		this.modifier = modifier;
	}

	public String getModifyIp() {
		return modifyIp;
	}

	public void setModifyIp(String modifyIp) {
		this.modifyIp = modifyIp;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

}
