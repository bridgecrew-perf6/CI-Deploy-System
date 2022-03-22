package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployLogBean extends BaseBean {
	private int id;
	private int taskId;
	private int taskServerId;
	private int serverTodoId;
	private String logContent;

	public DeployLogBean() {
	}

	public DeployLogBean(int taskId, int taskServerId, int serverTodoId, String logContent, String createDate,
			int creator, String createIp) {
		super(createDate, creator, createIp, null, 0, null);
		this.taskId = taskId;
		this.taskServerId = taskServerId;
		this.serverTodoId = serverTodoId;
		this.logContent = logContent;
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

	public int getTaskServerId() {
		return taskServerId;
	}

	public void setTaskServerId(int taskServerId) {
		this.taskServerId = taskServerId;
	}

	public int getServerTodoId() {
		return serverTodoId;
	}

	public void setServerTodoId(int serverTodoId) {
		this.serverTodoId = serverTodoId;
	}

	public String getLogContent() {
		return logContent;
	}

	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}

}
