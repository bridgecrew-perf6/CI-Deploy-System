package com.deploymentsys.ui.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.ExcelAsyncImportTaskBean;
import com.deploymentsys.beans.ExcelImportTestBean;
import com.deploymentsys.service.ExcelAsyncImportTaskService;
import com.deploymentsys.service.ExcelImportTestService;

public class ExcelImportListener extends AnalysisEventListener<ExcelImportTestBean> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportListener.class);

	private ExcelImportTestService excelImportTestService;
	private ExcelAsyncImportTaskService excelAsyncImportTaskService;
	private BaseBean baseBean;
	private int taskId;

	public ExcelImportListener(ExcelImportTestService excelImportTestService,
			ExcelAsyncImportTaskService excelAsyncImportTaskService, BaseBean baseBean, int taskId) {
		super();
		this.excelImportTestService = excelImportTestService;
		this.excelAsyncImportTaskService = excelAsyncImportTaskService;
		this.baseBean = baseBean;
		this.taskId = taskId;
	}

	private static final int BATCH_COUNT = 10;
	private List<ExcelImportTestBean> datas = new ArrayList<ExcelImportTestBean>();

	@Override
	public void onException(Exception exception, AnalysisContext context) throws Exception {
		throw exception;
	}

	@Override
	public void invoke(ExcelImportTestBean data, AnalysisContext context) {
		// LOGGER.info("解析到一条数据:{}", JSON.toJSONString(data));
		if (data.getUserName().length() > 20) {
			throw new RuntimeException(String.format("用户名：%s 长度超过20", data.getUserName()));
		}
		// 验证手机号码格式
		if (!isMobileNumber(data.getCellPhone())) {
			throw new RuntimeException(String.format("手机号码：%s 格式不对", data.getCellPhone()));
		}

		BeanUtils.copyProperties(baseBean, data);
		data.setTaskId(taskId);
		datas.add(data);
		if (datas.size() >= BATCH_COUNT) {
			excelImportTestService.addBatch(datas);
			datas.clear();
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext context) {
		// System.out.println(String.format("Excel读取完毕，总共：%d 条数据", datas.size()));
		excelImportTestService.addBatch(datas);
		datas.clear();// 解析结束销毁不用的资源

		if (taskId != 0) {
			ExcelAsyncImportTaskBean formBean = new ExcelAsyncImportTaskBean();
			BeanUtils.copyProperties(baseBean, formBean);
			formBean.setId(taskId);
			formBean.setStatus("成功");
			excelAsyncImportTaskService.update(formBean);
		}
	}

	private boolean isMobileNumber(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		String s2 = "^[1](([3][0-9])|([4][5,7,9])|([5][0-9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";// 验证手机号
		if (StringUtils.isNotBlank(str)) {
			p = Pattern.compile(s2);
			m = p.matcher(str);
			b = m.matches();
		}
		return b;
	}

}
