package com.deploymentsys.beans;

public class OperationLogBean extends BaseBean {
	private int id;
	private String url;
	private String requestMethod;
	private String contentType;
	private String requestParameters;
	private String staffName;

	public OperationLogBean() {
	}

	public OperationLogBean(int id, String url, String createDate, int creator, String createIp, String modifyDate,
			int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.url = url;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(String requestParameters) {
		this.requestParameters = requestParameters;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}
}
