package com.deploymentsys.ui.controller.deployment;

import java.util.ArrayList;
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
import com.deploymentsys.beans.deploy.DeployLogBean;
import com.deploymentsys.service.deploy.DeployLogServiceDbImpl;

@Controller
@RequestMapping("/deploy/log")
public class DeployLogController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployLogController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployLogServiceDbImpl logService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int taskId, String taskServerId, String serverTodoId, Model model) {
		model.addAttribute("taskId", taskId);
		model.addAttribute("taskServerId",
				(null == taskServerId || "".equals(taskServerId)) ? 0 : Integer.valueOf(taskServerId));
		model.addAttribute("serverTodoId",
				(null == serverTodoId || "".equals(serverTodoId)) ? 0 : Integer.valueOf(serverTodoId));

		return "deploy/taskloglist";
	}

	@ResponseBody
	@RequestMapping(value = "/getlist", method = RequestMethod.POST)
	public String getList(int page, int limit, int taskId, int taskServerId, int serverTodoId) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = 0;
		List<DeployLogBean> data = new ArrayList<>();
		if (taskServerId != 0 && serverTodoId != 0) {
			count = logService.getListCountByTaskServerToDoId(taskId, taskServerId, serverTodoId);
			jsonResult.put("count", count);

			data = logService.listByTaskServerToDoId(pageStart, pageSize, taskId, taskServerId, serverTodoId);
			jsonResult.put("data", data);
			return jsonResult.toJSONString();
		}

		if (taskServerId != 0) {
			count = logService.getListCountByTaskServerId(taskId, taskServerId);
			jsonResult.put("count", count);

			data = logService.listByTaskServerId(pageStart, pageSize, taskId, taskServerId);
			jsonResult.put("data", data);
			return jsonResult.toJSONString();
		}

		count = logService.getListCountByTaskId(taskId);
		jsonResult.put("count", count);

		data = logService.listByTaskId(pageStart, pageSize, taskId);
		jsonResult.put("data", data);

		return jsonResult.toJSONString();
	}

}
