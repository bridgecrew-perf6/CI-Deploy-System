package com.deploymentsys.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.SysConstants;

@Component
public class WebUtils {

	/**
	 * 获取客户端请求ip
	 * 
	 * @param request
	 * @return
	 */
	public String getClientIp(HttpServletRequest request) {

		String remoteAddr = "";

		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}

		return remoteAddr;
	}

	/**
	 * 获得当前系统登录账号信息
	 * 
	 * @param request
	 * @return
	 */
	public StaffBean getCurrentLoginInfo(HttpServletRequest request) {
		HttpSession session = request.getSession();
		StaffBean staff = (StaffBean) session.getAttribute(SysConstants.CURRENT_LOGIN_INFO);
		return staff;
	}

	/**
	 * 获取当前时间字符串形式
	 * 
	 * @return
	 */
	public String getCurrentDateStr() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateStr = sdf.format(now);

		return currentDateStr;
	}

	/**
	 * 获取本机ip
	 * 
	 * @return
	 * @throws UnknownHostException
	 */
	public String getLocalIp() throws UnknownHostException {
		return InetAddress.getLocalHost().getHostAddress();
	}

}
