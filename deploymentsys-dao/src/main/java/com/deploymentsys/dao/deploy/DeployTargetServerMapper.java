package com.deploymentsys.dao.deploy;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.deploymentsys.beans.deploy.DeployTargetServerBean;

public interface DeployTargetServerMapper {
	DeployTargetServerBean getDeployTargetServer(@Param("id") int id);

	int add(DeployTargetServerBean bean);

	int update(DeployTargetServerBean bean);

	List<DeployTargetServerBean> list(@Param("pageStart") int pageStart, @Param("size") int size,
			@Param("serverIp") String serverIp, @Param("serverPort") String serverPort);

	int getListCount(@Param("serverIp") String serverIp, @Param("serverPort") String serverPort);

	/**
	 * 软删除
	 * 
	 * @param ids
	 * @return
	 */
	int softDelete(int[] ids);
}
