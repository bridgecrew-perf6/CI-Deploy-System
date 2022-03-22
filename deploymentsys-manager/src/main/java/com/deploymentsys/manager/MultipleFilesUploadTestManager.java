package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.MultipleFilesUploadTestBean;
import com.deploymentsys.dao.MultipleFilesUploadTestMapper;

@Component
public class MultipleFilesUploadTestManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(MultipleFilesUploadTestManager.class);

	@Autowired
	MultipleFilesUploadTestMapper filesUploadMapper;

	public MultipleFilesUploadTestBean getById(int id) {
		try {
			return filesUploadMapper.getById(id);
		} catch (Exception ex) {
			LOGGER.error("MultipleFilesUploadTestManager.getById catch an exception： ", ex);
			return new MultipleFilesUploadTestBean();
		}
	}

	public List<MultipleFilesUploadTestBean> list(int pageStart, int size) {
		try {
			return filesUploadMapper.list(pageStart, size);
		} catch (Exception ex) {
			LOGGER.error("MultipleFilesUploadTestManager.list catch an exception: ", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount() {
		try {
			return filesUploadMapper.getListCount();
		} catch (Exception ex) {
			LOGGER.error("MultipleFilesUploadTestManager.getListCount catch an exception: ", ex);
			return 0;
		}
	}

	public int add(MultipleFilesUploadTestBean bean) {
		try {
			return filesUploadMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("MultipleFilesUploadTestManager.add catch an exception: ", ex);
			return 0;
		}
	}

}
