package com.deploymentsys.ui.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.ui.controller.rbac.MenuController;

@ControllerAdvice
public class ErrorController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@ResponseBody
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception ex) {
		LOGGER.error("ErrorController.exceptionHandler catch an exception", ex);
		JSONObject jsonResult = new JSONObject();
		jsonResult.put(ERROR, 1);
		jsonResult.put(MSG, "系统出错啦！");
		return jsonResult.toJSONString();
	}
}
