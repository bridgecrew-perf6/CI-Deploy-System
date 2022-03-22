package com.deploymentsys.ui.utils;

//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.service.deploy.DeployAppService;

//要想@PostConstruct 和 @PreDestroy 生效，这个注解一定要加上
//@Component  
public class Global {
	
	@Autowired
	private DeployAppService deployAppService;

	/**
	 * 
	 * 在web启动时执行
	 * 
	 */
	//@PostConstruct
	public void applicationStart() {
		System.out.println("application start 站点启动");
		
		//这是以读到数据库的
		int count = deployAppService.getListCount("aa");
		
		System.out.println("count: "+count);
		System.out.println("测试完毕");
	}

	/**
	 * 
	 * 在web结束时执行
	 * 
	 */
	//@PreDestroy
	public void applicationEnd() {
		System.out.println("InskyScheduleCenter application end    站点关闭");
	}
}
