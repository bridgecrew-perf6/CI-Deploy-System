package com.deploymentsys.service.netty;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.beans.deploy.DeployTaskServerToDoBean;

import io.netty.bootstrap.Bootstrap;

/**
 * 部署系统Web端Netty通信组件
 * 
 * @author adminjack
 *
 */
@Service
public class NettyDeployTransferClientBootstrap {

	/**
	 * 单文件传输
	 * 
	 * @param fileName
	 * @param ip
	 * @param port
	 * @throws InterruptedException
	 */
	public void remoteFileTransfer(List<DeployTaskFileBean> files, String sourceFilePathBase, String targetDirBase,
			String ip, int port, DeployTaskServerToDoBean toDo) throws InterruptedException {
		// FileTransferResponseForWebHandler customHandler = new
		// FileTransferResponseForWebHandler(fileName);

		Bootstrap bootstrap = NettyClientInit.initForFileTransfer(files, sourceFilePathBase, targetDirBase, toDo);
		NettyClientInit.connect(bootstrap, ip, port);
	}

	/**
	 * 远程文件拷贝
	 * 
	 * @param ip
	 * @param port
	 * @param toDo
	 * @throws InterruptedException
	 */
	public void remoteFileCopy(String ip, int port, DeployTaskServerToDoBean toDo) throws InterruptedException {
		Bootstrap bootstrap = NettyClientInit.initForFileCopy(toDo);
		NettyClientInit.connect(bootstrap, ip, port);
	}

	/**
	 * 远程删除文件或目录
	 * 
	 * @param ip
	 * @param port
	 * @param toDo
	 * @throws InterruptedException
	 */
	public void remoteDeleteDir(String ip, int port, DeployTaskServerToDoBean toDo) throws InterruptedException {
		Bootstrap bootstrap = NettyClientInit.initForDeleteDir(toDo);
		NettyClientInit.connect(bootstrap, ip, port);
	}

	/**
	 * 远程清空目录
	 * 
	 * @param ip
	 * @param port
	 * @param toDo
	 * @throws InterruptedException
	 */
	public void remoteEmptyDir(String ip, int port, DeployTaskServerToDoBean toDo) throws InterruptedException {
		Bootstrap bootstrap = NettyClientInit.initForEmptyDir(toDo);
		NettyClientInit.connect(bootstrap, ip, port);
	}

	/**
	 * 远程执行命令行
	 * 
	 * @param ip
	 * @param port
	 * @param toDo
	 * @throws InterruptedException
	 */
	public void remoteExecShell(String ip, int port, DeployTaskServerToDoBean toDo) throws InterruptedException {
		Bootstrap bootstrap = NettyClientInit.initForExecShell(toDo);
		NettyClientInit.connect(bootstrap, ip, port);
	}
}
