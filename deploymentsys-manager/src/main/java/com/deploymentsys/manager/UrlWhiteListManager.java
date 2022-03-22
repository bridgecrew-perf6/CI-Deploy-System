package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.UrlWhiteListBean;
import com.deploymentsys.dao.UrlWhiteListMapper;

@Component
public class UrlWhiteListManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(UrlWhiteListManager.class);

	@Autowired
	UrlWhiteListMapper urlWhiteListMapper;

	public List<String> getAllUrlWhiteList() {
		try {
			return urlWhiteListMapper.getAllUrlWhiteList();
		} catch (Exception ex) {
			LOGGER.error("UrlWhiteListManager.getAllUrlWhiteList catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public List<UrlWhiteListBean> list(int pageStart, int size, String url) {
		try {
			return urlWhiteListMapper.list(pageStart, size, url);
		} catch (Exception ex) {
			LOGGER.error("UrlWhiteListManager.list catch an exception", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String url) {
		try {
			return urlWhiteListMapper.getListCount(url);
		} catch (Exception ex) {
			LOGGER.error("UrlWhiteListManager.getListCount catch an exception", ex);
			return 0;
		}
	}

	public int delete(int[] ids) {
		try {
			return urlWhiteListMapper.delete(ids);
		} catch (Exception ex) {
			LOGGER.error("UrlWhiteListManager.delete catch an exception", ex);
			return 0;
		}
	}

	public int add(UrlWhiteListBean bean) {
		try {
			return urlWhiteListMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("UrlWhiteListManager.add catch an exception", ex);
			return 0;
		}
	}

}
