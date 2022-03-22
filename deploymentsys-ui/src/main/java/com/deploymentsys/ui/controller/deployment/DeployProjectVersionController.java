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
import com.deploymentsys.beans.deploy.DeployProjectBean;
import com.deploymentsys.beans.deploy.DeployProjectVersionBean;
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.service.deploy.DeployProjectService;
import com.deploymentsys.service.deploy.DeployProjectVersionService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/projectversion")
public class DeployProjectVersionController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployProjectVersionController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployAppService deployAppService;
	@Autowired
	private DeployProjectService projectService;
	@Autowired
	private DeployProjectVersionService projectVersionService;
	@Autowired
	private WebUtils webUtils;

	@ResponseBody
	@RequestMapping(value = "/getprojectversionsbyprojectid", method = RequestMethod.POST)
	public String getAllProjectVersionsByProjectId(int appId) {
		DeployAppBean app = deployAppService.getDeployApp(appId);
		int projectId = null != app ? app.getProjectId() : 0;

		List<DeployProjectVersionBean> data = projectVersionService.listAllByProjectId(projectId);
		return JSONObject.toJSON(data).toString();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(int projectId, Model model) {
		DeployProjectBean project = projectService.getById(projectId);

		model.addAttribute("projectId", projectId);
		model.addAttribute("projectName", null != project ? project.getProjectName() : "");
		return "deploy/projectversionlist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getListData(int page, int limit, int projectId) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = projectVersionService.getListCount(projectId);
		jsonResult.put("count", count);

		List<DeployProjectVersionBean> data = projectVersionService.list(pageStart, pageSize, projectId);

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
		projectVersionService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(int projectId, Model model) {
		DeployProjectVersionBean formBean = new DeployProjectVersionBean();
		formBean.setProjectId(projectId);
		model.addAttribute("projectVersion", formBean);
		return "deploy/projectversionadd";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeployProjectVersionBean formBean, HttpServletRequest request) {
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

		projectVersionService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		DeployProjectVersionBean projectVersion = projectVersionService.getById(id);
		model.addAttribute("projectVersion", projectVersion);

		return "deploy/projectversionadd";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeployProjectVersionBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));
		projectVersionService.update(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
