package com.deploymentsys.utils.dtree;

/** 信息状态类 */
public class Status {
	/** 状态码 */
	private int code = 200;
	/** 信息标识 */
	private String message = "success";

	public Status() {

	}

	public Status(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "Status [code=" + code + ", message=" + message + "]";
	}

}
