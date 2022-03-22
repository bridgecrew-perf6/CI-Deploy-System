package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.OperationLogBean;
import com.deploymentsys.dao.OperationLogMapper;

@Component
public class OperationLogManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationLogManager.class);

	@Autowired
	OperationLogMapper operationLogMapper;

	public List<OperationLogBean> list(int pageStart, int size, String url, String staffName) {
		try {
			return operationLogMapper.list(pageStart, size, url, staffName);
		} catch (Exception ex) {
			LOGGER.error("OperationLogManager.list catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String url, String staffName) {
		try {
			return operationLogMapper.getListCount(url, staffName);
		} catch (Exception ex) {
			LOGGER.error("OperationLogManager.getListCount catch an exception", ex);
			return 0;
		}
	}

	public int add(OperationLogBean bean) {
		try {
			return operationLogMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("OperationLogManager.add catch an exception", ex);
			return 0;
		}
	}

}
