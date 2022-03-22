package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.UrlWhiteListBean;

public interface UrlWhiteListMapper {

	int add(UrlWhiteListBean bean);

	List<String> getAllUrlWhiteList();

	List<UrlWhiteListBean> list(@Param("pageStart") int pageStart, @Param("size") int size, @Param("url") String url);

	int getListCount(@Param("url") String url);

	int delete(int[] ids);
}