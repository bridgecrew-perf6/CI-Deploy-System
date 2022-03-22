package com.deploymentsys.service.deploy;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.constants.TargetServerToDoType;
import com.deploymentsys.beans.deploy.DeployFlowServerBean;
import com.deploymentsys.beans.deploy.DeployFlowServerToDoBean;
import com.deploymentsys.manager.deploy.DeployFlowServerManager;
import com.deploymentsys.manager.deploy.DeployFlowServerToDoManager;

@Service
public class DeployFlowServerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployFlowServerService.class);

	@Autowired
	private DeployFlowServerManager deployFlowServerManager;
	@Autowired
	private DeployFlowServerToDoManager deployFlowServerToDoManager;

	public List<DeployFlowServerBean> list(int pageStart, int size, int deployFlowId) {
		return deployFlowServerManager.list(pageStart, size, deployFlowId);
	}

	public int getListCount(int deployFlowId) {
		return deployFlowServerManager.getListCount(deployFlowId);
	}

	public DeployFlowServerBean getDeployFlowServer(int id) {
		return deployFlowServerManager.getDeployFlowServer(id);
	}

	public DeployFlowServerBean getDeployFlowServer(int flowId, int targetServerId) {
		return deployFlowServerManager.getDeployFlowServer(flowId, targetServerId);
	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public int softDelete(int[] ids) {
		for (int i : ids) {
			deployFlowServerToDoManager.softDeleteByFlowServerId(i);
		}
		return deployFlowServerManager.softDelete(ids);
	}

	public int update(DeployFlowServerBean bean) {
		return deployFlowServerManager.update(bean);
	}

	/**
	 * 
	 * @param flowServerBean
	 * @param targetServerDeployTempDir
	 *            目标服务器上的部署临时目录
	 * @param deployAppName
	 *            需要部署的应用名称
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public void add(DeployFlowServerBean flowServerBean, String targetServerDeployTempDir, String deployAppName) {
		// int error = 0;
		// String msg = "";
		// JSONObject jsonResult = new JSONObject();
		deployFlowServerManager.add(flowServerBean);
		// LOGGER.info("DeployFlowServerService.add result2:{}",
		// flowServerBean.getId());

		String fileTransferTempDir = targetServerDeployTempDir + SysConstants.FILE_SEPARATOR + deployAppName
				+ SysConstants.FILE_SEPARATOR + SysConstants.BATCHNO_STR;
		DeployFlowServerToDoBean toDoBean1 = getToDoBean(flowServerBean, TargetServerToDoType.FILE_TRANSFER, 0,
				fileTransferTempDir);
		DeployFlowServerToDoBean toDoBean2 = getToDoBean(flowServerBean, TargetServerToDoType.FILE_COPY, 1,
				fileTransferTempDir, flowServerBean.getDeployDir());

		// 添加默认的两个部署流程目标服务器执行明细，文件传输、文件拷贝
		deployFlowServerToDoManager.add(toDoBean1);
		deployFlowServerToDoManager.add(toDoBean2);
	}

	private DeployFlowServerToDoBean getToDoBean(DeployFlowServerBean flowServerBean, String todoType, int todoOrder,
			String... params) {
		DeployFlowServerToDoBean toDoBean = new DeployFlowServerToDoBean();

		toDoBean.setFlowServerId(flowServerBean.getId());
		toDoBean.setTodoType(todoType);
		toDoBean.setTodoOrder(todoOrder);

		for (int i = 0; i < params.length; i++) {
			if (0 == i) {
				toDoBean.setParam1(params[i]);
			}
			if (1 == i) {
				toDoBean.setParam2(params[i]);
			}
			if (2 == i) {
				toDoBean.setParam3(params[i]);
			}
		}

		toDoBean.setCreateDate(flowServerBean.getCreateDate());
		toDoBean.setCreator(flowServerBean.getCreator());
		toDoBean.setCreateIp(flowServerBean.getCreateIp());
		toDoBean.setModifyDate(flowServerBean.getModifyDate());
		toDoBean.setModifier(flowServerBean.getModifier());
		toDoBean.setModifyIp(flowServerBean.getModifyIp());
		return toDoBean;
	}

}