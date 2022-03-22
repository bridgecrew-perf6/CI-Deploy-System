package com.deploymentsys.ui.controller.deployment;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.DeployTaskStatus;
import com.deploymentsys.beans.constants.FlowType;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.deploy.DeployTaskBean;
import com.deploymentsys.service.StaffService;
import com.deploymentsys.service.deploy.DeployTaskService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/task")
public class DeployTaskController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskController.class);

	@Autowired
	private DeployTaskService deployTaskService;
	@Autowired
	private StaffService staffService;
	@Autowired
	private WebUtils webUtils;

	/**
	 * 部署测试任务管理页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/test/list", method = RequestMethod.GET)
	public String listTest() {
		return "deploy/tasktestlist";
	}

	@ResponseBody
	@RequestMapping(value = "/test/listdata", method = RequestMethod.POST)
	public String getTestListData(int page, int limit, String appId, String batchNo, String deploymentApplicant,
			String rollback) {
		return getTaskListByType(page, limit, appId, batchNo, deploymentApplicant, rollback, FlowType.TEST);
	}

	private String getTaskListByType(int page, int limit, String appId, String batchNo, String deploymentApplicant,
			String rollback, String flowType) {
		int pageSize = limit > SysConstants.PAGE_SIZE_MAX ? SysConstants.PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int deploymentApplicantId = 0;
		if (!StringUtils.isBlank(deploymentApplicant)) {
			StaffBean staff = staffService.getStaffByLoginName(deploymentApplicant);
			deploymentApplicantId = null != staff ? staff.getId() : -1;
		}

		rollback = "0".equals(rollback) ? null : rollback;
		int count = deployTaskService.getListCount(flowType, appId, batchNo, deploymentApplicantId, rollback);
		jsonResult.put("count", count);

		List<DeployTaskBean> data = deployTaskService.list(pageStart, pageSize, flowType, appId, batchNo,
				deploymentApplicantId, rollback);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	/**
	 * 部署正式任务管理页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/formal/list", method = RequestMethod.GET)
	public String listFormal() {
		return "deploy/task_formal_list";
	}

	@ResponseBody
	@RequestMapping(value = "/formal/listdata", method = RequestMethod.POST)
	public String getFormalListData(int page, int limit, String appId, String batchNo, String deploymentApplicant,
			String rollback) {
		return getTaskListByType(page, limit, appId, batchNo, deploymentApplicant, rollback, FlowType.FORMAL);
	}

	/**
	 * 终止部署测试任务
	 * 
	 * @param taskId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/test/abort", method = RequestMethod.POST)
	public String abortTestTask(int taskId, HttpServletRequest request) {
		return abortTask(taskId, request);
	}

	/**
	 * 终止部署正式任务
	 * 
	 * @param taskId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/formal/abort", method = RequestMethod.POST)
	public String abortFormalTask(int taskId, HttpServletRequest request) {
		return abortTask(taskId, request);
	}

	/**
	 * 终止任务公共方法
	 * 
	 * @param taskId
	 * @param request
	 * @return
	 */
	private String abortTask(int taskId, HttpServletRequest request) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);

		BaseBean baseBean = new BaseBean(currDate, creatorId, clientIp, currDate, creatorId, clientIp);
		DeployTaskBean taskBean = new DeployTaskBean();
		BeanUtils.copyProperties(baseBean, taskBean);
		taskBean.setId(taskId);
		taskBean.setStatus(DeployTaskStatus.ABORT);

		deployTaskService.updateTaskStatusCascade(taskBean, baseBean);

		jsonResult.put(SysConstants.ERROR_STR, error);
		jsonResult.put(SysConstants.MSG_STR, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 部署测试任务
	 * 
	 * @param taskId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/test/deploy", method = RequestMethod.POST)
	public String deployTestTask(int taskId, HttpServletRequest request) {
		return deployTask(taskId, request);
	}

	/**
	 * 部署正式任务
	 * 
	 * @param taskId
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/formal/deploy", method = RequestMethod.POST)
	public String deployFormalTask(int taskId, HttpServletRequest request) {
		return deployTask(taskId, request);
	}

	/**
	 * 部署任务公共方法
	 * 
	 * @param taskId
	 * @param request
	 * @return
	 */
	private String deployTask(int taskId, HttpServletRequest request) {
		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);

		BaseBean baseBean = new BaseBean(currDate, creatorId, clientIp, currDate, creatorId, clientIp);
		// deployTaskService.updateTaskStatusCascade(taskId,
		// DeployTaskStatus.WAIT_FOR_EXEC_DEPLOY, baseBean);
		return deployTaskService.deployTask(taskId, baseBean);
	}

	@ResponseBody
	@RequestMapping(value = "/formal/rollback", method = RequestMethod.POST)
	public String rollback(int taskId, HttpServletRequest request) {
		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);

		BaseBean baseBean = new BaseBean(currDate, creatorId, clientIp, currDate, creatorId, clientIp);

		return deployTaskService.rollback(taskId, baseBean);
	}

}
