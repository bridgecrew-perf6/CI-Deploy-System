package com.deploymentsys.ui.controller.test;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/test")
public class TestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@Autowired
	WebUtils webUtils;

	@ResponseBody
	@RequestMapping("/test01")
	public String submitCategory() {
		System.out.println("SSM接受到浏览器提交的json，并转换为Category对象:");
		return "ok";
	}

	@ResponseBody
	@RequestMapping("/test02")
	public String submitCategory2() {
		System.out.println("SSM接受到浏览器提交的json，并转换为Category对象asdasdasdsadad:");
		return "ok21312312";
	}

	@RequestMapping("/gohtml")
	public String gotoHtmlView() {
		System.out.println("访问html");
		return "test/login";
	}
	
	@RequestMapping("/uploadimages01")
	public String uploadImages01() {
		System.out.println("========uploadImages01");
		return "test/uploadimages01";
	}

	@RequestMapping("/firstlayui")
	public String firstlayui() {
		return "test/firstlayui";
	}

	@ResponseBody
	@RequestMapping("/logtest01")
	public String logTest01() {
		LOGGER.debug("这是debug消息");
		LOGGER.info("这是info消息");
		LOGGER.error("这是error消息");
		return "logTest01";
	}

	@ResponseBody
	@RequestMapping("/getip")
	public String getIp(HttpServletRequest request) {
		String ip = webUtils.getClientIp(request);
		return ip;
	}
	
	/**
	 * dtree 测试
	 * @return
	 */
	@RequestMapping("/dtree")
	public String dtree() {
		System.out.println("dtree 测试");
		return "test/dtreetest";
	}
}
