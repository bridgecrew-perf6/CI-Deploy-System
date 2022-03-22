package com.deploymentsys.service.deploy;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.constants.DeployTaskStatus;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.deploy.DeployAppBean;
import com.deploymentsys.beans.deploy.DeployConfigBean;
import com.deploymentsys.beans.deploy.DeployFlowBean;
import com.deploymentsys.beans.deploy.DeployFlowServerBean;
import com.deploymentsys.beans.deploy.DeployFlowServerToDoBean;
import com.deploymentsys.beans.deploy.DeployTaskBean;
import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.beans.deploy.DeployTaskServerBean;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.manager.deploy.DeployFlowManager;
import com.deploymentsys.manager.deploy.DeployFlowServerManager;
import com.deploymentsys.manager.deploy.DeployFlowServerToDoManager;
import com.deploymentsys.manager.deploy.DeployLogManager;
import com.deploymentsys.manager.deploy.DeployTaskFileManager;
import com.deploymentsys.manager.deploy.DeployTaskManager;
import com.deploymentsys.manager.deploy.DeployTaskServerManager;
import com.deploymentsys.manager.deploy.DeployTaskServerToDoManager;
import com.deploymentsys.utils.DateUtil;
import com.deploymentsys.utils.FileUtil;
import com.deploymentsys.utils.WebUtils;
import com.deploymentsys.utils.dtree.CheckArr;
import com.deploymentsys.utils.dtree.DTree;
import com.deploymentsys.utils.dtree.DTreeResponse;
import com.deploymentsys.utils.dtree.Status;

@Service
public class DeployService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployService.class);
	private static ConcurrentLinkedQueue<String> deployQueue = new ConcurrentLinkedQueue<String>();

	/**
	 * 处理部署任务的线程池
	 */
	private static ExecutorService deployTaskThreadPool = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	@Autowired
	private DeployTaskFileManager deployTaskFileManager;
	@Autowired
	private DeployTaskManager deployTaskManager;
	@Autowired
	private DeployTaskServerManager taskServerManager;
	@Autowired
	private DeployTaskServerToDoManager taskServerToDoManager;

	@Autowired
	private DeployLogManager logManager;

	@Autowired
	private DeployFlowManager deployFlowManager;
	@Autowired
	private DeployFlowServerManager flowServerManager;
	@Autowired
	private DeployFlowServerToDoManager flowServerToDoManager;
	@Autowired
	private DeploySysConfigService deploySysConfigService;
	@Autowired
	private WebUtils webUtils;

	@Autowired
	DeployTaskService deployTaskService;

	/**
	 * 站点启动时启动部署监控
	 */
	// public void startupDeployMonitoring() {
	// while (true) {
	// try {
	// Thread.sleep(3000);
	// } catch (InterruptedException ex) {
	// LOGGER.error("startupDeployMonitoring发生异常", ex);
	// }
	//
	// // 查询任务状态是 等待执行部署 的任务
	// List<DeployTaskBean> tasks =
	// deployTaskManager.listByStatus(DeployTaskStatus.WAIT_FOR_EXEC_DEPLOY, 10);
	// LOGGER.info("发现{}个待部署任务", tasks.size());
	//
	// if (tasks.size() > 0) {
	// int creatorId = 1;// 写死超级管理员的id
	// String currDate = webUtils.getCurrentDateStr();
	// String clientIp = "";
	// try {
	// clientIp = webUtils.getLocalIp();
	// } catch (Exception ex) {
	// // e.printStackTrace();
	// LOGGER.error("webUtils.getLocalIp发生异常", ex);
	// }
	//
	// BaseBean baseBean = new BaseBean(currDate, creatorId, clientIp, currDate,
	// creatorId, clientIp);
	//
	// for (DeployTaskBean taskBean : tasks) {
	// // 先在本线程中将任务状态改变，然后在放到多线程中处理
	// //deployTaskService.updateTaskStatusCascade(taskBean.getId(),
	// DeployTaskStatus.RUNNING, baseBean);
	//
	//// deployTaskThreadPool.execute(new Runnable() {
	//// public void run() {
	//// processDeployTask(taskBean);
	//// }
	//// });
	// }
	// // if (!deployQueue.isEmpty()) {
	// // String task = deployQueue.poll();
	// // service.execute(new Runnable() {
	// // public void run() {
	// // LOGGER.info(String.format("正在处理任务：%s", task));
	// // LOGGER.info(String.format("任务：%s处理完毕", task));
	// // }
	// // });
	// // }
	// }
	// }
	// }

	// private void processDeployTask(DeployTaskBean taskBean) {
	// try {
	// logManager.add(new DeployLogBean(taskBean.getId(), 0, 0,
	// MessageFormat.format("开始处理任务：{0}", JSON.toJSONString(taskBean)),
	// webUtils.getCurrentDateStr(), 1,
	// webUtils.getLocalIp()));
	// System.out.println("processDeployTask end");
	// } catch (Exception ex) {
	// LOGGER.error("processDeployTask发生异常", ex);
	// }
	// }

	/**
	 * 生成部署的任务批次号
	 * 
	 * @return
	 */
	private String generateTaskBatchNo(String projectVersion) {
		// return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6) + "-"
		// + DateUtil.getStringToday2();
		return projectVersion + "-" + DateUtil.getStringToday2();
	}

	/**
	 * 添加部署任务
	 * 
	 * @param task
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public String addDeployTask(DeployAppBean app, String description, String projectVersion, DeployConfigBean config,
			BaseBean baseBean, String[] files) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		int appId = app.getId();
		int configId = config.getId();

		String taskBatchNo = generateTaskBatchNo(projectVersion);
		while (true) {
			// 检查批次号是否重复
			if (deployTaskManager.isExistByAppIdAndBatchNo(appId, taskBatchNo) == 0) {
				break;
			}
			taskBatchNo = generateTaskBatchNo(projectVersion);
		}

		// 插入部署任务t_deploy_task，对应t_deploy_flow表的每一条数据
		List<DeployFlowBean> flows = deployFlowManager.getListByConfigId(configId);
		if (null != flows && flows.size() == 0) {
			jsonResult.put(SysConstants.ERROR_STR, 1);
			jsonResult.put(SysConstants.MSG_STR, "请先配置部署流程");
			return jsonResult.toJSONString();
		}

		List<DeployTaskBean> taskList = new ArrayList<>();
		for (int i = 0; i < flows.size(); i++) {
			DeployTaskBean taskBean = new DeployTaskBean();
			BeanUtils.copyProperties(baseBean, taskBean);

			taskBean.setFlowId(flows.get(i).getId());
			taskBean.setConfigId(configId);
			taskBean.setConfigName(config.getConfigName());
			taskBean.setAppId(appId);
			taskBean.setFlowType(flows.get(i).getFlowType());
			taskBean.setFlowOrder(flows.get(i).getFlowOrder());
			taskBean.setTargetServerOrderType(flows.get(i).getTargetServerOrderType());
			taskBean.setBatchNo(taskBatchNo);
			taskBean.setStatus(i == 0 ? DeployTaskStatus.WAIT_FOR_DEPLOY : DeployTaskStatus.NOT_READY);
			taskBean.setDescription(description);

			taskList.add(taskBean);
		}
		// 根据部署流程，批量添加部署任务
		deployTaskManager.addBatch(taskList);

		List<DeployTaskServerBean> taskServerListTotal = new ArrayList<>();
		for (int i = 0; i < taskList.size(); i++) {
			List<DeployFlowServerBean> flowServers = flowServerManager.getListByFlowId(taskList.get(i).getFlowId());

			List<DeployTaskServerBean> taskServerList = new ArrayList<>();
			for (DeployFlowServerBean deployFlowServerBean : flowServers) {
				DeployTaskServerBean taskServerBean = new DeployTaskServerBean();
				BeanUtils.copyProperties(baseBean, taskServerBean);

				taskServerBean.setDeployTaskId(taskList.get(i).getId());
				taskServerBean.setAppId(appId);
				taskServerBean.setBatchNo(taskBatchNo);
				taskServerBean.setServerOrder(deployFlowServerBean.getServerOrder());
				taskServerBean.setFlowServerId(deployFlowServerBean.getId());
				taskServerBean.setTargetServerId(deployFlowServerBean.getTargetServerId());
				taskServerBean.setDeployDir(deployFlowServerBean.getDeployDir());
				taskServerBean.setStatus(i == 0 ? DeployTaskStatus.WAIT_FOR_DEPLOY : DeployTaskStatus.NOT_READY);

				taskServerList.add(taskServerBean);
			}

			// 循环批量添加每个部署流程下面的目标服务器（需要判断List中元素个数是否大于0，否则mybatis批量插入会抛异常）
			if (taskServerList.size() > 0) {
				taskServerManager.addBatch(taskServerList);
				taskServerListTotal.addAll(taskServerList);
			}
		}

		for (DeployTaskServerBean deployTaskServerBean : taskServerListTotal) {
			List<DeployFlowServerToDoBean> flowServerToDos = flowServerToDoManager
					.getListByFlowServerId(deployTaskServerBean.getFlowServerId());

			List<DeployTaskServerToDoBean> taskServerToDoList = new ArrayList<>();
			for (DeployFlowServerToDoBean flowServerToDoBean : flowServerToDos) {
				DeployTaskServerToDoBean taskServerToDoBean = new DeployTaskServerToDoBean();
				BeanUtils.copyProperties(baseBean, taskServerToDoBean);

				taskServerToDoBean.setDeployTaskServerId(deployTaskServerBean.getId());
				taskServerToDoBean.setTodoType(flowServerToDoBean.getTodoType());
				taskServerToDoBean.setTodoOrder(flowServerToDoBean.getTodoOrder());
				taskServerToDoBean.setParam1(flowServerToDoBean.getParam1());
				taskServerToDoBean.setParam2(flowServerToDoBean.getParam2());
				taskServerToDoBean.setParam3(flowServerToDoBean.getParam3());
				taskServerToDoBean.setAppId(appId);
				taskServerToDoBean.setBatchNo(taskBatchNo);
				taskServerToDoBean.setStatus(deployTaskServerBean.getStatus());

				taskServerToDoList.add(taskServerToDoBean);
			}

			// 批量插入目标服务器执行明细（需要判断List中元素个数是否大于0，否则mybatis批量插入会抛异常）
			if (taskServerToDoList.size() > 0) {
				taskServerToDoManager.addBatch(taskServerToDoList);
			}
		}

		List<DeployTaskFileBean> fileList = new ArrayList<>();
		for (String file : files) {
			DeployTaskFileBean fileBean = new DeployTaskFileBean();
			BeanUtils.copyProperties(baseBean, fileBean);

			fileBean.setAppId(appId);
			fileBean.setRelativePath(file);
			fileBean.setBatchNo(taskBatchNo);
			try {
				fileBean.setMd5(FileUtil.getFileMd5(app.getFileDir() + file));
			} catch (Exception ex) {
				LOGGER.error("添加部署任务发生异常", ex);

				jsonResult.put(SysConstants.ERROR_STR, 1);
				jsonResult.put(SysConstants.MSG_STR, "添加部署任务发生异常");
				return jsonResult.toJSONString();
			}
			fileList.add(fileBean);
		}
		// 批量添加部署任务文件
		deployTaskFileManager.addBatch(fileList);

		// 将文件拷贝到部署系统所在服务器的临时目录
		for (String file : files) {
			String sourceFile = FileUtil.filePathConvert(app.getFileDir()) + file;
			String targetDir = deploySysConfigService.getDeploySysTempDir() + SysConstants.FILE_SEPARATOR
					+ app.getAppName() + SysConstants.FILE_SEPARATOR + taskBatchNo
					+ file.substring(0, file.lastIndexOf("/"));

			try {
				FileUtils.copyFileToDirectory(new File(sourceFile), new File(targetDir));
			} catch (Exception ex) {
				LOGGER.error(
						"DeployService.addDeployTask params:sourceFile: " + sourceFile + ",targetDir: " + targetDir,
						ex);
				try {
					throw ex;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		jsonResult.put(SysConstants.ERROR_STR, error);
		jsonResult.put(SysConstants.MSG_STR, msg);
		return jsonResult.toJSONString();
	}

	public DTreeResponse getDeployAppFiles(String appFileDir) {
		DTreeResponse resp = new DTreeResponse();
		File rootDir = new File(appFileDir);

		if (!rootDir.exists()) {
			resp.setStatus(new Status(201, "配置的应用文件目录不存在"));
			return resp;
		}
		if (!rootDir.isDirectory()) {
			resp.setStatus(new Status(202, "配置的应用目录不是一个文件夹"));
			return resp;
		}

		List<DTree> appFilesTreeData = getAppFilesTreeData(appFileDir, rootDir, null);
		resp.setData(appFilesTreeData);

		return resp;
	}

	private List<DTree> getAppFilesTreeData(String appFileDir, File rootDir, String parentId) {
		List<DTree> appFilesTreeData = new ArrayList<DTree>();
		File[] fileList = rootDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isFile()) {
					if (FileUtil.getTotalFilesInDirExcludeBySuffix(file,
							deploySysConfigService.getFilesTreeIgnore()) > 0) {
						return true;
					}
					return false;
				}
				return true;
			}
		});

		// 文件数复选框未选中状态
		List<CheckArr> checkArrs0 = new ArrayList<CheckArr>() {
			private static final long serialVersionUID = 1L;

			{
				add(new CheckArr("0", "0"));
			}
		};

		String pid = null != parentId ? parentId : "0";

		for (File file : fileList) {
			if (file.isDirectory()) {
				if (FileUtil.getTotalFilesInDirExcludeBySuffix(file, deploySysConfigService.getFilesTreeIgnore()) > 0) {
					DTree root = new DTree();
					root.setId(FileUtil.filePathConvert(file.getAbsolutePath()).substring(appFileDir.length()));
					root.setTitle(file.getName());
					// root.setLevel("1");//这个level貌似没多大意义
					root.setSpread(false);
					root.setParentId(pid);
					root.setIsLast(false);
					root.setCheckArr(checkArrs0);
					root.setChildren(getAppFilesTreeData(appFileDir, file, root.getId()));
					appFilesTreeData.add(root);
				}
			} else {
				DTree root = new DTree();
				root.setId(FileUtil.filePathConvert(file.getAbsolutePath()).substring(appFileDir.length()));
				root.setTitle(file.getName());
				// root.setLevel("1");//这个level貌似没多大意义
				root.setSpread(false);
				root.setParentId(pid);
				root.setIsLast(true);
				root.setCheckArr(checkArrs0);
				appFilesTreeData.add(root);
			}

		}

		return appFilesTreeData;
	}

}
