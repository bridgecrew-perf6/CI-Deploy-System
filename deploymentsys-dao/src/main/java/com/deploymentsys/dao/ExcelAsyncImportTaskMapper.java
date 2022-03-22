package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.ExcelAsyncImportTaskBean;

public interface ExcelAsyncImportTaskMapper {

	List<ExcelAsyncImportTaskBean> list(@Param("importStartTime") String importStartTime,
			@Param("importEndTime") String importEndTime, @Param("pageStart") int pageStart, @Param("size") int size);

	int getListCount(@Param("importStartTime") String importStartTime, @Param("importEndTime") String importEndTime);

	int add(ExcelAsyncImportTaskBean bean);

	int update(ExcelAsyncImportTaskBean bean);

}