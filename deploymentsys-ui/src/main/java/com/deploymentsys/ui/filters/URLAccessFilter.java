package com.deploymentsys.ui.filters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.OperationLogBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.beans.constants.SysConstants;
import com.deploymentsys.service.MenuService;
import com.deploymentsys.service.OperationLogService;
import com.deploymentsys.service.UrlWhiteListService;
import com.deploymentsys.ui.controller.rbac.LoginController;
import com.deploymentsys.utils.WebUtils;

@Component
public class URLAccessFilter implements Filter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	// 如果请求url中包含有这些字符则忽略
	private static final String[] IGNORE_URL = { ".json", ".js", ".css", ".ico", ".jpg", ".gif", ".png", ".htm",
			".html", ".woff", ".ttf" };

	@Autowired
	private UrlWhiteListService urlWhiteListService;

	@Autowired
	private OperationLogService operationLogService;

	@Autowired
	private MenuService menuService;

	@Autowired
	private WebUtils webUtils;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	/**
	 * 使用原生方式获取请求参数
	 * 
	 * @param req
	 * @return
	 */
	private String getRequestPayload(HttpServletRequest req) {
		StringBuilder sb = new StringBuilder();
		try (BufferedReader reader = req.getReader()) {
			char[] buff = new char[1024];
			int len;
			while ((len = reader.read(buff)) != -1) {
				sb.append(buff, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// 获得在下面代码中要用的request,response,session对象
		HttpServletRequest servletRequest = (HttpServletRequest) request;
		HttpServletResponse servletResponse = (HttpServletResponse) response;
		HttpSession session = servletRequest.getSession();
		JSONObject jsonResult = new JSONObject();

		// 所有请求返回前都清除缓存
		servletResponse.setHeader("Cache-Control", "no-cache");
		servletResponse.setHeader("Pragma", "no-cache");
		servletResponse.setDateHeader("expires", -1);

		// 获得用户请求的URI
		String requestUrl = servletRequest.getScheme() // 当前链接使用的协议
				+ "://" + servletRequest.getServerName()// 服务器地址
				+ (80 == servletRequest.getServerPort() ? "" : ":" + servletRequest.getServerPort()) // 端口号
				+ servletRequest.getContextPath() // 应用名称，如果应用名称为
				+ servletRequest.getServletPath(); // 请求的相对url
		// + "?" + servletRequest.getQueryString(); // 请求参数
		
		//项目演示，暂时不允许删除操作，2022年2月28日16:02:24
		if(requestUrl.toLowerCase().contains("delete")) {
			jsonResult.put(ERROR, 1);
			jsonResult.put(MSG, "项目演示中，暂时不允许删除操作！");

			servletResponse.setCharacterEncoding("UTF-8");
			servletResponse.setContentType("application/json;charset=UTF-8");
			PrintWriter writer = servletResponse.getWriter();
			writer.write(jsonResult.toJSONString());
			writer.close();
			servletResponse.flushBuffer();
			return;
		}

		String requestMethod = servletRequest.getMethod();
		String contentType = servletRequest.getContentType();
		String queryString = "";
		// System.out.println(requestUrl);
		// System.out.println(requestMethod);
		// System.out.println("contentType: " + contentType);

		if ("get".equalsIgnoreCase(requestMethod)) {
			queryString = null != servletRequest.getQueryString()
					? URLDecoder.decode(servletRequest.getQueryString(), "UTF-8")
					: queryString;
		}

		if ("post".equalsIgnoreCase(requestMethod)) {
			// System.out.println("这是post请求");
			Map<String, String[]> params = request.getParameterMap();
			for (String key : params.keySet()) {
				String[] values = params.get(key);
				for (int i = 0; i < values.length; i++) {
					String value = values[i];
					queryString += key + "=" + value + "&";
				}
			}
			// 去掉最后一个空格
			queryString = queryString.length() > 0 ? queryString.substring(0, queryString.length() - 1) : queryString;
			// queryString = getRequestPayload(servletRequest);
		}

		boolean isNeedLogin = true;
		for (String str : IGNORE_URL) {
			if (requestUrl.indexOf(str) != -1) {
				isNeedLogin = false;
				break;
			}
		}

		if (isNeedLogin) {
			// 判断请求的url是否在白名单中，如果在则跳过登录状态判断
			List<String> whiteList = urlWhiteListService.getAllUrlWhiteList();
			for (String url : whiteList) {
				if (url.equalsIgnoreCase(requestUrl)) {
					chain.doFilter(request, response);
					return;
				}
			}

			// 判断是否为ajax请求
			boolean isAjaxRequest = false;
			if (!StringUtils.isBlank(servletRequest.getHeader("x-requested-with"))
					&& servletRequest.getHeader("x-requested-with").equals("XMLHttpRequest")) {
				isAjaxRequest = true;
			}

			// 判断登录状态
			StaffBean staff = (StaffBean) session.getAttribute(SysConstants.CURRENT_LOGIN_INFO);
			if (staff != null) {
				// chain.doFilter(request, response);
				// return;
				// 判断该人员是否有所请求的url的操作权限
				List<String> permissionList = menuService.getStaffPermissions(staff.getId());
				for (String url : permissionList) {
					if (url.equalsIgnoreCase(requestUrl)) {
						// 记录操作日志
						OperationLogBean operationLogBean = new OperationLogBean();
						operationLogBean.setCreateDate(webUtils.getCurrentDateStr());
						operationLogBean.setUrl(requestUrl);
						operationLogBean.setRequestMethod(requestMethod);
						operationLogBean.setContentType(contentType);
						operationLogBean.setRequestParameters(
								queryString.length() > 800 ? queryString.substring(0, 800) : queryString);
						operationLogBean.setCreateIp(webUtils.getClientIp(servletRequest));
						operationLogBean.setCreator(staff.getId());
						operationLogService.add(operationLogBean);

						chain.doFilter(request, response);
						return;
					}
				}

				// 没有权限
				if (isAjaxRequest) {
					// 如果是ajax请求，则不是跳转页面，使用response返回json
					jsonResult.put(ERROR, 1);
					jsonResult.put(MSG, "没有此操作权限！");

					servletResponse.setCharacterEncoding("UTF-8");
					servletResponse.setContentType("application/json;charset=UTF-8");
					PrintWriter writer = servletResponse.getWriter();
					writer.write(jsonResult.toJSONString());
					writer.close();
					servletResponse.flushBuffer();
				} else {
					// 跳转到无权限访问提示页
					servletResponse.sendRedirect("/403.html");
				}
			} else {
				if (isAjaxRequest) {
					// 如果是ajax请求，则不是跳转页面，使用response返回json
					jsonResult.put(ERROR, 1);
					jsonResult.put(MSG, "登录已失效，请刷新页面或重新登录！");

					servletResponse.setCharacterEncoding("UTF-8");
					servletResponse.setContentType("application/json;charset=UTF-8");
					PrintWriter writer = servletResponse.getWriter();
					writer.write(jsonResult.toJSONString());
					writer.close();
					servletResponse.flushBuffer();
				} else {
					// 跳转到登录页面
					servletResponse.sendRedirect("/login");
				}
			}
		} else {
			// 直接忽略
			chain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {

	}

}
