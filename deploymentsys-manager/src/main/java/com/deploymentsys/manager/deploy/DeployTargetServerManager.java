package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployTargetServerBean;
import com.deploymentsys.dao.deploy.DeployTargetServerMapper;

@Component
public class DeployTargetServerManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTargetServerManager.class);

	@Autowired
	DeployTargetServerMapper deployTargetServerMapper;

	public DeployTargetServerBean getDeployTargetServer(int id) {
		try {
			return deployTargetServerMapper.getDeployTargetServer(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取目标服务器发生异常", ex);
			return new DeployTargetServerBean();
		}
	}

	public List<DeployTargetServerBean> list(int pageStart, int size, String serverIp, String serverPort) {
		try {
			return deployTargetServerMapper.list(pageStart, size, serverIp, serverPort);
		} catch (Exception ex) {
			LOGGER.error("分页获取目标服务器列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String serverIp, String serverPort) {
		try {
			return deployTargetServerMapper.getListCount(serverIp, serverPort);
		} catch (Exception ex) {
			LOGGER.error("获取目标服务器列表总和发生异常", ex);
			return 0;
		}
	}

	public int add(DeployTargetServerBean bean) {
		try {
			return deployTargetServerMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加目标服务器发生异常", ex);
			return 0;
		}
	}

	public int update(DeployTargetServerBean bean) {
		try {
			return deployTargetServerMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("修改目标服务器发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		try {
			return deployTargetServerMapper.softDelete(ids);
		} catch (Exception ex) {
			LOGGER.error("软删除目标服务器发生异常", ex);
			return 0;
		}
	}

}
