package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployTaskServerToDoBean extends BaseBean {
	private int id;
	private int deployTaskServerId;
	private int taskId;
	private int appId;
	private String batchNo;
	private String todoType;
	private int todoOrder;
	private String param1;
	private String param2;
	private String param3;
	private String status;

	//private String errorMsg;

	public DeployTaskServerToDoBean() {
	}

//	public String getErrorMsg() {
//		return errorMsg;
//	}
//
//	public void setErrorMsg(String errorMsg) {
//		this.errorMsg = errorMsg;
//	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public int getDeployTaskServerId() {
		return deployTaskServerId;
	}

	public void setDeployTaskServerId(int deployTaskServerId) {
		this.deployTaskServerId = deployTaskServerId;
	}

	public String getTodoType() {
		return todoType;
	}

	public void setTodoType(String todoType) {
		this.todoType = todoType;
	}

	public int getTodoOrder() {
		return todoOrder;
	}

	public void setTodoOrder(int todoOrder) {
		this.todoOrder = todoOrder;
	}

	public String getParam1() {
		return param1;
	}

	public void setParam1(String param1) {
		this.param1 = param1;
	}

	public String getParam2() {
		return param2;
	}

	public void setParam2(String param2) {
		this.param2 = param2;
	}

	public String getParam3() {
		return param3;
	}

	public void setParam3(String param3) {
		this.param3 = param3;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
