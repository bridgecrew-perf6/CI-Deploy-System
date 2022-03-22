package com.deploymentsys.ui.controller.deployment;

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
import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.service.deploy.DeployConfigService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/config")
public class DeployConfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployConfigController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployConfigService deployConfigService;
	@Autowired
	private DeployAppService deployAppService;
	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "deploy/configlist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getDeployConfigList(int page, int limit, String appId) {
		// 考虑到界面交互问题，一开始如果 appName 参数为null，则默认查出最新的appId

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		// if (!StringUtils.isBlank(appName)) {
		// // 先根据appName查出部署应用信息，如果没有此应用返回相关信息；如果有则继续查询该应用下面的部属配置
		// DeployAppBean app = deployAppService.getDeployAppByName(appName);
		// if (null != app) {
		// int appId = app.getId();
		//
		// int count = deployConfigService.getListCount(appId);
		// jsonResult.put("count", count);
		//
		// List<DeployConfigBean> data = deployConfigService.list(pageStart, pageSize,
		// appId);
		//
		// jsonResult.put("data", data);
		// return jsonResult.toJSONString();
		// }
		//
		// // int count = deployConfigService.getListCount(appId);
		// jsonResult.put("count", 0);
		//
		// List<DeployConfigBean> data = deployConfigService.list(pageStart, pageSize,
		// 0);
		//
		// jsonResult.put("data", null);
		// return jsonResult.toJSONString();
		// }

		int count = deployConfigService.getListCount(!StringUtils.isBlank(appId) ? Integer.parseInt(appId) : 0);
		jsonResult.put("count", count);

		List<DeployConfigBean> data = deployConfigService.list(pageStart, pageSize,
				!StringUtils.isBlank(appId) ? Integer.parseInt(appId) : 0);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/getlistbyappId", method = RequestMethod.POST)
	public String getListByAppId(int appId) {
		List<DeployConfigBean> data = deployConfigService.getListByAppId(appId);

		return JSONObject.toJSON(data).toString();
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
		deployConfigService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(String appName, String appId, Model model) {
		model.addAttribute("appName", appName);
		model.addAttribute("appId", appId);
		return "deploy/configadd";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeployConfigBean formBean, HttpServletRequest request) {
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

		deployConfigService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		DeployConfigBean formBean = deployConfigService.getDeployConfig(id);
		model.addAttribute("config", formBean);
		
		return "deploy/configedit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeployConfigBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));
		
		deployConfigService.update(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
