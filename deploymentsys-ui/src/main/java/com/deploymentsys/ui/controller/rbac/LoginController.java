package com.deploymentsys.ui.controller.rbac;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.service.StaffService;
import com.deploymentsys.ui.beans.LoginBean;
import com.deploymentsys.utils.MD5;

@Controller
@RequestMapping("")
public class LoginController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private StaffService staffService;

	@Autowired
	private MD5 md5Util;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(SysConstants.CURRENT_LOGIN_INFO, null);

		return "rbac/login";
	}

	@ResponseBody
	@RequestMapping(value = "/loginpost", method = RequestMethod.POST)
	public String loginPost(@RequestBody LoginBean loginBean, HttpServletRequest request) {
		LOGGER.info(String.format("go into LoginController.loginPost, loginName:%s", loginBean.getUserName()));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = staffService.getStaff(loginBean.getUserName(), md5Util.md5(loginBean.getPassword()));
		if (null == staff) {
			error = 1;
			msg = "账号不存在或账号密码错误";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}

		HttpSession session = request.getSession();
		session.setAttribute(SysConstants.CURRENT_LOGIN_INFO, staff);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse servletResponse) {
		// 清空登录信息
		HttpSession session = request.getSession();
		session.setAttribute(SysConstants.CURRENT_LOGIN_INFO, null);

		servletResponse.setHeader("Cache-Control", "no-cache");
		servletResponse.setHeader("Pragma", "no-cache");
		servletResponse.setDateHeader("expires", -1);

		return "redirect:/login";
	}

}
