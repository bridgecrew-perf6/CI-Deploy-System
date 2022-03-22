package com.deploymentsys.ui.controller.deployment;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.service.deploy.DeployTaskServerToDoService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/taskservertodo")
public class DeployTaskServerToDoController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskServerToDoController.class);

	@Autowired
	private DeployTaskServerToDoService taskServerToDoService;
	@Autowired
	private WebUtils webUtils;

	/**
	 * 部署任务目标服务器todo列表管理页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int taskId, int taskServerId, String appName, String flowType, String serverIp, Model model) {
		LOGGER.info(
				"DeployTaskServerToDoController.list params, taskServerId: {} appName: {} flowType: {} serverIp: {}",
				taskServerId, appName, flowType, serverIp);
		model.addAttribute("taskId", taskId);
		model.addAttribute("taskServerId", taskServerId);
		model.addAttribute("appName", appName);
		model.addAttribute("flowType", flowType);
		model.addAttribute("serverIp", serverIp);

		return "deploy/taskservertodolist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getList(int page, int limit, int taskServerId) {
		int pageSize = limit > SysConstants.PAGE_SIZE_MAX ? SysConstants.PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = taskServerToDoService.getListCount(taskServerId);
		jsonResult.put("count", count);

		List<DeployTaskServerToDoBean> data = taskServerToDoService.list(pageStart, pageSize, taskServerId);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}
}
