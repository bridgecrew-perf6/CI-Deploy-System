package com.deploymentsys.ui.controller.rbac;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.MenuGroupBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.service.MenuGroupService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/menugroup")
public class MenuGroupController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuGroupController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private WebUtils webUtils;

	/**
	 * Ajax 功能组下拉框数据获取
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getallmenugroups", method = RequestMethod.POST)
	public String getAllMenuGroups() {
		List<MenuGroupBean> data = menuGroupService.getAllMenuGroups();

		return JSONObject.toJSON(data).toString();
	}

	/**
	 * 显示页面：功能组管理列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "rbac/menugroupslist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getMenuGroupsList(int page, int limit, String name) {
		LOGGER.info(String.format("go into MenuGroupController.getMenuGroupsList, page:%d, limit:%d, name:%s", page,
				limit, name));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = menuGroupService.getListCount(name);
		jsonResult.put("count", count);

		List<MenuGroupBean> data = menuGroupService.list(pageStart, pageSize, name);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	/**
	 * 删除实体，可删除单个或多个
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids[]") int[] ids) {
		LOGGER.info("go into MenuGroupController.delete, ids:" + Arrays.toString(ids));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		if (ids.length == 0) {
			error = 1;
			msg = "请传入正确的参数";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}
		menuGroupService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：添加角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/menugroupadd", method = RequestMethod.GET)
	public String addMenuGroup() {
		return "rbac/menugroupadd";
	}

	@ResponseBody
	@RequestMapping(value = "/menugroupadd", method = RequestMethod.POST)
	public String addMenuGroup(MenuGroupBean formBean, HttpServletRequest request) {
		LOGGER.info(String.format("go into MenuGroupController.addMenuGroup, name:%s, show:%b, description:%s",
				formBean.getName(), formBean.isShow(), formBean.getDescription()));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);

		formBean.setCreateDate(currDate);
		formBean.setCreator(creatorId);
		formBean.setCreateIp(clientIp);
		formBean.setModifyDate(currDate);
		formBean.setModifier(creatorId);
		formBean.setModifyIp(clientIp);

		menuGroupService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/editmenugroup", method = RequestMethod.GET)
	public String editMenuGroup(int id, Model model) {
		LOGGER.info(String.format("go into MenuGroupController.editMenuGroup, id:%d", id));
		model.addAttribute("id", id);

		MenuGroupBean menuGroup = menuGroupService.getMenuGroup(id);
		model.addAttribute("menuGroup", menuGroup);
		return "rbac/menugroupadd";
	}

	@ResponseBody
	@RequestMapping(value = "/editmenugroup", method = RequestMethod.POST)
	public String editMenuGroup(MenuGroupBean formBean, HttpServletRequest request) {
		LOGGER.info(String.format("go into MenuGroupController.editMenuGroup, id:%d, name:%s, show:%b, description:%s",
				formBean.getId(), formBean.getName(), formBean.isShow(), formBean.getDescription()));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));
		menuGroupService.updateMenuGroup(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/showmenusofmenugroup", method = RequestMethod.GET)
	public String showMenusOfMenuGroup(int id, Model model) {
		LOGGER.info(String.format("go into MenuGroupController.showMenusOfMenuGroup, id:%d", id));
		model.addAttribute("menuGroupId", id);
		return "rbac/showmenusofmenugroup";
	}

}
