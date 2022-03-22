package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployFlowServerToDoBean;
import com.deploymentsys.dao.deploy.DeployFlowServerToDoMapper;

@Component
public class DeployFlowServerToDoManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowServerToDoManager.class);

	@Autowired
	DeployFlowServerToDoMapper deployFlowServerToDoMapper;

	public List<DeployFlowServerToDoBean> list(int pageStart, int size, int flowServerId) {
		try {
			return deployFlowServerToDoMapper.list(pageStart, size, flowServerId);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署配置流程服务器todo列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public List<DeployFlowServerToDoBean> getListByFlowServerId(int flowServerId) {
		return deployFlowServerToDoMapper.getListByFlowServerId(flowServerId);
	}

	public int getListCount(int flowServerId) {
		try {
			return deployFlowServerToDoMapper.getListCount(flowServerId);
		} catch (Exception ex) {
			LOGGER.error("获取部署配置流程服务器todo列表总和发生异常", ex);
			return 0;
		}
	}

	public DeployFlowServerToDoBean getOneById(int id) {
		try {
			return deployFlowServerToDoMapper.getOneById(id);
		} catch (Exception ex) {
			LOGGER.error("根据id获取服务器todo发生异常", ex);
			return new DeployFlowServerToDoBean();
		}
	}

	public int add(DeployFlowServerToDoBean bean) {
		return deployFlowServerToDoMapper.add(bean);
	}

	public int update(DeployFlowServerToDoBean bean) {
		try {
			return deployFlowServerToDoMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("修改服务器todo发生异常", ex);
			return 0;
		}
	}

	public int softDeleteByFlowServerId(int flowServerId) {
		return deployFlowServerToDoMapper.softDeleteByFlowServerId(flowServerId);
	}

	public int softDelete(int[] ids) {
		try {
			return deployFlowServerToDoMapper.softDelete(ids);
		} catch (Exception ex) {
			LOGGER.error("软删除部署配置流程服务器todo发生异常", ex);
			return 0;
		}
	}

}
