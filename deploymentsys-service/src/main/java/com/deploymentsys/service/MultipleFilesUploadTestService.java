package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.MultipleFilesUploadTestBean;
import com.deploymentsys.manager.MultipleFilesUploadTestManager;

@Service
public class MultipleFilesUploadTestService {

	@Autowired
	private MultipleFilesUploadTestManager filesUploadManager;

	public List<MultipleFilesUploadTestBean> list(int pageStart, int size) {
		return filesUploadManager.list(pageStart, size);
	}

	public int getListCount() {
		return filesUploadManager.getListCount();
	}

	public MultipleFilesUploadTestBean getById(int id) {
		return filesUploadManager.getById(id);
	}

	public int add(MultipleFilesUploadTestBean bean) {
		return filesUploadManager.add(bean);
	}

}
