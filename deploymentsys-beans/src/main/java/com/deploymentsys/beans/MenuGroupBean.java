package com.deploymentsys.beans;

public class MenuGroupBean extends BaseBean {
	private int id;
	private String name;
	private String icon;
	private boolean show;
	private int sort;
	private String description;

	public MenuGroupBean() {
	}

	public MenuGroupBean(int id, String name, String icon, boolean show, int sort, String description,
			String createDate, int creator, String createIp, String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.show = show;
		this.sort = sort;
		this.description = description;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isShow() {
		return show;
	}

	public void setShow(boolean show) {
		this.show = show;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public String toString() {
		return "MenuGroupBean [id=" + id + ", name=" + name + ", icon=" + icon + "]";
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object instanceof MenuGroupBean) {
			MenuGroupBean mgb = (MenuGroupBean) object;
			return this.id == mgb.id && this.name.equals(mgb.name) && this.icon.equals(mgb.icon);
		} else {
			return false;
		}
	}

	@Override
	public final int hashCode() {
		int hashcode = 17;
		hashcode = hashcode * 31 + (int) this.id;
		return hashcode;
	}
}
