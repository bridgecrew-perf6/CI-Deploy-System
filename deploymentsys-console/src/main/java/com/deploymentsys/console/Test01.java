package com.deploymentsys.console;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.deploymentsys.service.UrlWhiteListService;

public class Test01 {
	private static final Logger LOGGER = LoggerFactory.getLogger(Test01.class);

	// @Autowired
	// private static UrlWhiteListService urlWhiteListService;

	static {
		PropertyConfigurator.configure(
				System.getProperty("user.dir") + File.separator + "config" + File.separator + "log4j.properties");

		// 下面的代码貌似行不通！！
		// 加载spring的配置文件
		// ApplicationContext context = new ClassPathXmlApplicationContext(paths);
		// BeanFactory factory = (BeanFactory) context;
	}

	public static void main(String[] args) throws IOException {
		// 加载spring的配置文件
		String paths[] = { "applicationContext.xml" };
		ApplicationContext context = new ClassPathXmlApplicationContext(paths);
		BeanFactory factory = (BeanFactory) context;
		UrlWhiteListService urlWhiteListService = (UrlWhiteListService) factory.getBean(UrlWhiteListService.class);

		int count = urlWhiteListService.getListCount("");
		System.out.println(count);

		List<String> urls = urlWhiteListService.getAllUrlWhiteList();
		for (String url : urls) {
			System.out.println(url);
		}

		LOGGER.error("出错了");
		LOGGER.info("this is msg");
		System.out.println(111);

		// 类似于C#的 Console.ReadLine() 效果
		System.in.read();
	}

}
