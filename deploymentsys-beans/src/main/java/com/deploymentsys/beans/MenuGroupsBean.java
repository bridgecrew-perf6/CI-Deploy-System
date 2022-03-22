package com.deploymentsys.beans;

import java.util.List;

public class MenuGroupsBean {
	private MenuGroupBean menuGroupBean;
	private List<MenuBean> menuBeans;

	public MenuGroupsBean(MenuGroupBean menuGroupBean, List<MenuBean> menuBeans) {
		super();
		this.menuGroupBean = menuGroupBean;
		this.menuBeans = menuBeans;
	}

	public MenuGroupBean getMenuGroupBean() {
		return menuGroupBean;
	}

	public void setMenuGroupBean(MenuGroupBean menuGroupBean) {
		this.menuGroupBean = menuGroupBean;
	}

	public List<MenuBean> getMenuBeans() {
		return menuBeans;
	}

	public void setMenuBeans(List<MenuBean> menuBeans) {
		this.menuBeans = menuBeans;
	}

}
