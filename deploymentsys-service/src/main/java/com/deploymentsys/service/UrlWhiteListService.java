package com.deploymentsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.UrlWhiteListBean;
import com.deploymentsys.manager.UrlWhiteListManager;

@Service
public class UrlWhiteListService {

	@Autowired
	private UrlWhiteListManager urlWhiteListManager;

	public List<String> getAllUrlWhiteList() {
		return urlWhiteListManager.getAllUrlWhiteList();
	}

	public List<UrlWhiteListBean> list(int pageStart, int size, String url) {
		return urlWhiteListManager.list(pageStart, size, url);
	}

	public int getListCount(String url) {
		return urlWhiteListManager.getListCount(url);
	}

	public int delete(int[] ids) {
		return urlWhiteListManager.delete(ids);
	}

	public int add(UrlWhiteListBean bean) {
		return urlWhiteListManager.add(bean);
	}
}
