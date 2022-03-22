package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.ExcelImportTestBean;
import com.deploymentsys.manager.ExcelImportTestManager;

@Service
public class ExcelImportTestService {

	@Autowired
	private ExcelImportTestManager excelImportTestManager;

	public List<ExcelImportTestBean> list(String importStartTime, String importEndTime, int taskId, int pageStart,
			int size) {
		return excelImportTestManager.list(importStartTime, importEndTime, taskId, pageStart, size);
	}

	public List<ExcelImportTestBean> listNotPaging(String importStartTime, String importEndTime, int taskId) {
		return excelImportTestManager.listNotPaging(importStartTime, importEndTime, taskId);
	}

	public int getListCount(String importStartTime, String importEndTime, int taskId) {
		return excelImportTestManager.getListCount(importStartTime, importEndTime, taskId);
	}

	public void addBatch(List<ExcelImportTestBean> beans) {
		if (beans.size() > 0) {
			excelImportTestManager.addBatch(beans);
		}
	}

}
