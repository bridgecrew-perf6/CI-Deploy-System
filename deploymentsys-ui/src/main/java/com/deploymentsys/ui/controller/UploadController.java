package com.deploymentsys.ui.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/upload")
public class UploadController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadController.class);

	@Autowired
	private DeploySysConfigService sysConfigService;
	@Autowired
	WebUtils webUtils;

	@RequestMapping(value = "/uploadimage", method = { RequestMethod.POST })
	@ResponseBody
	public Object uploadImage(@RequestParam(value = "file", required = false) MultipartFile file) {
		String postfix = "";

		String imageLocalPath = "";
		String imageVirtualUrl = "";

		Map<String, Object> map2 = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		if (file != null) {
			String originalName = file.getOriginalFilename();
			postfix = originalName.substring(originalName.lastIndexOf("."));

			String newFileName = originalName.substring(0, originalName.lastIndexOf(".")) + DateUtil.getStringToday2()
					+ postfix;
			imageLocalPath = sysConfigService.getUploadImagesLocalPath() + SysConstants.FILE_SEPARATOR + newFileName;
			imageVirtualUrl = sysConfigService.getUploadImagesVirtualUrlPrefix() + newFileName;

			File fileTarget = new File(imageLocalPath);

			try {
				if (!fileTarget.getParentFile().exists()) {
					fileTarget.getParentFile().mkdirs();
				}
				file.transferTo(fileTarget);

				map.put("code", 0);
				map.put("msg", "");
				map.put("data", map2);
				map2.put("localPhotoPath", imageLocalPath);
				map2.put("virtualPhotoUrl", imageVirtualUrl);
				return map;
			} catch (Exception e) {
				LOGGER.error("UploadTestController.headImg 出现异常： ", e);
				map.put("code", 1);
				map.put("msg", "图片上传出现异常！");
				return map;
			}

		}

		map.put("code", 1);
		map.put("msg", "图片上传文件不能为空！");
		return map;
	}

}
