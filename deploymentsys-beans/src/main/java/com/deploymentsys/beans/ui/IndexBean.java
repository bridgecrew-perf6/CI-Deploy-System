package com.deploymentsys.beans.ui;

import java.util.List;

import com.deploymentsys.beans.MenuGroupsBean;

public class IndexBean {
	private String loginName;
	// private Map<MenuGroupBean, List<MenuBean>> staffMenus;
	private List<MenuGroupsBean> staffMenus;

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	// public Map<MenuGroupBean, List<MenuBean>> getStaffMenus() {
	// return staffMenus;
	// }
	//
	// public void setStaffMenus(Map<MenuGroupBean, List<MenuBean>> staffMenus) {
	// this.staffMenus = staffMenus;
	// }

	@Override
	public String toString() {
		return "IndexBean [loginName=" + loginName + ", staffMenus=" + staffMenus + "]";
	}

	public List<MenuGroupsBean> getStaffMenus() {
		return staffMenus;
	}

	public void setStaffMenus(List<MenuGroupsBean> staffMenus) {
		this.staffMenus = staffMenus;
	}

}
