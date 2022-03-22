package com.deploymentsys.service.deploy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.BaseBean;
import com.deploymentsys.beans.constants.DeployTaskStatus;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.beans.constants.TargetServerOrderType;
import com.deploymentsys.beans.constants.TargetServerToDoType;
import com.deploymentsys.beans.deploy.DeployLogBean;
import com.deploymentsys.beans.deploy.DeployTaskBean;
import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.beans.deploy.DeployTaskServerBean;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;
import com.deploymentsys.manager.deploy.DeployTaskFileManager;
import com.deploymentsys.manager.deploy.DeployTaskManager;
import com.deploymentsys.manager.deploy.DeployTaskServerManager;
import com.deploymentsys.manager.deploy.DeployTaskServerToDoManager;
import com.deploymentsys.service.netty.NettyDeployTransferClientBootstrap;
import com.deploymentsys.utils.SocketIOUtils;
import com.deploymentsys.utils.WebUtils;

@Service
public class DeployTaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskService.class);

	/**
	 * 处理部署任务的线程池
	 */
	private static final ExecutorService DEPLOY_TASK_THREADPOOL = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
	/**
	 * 处理部署任务下面多个目标服务器的线程池
	 */
	private static final ExecutorService DEPLOY_TASKSERVER_THREADPOOL = Executors
			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	/**
	 * 处理netty远程操作的线程池
	 */
//	private static final ExecutorService NETTY_REMOTE_THREADPOOL = Executors
//			.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);

	// AtomicInteger count = new AtomicInteger(0);// 多线程模式下可以正确统计执行数量

	/**
	 * 这个map结构，key的组成格式是taskId#待部署的目标服务器数量，value的值是目前已成功部署的目标服务器数量；另外有单独线程判断“待部署的目标服务器数量”和“目前已成功部署的目标服务器数量”是否相等，如果相等则更新部署任务状态为成功；判断任务状态失败是在执行部署目标服务器的线程上来做的
	 */
	//private static Map<String, Integer> mapForUpdateDeployTaskStatus = new ConcurrentHashMap<String, Integer>();

	private static ConcurrentLinkedQueue<DeployTaskServerToDoBean> TaskServerToDoQueue = new ConcurrentLinkedQueue<>();

	private static ConcurrentLinkedQueue<DeployLogBean> TaskServerToDoLogQueue = new ConcurrentLinkedQueue<>();

	@Autowired
	private DeployTaskManager deployTaskManager;
	@Autowired
	private DeployTaskServerManager taskServerManager;
	@Autowired
	private DeployTaskServerToDoManager taskServerToDoManager;
	@Autowired
	private DeployTaskFileManager taskFileManager;
	@Autowired
	private DeploySysConfigService deploySysConfigService;

	// @Autowired
	// private DeployLogManager logManager;

	@Autowired
	@Qualifier("deployLogServiceDbImpl")
	private IDeployLogService logService;

	@Autowired
	private WebUtils webUtils;

	@Autowired
	private NettyDeployTransferClientBootstrap nettyClientBootstrap;

	/**
	 * 添加队列元素
	 * 
	 * @param data
	 */
	public void addToTaskServerToDoQueue(DeployTaskServerToDoBean toDo) {
		TaskServerToDoQueue.offer(toDo);
	}

	/**
	 * 将远程操作日志添加至队列
	 * 
	 * @param log
	 */
	public void addToLogQueue(DeployLogBean log) {
		TaskServerToDoLogQueue.offer(log);
	}

	/**
	 * 部署任务状态更新监控
	 * 
	 * @throws UnknownHostException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public void deployTaskStatusMonitoring() throws UnknownHostException {
		List<DeployTaskBean> tasks = deployTaskManager.listByStatus(DeployTaskStatus.RUNNING, 10);
		if (tasks.size() > 0) {
			LOGGER.info("deployTaskStatusMonitoring 有 {} 个部署任务等待状态更新", tasks.size());
		}

		BaseBean baseBean = new BaseBean(webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp(),
				webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp());

		task: for (DeployTaskBean task : tasks) {
			BeanUtils.copyProperties(baseBean, task);
			// 查出部署任务下面的task servers，判断有无失败的状态
			List<DeployTaskServerBean> taskServers = taskServerManager.listAllByTaskId(task.getId());
			int taskServersCount = taskServers.size();
			int tempTaskServersCount = 0;

			taskServer: for (DeployTaskServerBean taskServer : taskServers) {
				BeanUtils.copyProperties(baseBean, taskServer);
				if (taskServer.getStatus().equals(DeployTaskStatus.FAILURE)) {
					task.setStatus(DeployTaskStatus.FAILURE);
					deployTaskManager.updateStatus(task);
					continue task;
				}

				if (taskServer.getStatus().equals(DeployTaskStatus.RUNNING)) {
					List<DeployTaskServerToDoBean> toDos = taskServerToDoManager
							.listAllByTaskServerId(taskServer.getId());

					int toDosCount = toDos.size();
					int tempToDosCount = 0;
					for (DeployTaskServerToDoBean toDo : toDos) {
						if (toDo.getStatus().equals(DeployTaskStatus.FAILURE)) {

							taskServer.setStatus(DeployTaskStatus.FAILURE);
							taskServerManager.updateStatus(taskServer);

							task.setStatus(DeployTaskStatus.FAILURE);
							deployTaskManager.updateStatus(task);
							continue taskServer;
						}

						if (toDo.getStatus().equals(DeployTaskStatus.SUCCESS)) {
							++tempToDosCount;
							if (tempToDosCount == toDosCount) {
								taskServer.setStatus(DeployTaskStatus.SUCCESS);
								taskServerManager.updateStatus(taskServer);

								tempTaskServersCount++;
							}
						}
					}
				}
			}

			if (tempTaskServersCount == taskServersCount) {
				task.setStatus(DeployTaskStatus.SUCCESS);
				deployTaskManager.updateStatus(task);

				// 将下一个任务的状态更新为 等待部署
				updateNextTaskStatus(task, baseBean);
			}
		}
		if (tasks.size() > 0) {
			LOGGER.info("deployTaskStatusMonitoring 已更新 {} 个部署任务的状态", tasks.size());
		}
	}

	/**
	 * 部署任务服务器todo状态更新执行
	 */
	public void taskServerToDoQueueMonitoring() {
		while (!TaskServerToDoQueue.isEmpty()) {
			DeployTaskServerToDoBean toDo = TaskServerToDoQueue.poll();

			taskServerToDoManager.updateStatus(toDo);
		}
	}

	/**
	 * 部署日志写入队列 监控
	 */
	public void logQueueMonitoring() {
		while (!TaskServerToDoLogQueue.isEmpty()) {
			DeployLogBean log = TaskServerToDoLogQueue.poll();

			logService.log(log);
		}
	}

	// public void printTest01() {
	// System.out.println("webUtils.getCurrentDateStr(): " +
	// webUtils.getCurrentDateStr());
	// }

	/**
	 * 站点启动的时候更新部署任务，把正在运行的任务全部更新为失败
	 * 
	 * @throws UnknownHostException
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public void updateDeployTaskAtTheBeginning() throws UnknownHostException {
		BaseBean baseBean = new BaseBean(webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp(),
				webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp());
		updateTaskStatusAtTheBeginning1(baseBean);
		updateTaskStatusAtTheBeginning2(baseBean);
		updateTaskStatusAtTheBeginning3(baseBean);
	}

	private void updateTaskStatusAtTheBeginning3(BaseBean baseBean) {
		// 查询任务状态是“执行中”的部署任务列表
		List<DeployTaskBean> tasks = deployTaskManager.listByStatus2(DeployTaskStatus.RUNNING);
		for (DeployTaskBean task : tasks) {
			// 更新部署任务状态
			BeanUtils.copyProperties(baseBean, task);
			updateStatusAndLog(task, DeployTaskStatus.FAILURE,
					MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(task)), baseBean);
		}
	}

	private void updateTaskStatusAtTheBeginning2(BaseBean baseBean) {
		// 查询任务状态是“执行中”的部署任务目标服务器列表
		List<DeployTaskServerBean> taskServers = taskServerManager.listByStatus(DeployTaskStatus.RUNNING);
		for (DeployTaskServerBean server : taskServers) {
			DeployTaskBean task = deployTaskManager.getById(server.getDeployTaskId());

			int taskId = 0;
			if (null != task) {
				taskId = task.getId();
				// 更新部署任务状态
				BeanUtils.copyProperties(baseBean, task);
				updateStatusAndLog(task, DeployTaskStatus.FAILURE,
						MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(task)), baseBean);
			}

			// 更新部署任务目标服务器状态
			BeanUtils.copyProperties(baseBean, server);
			updateStatusAndLog(taskId, server, DeployTaskStatus.FAILURE,
					MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(server)), baseBean);
		}
	}

	private void updateTaskStatusAtTheBeginning1(BaseBean baseBean) {
		// 查询任务状态是“执行中”的部署任务目标服务器todo列表
		List<DeployTaskServerToDoBean> toDos = taskServerToDoManager.listByStatus(DeployTaskStatus.RUNNING);
		for (DeployTaskServerToDoBean toDo : toDos) {
			DeployTaskServerBean taskServer = taskServerManager.getById(toDo.getDeployTaskServerId());

			int taskServerId = 0;
			int taskId = 0;
			if (null != taskServer) {
				taskServerId = taskServer.getId();

				DeployTaskBean task = deployTaskManager.getById(taskServer.getDeployTaskId());
				if (null != task) {
					taskId = task.getId();
					// 更新部署任务状态
					BeanUtils.copyProperties(baseBean, task);
					updateStatusAndLog(task, DeployTaskStatus.FAILURE,
							MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(task)), baseBean);
				}
				// 更新部署任务目标服务器状态
				BeanUtils.copyProperties(baseBean, taskServer);
				updateStatusAndLog(taskId, taskServer, DeployTaskStatus.FAILURE,
						MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(taskServer)), baseBean);
			}
			// 更新部署任务目标服务器todo状态
			updateStatusAndLog(taskId, taskServerId, toDo, DeployTaskStatus.FAILURE,
					MessageFormat.format("执行todo任务失败：{0}", JSON.toJSONString(toDo)), baseBean);
		}
	}

//	public void deployTaskStatusMapMonitoring() throws UnknownHostException {
//		if (mapForUpdateDeployTaskStatus.keySet().size() > 0) {
//			LOGGER.info("当前mapForUpdateDeployTaskStatus 中一共有 {} 个待处理部署任务",
//					mapForUpdateDeployTaskStatus.keySet().size());
//		}
//
//		Iterator<String> iter = mapForUpdateDeployTaskStatus.keySet().iterator();
//		while (iter.hasNext()) {
//			String key = iter.next();
//			LOGGER.info("待处理的key是：{}", key);
//
//			String taskIdStr = key.split("#")[0];
//			LOGGER.info("取到的taskIdStr：{}", taskIdStr);
//
//			DeployTaskBean task = deployTaskManager.getById(Integer.valueOf(taskIdStr));
//			LOGGER.info("deployTaskManager.getById取到的task bean：{}", JSON.toJSONString(task));
//
//			if (null == task) {
//				LOGGER.info("task为null");
//				mapForUpdateDeployTaskStatus.remove(key);
//				continue;
//			}
//
//			// 如果发现任务状态已经被更新为失败，则移除这个key
//			if (task.getStatus().equals(DeployTaskStatus.FAILURE)) {
//				mapForUpdateDeployTaskStatus.remove(key);
//				continue;
//			}
//
//			Integer successCount = mapForUpdateDeployTaskStatus.get(key);
//			Integer expectedCount = Integer.valueOf(key.split("#")[1]);
//			if (successCount.equals(expectedCount)) {
//				BaseBean baseBean = new BaseBean(webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp(),
//						webUtils.getCurrentDateStr(), 1, webUtils.getLocalIp());
//				updateStatusAndLog(task, DeployTaskStatus.SUCCESS,
//						MessageFormat.format("部署任务成功：{0}", JSON.toJSONString(task)), baseBean);
//
//				// 将下一个任务的状态更新为 等待部署
//				updateNextTaskStatus(task, baseBean);
//
//				// 移除相应的task key
//				mapForUpdateDeployTaskStatus.remove(key);
//			}
//		}
//	}

	public List<DeployTaskBean> list(int pageStart, int size, String flowType, String appId, String batchNo,
			int deploymentApplicantId, String rollback) {
		return deployTaskManager.list(pageStart, size, flowType, appId, batchNo, deploymentApplicantId, rollback);
	}

	public int getListCount(String flowType, String appId, String batchNo, int deploymentApplicantId, String rollback) {
		return deployTaskManager.getListCount(flowType, appId, batchNo, deploymentApplicantId, rollback);
	}

	public static void main(String[] args) {
		List<DeployTaskServerBean> taskServersCopy = new ArrayList<>();
		List<DeployTaskServerBean> taskServers = new ArrayList<>();
		taskServersCopy = taskServers;
		DeployTaskServerBean bean1 = new DeployTaskServerBean();
		bean1.setId(66);

		taskServers.add(bean1);

		System.out.println(taskServersCopy.size());
		System.out.println(taskServersCopy.get(0).getId());

		for (DeployTaskServerBean deployTaskServerBean : taskServers) {
			deployTaskServerBean.setId(444);
		}

		System.out.println(taskServersCopy.size());
		System.out.println(taskServersCopy.get(0).getId());

		System.out.println(taskServers.get(0).getId());
	}

	/**
	 * 回滚到此次部署
	 * 
	 * @param taskId
	 * @param baseBean
	 * @return
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public String rollback(int taskId, BaseBean baseBean) {
		JSONObject jsonResult = new JSONObject();

		DeployTaskBean taskBean = deployTaskManager.getById(taskId);

		if (null == taskBean || !taskBean.getStatus().equals(DeployTaskStatus.SUCCESS)) {
			jsonResult.put(SysConstants.ERROR_STR, 1);
			jsonResult.put(SysConstants.MSG_STR, MessageFormat.format("编号：{0}的任务不存在或者任务状态异常", taskId));
			return jsonResult.toJSONString();
		}

		List<DeployTaskBean> taskList = new ArrayList<>();
		taskBean.setId(0);
		BeanUtils.copyProperties(baseBean, taskBean);
		taskBean.setRollback(true);
		taskBean.setStatus(DeployTaskStatus.WAIT_FOR_DEPLOY);
		taskList.add(taskBean);
		// 添加回滚任务
		deployTaskManager.addBatch(taskList);

		List<Integer> taskServersId = new ArrayList<>();
		List<DeployTaskServerBean> taskServers = taskServerManager.listAllByTaskId(taskId);

		for (DeployTaskServerBean deployTaskServerBean : taskServers) {
			taskServersId.add(deployTaskServerBean.getId());
			deployTaskServerBean.setId(0);
			BeanUtils.copyProperties(baseBean, deployTaskServerBean);
			deployTaskServerBean.setStatus(DeployTaskStatus.WAIT_FOR_DEPLOY);
			deployTaskServerBean.setDeployTaskId(taskList.get(0).getId());
		}

		taskServerManager.addBatch(taskServers);

		for (int i = 0; i < taskServersId.size(); i++) {
			List<DeployTaskServerToDoBean> toDos = taskServerToDoManager.listAllByTaskServerId(taskServersId.get(i));
			for (int j = 0; j < toDos.size(); j++) {
				if (toDos.get(j).getTodoType().equals(TargetServerToDoType.FILE_TRANSFER)) {
					toDos.remove(j);
					--j;
				} else {
					toDos.get(j).setId(0);
					BeanUtils.copyProperties(baseBean, toDos.get(j));
					toDos.get(j).setStatus(DeployTaskStatus.WAIT_FOR_DEPLOY);
					toDos.get(j).setDeployTaskServerId(taskServers.get(i).getId());
				}
			}
			taskServerToDoManager.addBatch(toDos);
		}

		return deployTask(taskList.get(0).getId(), baseBean);

		// String taskStatus = taskBean.getStatus();
		// // 查询部署任务下的目标服务器
		// List<DeployTaskServerBean> taskServers =
		// taskServerManager.listAllByTaskId(taskBean.getId());
		// if (taskServers.size() == 0) {
		// taskStatus = DeployTaskStatus.FAILURE;
		// BeanUtils.copyProperties(baseBean, taskBean);
		// taskBean.setStatus(taskStatus);
		//
		// // 先在本线程更新状态
		// updateTaskStatusCascade(taskBean, baseBean);
		//
		// jsonResult.put(SysConstants.ERROR_STR, 1);
		// jsonResult.put(SysConstants.MSG_STR,
		// MessageFormat.format("编号：{0}的任务没有配置目标服务器", taskId));
		// return jsonResult.toJSONString();
		// }
		//
		// taskStatus = DeployTaskStatus.RUNNING;
		// BeanUtils.copyProperties(baseBean, taskBean);
		// taskBean.setStatus(taskStatus);
		//
		// // 先在本线程更新状态
		// updateTaskStatusCascade(taskBean, baseBean);
		//
		// DEPLOY_TASK_THREADPOOL.execute(new Runnable() {
		// public void run() {
		// processDeployTask(taskBean, baseBean, taskServers);
		// }
		// });
		//
		// jsonResult.put(SysConstants.ERROR_STR, error);
		// jsonResult.put(SysConstants.MSG_STR, msg);
		// return jsonResult.toJSONString();
	}

	/**
	 * 部署任务
	 * 
	 * @param taskId
	 * @param baseBean
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackForClassName = "Exception")
	public String deployTask(int taskId, BaseBean baseBean) {
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		DeployTaskBean taskBean = deployTaskManager.getById(taskId);
		if (null == taskBean || !taskBean.getStatus().equals(DeployTaskStatus.WAIT_FOR_DEPLOY)) {
			jsonResult.put(SysConstants.ERROR_STR, 1);
			jsonResult.put(SysConstants.MSG_STR, MessageFormat.format("编号：{0}的任务不存在或者任务状态异常", taskId));
			return jsonResult.toJSONString();
		}

		String taskStatus = taskBean.getStatus();
		// 查询部署任务下的目标服务器
		List<DeployTaskServerBean> taskServers = taskServerManager.listAllByTaskId(taskBean.getId());
		if (taskServers.size() == 0) {
			taskStatus = DeployTaskStatus.FAILURE;
			BeanUtils.copyProperties(baseBean, taskBean);
			taskBean.setStatus(taskStatus);

			// 先在本线程更新状态
			updateTaskStatusCascade(taskBean, baseBean);

			jsonResult.put(SysConstants.ERROR_STR, 1);
			jsonResult.put(SysConstants.MSG_STR, MessageFormat.format("编号：{0}的任务没有配置目标服务器", taskId));
			return jsonResult.toJSONString();
		}

		taskStatus = DeployTaskStatus.RUNNING;
		BeanUtils.copyProperties(baseBean, taskBean);
		taskBean.setStatus(taskStatus);

		// 先在本线程更新状态
		updateTaskStatusCascade(taskBean, baseBean);

		DEPLOY_TASK_THREADPOOL.execute(new Runnable() {
			public void run() {
				processDeployTask(taskBean, baseBean, taskServers);
			}
		});

		jsonResult.put(SysConstants.ERROR_STR, error);
		jsonResult.put(SysConstants.MSG_STR, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 处理部署任务
	 * 
	 * @param taskBean
	 */
	private void processDeployTask(DeployTaskBean taskBean, BaseBean baseBean, List<DeployTaskServerBean> taskServers) {
		int taskId = taskBean.getId();
		// String currentDateStr = baseBean.getCreateDate();
		// int creatorId = baseBean.getCreator();
		// String createIp = baseBean.getCreateIp();
		try {

			logService.log(taskId, MessageFormat.format("开始处理任务：{0}", JSON.toJSONString(taskBean)), baseBean);
			// 队列执行
			if (taskBean.getTargetServerOrderType().equals(TargetServerOrderType.QUEUE)) {
				boolean isTaskSuccess = false;
				for (DeployTaskServerBean targetServer : taskServers) {
					// if (!processDeployTaskServer(taskBean, targetServer, baseBean)) {
					// isTaskSuccess = false;
					// break;
					// }

					processDeployTaskServer(taskBean, targetServer, baseBean);
				}

				if (isTaskSuccess) {
					updateStatusAndLog(taskBean, DeployTaskStatus.SUCCESS,
							MessageFormat.format("部署任务成功：{0}", JSON.toJSONString(taskBean)), baseBean);

					// 将下一个任务的状态更新为 等待部署
					updateNextTaskStatus(taskBean, baseBean);
					// List<DeployTaskBean>
					// taskList=deployTaskManager.listByAppIdAndBatchNo(taskBean.getAppId(),
					// taskBean.getBatchNo());
					// if (taskList.indexOf(taskBean)!=-1) {
					// DeployTaskBean taskBeanNext=taskList.get(taskList.indexOf(taskBean)+1);
					//
					// String status = DeployTaskStatus.WAIT_FOR_DEPLOY;
					// BeanUtils.copyProperties(baseBean, taskBeanNext);
					// taskBean.setStatus(status);
					//
					// // 先在本线程更新状态
					// updateTaskStatusCascade(taskBean, baseBean);
					// }
				}
			}

			// 并发执行（多线程模型）
			if (taskBean.getTargetServerOrderType().equals(TargetServerOrderType.CONCURRENCE)) {
				// 待执行的目标服务器数量
//				int taskServersToDeploySum = taskServers.size();
//				String mapKey = taskId + "#" + taskServersToDeploySum;
//
//				// 初始部署成功总数为0
//				int initSuccessSum = 0;
//				AtomicInteger taskServersDeploySuccessSum = new AtomicInteger(initSuccessSum);
//				mapForUpdateDeployTaskStatus.put(mapKey, initSuccessSum);

				for (DeployTaskServerBean targetServer : taskServers) {

					DEPLOY_TASKSERVER_THREADPOOL.execute(new Runnable() {
						public void run() {

							// if (processDeployTaskServer(taskBean, targetServer, baseBean)) {
							// if (mapForUpdateDeployTaskStatus.containsKey(mapKey)) {
							// mapForUpdateDeployTaskStatus.put(mapKey,
							// taskServersDeploySuccessSum.incrementAndGet());
							// }
							// }

							processDeployTaskServer(taskBean, targetServer, baseBean);

						}
					});

				}
			}

			LOGGER.info("部署任务处理完毕：{}", JSON.toJSONString(taskBean));
		} catch (Exception ex) {
			LOGGER.error("processDeployTask发生异常", ex);
			logService.log(taskId, MessageFormat.format("部署任务发生异常：{0}，异常信息：{1}", JSON.toJSONString(taskBean),
					ExceptionUtils.getStackTrace(ex)), baseBean);

			taskBean.setStatus(DeployTaskStatus.FAILURE);
			deployTaskManager.updateStatus(taskBean);
		}
	}

	/**
	 * 将下一个部署任务的状态更新为 等待部署
	 * 
	 * @param taskBean
	 * @param baseBean
	 */
	private void updateNextTaskStatus(DeployTaskBean taskBean, BaseBean baseBean) {
		List<DeployTaskBean> taskList = deployTaskManager.listByAppIdAndBatchNo(taskBean.getAppId(),
				taskBean.getBatchNo());

		int currentTaskIndex = taskList.indexOf(taskBean);
		// 判断taskList中存在该部署任务，并且当前任务不是最后一个任务（最后一个任务已经没有下一个任务更新状态了）
		if (currentTaskIndex != -1 && (currentTaskIndex + 1) < taskList.size()) {
			DeployTaskBean taskBeanNext = taskList.get(taskList.indexOf(taskBean) + 1);

			String status = DeployTaskStatus.WAIT_FOR_DEPLOY;
			BeanUtils.copyProperties(baseBean, taskBeanNext);
			taskBeanNext.setStatus(status);

			updateTaskStatusCascade(taskBeanNext, baseBean);
		}
	}

	/**
	 * 处理单个目标服务器
	 * 
	 * @param taskBean
	 * @param targetServer
	 * @param baseBean
	 * @return
	 */
	private void processDeployTaskServer(DeployTaskBean taskBean, DeployTaskServerBean targetServer,
			BaseBean baseBean) {
		int taskId = taskBean.getId();
		int appId = taskBean.getAppId();
		String appName = taskBean.getAppName();
		String batchNo = taskBean.getBatchNo();
		int targetServerId = targetServer.getId();
		String targetIp = targetServer.getTargetServerIp();
		int targetPort = targetServer.getTargetServerPort();

		// 先测试连接，如果连接不通，则更新状态为失败
		try {
			if (!SocketIOUtils.testConnect(targetIp, targetPort, 10)) {
				targetServer.setStatus(DeployTaskStatus.FAILURE);
				taskServerManager.updateStatus(targetServer);

				taskBean.setStatus(DeployTaskStatus.FAILURE);
				deployTaskManager.updateStatus(taskBean);

				// 这里用级联更新状态感觉体验有些不好
				// updateTaskStatusCascade(taskBean, baseBean);

				logService.log(taskId, MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);

				logService.log(taskId, targetServerId,
						MessageFormat.format("目标服务器连接失败：{0}", JSON.toJSONString(targetServer)), baseBean);

				// return false;
				return;
			}
		} catch (Exception ex) {
			LOGGER.error("processDeployTaskServer捕获异常：", ex);
			updateStatusAndLog(taskBean, DeployTaskStatus.FAILURE,
					MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);
			updateStatusAndLog(taskId, targetServer, DeployTaskStatus.FAILURE,
					MessageFormat.format("目标服务器连接失败：{0}", JSON.toJSONString(targetServer)), baseBean);

			// return false;
		}

		// 查询目标服务器下面的todo列表
		List<DeployTaskServerToDoBean> serverToDos = taskServerToDoManager.listAllByTaskServerId(targetServerId);

		if (serverToDos.size() == 0) {
			targetServer.setStatus(DeployTaskStatus.FAILURE);
			taskServerManager.updateStatus(targetServer);

			taskBean.setStatus(DeployTaskStatus.FAILURE);
			deployTaskManager.updateStatus(taskBean);

			logService.log(taskId, MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);

			logService.log(taskId, targetServerId,
					MessageFormat.format("目标服务器没有配置todo列表：{0}", JSON.toJSONString(targetServer)), baseBean);

			// return false;
			return;
		}

		first: for (DeployTaskServerToDoBean toDo : serverToDos) {
			toDo.setTaskId(taskId);
			try {
				second: switch (toDo.getTodoType()) {

				case TargetServerToDoType.FILE_TRANSFER:
					List<DeployTaskFileBean> files = taskFileManager.listAll(appId, batchNo);

					// if (!fileTransfer(taskId, targetServerId, toDo, baseBean, targetIp,
					// targetPort)) {
					// updateStatusAndLog(taskBean, DeployTaskStatus.FAILURE,
					// MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);
					// updateStatusAndLog(taskId, targetServer, DeployTaskStatus.FAILURE,
					// MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(targetServer)),
					// baseBean);
					// updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
					// MessageFormat.format("执行todo任务失败：{0}", JSON.toJSONString(toDo)), baseBean);
					//
					// //break first;
					// //return;
					// }

					fileTransfer(files, appName, taskId, targetServerId, toDo, baseBean, targetIp, targetPort);

					while (true) {
						Thread.sleep(1000);
						LOGGER.info("正在判断 TaskServerToDo 文件传输后的任务状态。。。");
						DeployTaskServerToDoBean toDoCurrent = taskServerToDoManager.getById(toDo.getId());
						if (toDoCurrent.getStatus().equals(DeployTaskStatus.FAILURE)) {
							break first;
						}

						if (toDoCurrent.getStatus().equals(DeployTaskStatus.SUCCESS)) {
							break second;
						}

					}

					// updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.SUCCESS,
					// MessageFormat.format("执行todo任务成功：{0}", JSON.toJSONString(toDo)), baseBean);
					// break second;

				case TargetServerToDoType.FILE_COPY:
					// if (!fileCopy(taskId, targetServerId, toDo, baseBean)) {
					// updateStatusAndLog(taskBean, DeployTaskStatus.FAILURE,
					// MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);
					// updateStatusAndLog(taskId, targetServer, DeployTaskStatus.FAILURE,
					// MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(targetServer)),
					// baseBean);
					//
					// updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
					// MessageFormat.format("执行todo任务失败：{0}", JSON.toJSONString(toDo)), baseBean);
					//
					// // break first;
					// // return false;
					// }

					fileCopy(targetIp, targetPort, taskId, targetServerId, toDo, baseBean);

					// updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.SUCCESS,
					// MessageFormat.format("执行todo任务成功：{0}", JSON.toJSONString(toDo)), baseBean);
					break second;

				case TargetServerToDoType.EMPTY_DIR:
					// if (!emptyDir(taskId, targetServerId, toDo, baseBean)) {
					// updateStatusAndLog(taskBean, DeployTaskStatus.FAILURE,
					// MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);
					// updateStatusAndLog(taskId, targetServer, DeployTaskStatus.FAILURE,
					// MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(targetServer)),
					// baseBean);
					//
					// updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
					// MessageFormat.format("执行todo任务失败：{0}", JSON.toJSONString(toDo)), baseBean);
					//
					// // break first;
					// // return false;
					// }
					//
					// updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.SUCCESS,
					// MessageFormat.format("执行todo任务成功：{0}", JSON.toJSONString(toDo)), baseBean);

					emptyDir(targetIp, targetPort, taskId, targetServerId, toDo, baseBean);
					break second;

				case TargetServerToDoType.DELETE_DIR:
					deleteDir(targetIp, targetPort, taskId, targetServerId, toDo, baseBean);
					break second;

				case TargetServerToDoType.REMOTE_EXEC_SHELL:
					remoteExecShell(targetIp, targetPort, taskId, targetServerId, toDo, baseBean);
					break second;

				default:
					updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
							MessageFormat.format("执行未知类型的todo任务：{0}", JSON.toJSONString(toDo)), baseBean);

					updateStatusAndLog(taskId, targetServer, DeployTaskStatus.FAILURE,
							MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(targetServer)), baseBean);

					updateStatusAndLog(taskBean, DeployTaskStatus.FAILURE,
							MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);

					updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
							MessageFormat.format("执行todo任务失败：{0}", JSON.toJSONString(toDo)), baseBean);

					// break first;
					// return false;
				}
			} catch (Exception ex) {
				LOGGER.error("processDeployTaskServer-FOR循环处理目标服务器todo任务时捕获异常：", ex);

				updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
						MessageFormat.format("执行未知类型的todo任务：{0}", JSON.toJSONString(toDo)), baseBean);

				updateStatusAndLog(taskId, targetServer, DeployTaskStatus.FAILURE,
						MessageFormat.format("目标服务器执行失败：{0}", JSON.toJSONString(targetServer)), baseBean);

				updateStatusAndLog(taskBean, DeployTaskStatus.FAILURE,
						MessageFormat.format("部署任务失败：{0}", JSON.toJSONString(taskBean)), baseBean);

				updateStatusAndLog(taskId, targetServerId, toDo, DeployTaskStatus.FAILURE,
						MessageFormat.format("执行todo任务失败：{0}", JSON.toJSONString(toDo)), baseBean);

				// break first;
				// return false;
			}

		}
	}

	/**
	 * 更新任务状态并记录日志
	 * 
	 * @param taskId
	 * @param targetServer
	 * @param status
	 * @param logContent
	 * @param baseBean
	 */
	private void updateStatusAndLog(int taskId, DeployTaskServerBean targetServer, String status, String logContent,
			BaseBean baseBean) {
		targetServer.setStatus(status);
		taskServerManager.updateStatus(targetServer);
		logService.log(taskId, targetServer.getId(), logContent, baseBean);
	}

	public void updateStatusAndLog(int taskId, int taskServerId, DeployTaskServerToDoBean toDo, String status,
			String logContent, BaseBean baseBean) {
		toDo.setStatus(status);
		taskServerToDoManager.updateStatus(toDo);
		logService.log(taskId, taskServerId, toDo.getId(), logContent, baseBean);
	}

	private void updateStatusAndLog(DeployTaskBean taskBean, String status, String logContent, BaseBean baseBean) {
		taskBean.setStatus(status);
		deployTaskManager.updateStatus(taskBean);

		logService.log(taskBean.getId(), logContent, baseBean);
	}

	/**
	 * 文件传输
	 * 
	 * @param taskId
	 * @param targetServerId
	 * @param toDo
	 * @param baseBean
	 * @return
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void fileTransfer(List<DeployTaskFileBean> files, String appName, int taskId, int targetServerId,
			DeployTaskServerToDoBean toDo, BaseBean baseBean, String targetIp, int targetPort)
			throws InterruptedException, FileNotFoundException, IOException {
		logService.log(taskId, targetServerId, toDo.getId(),
				MessageFormat.format("执行todo任务：{0}", JSON.toJSONString(toDo)), baseBean);
		String batchNo = toDo.getBatchNo();
		// String sourceFilePath = "";
		// String targetDir = "";
		// String relativePath = "";

		String sourceFilePathBase = deploySysConfigService.getDeploySysTempDir() + SysConstants.FILE_SEPARATOR + appName
				+ SysConstants.FILE_SEPARATOR + batchNo;
		String targetDirBase = toDo.getParam1().replace(SysConstants.BATCHNO_STR, batchNo);

		nettyClientBootstrap.remoteFileTransfer(files, sourceFilePathBase, targetDirBase, targetIp, targetPort, toDo);

		// for (DeployTaskFileBean taskFile : files) {
		//
		// relativePath = taskFile.getRelativePath();
		// sourceFilePath = deploySysConfigService.getDeploySysTempDir() +
		// SysConstants.FILE_SEPARATOR + appName
		// + SysConstants.FILE_SEPARATOR + batchNo + relativePath;
		//
		// File tempFile = new File(sourceFilePath);
		// if (!tempFile.exists()) {
		// logService.log(new DeployLogBean(taskId, targetServerId, toDo.getId(),
		// MessageFormat.format("文件：{0} 不存在", sourceFilePath),
		// webUtils.getCurrentDateStr(), 1,
		// webUtils.getLocalIp()));
		// toDo.setStatus(DeployTaskStatus.FAILURE);
		// taskServerToDoManager.updateStatus(toDo);
		// return;
		// }
		// // 文件传输之前验证一次MD5
		// String md5Current = FileUtil.getFileMd5(sourceFilePath);
		// if (!md5Current.equals(taskFile.getMd5())) {
		// logService.log(new DeployLogBean(taskId, targetServerId, toDo.getId(),
		// MessageFormat.format("文件：{0} md5验证不通过", sourceFilePath),
		// webUtils.getCurrentDateStr(), 1,
		// webUtils.getLocalIp()));
		// toDo.setStatus(DeployTaskStatus.FAILURE);
		// taskServerToDoManager.updateStatus(toDo);
		// return;
		// }
		//
		// targetDir = toDo.getParam1().replace(SysConstants.BATCHNO_STR, batchNo)
		// + relativePath.substring(0, relativePath.lastIndexOf("/"));
		//
		// // 不能用多线程，因为后面的todo 操作可能会不连贯，比如后面的文件拷贝，会找不到文件
		//
		// // NETTY_REMOTE_THREADPOOL.submit(new Runnable() {
		// // @Override
		// // public void run() {
		// // try {
		// // nettyClientBootstrap.remoteFileTransfer(sourceFilePath2, targetDir2,
		// // targetIp, targetPort,
		// // toDo);
		// // } catch (Exception e) {
		// // // e.printStackTrace();
		// // LOGGER.error("nettyClientBootstrap.remoteFileTransfer 出现异常 ", e);
		// // }
		// // }
		// // });
		//
		// nettyClientBootstrap.remoteFileTransfer(sourceFilePath, targetDir, targetIp,
		// targetPort, toDo);
		//
		// }
		// return true;
	}

	private void fileCopy(String targetIp, int targetPort, int taskId, int targetServerId,
			DeployTaskServerToDoBean toDo, BaseBean baseBean) throws InterruptedException {
		logService.log(taskId, targetServerId, toDo.getId(),
				MessageFormat.format("执行todo任务：{0}", JSON.toJSONString(toDo)), baseBean);

		nettyClientBootstrap.remoteFileCopy(targetIp, targetPort, toDo);
		// return true;
	}

	/**
	 * 删除目录或文件
	 * 
	 * @param taskId
	 * @param targetServerId
	 * @param toDo
	 * @param baseBean
	 * @return
	 * @throws InterruptedException
	 */
	private void deleteDir(String targetIp, int targetPort, int taskId, int targetServerId,
			DeployTaskServerToDoBean toDo, BaseBean baseBean) throws InterruptedException {
		logService.log(taskId, targetServerId, toDo.getId(),
				MessageFormat.format("执行todo任务：{0}", JSON.toJSONString(toDo)), baseBean);

		nettyClientBootstrap.remoteDeleteDir(targetIp, targetPort, toDo);
	}

	/**
	 * 清空目录
	 * 
	 * @param targetIp
	 * @param targetPort
	 * @param taskId
	 * @param targetServerId
	 * @param toDo
	 * @param baseBean
	 * @throws InterruptedException
	 */
	private void emptyDir(String targetIp, int targetPort, int taskId, int targetServerId,
			DeployTaskServerToDoBean toDo, BaseBean baseBean) throws InterruptedException {
		logService.log(taskId, targetServerId, toDo.getId(),
				MessageFormat.format("执行todo任务：{0}", JSON.toJSONString(toDo)), baseBean);

		nettyClientBootstrap.remoteEmptyDir(targetIp, targetPort, toDo);
	}

	/**
	 * 远程执行命令行
	 * 
	 * @param taskId
	 * @param targetServerId
	 * @param toDo
	 * @param baseBean
	 * @return
	 * @throws InterruptedException
	 */
	private void remoteExecShell(String targetIp, int targetPort, int taskId, int targetServerId,
			DeployTaskServerToDoBean toDo, BaseBean baseBean) throws InterruptedException {
		logService.log(taskId, targetServerId, toDo.getId(),
				MessageFormat.format("执行todo任务：{0}", JSON.toJSONString(toDo)), baseBean);
		nettyClientBootstrap.remoteExecShell(targetIp, targetPort, toDo);
	}

	/**
	 * 级联更新部署任务状态，包括更新部署任务服务器、服务器todo的状态
	 * 
	 * @param taskId
	 * @param status
	 * @param baseBean
	 */
	public void updateTaskStatusCascade(DeployTaskBean taskBean, BaseBean baseBean) {
		// DeployTaskBean taskBean = new DeployTaskBean();
		// BeanUtils.copyProperties(baseBean, taskBean);
		// taskBean.setId(taskId);
		// taskBean.setStatus(status);
		// 更新t_deploy_task状态
		deployTaskManager.updateStatus(taskBean);

		DeployTaskServerBean taskServerBean = new DeployTaskServerBean();
		BeanUtils.copyProperties(baseBean, taskServerBean);
		taskServerBean.setDeployTaskId(taskBean.getId());
		taskServerBean.setStatus(taskBean.getStatus());
		// 更新t_deploy_task状态
		taskServerManager.updateStatusByTaskId(taskServerBean);

		List<DeployTaskServerBean> taskServers = taskServerManager.listAllByTaskId(taskBean.getId());
		for (DeployTaskServerBean deployTaskServerBean : taskServers) {
			DeployTaskServerToDoBean bean = new DeployTaskServerToDoBean();
			BeanUtils.copyProperties(baseBean, bean);
			bean.setDeployTaskServerId(deployTaskServerBean.getId());
			bean.setStatus(taskBean.getStatus());
			// 更新t_deploy_task_server_todo状态
			taskServerToDoManager.updateStatusByServerTaskId(bean);
		}
	}

}
