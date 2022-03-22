package com.deploymentsys.ui.controller.deployment;

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
import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.beans.deploy.DeployFlowBean;
import com.deploymentsys.beans.deploy.DeployFlowServerBean;
import com.deploymentsys.beans.deploy.DeployTargetServerBean;
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.service.deploy.DeployConfigService;
import com.deploymentsys.service.deploy.DeployFlowServerService;
import com.deploymentsys.service.deploy.DeployFlowService;
import com.deploymentsys.service.deploy.DeployTargetServerService;
import com.deploymentsys.utils.FileUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/flowserver")
public class DeployFlowServerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowServerController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployFlowServerService deployFlowServerService;
	@Autowired
	private DeployConfigService deployConfigService;
	@Autowired
	private DeployAppService deployAppService;
	@Autowired
	private DeployFlowService deployFlowService;
	@Autowired
	private DeployTargetServerService targetServerService;
	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int deployFlowId, Model model) {
		LOGGER.info(String.format("go into DeployFlowServerController.list, deployFlowId:%d", deployFlowId));

		DeployFlowBean flow = deployFlowService.getDeployFlow(deployFlowId);

		DeployConfigBean config = null != flow ? deployConfigService.getDeployConfig(flow.getDeployConfigId()) : null;

		DeployAppBean app = null != config ? deployAppService.getDeployApp(config.getAppId()) : null;

		model.addAttribute("deployFlowId", deployFlowId);
		model.addAttribute("deployFlow", null != flow ? flow.getFlowType() : "");
		model.addAttribute("deployConfigName", null != config ? config.getConfigName() : "");
		model.addAttribute("deployAppName", null != app ? app.getAppName() : "");
		return "deploy/flowserverlist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getDeployFlowServerList(int page, int limit, int deployFlowId) {
		LOGGER.info(String.format(
				"go into DeployFlowServerController.getDeployFlowServerList, page:%d, limit:%d, deployFlowId:%d", page,
				limit, deployFlowId));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = deployFlowServerService.getListCount(deployFlowId);
		jsonResult.put("count", count);

		List<DeployFlowServerBean> data = deployFlowServerService.list(pageStart, pageSize, deployFlowId);
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
		deployFlowServerService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(int flowId, String deployAppName, Model model) {
		model.addAttribute("flowId", flowId);
		model.addAttribute("deployAppName", deployAppName);

		return "deploy/flowserveradd";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeployFlowServerBean formBean, String deployAppName, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();
		
		if (!FileUtil.filePathValidate(formBean.getDeployDir().trim())) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "目录格式不对");
			return jsonResult.toJSONString();
		}

		// 目录格式验证后将目录路径中的\替换成/
		formBean.setDeployDir(FileUtil.filePathConvert(formBean.getDeployDir().trim()));

		// 需要判断输入的目标服务器是否存在，根据targetServerId来判断吧
		DeployTargetServerBean targetServer = targetServerService.getDeployTargetServer(formBean.getTargetServerId());
		if (null == targetServer) {
			error = 1;
			msg = "目标服务器不存在";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}
		// 判断当前部署流程是否已存在该服务器，同一流程下的同一个服务器只允许添加一次
		if (null != deployFlowServerService.getDeployFlowServer(formBean.getFlowId(), formBean.getTargetServerId())) {
			error = 1;
			msg = "不可重复添加相同的目标服务器";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}

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

		deployFlowServerService.add(formBean,targetServer.getDeployTempDir(), deployAppName);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		DeployFlowServerBean formBean = deployFlowServerService.getDeployFlowServer(id);
		model.addAttribute("flowServer", formBean);

		return "deploy/flowserveredit";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeployFlowServerBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();
		
		if (!FileUtil.filePathValidate(formBean.getDeployDir().trim())) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "目录格式不对");
			return jsonResult.toJSONString();
		}

		// 目录格式验证后将目录路径中的\替换成/
		formBean.setDeployDir(FileUtil.filePathConvert(formBean.getDeployDir().trim()));

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));

		deployFlowServerService.update(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
