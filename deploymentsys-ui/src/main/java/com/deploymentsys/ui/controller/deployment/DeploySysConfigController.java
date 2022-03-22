package com.deploymentsys.ui.controller.deployment;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/deploy/sysconfig")
public class DeploySysConfigController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeploySysConfigController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeploySysConfigService sysConfigService;
	@Autowired
	private WebUtils webUtils;

	@RequestMapping(value = "/getsysconfigtest", method = RequestMethod.GET)
	public void getSysConfigTest() {
		System.out.println("DeploySysConfigController.getSysConfigTest");
		System.out.println(sysConfigService.getFilesTreeIgnore());
		System.out.println(sysConfigService.getDeploySysTempDir());
		System.out.println(sysConfigService.getDeploySysTempDirForPack());
		System.out.println("end");
	}	
	
}
