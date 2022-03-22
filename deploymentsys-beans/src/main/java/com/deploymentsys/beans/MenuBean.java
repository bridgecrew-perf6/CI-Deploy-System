package com.deploymentsys.beans;

public class MenuBean extends BaseBean {
	private int id;
	private String name;
	private String url;
	private boolean show;
	private int sort;
	private String description;
	private int menuGroupId;
	private String menuGroupName;

	public MenuBean() {
	}

	public MenuBean(int id, String name, String url, boolean show, int sort, String description, int menuGroupId,
			String createDate, int creator, String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.name = name;
		this.url = url;
		this.show = show;
		this.sort = sort;
		this.description = description;
		this.menuGroupId = menuGroupId;
	}

	public MenuBean(int id, String name, String url, boolean show, String description, int menuGroupId,
			String menuGroupName, String createDate, int creator, String createIp, String modifyDate, int modifier,
			String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.name = name;
		this.url = url;
		this.show = show;
		this.description = description;
		this.menuGroupId = menuGroupId;
		this.menuGroupName = menuGroupName;
	}

	public String getMenuGroupName() {
		return menuGroupName;
	}

	public void setMenuGroupName(String menuGroupName) {
		this.menuGroupName = menuGroupName;
	}

	public int getMenuGroupId() {
		return menuGroupId;
	}

	public void setMenuGroupId(int menuGroupId) {
		this.menuGroupId = menuGroupId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

}
