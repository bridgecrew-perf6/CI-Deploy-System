package com.deploymentsys.ui.controller.deployment;

import java.io.IOException;
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
import com.deploymentsys.beans.deploy.DeployTargetServerBean;
import com.deploymentsys.service.deploy.DeployTargetServerService;
import com.deploymentsys.utils.FileUtil;
import com.deploymentsys.utils.SocketIOUtils;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/targetserver")
public class DeployTargetServerController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTargetServerController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployTargetServerService deployTargetServerService;

	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "deploy/targetserverlist";
	}

	@ResponseBody
	@RequestMapping(value = "/getlistdata", method = RequestMethod.POST)
	public String getListData(int page, int limit, String serverIp, String serverPort) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = deployTargetServerService.getListCount(serverIp, serverPort);
		jsonResult.put("count", count);

		List<DeployTargetServerBean> data = deployTargetServerService.list(pageStart, pageSize, serverIp, serverPort);

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
		deployTargetServerService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 测试连接
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/testconnect", method = RequestMethod.POST)
	public String testConnect(String ip, int port) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		boolean isConnected = SocketIOUtils.testConnect(ip, port, 1);
//		try {
//			isConnected = SocketIOUtils.testConnect(ip, port, 3);
//		} catch (IOException e) {
//			LOGGER.error("testConnect发生异常：", e);
//		}

		error = isConnected ? error : 1;
		msg = isConnected ? msg : "连接失败";

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add() {
		return "deploy/targetserveradd";
	}

	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String add(DeployTargetServerBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		if (!FileUtil.filePathValidate(formBean.getDeployTempDir().trim())) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "目录格式不对");
			return jsonResult.toJSONString();
		}

		// 目录格式验证后将目录路径中的\替换成/
		formBean.setDeployTempDir(FileUtil.filePathConvert(formBean.getDeployTempDir().trim()));

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

		deployTargetServerService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		DeployTargetServerBean formBean = deployTargetServerService.getDeployTargetServer(id);
		model.addAttribute("targetServer", formBean);
		return "deploy/targetserveradd";
	}

	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	public String edit(DeployTargetServerBean formBean, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();
		if (!FileUtil.filePathValidate(formBean.getDeployTempDir().trim())) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "目录格式不对");
			return jsonResult.toJSONString();
		}

		// 目录格式验证后将目录路径中的\替换成/
		formBean.setDeployTempDir(FileUtil.filePathConvert(formBean.getDeployTempDir().trim()));

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));

		deployTargetServerService.update(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
