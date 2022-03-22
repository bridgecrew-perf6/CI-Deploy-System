package com.deploymentsys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.OperationLogBean;

public interface OperationLogMapper {

	int add(OperationLogBean bean);

	List<OperationLogBean> list(@Param("pageStart") int pageStart, @Param("size") int size, @Param("url") String url,
			@Param("staffName") String staffName);

	int getListCount(@Param("url") String url, @Param("staffName") String staffName);
}