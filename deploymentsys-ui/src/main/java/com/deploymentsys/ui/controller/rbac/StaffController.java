package com.deploymentsys.ui.controller.rbac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.StaffRoleBean;
import com.deploymentsys.service.StaffRoleService;
import com.deploymentsys.service.StaffService;
import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.utils.MD5;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/staff")
public class StaffController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private StaffService staffService;
	@Autowired
	private StaffRoleService staffRoleService;
	@Autowired
	private DeploySysConfigService sysConfigService;

	@Autowired
	private WebUtils webUtils;

	@Autowired
	private MD5 md5Util;

	@RequestMapping(value = "/personaldata", method = RequestMethod.GET)
	public String personalData(Model model, HttpServletRequest request) {
		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		staff = staffService.getById(null != staff ? staff.getId() : 0);
		
		model.addAttribute("staff", staff);

		return "rbac/personal_data";
	}

	@ResponseBody
	@RequestMapping(value = "/personaldata", method = RequestMethod.POST)
	public String personalData(StaffBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));

		if (StringUtils.isBlank(formBean.getVirtualPhotoUrl())) {
			formBean.setVirtualPhotoUrl(sysConfigService.getStaffDefaultPhotoVirtualUrl());
		}
		formBean.setBirthday(formBean.getBirthday() + "-01 00:00:00");

		int result = staffService.update(formBean);
		if (result == 0) {
			error = 1;
			msg = "修改失败";
		}

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：管理员修改密码页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/password", method = RequestMethod.GET)
	public String password() {
		return "rbac/password";
	}

	/**
	 * ajax处理：管理员修改密码业务处理
	 * 
	 * @param oldPassword
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request, String oldPassword, String password) {
		LOGGER.info("go into StaffController.changePassword");
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int staffId = staff.getId();
		String oldPwd = staffService.getStaffPwd(staffId);
		
		if ("sa".equalsIgnoreCase(staff.getLoginName().strip())) {
			//项目演示中，暂时不允许修改sa账号的密码，2022年3月4日04:17:12
			error = 1;
			msg = "项目演示中，暂时不允许修改sa账号的密码";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}

		if (!oldPwd.equals(md5Util.md5(oldPassword))) {
			error = 1;
			msg = "原密码错误";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}

		staffService.changeStaffPwd(staffId, md5Util.md5(password), webUtils.getCurrentDateStr(), staffId,
				webUtils.getClientIp(request));

		jsonResult.put(ERROR, error);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：人员管理列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "rbac/stafflist";
	}

	/**
	 * ajax post获取人员管理列表数据
	 * 
	 * @param page
	 * @param limit
	 * @param loginName
	 * @param createDate
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getStaffList(int page, int limit, String loginName) {
		LOGGER.info(String.format("go into StaffController.getStaffList, page:%d, limit:%d, loginName:%s", page, limit,
				loginName));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = staffService.getListCount(loginName);
		jsonResult.put("count", count);

		List<StaffBean> data = staffService.list(pageStart, pageSize, loginName);

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
		LOGGER.info("go into StaffController.delete, ids:" + Arrays.toString(ids));
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
		staffService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：添加人员
	 * 
	 * @return
	 */
	@RequestMapping(value = "/staffadd", method = RequestMethod.GET)
	public String addStaff() {
		return "rbac/staffadd";
	}

	/**
	 * ajax处理：添加人员
	 * 
	 * @param loginName
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/staffadd", method = RequestMethod.POST)
	public String addStaff(String loginName, String password, HttpServletRequest request) {
		LOGGER.info(String.format("go into StaffController.addStaff, loginName:%s", loginName));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);
		StaffBean bean = new StaffBean(0, loginName, md5Util.md5(password), currDate, creatorId, clientIp, currDate,
				creatorId, clientIp);
		bean.setVirtualPhotoUrl(sysConfigService.getStaffDefaultPhotoVirtualUrl());
		int result = staffService.add(bean);
		if (!(result > 0)) {
			error = 1;
			msg = "添加失败";
		}

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：修改人员登录密码
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/changestaffpwd", method = RequestMethod.GET)
	public String changeStaffPwd(int id, Model model) {
		LOGGER.info(String.format("go into StaffController.changeStaffPwd, id:%d", id));
		model.addAttribute("id", id);
		return "rbac/staffchangepwd";
	}

	/**
	 * ajax处理: 修改人员登录密码
	 * 
	 * @param id
	 * @param password
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/changestaffpwd", method = RequestMethod.POST)
	public String changeStaffPwd(int id, String password, HttpServletRequest request) {
		LOGGER.info(String.format("go into StaffController.changeStaffPwd, id:%d", id));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		
		if (staff != null && "sa".equalsIgnoreCase(staff.getLoginName().strip()) && id == staff.getId()) {
			error = 1;
			msg = "项目演示中，暂时不允许修改sa账号的密码";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}
		
		int modifierId = null != staff ? staff.getId() : 0;
		staffService.changeStaffPwd(id, md5Util.md5(password), webUtils.getCurrentDateStr(), modifierId,
				webUtils.getClientIp(request));

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();

	}

	/**
	 * 显示页面：人员分配角色
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/staffassignroles", method = RequestMethod.GET)
	public String staffAssignRoles(int id, Model model) {
		LOGGER.info(String.format("go into StaffController.staffAssignRoles, id:%d", id));
		model.addAttribute("id", id);
		return "rbac/staffassignroles";
	}

	/**
	 * ajax处理：人员分配角色
	 * 
	 * @param roleIds
	 * @param staffId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/staffassignroles", method = RequestMethod.POST)
	public String staffAssignRoles(@RequestParam(value = "roleIds[]", required = false) int[] roleIds, int staffId,
			HttpServletRequest request) {
		LOGGER.info("go into StaffController.staffAssignRoles, roleIds：" + Arrays.toString(roleIds) + ", staffId:"
				+ staffId);

		List<StaffRoleBean> beans = new ArrayList<>();

		if (null != roleIds) {
			for (int roleId : roleIds) {
				beans.add(new StaffRoleBean(staffId, roleId, webUtils.getCurrentDateStr(), 0,
						webUtils.getClientIp(request)));
			}
		}

		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();
		try {
			staffRoleService.addNewStaffRoleRelation(staffId, beans);
			jsonResult.put(ERROR, error);
			return jsonResult.toJSONString();
		} catch (Exception ex) {
			LOGGER.error("StaffController.staffAssignRoles catch an exception", ex);
			error = 1;
			msg = "操作异常";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}
	}
}
