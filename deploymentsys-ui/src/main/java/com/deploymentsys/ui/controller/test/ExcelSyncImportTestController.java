package com.deploymentsys.ui.controller.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.MessageFormat;
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

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.ExcelImportTestBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.service.ExcelImportTestService;
import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.ui.listener.ExcelImportListener;
import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/excelsyncimporttest")
public class ExcelSyncImportTestController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelSyncImportTestController.class);
	private static final int PAGE_SIZE_MAX = 30;

	@Autowired
	private DeploySysConfigService sysConfigService;
	@Autowired
	private ExcelImportTestService excelImportTestService;
	@Autowired
	WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "test/ExcelSyncImportTest";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getListData(String importStartTime, String importEndTime, String taskIdStr, int page, int limit) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);
		
		int taskId = 0;
		if (!StringUtils.isBlank(taskIdStr)) {
			taskId = Integer.valueOf(taskIdStr);
		}

		int count = excelImportTestService.getListCount(importStartTime, importEndTime, taskId);
		jsonResult.put("count", count);

		List<ExcelImportTestBean> data = excelImportTestService.list(importStartTime, importEndTime, taskId, pageStart,
				pageSize);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/downloadtmpl")
	public void tmplDown(HttpServletResponse response) throws IOException {

		String realPath = sysConfigService.getExcelImportTemplateUrl();

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
		String fileName = downLoadFile.getName();
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

	@RequestMapping(value = "/export")
	public void exportData(String importStartTime, String importEndTime, HttpServletResponse response)
			throws IOException {
		LOGGER.info(MessageFormat.format("进来 exportData，importStartTime【{0}】，importEndTime【{1}】", importStartTime, importEndTime));
		List<ExcelImportTestBean> data = excelImportTestService.listNotPaging(importStartTime, importEndTime, 0);
		LOGGER.info("data的数量：" + data.size());
		response.setContentType("application/vnd.ms-excel");
		response.setCharacterEncoding("utf-8");
		String fileName = "数据导出" + DateUtil.getStringToday2() + ".xlsx";
		fileName = new String(fileName.getBytes(), "ISO8859-1");
		response.setHeader("Content-disposition", "attachment;filename=" + fileName);
		EasyExcel.write(response.getOutputStream(), ExcelImportTestBean.class).sheet("数据导出").doWrite(data);
	}

	@RequestMapping(value = "/import", method = { RequestMethod.POST })
	@ResponseBody
	public Object importExcel(@RequestParam(value = "file", required = false) MultipartFile file,
			HttpServletRequest request) {

		Map<String, Object> map = new HashMap<>();

		if (file != null) {
			StaffBean staff = webUtils.getCurrentLoginInfo(request);
			int creatorId = null != staff ? staff.getId() : 0;
			String currDate = webUtils.getCurrentDateStr();
			String clientIp = webUtils.getClientIp(request);

			BaseBean baseBean = new BaseBean(currDate, creatorId, clientIp, currDate, creatorId, clientIp);
			try {
				EasyExcel.read(file.getInputStream(), ExcelImportTestBean.class,
						new ExcelImportListener(excelImportTestService, null, baseBean, 0)).sheet().doRead();

				map.put("code", 0);
				map.put("msg", "");
				return map;
			} catch (Exception e) {
				LOGGER.error("ExcelSyncImportTestController.importExcel 出现异常： ", e);
				map.put("code", 1);
				map.put("msg", e.getCause().getMessage());
				return map;
			}

		}

		map.put("code", 1);
		map.put("msg", "导入文件不能为空！");
		return map;
	}

}
