package com.deploymentsys.ui.controller.deployment;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.service.deploy.DeployConfigService;
import com.deploymentsys.service.deploy.DeployService;
import com.deploymentsys.utils.WebUtils;
import com.deploymentsys.utils.dtree.DTreeResponse;
import com.deploymentsys.utils.dtree.Status;

@Controller
@RequestMapping("/deploy")
public class DeployController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployController.class);
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployService deployService;
	@Autowired
	private DeployAppService appService;
	@Autowired
	private DeployConfigService configService;
	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/applyfordeploy", method = RequestMethod.GET)
	public String applyForDeploy() {
		return "deploy/applyfordeploy";
	}

	/**
	 * 添加部署任务
	 * 
	 * @param appId
	 * @param description
	 * @param configId
	 * @param files
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/adddeploytask", method = RequestMethod.POST)
	public String addDeployTask(int appId, String description, int configId, String projectVersion,
			@RequestParam(value = "files[]") String[] files, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		LOGGER.info("DeployController.doAppDeployApply params: {} {} {}", appId, description, configId);
		// 验证app是否存在
		DeployAppBean app = appService.getDeployApp(appId);
		if (null == app) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "应用不存在");
			return jsonResult.toJSONString();
		}

		// 验证config是否存在
		DeployConfigBean config = configService.getDeployConfig(configId);
		if (null == config) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "部署配置不存在");
			return jsonResult.toJSONString();
		}

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);

		BaseBean baseBean = new BaseBean(currDate, creatorId, clientIp, currDate, creatorId, clientIp);

		return deployService.addDeployTask(app, description, projectVersion, config, baseBean, files);
	}

	/**
	 * 获取应用文件列表前的验证
	 * 
	 * @param appId
	 * @param configId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/applyfordeploy", method = RequestMethod.POST)
	public String applyForDeploy(int appId, int configId) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		// 验证app是否存在
		DeployAppBean app = appService.getDeployApp(appId);
		if (null == app) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "应用不存在");
			return jsonResult.toJSONString();
		}

		// 验证config是否存在
		if (null == configService.getDeployConfig(configId)) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "部署配置不存在");
			return jsonResult.toJSONString();
		}

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 获取应用的文件树
	 * 
	 * @param appId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getappdeployfiles", method = RequestMethod.POST)
	public String getAppDeployFiles(int appId) {
		DTreeResponse filesTreeData = new DTreeResponse();
		// 验证app是否存在
		DeployAppBean app = appService.getDeployApp(appId);
		if (null == app) {
			filesTreeData.setStatus(new Status(201, "应用不存在"));
			return JSONObject.toJSON(filesTreeData).toString();
		}

		filesTreeData = deployService.getDeployAppFiles(app.getFileDir());

		return JSONObject.toJSON(filesTreeData).toString();
	}

}
