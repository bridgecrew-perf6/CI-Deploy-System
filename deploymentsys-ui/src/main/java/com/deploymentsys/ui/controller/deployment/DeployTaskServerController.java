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
import com.deploymentsys.beans.deploy.DeployTaskServerBean;
import com.deploymentsys.service.deploy.DeployTaskServerService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/taskserver")
public class DeployTaskServerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskServerController.class);

	@Autowired
	private DeployTaskServerService taskServerService;
	@Autowired
	private WebUtils webUtils;

	/**
	 * 部署任务目标服务器管理页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int taskId, String appName, String flowType, Model model) {
		LOGGER.info("DeployTaskServerController.list params, taskId: {} appName: {} flowType: {}", taskId, appName,
				flowType);
		model.addAttribute("taskId", taskId);
		model.addAttribute("appName", appName);
		model.addAttribute("flowType", flowType);

		return "deploy/taskserverlist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getList(int page, int limit, int taskId) {
		int pageSize = limit > SysConstants.PAGE_SIZE_MAX ? SysConstants.PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = taskServerService.getListCount(taskId);
		jsonResult.put("count", count);

		List<DeployTaskServerBean> data = taskServerService.list(pageStart, pageSize, taskId);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}
}
