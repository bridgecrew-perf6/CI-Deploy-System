package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.ExcelImportTestBean;
import com.deploymentsys.dao.ExcelImportTestMapper;

@Component
public class ExcelImportTestManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelImportTestManager.class);

	@Autowired
	ExcelImportTestMapper excelImportTestMapper;

	public List<ExcelImportTestBean> list(String importStartTime, String importEndTime, int taskId, int pageStart,
			int size) {
		try {
			return excelImportTestMapper.list(importStartTime, importEndTime, taskId, pageStart, size);
		} catch (Exception ex) {
			LOGGER.error("ExcelImportTestManager.list catch an exception: ", ex);
			return new ArrayList<>();
		}
	}

	public List<ExcelImportTestBean> listNotPaging(String importStartTime, String importEndTime, int taskId) {
		try {
			return excelImportTestMapper.listNotPaging(importStartTime, importEndTime, taskId);
		} catch (Exception ex) {
			LOGGER.error("ExcelImportTestManager.listNotPaging catch an exception: ", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String importStartTime, String importEndTime, int taskId) {
		try {
			return excelImportTestMapper.getListCount(importStartTime, importEndTime, taskId);
		} catch (Exception ex) {
			LOGGER.error("ExcelImportTestManager.getListCount catch an exception: ", ex);
			return 0;
		}
	}

	public int addBatch(List<ExcelImportTestBean> list) {
		return excelImportTestMapper.addBatch(list);
	}

}
