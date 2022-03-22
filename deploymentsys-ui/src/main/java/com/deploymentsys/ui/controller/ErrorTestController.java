package com.deploymentsys.ui.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/error")
public class ErrorTestController {

	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@RequestMapping("/test01")
	public String errorTest1() throws Exception {
		throw new Exception("this is a new Exception");
	}

	@RequestMapping("/test02")
	public String errorTest2() {
		int a = 1, b = 0;

		int c = a / b;

		return "错误测试02";
	}

	@ResponseBody
	@RequestMapping("/test03")
	public String errorTest3() {
		int a = 1, b = 0;
		int c = a / b;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put(ERROR, 0);
		jsonResult.put(MSG, c);
		return jsonResult.toJSONString();
	}

}
