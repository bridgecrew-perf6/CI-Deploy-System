package com.deploymentsys.manager.deploy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.deploy.DeployTaskFileBean;
import com.deploymentsys.dao.deploy.DeployTaskFileMapper;

@Component
public class DeployTaskFileManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeployTaskFileManager.class);

	@Autowired
	DeployTaskFileMapper deployTaskFileMapper;

	public List<DeployTaskFileBean> list(int pageStart, int size, String fileName, int appId, String batchNo) {
		try {
			return deployTaskFileMapper.list(pageStart, size, fileName, appId, batchNo);
		} catch (Exception ex) {
			LOGGER.error("分页获取部署文件列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public List<DeployTaskFileBean> listAll(int appId, String batchNo) {
		return deployTaskFileMapper.listAll(appId, batchNo);
	}

	public int getListCount(String fileName, int appId, String batchNo) {
		try {
			return deployTaskFileMapper.getListCount(fileName, appId, batchNo);
		} catch (Exception ex) {
			LOGGER.error("获取部署文件列表总和发生异常", ex);
			return 0;
		}
	}

	public int addBatch(List<DeployTaskFileBean> list) {
		return deployTaskFileMapper.addBatch(list);
	}

}
