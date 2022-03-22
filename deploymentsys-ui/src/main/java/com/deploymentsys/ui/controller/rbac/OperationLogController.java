package com.deploymentsys.ui.controller.rbac;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.OperationLogBean;
import com.deploymentsys.service.OperationLogService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/operationlog")
public class OperationLogController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private OperationLogService operationLogService;

	@Autowired
	private WebUtils webUtils;

	/**
	 * 显示页面：操作日志管理列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "rbac/operationloglist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getOperationLogList(int page, int limit, String url, String staffName) {
//		LOGGER.info(String.format(
//				"go into OperationLogController.getOperationLogList, page:%d, limit:%d, url:%s, staffName:%s", page,
//				limit, url, staffName));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = operationLogService.getListCount(url, staffName);
		jsonResult.put("count", count);

		List<OperationLogBean> data = operationLogService.list(pageStart, pageSize, url, staffName);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}
}
