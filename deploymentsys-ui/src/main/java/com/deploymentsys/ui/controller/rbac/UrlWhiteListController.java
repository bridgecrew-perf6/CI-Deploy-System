package com.deploymentsys.ui.controller.rbac;

import java.util.Arrays;
import java.util.List;

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
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.UrlWhiteListBean;
import com.deploymentsys.service.UrlWhiteListService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/urlwhitelist")
public class UrlWhiteListController {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlWhiteListController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private UrlWhiteListService urlWhiteListService;

	@Autowired
	private WebUtils webUtils;

	/**
	 * 显示页面：白名单管理列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "rbac/urlwhitelist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getUrlWhiteLists(int page, int limit, String url) {
		LOGGER.info(String.format("go into UrlWhiteListController.getUrlWhiteLists, page:%d, limit:%d, url:%s", page,
				limit, url));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = urlWhiteListService.getListCount(url);
		jsonResult.put("count", count);

		List<UrlWhiteListBean> data = urlWhiteListService.list(pageStart, pageSize, url);

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
		LOGGER.info("go into UrlWhiteListController.delete, ids:" + Arrays.toString(ids));
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
		urlWhiteListService.delete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：添加白名单
	 * 
	 * @return
	 */
	@RequestMapping(value = "/whitelistadd", method = RequestMethod.GET)
	public String addWhiteList() {
		return "rbac/whitelistadd";
	}

	@ResponseBody
	@RequestMapping(value = "/whitelistadd", method = RequestMethod.POST)
	public String addWhiteList(UrlWhiteListBean formBean, HttpServletRequest request) {
		LOGGER.info(String.format("go into UrlWhiteListController.addWhiteList, url:%s", formBean.getUrl()));
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

		urlWhiteListService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}
}
