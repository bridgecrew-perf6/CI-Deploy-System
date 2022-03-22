package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.ExcelImportTestBean;

public interface ExcelImportTestMapper {

	List<ExcelImportTestBean> list(@Param("importStartTime") String importStartTime,
			@Param("importEndTime") String importEndTime, @Param("taskId") int taskId,
			@Param("pageStart") int pageStart, @Param("size") int size);

	List<ExcelImportTestBean> listNotPaging(@Param("importStartTime") String importStartTime,
			@Param("importEndTime") String importEndTime, @Param("taskId") int taskId);

	int getListCount(@Param("importStartTime") String importStartTime, @Param("importEndTime") String importEndTime,
			@Param("taskId") int taskId);

	int addBatch(List<ExcelImportTestBean> list);
}