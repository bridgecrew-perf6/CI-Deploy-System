package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployLogBean;

public interface DeployLogMapper {

	int add(DeployLogBean bean);

	List<DeployLogBean> listByTaskId(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("taskId") int taskId);
	int getListCountByTaskId(@Param("taskId") int taskId);

	List<DeployLogBean> listByTaskServerId(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("taskId") int taskId, @Param("taskServerId") int taskServerId);
	int getListCountByTaskServerId(@Param("taskId") int taskId, @Param("taskServerId") int taskServerId);

	List<DeployLogBean> listByTaskServerToDoId(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("taskId") int taskId, @Param("taskServerId") int taskServerId,
			@Param("serverTodoId") int serverTodoId);
	int getListCountByTaskServerToDoId(@Param("taskId") int taskId, @Param("taskServerId") int taskServerId,
			@Param("serverTodoId") int serverTodoId);

}