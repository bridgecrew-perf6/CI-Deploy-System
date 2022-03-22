package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.MultipleFilesUploadTestBean;

public interface MultipleFilesUploadTestMapper {

	int add(MultipleFilesUploadTestBean bean);

	MultipleFilesUploadTestBean getById(@Param("id") int id);

	List<MultipleFilesUploadTestBean> list(@Param("pageStart") int pageStart, @Param("size") int size);

	int getListCount();

}