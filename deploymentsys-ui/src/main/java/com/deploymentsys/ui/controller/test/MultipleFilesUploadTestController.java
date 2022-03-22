package com.deploymentsys.ui.controller.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.MultipleFilesUploadTestBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.service.MultipleFilesUploadTestService;
import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/multiplefilesupload")
public class MultipleFilesUploadTestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultipleFilesUploadTestController.class);
	private static final int PAGE_SIZE_MAX = 30;

	@Autowired
	private DeploySysConfigService sysConfigService;
	@Autowired
	private MultipleFilesUploadTestService filesUploadService;
	@Autowired
	WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "test/MultipleFilesUploadList";
	}

	@RequestMapping(value = "/uploadfile", method = RequestMethod.GET)
	public String uploadfile() {
		return "test/MultipleFilesUploadTest";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getFilesList(int page, int limit) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = filesUploadService.getListCount();
		jsonResult.put("count", count);

		List<MultipleFilesUploadTestBean> data = filesUploadService.list(pageStart, pageSize);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/uploadfile", method = { RequestMethod.POST })
	@ResponseBody
	public Object uploadFile(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {
		String postfix = "";

		String imageLocalPath = "";
		// String imageVirtualUrl = "";

		Map<String, Object> map2 = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		if (file != null) {
			String originalName = file.getOriginalFilename();
			postfix = originalName.substring(originalName.lastIndexOf("."));

			String newFileName = originalName.substring(0, originalName.lastIndexOf(".")) + DateUtil.getStringToday2()
					+ postfix;
			imageLocalPath = sysConfigService.getUploadFilesLocalPath() + SysConstants.FILE_SEPARATOR + newFileName;
			// imageVirtualUrl = sysConfigService.getUploadImagesVirtualUrlPrefix() +
			// newFileName;

			File fileTarget = new File(imageLocalPath);

			try {
				if (!fileTarget.getParentFile().exists()) {
					fileTarget.getParentFile().mkdirs();
				}
				file.transferTo(fileTarget);

				StaffBean staff = webUtils.getCurrentLoginInfo(request);
				int creatorId = null != staff ? staff.getId() : 0;
				String currDate = webUtils.getCurrentDateStr();
				String clientIp = webUtils.getClientIp(request);

				MultipleFilesUploadTestBean formBean = new MultipleFilesUploadTestBean();
				formBean.setFileLocalPath(imageLocalPath);
				formBean.setCreateDate(currDate);
				formBean.setCreator(creatorId);
				formBean.setCreateIp(clientIp);
				formBean.setModifyDate(currDate);
				formBean.setModifier(creatorId);
				formBean.setModifyIp(clientIp);

				int result = filesUploadService.add(formBean);
				int code = 0;
				if (result == 0) {
					code = 1;
				}
				map.put("code", code);
				map.put("msg", "");
				map.put("data", map2);
				map2.put("localPhotoPath", imageLocalPath);
				// map2.put("virtualPhotoUrl", imageVirtualUrl);
				return map;
			} catch (Exception e) {
				LOGGER.error("MultipleFilesUploadTestController.uploadFile 出现异常： ", e);
				map.put("code", 1);
				map.put("msg", "文件上传出现异常！");
				return map;
			}

		}

		map.put("code", 1);
		map.put("msg", "上传文件不能为空！");
		return map;
	}

	/**
	 * 文件下载
	 * 
	 * @param fileLocalPath
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download")
	public void fileDown(String fileLocalPath, HttpServletResponse response) throws IOException {
		if (StringUtils.isBlank(fileLocalPath)) {
			return;
		}

		File downLoadFile = new File(fileLocalPath);
		if (!downLoadFile.exists() || downLoadFile.isDirectory()) {
			PrintWriter writer = response.getWriter();
			String ret = fileLocalPath + " 文件不存在";
			writer.write(new String(ret.getBytes(), "ISO8859-1"));
			writer.close();
			response.flushBuffer();
			return;
		}

		// 在相应的头添加相关信息。防止浏览器对文件打开，下载的文件名
		// 理解addHeader和setContentType的区别
		// setContentType是设置相应的类型，修改相应的数据包中的已有的头信息
		// addHeader表示在响应的头中，添加信息的响应信息，如：告知浏览器如下响应的内容。禁止打开
		String fileName = fileLocalPath.substring(fileLocalPath.lastIndexOf("/") + 1);

		fileName = new String(fileName.getBytes(), "ISO8859-1");// 解决下载文件的文件名 中文无法显示的问题
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);

		byte[] arr = FileUtils.readFileToByteArray(downLoadFile);
		// 将文件响应给客户端浏览器
		OutputStream os = response.getOutputStream();
		os.write(arr);
		os.flush();
		// 关闭流
		os.close();
	}

}
