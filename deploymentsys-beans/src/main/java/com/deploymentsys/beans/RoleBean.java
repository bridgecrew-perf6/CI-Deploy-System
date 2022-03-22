package com.deploymentsys.beans;

public class RoleBean extends BaseBean {
	private int id;
	private String name;
	private String description;

	public RoleBean() {
	}

	public RoleBean(int id, String name, String description, String createDate, int creator, String createIp,
			String modifyDate, int modifier, String modifyIp) {
		super(createDate, creator, createIp, modifyDate, modifier, modifyIp);
		this.id = id;
		this.name = name;
		this.description = description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "RoleBean [id=" + id + ", name=" + name + ", description=" + description + "]";
	}
}
