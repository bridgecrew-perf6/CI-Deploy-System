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
import com.deploymentsys.beans.constants.FlowType;
import com.deploymentsys.beans.constants.TargetServerOrderType;
import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.beans.deploy.DeployFlowBean;
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.service.deploy.DeployConfigService;
import com.deploymentsys.service.deploy.DeployFlowService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/flow")
public class DeployFlowController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployFlowService deployFlowService;
	@Autowired
	private DeployConfigService deployConfigService;
	@Autowired
	private DeployAppService deployAppService;
	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int deployConfigId, Model model) {
		DeployConfigBean config = deployConfigService.getDeployConfig(deployConfigId);
		DeployAppBean app = null != config ? deployAppService.getDeployApp(config.getAppId()) : null;

		model.addAttribute("deployConfigId", deployConfigId);
		model.addAttribute("deployConfigName", null != config ? config.getConfigName() : "");
		model.addAttribute("deployAppName", null != app ? app.getAppName() : "");
		return "deploy/flowlist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getDeployFlowList(int page, int limit, int deployConfigId) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = deployFlowService.getListCount(deployConfigId);
		jsonResult.put("count", count);

		List<DeployFlowBean> data = deployFlowService.list(pageStart, pageSize, deployConfigId);
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
		deployFlowService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(int configId, Model model) {
		addModelAttribute(model);
		model.addAttribute("configId", configId);

		return "deploy/flowadd";
	}

	/**
	 * model.addAttribute 的键值不能写成 FlowType.TEST，否则前端jsp页面的EL表达式无法获得值
	 * @param model
	 */
	private void addModelAttribute(Model model) {
		model.addAttribute("FlowType_TEST", FlowType.TEST);
		model.addAttribute("FlowType_FORMAL", FlowType.FORMAL);
		model.addAttribute("TargetServerOrderType_CONCURRENCE", TargetServerOrderType.CONCURRENCE);
		model.addAttribute("TargetServerOrderType_QUEUE", TargetServerOrderType.QUEUE);
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeployFlowBean formBean, HttpServletRequest request) {
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

		deployFlowService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		DeployFlowBean formBean = deployFlowService.getDeployFlow(id);
		model.addAttribute("flow", formBean);
		addModelAttribute(model);
		
		return "deploy/flowadd";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeployFlowBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();		

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));
		
		deployFlowService.update(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

}
