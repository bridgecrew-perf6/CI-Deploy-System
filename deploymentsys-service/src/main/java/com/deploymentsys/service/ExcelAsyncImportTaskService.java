package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.ExcelAsyncImportTaskBean;
import com.deploymentsys.manager.ExcelAsyncImportTaskManager;

@Service
public class ExcelAsyncImportTaskService {

	@Autowired
	private ExcelAsyncImportTaskManager excelImportTaskManager;

	public List<ExcelAsyncImportTaskBean> list(String importStartTime, String importEndTime, int pageStart, int size) {
		return excelImportTaskManager.list(importStartTime, importEndTime, pageStart, size);
	}

	public int getListCount(String importStartTime, String importEndTime) {
		return excelImportTaskManager.getListCount(importStartTime, importEndTime);
	}

	public int add(ExcelAsyncImportTaskBean bean) {
		return excelImportTaskManager.add(bean);
	}

	public int update(ExcelAsyncImportTaskBean bean) {
		return excelImportTaskManager.update(bean);
	}
}
