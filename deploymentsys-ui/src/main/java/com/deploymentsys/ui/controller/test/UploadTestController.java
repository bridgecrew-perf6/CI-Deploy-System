package com.deploymentsys.ui.controller.test;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.deploymentsys.utils.FileUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/uploadtest")
public class UploadTestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UploadTestController.class);

	@Autowired
	WebUtils webUtils;

	@RequestMapping("/uploadimages02")
	public String uploadImages01() {
		System.out.println("========uploadImages02");
		return "test/uploadimages02";
	}

	@RequestMapping(value = "/uploadimages02", method = { RequestMethod.POST })
	@ResponseBody
	public Object headImg(@RequestParam(value = "file", required = false) MultipartFile file) {
		String prefix = "";
		String dateStr = "";

		String filepath = "";

		Map<String, Object> map2 = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		if (file != null) {
			String originalName = file.getOriginalFilename();
			prefix = originalName.substring(originalName.lastIndexOf(".") + 1);
			// dateStr = format.format(new Date());
			// String filepath = request.getServletContext().getRealPath("/static") +
			// uploadDir + dateStr + "." + prefix;
			filepath = FileUtil.filePathConvert("D:/test2/upload/images") + SysConstants.FILE_SEPARATOR + originalName;
			// filepath = filepath.replace("\\", "/");
			File fileTarget = new File(filepath);
			// 打印查看上传路径
			System.out.println(filepath);

			try {
				if (!fileTarget.getParentFile().exists()) {
					fileTarget.getParentFile().mkdirs();
				}
				file.transferTo(fileTarget);

				map.put("code", 0);
				map.put("msg", "");
				map.put("data", map2);
				map2.put("src", filepath);
				return map;
			} catch (Exception e) {
				LOGGER.error("UploadTestController.headImg 出现异常： ", e);
				map.put("code", 1);
				map.put("msg", "图片上传出现异常！");
				map.put("data", map2);
				map2.put("src", filepath);
				return map;
			}

		}

		map.put("code", 1);
		map.put("msg", "图片上传文件不能为空！");
		map.put("data", map2);
		map2.put("src", filepath);
		return map;
	}

}
