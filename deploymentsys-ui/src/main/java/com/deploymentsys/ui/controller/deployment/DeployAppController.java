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
import com.deploymentsys.service.deploy.DeployAppService;
import com.deploymentsys.utils.FileUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy")
public class DeployAppController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployAppController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployAppService deployAppService;
	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/app/list", method = RequestMethod.GET)
	public String list() {
		return "deploy/applist";
	}

	@ResponseBody
	@RequestMapping(value = "/getdeployapplist", method = RequestMethod.POST)
	public String getDeployAppList(int page, int limit, String appName) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = deployAppService.getListCount(appName);
		jsonResult.put("count", count);

		List<DeployAppBean> data = deployAppService.list(pageStart, pageSize, appName);

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
	@RequestMapping(value = "/app/delete", method = RequestMethod.POST)
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
		deployAppService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/app/add", method = RequestMethod.GET)
	public String addApp() {
		return "deploy/appadd";
	}

	@ResponseBody
	@RequestMapping(value = "/app/add", method = RequestMethod.POST)
	public String addApp(DeployAppBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		if (!FileUtil.filePathValidate(formBean.getFileDir().trim())) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "目录格式不对");
			return jsonResult.toJSONString();
		}

		// 目录格式验证后将目录路径中的\替换成/
		formBean.setFileDir(FileUtil.filePathConvert(formBean.getFileDir().trim()));

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

		if (deployAppService.add(formBean) > 0) {
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}

		jsonResult.put(ERROR, 1);
		jsonResult.put(MSG, "添加失败");
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/app/edit", method = RequestMethod.GET)
	public String editApp(int id, Model model) {
		DeployAppBean formBean = deployAppService.getDeployApp(id);
		model.addAttribute("app", formBean);
		return "deploy/appadd";
	}

	@ResponseBody
	@RequestMapping(value = "/app/edit", method = RequestMethod.POST)
	public String editApp(DeployAppBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		if (!FileUtil.filePathValidate(formBean.getFileDir().trim())) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "目录格式不对");
			return jsonResult.toJSONString();
		}

		// 目录格式验证后将目录路径中的\替换成/
		formBean.setFileDir(FileUtil.filePathConvert(formBean.getFileDir().trim()));

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));

		deployAppService.updateApp(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
