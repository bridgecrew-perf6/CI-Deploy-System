package com.deploymentsys.ui.controller.rbac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.deploymentsys.beans.RoleBean;
import com.deploymentsys.beans.RoleMenuBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.service.RoleMenuService;
import com.deploymentsys.service.RoleService;
import com.deploymentsys.service.StaffRoleService;
import com.deploymentsys.utils.WebUtils;
import com.deploymentsys.utils.dtree.CheckArr;
import com.deploymentsys.utils.dtree.DTree;
import com.deploymentsys.utils.dtree.DTreeResponse;

@Controller
@RequestMapping("/role")
public class RoleController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private RoleService roleService;

	@Autowired
	private StaffRoleService staffRoleService;

	@Autowired
	private RoleMenuService roleMenuService;

	@Autowired
	private WebUtils webUtils;

	/**
	 * 显示页面：角色管理列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "rbac/roleslist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getRolesList(int page, int limit, String name) {
		LOGGER.info(
				String.format("go into RoleController.getRolesList, page:%d, limit:%d, name:%s", page, limit, name));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = roleService.getListCount(name);
		jsonResult.put("count", count);

		List<RoleBean> data = roleService.list(pageStart, pageSize, name);

		jsonResult.put("data", data);
		return jsonResult.toJSONString();
	}

	/**
	 * 删除实体，可删除单个或多个
	 * 
	 * @param ids
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public String delete(@RequestParam(value = "ids[]") int[] ids) {
		LOGGER.info("go into RoleController.delete, ids:" + Arrays.toString(ids));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		if (ids.length == 0) {
			error = 1;
			msg = "请传入正确的参数";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}
		roleService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：添加角色
	 * 
	 * @return
	 */
	@RequestMapping(value = "/roleadd", method = RequestMethod.GET)
	public String addRole() {
		return "rbac/roleadd";
	}

	@ResponseBody
	@RequestMapping(value = "/roleadd", method = RequestMethod.POST)
	public String addRole(String name, String description, HttpServletRequest request) {
		LOGGER.info(String.format("go into RoleController.addRole, name:%s, description:%s", name, description));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);
		RoleBean bean = new RoleBean(0, name, description, currDate, creatorId, clientIp, currDate, creatorId,
				clientIp);
		roleService.add(bean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：编辑角色
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/editrole", method = RequestMethod.GET)
	public String editRole(int id, Model model) {
		LOGGER.info(String.format("go into RoleController.editRole, id:%d", id));
		model.addAttribute("id", id);

		RoleBean role = roleService.getRole(id);
		model.addAttribute("name", role.getName());
		model.addAttribute("description", role.getDescription());

		return "rbac/roleadd";
	}

	/**
	 * ajax处理：编辑角色
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/editrole", method = RequestMethod.POST)
	public String editRole(int id, String name, String description, HttpServletRequest request) {
		LOGGER.info(String.format("go into RoleController.editRole, id:%d, name:%s, description:%s", id, name,
				description));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		roleService.updateRole(id, name, description, webUtils.getCurrentDateStr(), modifierId,
				webUtils.getClientIp(request));

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 获取所有管理员角色
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getallroles", method = RequestMethod.POST)
	public String getAllRolesForTree(int staffId) {
		LOGGER.info(String.format("go into RoleController.getAllRolesForTree, staffId:%d", staffId));
		List<RoleBean> roles = new ArrayList<RoleBean>();
		roles = roleService.getAllRoles();

		List<Integer> ownedRoleIds = staffRoleService.getStaffRoleRelation(staffId);

		// 未选中
		List<CheckArr> checkArrs0 = new ArrayList<CheckArr>() {
			private static final long serialVersionUID = 1L;

			{
				add(new CheckArr("0", "0"));
			}
		};

		// 全选中
		List<CheckArr> checkArrs1 = new ArrayList<CheckArr>() {
			private static final long serialVersionUID = 1L;

			{
				add(new CheckArr("0", "1"));
			}
		};

		// 半选中
		List<CheckArr> checkArrs2 = new ArrayList<CheckArr>() {
			private static final long serialVersionUID = 1L;

			{
				add(new CheckArr("0", "2"));
			}
		};

		List<DTree> rootD = new ArrayList<DTree>();
		DTree root = new DTree();
		root.setId("root");
		root.setTitle("所有角色");
		root.setLevel("1");
		root.setParentId("0");
		if (ownedRoleIds != null && ownedRoleIds.size() == roles.size()) {
			root.setCheckArr(checkArrs1);
		} else if (ownedRoleIds != null && ownedRoleIds.size() > 0) {
			root.setCheckArr(checkArrs2);
		} else {
			root.setCheckArr(checkArrs0);
		}

		DTree child = null;
		List<DTree> children = new ArrayList<DTree>();
		for (RoleBean role : roles) {
			child = new DTree();
			child.setId(String.valueOf(role.getId()));
			child.setTitle(role.getName());
			child.setLevel("2");
			child.setParentId("root");
			if (ownedRoleIds.contains(role.getId())) {
				child.setCheckArr(checkArrs1);
			} else {
				child.setCheckArr(checkArrs0);
			}

			child.setIsLast(true);
			children.add(child);
		}

		root.setChildren(children);
		rootD.add(root);
		DTreeResponse resp = new DTreeResponse();
		resp.setData(rootD);

		return JSONObject.toJSON(resp).toString();
	}

	@RequestMapping(value = "/roleassignpermissions", method = RequestMethod.GET)
	public String roleAssignPermissions(int roleId, Model model) {
		LOGGER.info(String.format("go into RoleController.roleAssignPermissions, roleId:%d", roleId));
		model.addAttribute("roleId", roleId);
		return "rbac/roleassignpermissions";
	}

	@ResponseBody
	@RequestMapping(value = "/roleassignpermissions", method = RequestMethod.POST)
	public String roleAssignPermissions(@RequestParam(value = "menuIds[]", required = false) int[] menuIds, int roleId,
			HttpServletRequest request) {
		LOGGER.info("go into RoleController.roleAssignPermissions, menuIds：" + Arrays.toString(menuIds) + ", roleId:"
				+ roleId);

		List<RoleMenuBean> beans = new ArrayList<>();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		if (null != menuIds) {
			for (int menuId : menuIds) {
				beans.add(new RoleMenuBean(menuId, roleId, webUtils.getCurrentDateStr(), creatorId,
						webUtils.getClientIp(request)));
			}
		}

		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();
		try {
			roleMenuService.addNewRoleMenuRelation(roleId, beans);
			jsonResult.put(ERROR, error);
			return jsonResult.toJSONString();
		} catch (Exception ex) {
			LOGGER.error("RoleController.roleAssignPermissions catch an exception", ex);
			error = 1;
			msg = "操作异常";
			jsonResult.put(ERROR, error);
			jsonResult.put(MSG, msg);
			return jsonResult.toJSONString();
		}
	}
}
