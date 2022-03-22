package com.deploymentsys.beans;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

public class ExcelImportTestBean extends BaseBean {
	@ExcelIgnore
	private int id;

	@ExcelProperty("用户名")
	private String userName;

	@ExcelProperty("年龄")
	private int age;

	@ExcelProperty("手机号")
	private String cellPhone;

	@ExcelIgnore
	private int taskId;

	@ExcelIgnore
	private String creatorName;

	public ExcelImportTestBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

}
