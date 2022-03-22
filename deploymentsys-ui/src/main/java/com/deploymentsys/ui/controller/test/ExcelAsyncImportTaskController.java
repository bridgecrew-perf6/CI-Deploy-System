package com.deploymentsys.ui.controller.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.ExcelAsyncImportTaskBean;
import com.deploymentsys.beans.ExcelImportTestBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.service.ExcelAsyncImportTaskService;
import com.deploymentsys.service.ExcelImportTestService;
import com.deploymentsys.service.deploy.DeploySysConfigService;
import com.deploymentsys.ui.listener.ExcelImportListener;
import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.WebUtils;

@Controller
@RequestMapping("/excelasyncimporttask")
public class ExcelAsyncImportTaskController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelAsyncImportTaskController.class);
	private static final int PAGE_SIZE_MAX = 30;

	/**
	 * 处理Excel异步导入任务的线程池
	 */
	private static final ExecutorService EXCEL_ASYNC_IMPORT_THREADPOOL = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	@Autowired
	private DeploySysConfigService sysConfigService;
	@Autowired
	private ExcelAsyncImportTaskService excelAsyncImportTaskService;
	@Autowired
	private ExcelImportTestService excelImportTestService;
	@Autowired
	WebUtils webUtils;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "test/ExcelAsyncImportTaskList";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getListData(String importStartTime, String importEndTime, int page, int limit) {
		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = excelAsyncImportTaskService.getListCount(importStartTime, importEndTime);
		jsonResult.put("count", count);

		List<ExcelAsyncImportTaskBean> data = excelAsyncImportTaskService.list(importStartTime, importEndTime,
				pageStart, pageSize);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/taskdetails", method = RequestMethod.GET)
	public String taskDetails(int taskId, Model model) {
		model.addAttribute("taskId", taskId);
		return "test/ExcelAsyncImportTaskDetails";
	}

	@RequestMapping(value = "/import", method = { RequestMethod.POST })
	@ResponseBody
	public Object importTask(@RequestParam(value = "file", required = true) MultipartFile file,
			HttpServletRequest request) {
		String postfix = "";

		Map<String, Object> map2 = new HashMap<>();
		Map<String, Object> map = new HashMap<>();

		String originalName = file.getOriginalFilename();
		if (originalName.length() > 20) {
			map.put("code", 1);
			map.put("msg", "文件名长度（包括文件后缀）不能超过20个字符");
			return map;
		}

		postfix = originalName.substring(originalName.lastIndexOf("."));

		String newFileName = originalName.substring(0, originalName.lastIndexOf(".")) + "-" + DateUtil.getStringToday2()
				+ postfix;
		String taskName = originalName.substring(0, originalName.lastIndexOf(".")) + "-"
				+ UUID.randomUUID().toString().replaceAll("-", "").substring(0, 4) + postfix;
		String excelLocalPath = sysConfigService.getExcelOperatePath() + SysConstants.FILE_SEPARATOR + newFileName;

		File fileTarget = new File(excelLocalPath);

		try {
			if (!fileTarget.getParentFile().exists()) {
				fileTarget.getParentFile().mkdirs();
			}
			file.transferTo(fileTarget);

			// 生成Excel导入任务
			StaffBean staff = webUtils.getCurrentLoginInfo(request);
			int creatorId = null != staff ? staff.getId() : 0;
			String currDate = webUtils.getCurrentDateStr();
			String clientIp = webUtils.getClientIp(request);

			ExcelAsyncImportTaskBean formBean = new ExcelAsyncImportTaskBean();
			formBean.setCreateDate(currDate);
			formBean.setCreator(creatorId);
			formBean.setCreateIp(clientIp);
			formBean.setModifyDate(currDate);
			formBean.setModifier(creatorId);
			formBean.setModifyIp(clientIp);

			formBean.setTaskName(taskName);
			formBean.setOriginalFileName(originalName);
			formBean.setStatus("处理中");

			excelAsyncImportTaskService.add(formBean);
			System.out.println("id: " + formBean.getId());

			EXCEL_ASYNC_IMPORT_THREADPOOL.execute(new Runnable() {
				public void run() {
					processExcelImportTask(excelLocalPath, formBean);
				}
			});

			map.put("code", 0);
			map.put("msg", "");
			return map;
		} catch (Exception e) {
			LOGGER.error("ExcelAsyncImportTaskController.importTask 出现异常： ", e);
			map.put("code", 1);
			map.put("msg", "导入数据出现异常！");
			return map;
		}

	}

	/**
	 * 多线程异步处理Excel数据导入任务
	 * 
	 * @param excelFilePath
	 * @param formBean
	 */
	private void processExcelImportTask(String excelFilePath, ExcelAsyncImportTaskBean formBean) {
		try {
			BaseBean baseBean = new BaseBean();
			BeanUtils.copyProperties(formBean, baseBean);
			EasyExcel.read(excelFilePath, ExcelImportTestBean.class, new ExcelImportListener(excelImportTestService,
					excelAsyncImportTaskService, baseBean, formBean.getId())).sheet().doRead();
		} catch (Exception e) {
			LOGGER.error("ExcelAsyncImportTaskController.processExcelImportTask 出现异常： ", e);
			formBean.setStatus("失败");
			formBean.setFailureCause(
					e.getCause().getMessage().length() > 255 ? e.getCause().getMessage().substring(0, 255)
							: e.getCause().getMessage());
			excelAsyncImportTaskService.update(formBean);
		}

	}

}
