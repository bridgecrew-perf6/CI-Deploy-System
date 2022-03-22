package com.deploymentsys.tcp.protocol;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class Packet {
	/**
	 * 协议版本
	 */
	@JSONField(deserialize = false, serialize = false)
	private Byte version = 1;

	/**
	 * 响应码，默认0是正常响应，其他非0都是异常响应
	 */
	private int responseCode = 0;
	/**
	 * 响应消息
	 */
	private String responseMsg;

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}

	public Byte getVersion() {
		return version;
	}

	public void setVersion(Byte version) {
		this.version = version;
	}

	@JSONField(serialize = false)
	public abstract Byte getCommand();

}
