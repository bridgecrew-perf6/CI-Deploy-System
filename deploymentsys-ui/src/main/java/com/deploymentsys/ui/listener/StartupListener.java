package com.deploymentsys.ui.listener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.deploymentsys.service.deploy.DeployTaskService;

/**
 * spring mvc web应用启动时就执行特定处理
 * 
 * @author adminjack
 *
 */
@Component
public class StartupListener implements ApplicationContextAware {

	private static final Logger LOGGER = LoggerFactory.getLogger(StartupListener.class);

	@Autowired
	private DeployTaskService deployTaskService;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// 先更新历史任务的状态
		LOGGER.info("先更新历史任务状态，把状态为“执行中”的历史任务更新为失败。。。");
		try {
			deployTaskService.updateDeployTaskAtTheBeginning();
		} catch (Exception ex) {
			LOGGER.error("部署任务监控线程发生异常-deployTaskService.updateDeployTaskAtTheBeginning", ex);
			return;
		}

		startTaskServerToDoQueueMonitoringThread();

		// ============部署任务目标机器todo 日志监控线程
		startLogQueueMonitoringThread();

		startDeployTaskStatusMonitoringThread();
	}

	private void startDeployTaskStatusMonitoringThread() {
		LOGGER.info("启动部署任务状态更新线程。。。");

		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						deployTaskService.deployTaskStatusMonitoring();
					} catch (Exception ex) {
						//LOGGER.error("部署任务状态更新线程发生异常-deployTaskService.deployTaskStatusMonitoring", ex);
						
					}

				}
			}
		});

		t.setDaemon(true);//设置为后台线程
		t.start();
	}

	private void startLogQueueMonitoringThread() {
		LOGGER.info("启动部署日志监控线程。。。");
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						deployTaskService.logQueueMonitoring();
					} catch (Exception ex) {
						//LOGGER.error("部署任务日志监控线程发生异常-deployTaskService.logQueueMonitoring", ex);
					}

				}
			}
		});

		t.setDaemon(true);//设置为后台线程
		t.start();
	}

	private void startTaskServerToDoQueueMonitoringThread() {
		LOGGER.info("启动部署任务服务器todo状态更新线程。。。");
		
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					try {
						deployTaskService.taskServerToDoQueueMonitoring();
					} catch (Exception ex) {
						//LOGGER.error("部署任务监控线程发生异常-deployTaskService.taskServerToDoQueueMonitoring", ex);
					}

				}
			}
		});

		t.setDaemon(true);//设置为后台线程
		t.start();
	}

}
