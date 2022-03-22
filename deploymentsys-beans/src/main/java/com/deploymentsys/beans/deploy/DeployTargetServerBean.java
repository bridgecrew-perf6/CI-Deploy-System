package com.deploymentsys.beans.deploy;

import com.deploymentsys.beans.BaseBean;

public class DeployTargetServerBean extends BaseBean {
	private int id;
	private String serverIp;
	private int serverPort;
	private String deployTempDir;

	public DeployTargetServerBean() {
	}

	public String getDeployTempDir() {
		return deployTempDir;
	}

	public void setDeployTempDir(String deployTempDir) {
		this.deployTempDir = deployTempDir;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

}
