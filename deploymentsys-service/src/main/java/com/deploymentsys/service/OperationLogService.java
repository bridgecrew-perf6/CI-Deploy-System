package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.OperationLogBean;
import com.deploymentsys.manager.OperationLogManager;

@Service
public class OperationLogService {

	@Autowired
	private OperationLogManager operationLogManager;

	public List<OperationLogBean> list(int pageStart, int size, String url, String staffName) {
		return operationLogManager.list(pageStart, size, url, staffName);
	}

	public int getListCount(String url, String staffName) {
		return operationLogManager.getListCount(url, staffName);
	}

	public int add(OperationLogBean bean) {
		return operationLogManager.add(bean);
	}
}
