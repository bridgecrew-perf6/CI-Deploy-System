package com.deploymentsys.service.deploy;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.manager.deploy.DeployTaskFileManager;

@Service
public class DeployTaskFileService {

	@Autowired
	private DeployTaskFileManager taskFileManager;

	// public int add(DeployTargetServerBean bean) {
	// return deployTargetServerManager.add(bean);
	// }

	public List<DeployTaskFileBean> list(int pageStart, int size, String fileName, int appId, String batchNo) {
		return taskFileManager.list(pageStart, size, fileName, appId, batchNo);
	}

	public int getListCount(String fileName, int appId, String batchNo) {
		return taskFileManager.getListCount(fileName, appId, batchNo);
	}

}
