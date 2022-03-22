package com.deploymentsys.ui.controller.deployment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.service.deploy.DeployTaskFileService;
import com.deploymentsys.utils.ZipUtils;

@Controller
@RequestMapping("/deploy/taskfile")
public class DeployTaskFileController {
	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskFileController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private DeployTaskFileService taskFileService;
	@Autowired
	private DeploySysConfigService sysConfigService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(String appName, int appId, String batchNo, Model model) {
		model.addAttribute("appName", appName);
		model.addAttribute("appId", appId);
		model.addAttribute("batchNo", batchNo);

		return "deploy/taskfilelist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getListData(int page, int limit, String fileName, int appId, String batchNo) {
		LOGGER.info("DeployTaskFileController.getListData params: batchNo: {}, appId: {}", batchNo, appId);
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = taskFileService.getListCount(fileName, appId, batchNo);
		jsonResult.put("count", count);

		List<DeployTaskFileBean> data = taskFileService.list(pageStart, pageSize, fileName, appId, batchNo);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/download")
	public void fileDown(String appName, String batchNo, String relativePath, HttpServletResponse response)
			throws IOException {
		if (StringUtils.isBlank(appName) || StringUtils.isBlank(appName) || StringUtils.isBlank(relativePath)) {
			return;
		}

		String realPath = sysConfigService.getDeploySysTempDir() + SysConstants.FILE_SEPARATOR + appName
				+ SysConstants.FILE_SEPARATOR + batchNo + relativePath;

		File downLoadFile = new File(realPath);
		if (!downLoadFile.exists() || downLoadFile.isDirectory()) {
			PrintWriter writer = response.getWriter();
			String ret = realPath + " 文件不存在";
			writer.write(new String(ret.getBytes(), "ISO8859-1"));
			writer.close();
			response.flushBuffer();
			return;
		}

		// 在相应的头添加相关信息。防止浏览器对文件打开，下载的文件名
		// 理解addHeader和setContentType的区别
		// setContentType是设置相应的类型，修改相应的数据包中的已有的头信息
		// addHeader表示在响应的头中，添加信息的响应信息，如：告知浏览器如下响应的内容。禁止打开
		String fileName = relativePath.substring(relativePath.lastIndexOf("/") + 1);
		fileName = new String(fileName.getBytes(), "ISO8859-1");// 解决下载文件的文件名 中文无法显示的问题，如果使用
																// URLEncoder.encode(fileName,"UTF-8"); 文件名包含空格，空格会被转换成
																// + 加号
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);

		byte[] arr = FileUtils.readFileToByteArray(downLoadFile);
		// 将文件响应给客户端浏览器
		OutputStream os = response.getOutputStream();
		os.write(arr);
		os.flush();
		// 关闭流
		os.close();
	}

	/**
	 * 批量打包下载文件，注意下载文件如果使用ajax请求将无法响应下载文件；另外前端使用form post提交参数，spring mvc接收
	 * Array数组参数，和ajax提交array数组参数不一样，不需要加上@RequestParam(value = "relativePaths[]")
	 * 
	 * @param appName
	 * @param batchNo
	 * @param relativePaths
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/download", method = RequestMethod.POST)
	public void filesDown(String appName, String batchNo, String[] relativePaths, HttpServletResponse response)
			throws IOException {
		if (StringUtils.isBlank(appName) || StringUtils.isBlank(appName) || relativePaths.length == 0) {
			return;
		}
		LOGGER.info("filesDown params: appName: {}, batchNo: {}", appName, batchNo);
		// 先将需要打包的文件拷贝到临时目录
		String root = sysConfigService.getDeploySysTempDir() + SysConstants.FILE_SEPARATOR + appName
				+ SysConstants.FILE_SEPARATOR + batchNo;
		String packRoot = sysConfigService.getDeploySysTempDirForPack() + SysConstants.FILE_SEPARATOR + appName + "-"
				+ batchNo;
		for (String path : relativePaths) {
			String sourceFile = root + path;
			String targetDir = packRoot + path.substring(0, path.lastIndexOf("/"));
			
			File downLoadFile = new File(sourceFile);
			if (!downLoadFile.exists() || downLoadFile.isDirectory()) {
				PrintWriter writer = response.getWriter();
				String ret = sourceFile + " 文件不存在";
				writer.write(new String(ret.getBytes(), "ISO8859-1"));
				writer.close();
				response.flushBuffer();
				return;
			}

			FileUtils.copyFileToDirectory(downLoadFile, new File(targetDir));
		}

		// zip打包
		String zipFilePath = packRoot + ".zip";
		File zipFile = new File(zipFilePath);
		FileOutputStream fos1 = new FileOutputStream(zipFile);
		ZipUtils.toZip(packRoot, fos1, true);

		// 在相应的头添加相关信息。防止浏览器对文件打开，下载的文件名
		String fileName = appName + "-" + batchNo + ".zip";
		fileName = new String(fileName.getBytes(), "ISO8859-1");// 解决下载文件的文件名 中文无法显示的问题
		response.addHeader("Content-disposition", "attachment;filename=" + fileName);

		byte[] arr = FileUtils.readFileToByteArray(zipFile);
		// 将文件响应给客户端浏览器
		OutputStream os = response.getOutputStream();
		os.write(arr);
		os.flush();
		// 关闭流
		os.close();

		// 删除打包的临时文件
		FileUtils.deleteQuietly(zipFile);
		FileUtils.deleteQuietly(new File(packRoot));
	}

}
