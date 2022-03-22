package com.deploymentsys.beans;

public class UrlWhiteListBean extends BaseBean {
	private int id;
	private String url;
	private String description;

	public UrlWhiteListBean() {
	}

	public UrlWhiteListBean(int id, String url, String description, String createDate, int creator, String createIp,
			String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.url = url;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
