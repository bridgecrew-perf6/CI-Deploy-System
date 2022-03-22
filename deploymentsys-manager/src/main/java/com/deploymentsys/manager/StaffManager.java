package com.deploymentsys.manager;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.dao.StaffMapper;

@Component
public class StaffManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffManager.class);

	@Autowired
	StaffMapper staffMapper;

	public String getStaffPwd(int id) {
		try {
			return staffMapper.getStaffPwd(id);
		} catch (Exception ex) {
			LOGGER.error("StaffManager.getStaffPwd catch an exception", ex);
			return "";
		}
	}

	public StaffBean getById(int id) {
		try {
			return staffMapper.getById(id);
		} catch (Exception ex) {
			LOGGER.error("StaffManager.getById catch an exception", ex);
			return new StaffBean();
		}
	}

	public StaffBean getStaff(String loginName, String password) {
		try {
			return staffMapper.getStaff(loginName, password);
		} catch (Exception ex) {
			LOGGER.error("StaffManager.getStaff catch an exception", ex);
			return new StaffBean();
		}
	}

	public StaffBean getStaffByLoginName(String loginName) {
		try {
			return staffMapper.getStaffByLoginName(loginName);
		} catch (Exception ex) {
			LOGGER.error("StaffManager.getStaffByLoginName catch an exception", ex);
			return new StaffBean();
		}
	}

	public List<StaffBean> list(int pageStart, int size, String loginName) {
		try {
			return staffMapper.list(pageStart, size, loginName);
		} catch (Exception ex) {
			LOGGER.error("分页获取人员列表发生异常", ex);
			return new ArrayList<>();
		}
	}

	public int getListCount(String loginName) {
		try {
			return staffMapper.getListCount(loginName);
		} catch (Exception ex) {
			LOGGER.error("获取人员列表总和发生异常", ex);
			return 0;
		}
	}

	public int delete(int[] ids) {
		try {
			return staffMapper.delete(ids);
		} catch (Exception ex) {
			LOGGER.error("删除人员发生异常", ex);
			return 0;
		}
	}

	public int add(StaffBean bean) {
		try {
			return staffMapper.add(bean);
		} catch (Exception ex) {
			LOGGER.error("添加人员发生异常", ex);
			return 0;
		}
	}

	public int softDelete(int[] ids) {
		// try {
		// return staffMapper.softDelete(ids);
		// } catch (Exception ex) {
		// LOGGER.error("软删除人员发生异常", ex);
		// return 0;
		// }

		return staffMapper.softDelete(ids);
	}

	public int changeStaffPwd(int id, String password, String modifyDate, int modifier, String modifyIp) {
		try {
			return staffMapper.changeStaffPwd(id, password, modifyDate, modifier, modifyIp);
		} catch (Exception ex) {
			LOGGER.error("修改人员密码发生异常", ex);
			return 0;
		}
	}

	public int update(StaffBean bean) {
		try {
			return staffMapper.update(bean);
		} catch (Exception ex) {
			LOGGER.error("修改个人信息发生异常", ex);
			return 0;
		}
	}

}
