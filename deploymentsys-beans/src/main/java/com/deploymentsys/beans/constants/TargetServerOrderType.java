package com.deploymentsys.beans.constants;

/**
 * 流程配置的目标机器的执行顺序类型
 * 
 * @author adminjack
 *
 */
public interface TargetServerOrderType {
	/**
	 * 并发执行
	 */
	String CONCURRENCE = "并发执行";
	/**
	 * 队列执行
	 */
	String QUEUE = "队列执行";
}
