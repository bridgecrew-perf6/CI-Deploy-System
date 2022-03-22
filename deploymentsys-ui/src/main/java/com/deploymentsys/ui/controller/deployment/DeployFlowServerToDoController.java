package com.deploymentsys.ui.controller.deployment;

import java.util.ArrayList;
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
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.TargetServerToDoType;
import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.beans.deploy.DeployFlowBean;
import com.deploymentsys.beans.deploy.DeployFlowServerBean;
import com.deploymentsys.beans.deploy.DeployFlowServerToDoBean;
import com.deploymentsys.beans.deploy.DeployTargetServerBean;
import com.deploymentsys.beans.ui.deploy.ServerToDo;
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.service.deploy.DeployConfigService;
import com.deploymentsys.service.deploy.DeployFlowServerService;
import com.deploymentsys.service.deploy.DeployFlowServerToDoService;
import com.deploymentsys.service.deploy.DeployFlowService;
import com.deploymentsys.service.deploy.DeployTargetServerService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/flowservertodo")
public class DeployFlowServerToDoController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowServerToDoController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployFlowServerToDoService flowServerToDoService;
	@Autowired
	private DeployFlowServerService deployFlowServerService;
	@Autowired
	private DeployConfigService deployConfigService;
	@Autowired
	private DeployAppService deployAppService;
	@Autowired
	private DeployFlowService deployFlowService;
	@Autowired
	private WebUtils webUtils;

	@Autowired
	private DeployTargetServerService deployTargetServerService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int flowServerId, Model model) {
		DeployFlowServerBean flowServer = deployFlowServerService.getDeployFlowServer(flowServerId);

		DeployTargetServerBean targetServer = null != flowServer
				? deployTargetServerService.getDeployTargetServer(flowServer.getTargetServerId())
				: null;

		DeployFlowBean flow = null != flowServer ? deployFlowService.getDeployFlow(flowServer.getFlowId()) : null;

		DeployConfigBean config = null != flow ? deployConfigService.getDeployConfig(flow.getDeployConfigId()) : null;

		DeployAppBean app = null != config ? deployAppService.getDeployApp(config.getAppId()) : null;

		model.addAttribute("flowServerId", flowServerId);
		model.addAttribute("deployFlow", null != flow ? flow.getFlowType() : "");
		model.addAttribute("deployConfigName", null != config ? config.getConfigName() : "");
		model.addAttribute("deployAppName", null != app ? app.getAppName() : "");
		model.addAttribute("targetServerIp", null != targetServer ? targetServer.getServerIp() : "");
		return "deploy/flowservertodolist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getFlowServerToDoList(int page, int limit, int flowServerId) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = flowServerToDoService.getListCount(flowServerId);
		jsonResult.put("count", count);

		List<DeployFlowServerToDoBean> data = flowServerToDoService.list(pageStart, pageSize, flowServerId);
		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}
	
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
		flowServerToDoService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
	
	/**
	 * 生成目标服务器执行类型json字符串
	 * @return
	 */
	private String getToDoTypesJsonStr() {
		List<ServerToDo> data = new ArrayList<ServerToDo>();
		data.add(new ServerToDo(TargetServerToDoType.FILE_TRANSFER));
		data.add(new ServerToDo(TargetServerToDoType.FILE_COPY));
		data.add(new ServerToDo(TargetServerToDoType.DELETE_DIR));
		data.add(new ServerToDo(TargetServerToDoType.EMPTY_DIR));
		data.add(new ServerToDo(TargetServerToDoType.REMOTE_EXEC_SHELL));

		String toDoTypes=JSONObject.toJSON(data).toString();
		return toDoTypes;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(int flowServerId, Model model) {		
		String toDoTypes=getToDoTypesJsonStr();		
		model.addAttribute("flowServerId", flowServerId);
		model.addAttribute("toDoTypes", toDoTypes);
		
		return "deploy/flowservertodoadd";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeployFlowServerToDoBean formBean, HttpServletRequest request) {
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

		flowServerToDoService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		DeployFlowServerToDoBean formBean = flowServerToDoService.getOneById(id);
		model.addAttribute("todo", formBean);
		
		String toDoTypes=getToDoTypesJsonStr();		
		model.addAttribute("toDoTypes", toDoTypes);
		return "deploy/flowservertodoadd";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeployFlowServerToDoBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));
		
		flowServerToDoService.update(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
