package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployFlowServerToDoBean extends BaseBean {
	private int id;
	private int flowServerId;
	private String todoType;
	private int todoOrder;
	private String param1;
	private String param2;
	private String param3;

	public DeployFlowServerToDoBean() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFlowServerId() {
		return flowServerId;
	}

	public void setFlowServerId(int flowServerId) {
		this.flowServerId = flowServerId;
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

}
