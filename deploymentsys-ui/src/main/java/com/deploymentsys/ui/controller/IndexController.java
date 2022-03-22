package com.deploymentsys.ui.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.MenuBean;
import com.deploymentsys.beans.MenuGroupBean;
import com.deploymentsys.beans.MenuGroupsBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.ui.IndexBean;
import com.deploymentsys.service.MenuGroupService;
import com.deploymentsys.service.MenuService;

@Controller
@RequestMapping("/index")
public class IndexController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private MenuService menuService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@ResponseBody
	@RequestMapping(value = "/getstaffmenus", method = RequestMethod.POST)
	public String getStaffMenus(HttpServletRequest request) {
		IndexBean indexBean = new IndexBean();

		HttpSession session = request.getSession();
		StaffBean staff = (StaffBean) session.getAttribute(SysConstants.CURRENT_LOGIN_INFO);

		indexBean.setLoginName(null != staff ? staff.getLoginName() : "");
		int staffId = null != staff ? staff.getId() : 0;

		List<MenuGroupBean> listMg = menuGroupService.getStaffMenuGroups(staffId);
		List<MenuBean> listM = menuService.getStaffMenus(staffId);

		List<MenuGroupsBean> staffMenuGroups = new ArrayList<MenuGroupsBean>();
		for (MenuGroupBean menuGroupBean : listMg) {
			List<MenuBean> subListM = listM.stream().filter(x -> x.getMenuGroupId() == menuGroupBean.getId())
					.sorted(Comparator.comparing(MenuBean::getSort).reversed().thenComparing(MenuBean::getCreateDate)
							.reversed().thenComparing(MenuBean::getId).reversed())
					.collect(Collectors.toList());
			staffMenuGroups.add(new MenuGroupsBean(menuGroupBean, subListM));
		}

		indexBean.setStaffMenus(staffMenuGroups);

		return JSONObject.toJSON(indexBean).toString();
	}

}
