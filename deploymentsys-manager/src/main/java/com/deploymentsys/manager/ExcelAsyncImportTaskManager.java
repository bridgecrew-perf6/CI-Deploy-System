package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.ExcelAsyncImportTaskBean;
import com.deploymentsys.dao.ExcelAsyncImportTaskMapper;

@Component
public class ExcelAsyncImportTaskManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelAsyncImportTaskManager.class);

	@Autowired
	ExcelAsyncImportTaskMapper excelImportTaskMapper;

	public List<ExcelAsyncImportTaskBean> list(String importStartTime, String importEndTime, int pageStart, int size) {
		try {
			return excelImportTaskMapper.list(importStartTime, importEndTime, pageStart, size);
		} catch (Exception ex) {
			LOGGER.error("ExcelImportTaskManager.list catch an exception: ", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String importStartTime, String importEndTime) {
		try {
			return excelImportTaskMapper.getListCount(importStartTime, importEndTime);
		} catch (Exception ex) {
			LOGGER.error("ExcelImportTaskManager.getListCount catch an exception: ", ex);
			return 0;
		}
	}

	public int add(ExcelAsyncImportTaskBean bean) {
		try {
			return excelImportTaskMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("ExcelAsyncImportTaskManager.add catch an exception", ex);
			return 0;
		}
	}

	public int update(ExcelAsyncImportTaskBean bean) {
		try {
			return excelImportTaskMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("ExcelAsyncImportTaskManager.update catch an exception", ex);
			return 0;
		}
	}

}
