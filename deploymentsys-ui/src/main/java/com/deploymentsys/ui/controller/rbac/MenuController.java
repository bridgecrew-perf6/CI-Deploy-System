package com.deploymentsys.ui.controller.rbac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
import com.deploymentsys.beans.MenuBean;
import com.deploymentsys.beans.MenuGroupBean;
import com.deploymentsys.beans.StaffBean;
import com.deploymentsys.service.MenuGroupService;
import com.deploymentsys.service.MenuService;
import com.deploymentsys.service.RoleMenuService;
import com.deploymentsys.utils.WebUtils;
import com.deploymentsys.utils.dtree.CheckArr;
import com.deploymentsys.utils.dtree.DTree;
import com.deploymentsys.utils.dtree.DTreeResponse;

@Controller
@RequestMapping("/menu")
public class MenuController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);
	private static final int PAGE_SIZE_MAX = 30;
	private static final String ERROR = "error";
	private static final String MSG = "msg";

	@Autowired
	private MenuService menuService;

	@Autowired
	private MenuGroupService menuGroupService;

	@Autowired
	private RoleMenuService roleMenuService;

	@Autowired
	private WebUtils webUtils;

	/**
	 * 显示页面：功能管理列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list() {
		return "rbac/menuslist";
	}

	@ResponseBody
	@RequestMapping(value = "/listdata", method = RequestMethod.POST)
	public String getMenusList(int page, int limit, String name, String url, Integer menuGroupId) {
		// LOGGER.info(
		// String.format("go into MenuController.getMenusList, page:%d, limit:%d,
		// name:%s, url:%s, menuGroupId:%d",
		// page, limit, name, url, menuGroupId));

		int pageSize = limit > PAGE_SIZE_MAX ? PAGE_SIZE_MAX : limit;
		int pageStart = (page - 1) * pageSize;

		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", 0);

		int count = menuService.getListCount(name, url, menuGroupId);
		jsonResult.put("count", count);

		List<MenuBean> data = menuService.list(pageStart, pageSize, name, url, menuGroupId);

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
		LOGGER.info("go into MenuController.delete, ids:" + Arrays.toString(ids));
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
		menuService.softDelete(ids);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	/**
	 * 显示页面：添加功能
	 * 
	 * @return
	 */
	@RequestMapping(value = "/menuadd", method = RequestMethod.GET)
	public String add() {
		return "rbac/menuadd";
	}

	@ResponseBody
	@RequestMapping(value = "/menuadd", method = RequestMethod.POST)
	public String add(MenuBean formBean, HttpServletRequest request) {
		LOGGER.info(String.format("go into MenuController.addMenu, name:%s, show:%b, description:%s",
				formBean.getName(), formBean.isShow(), formBean.getDescription()));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int creatorId = null != staff ? staff.getId() : 0;
		String currDate = webUtils.getCurrentDateStr();
		String clientIp = webUtils.getClientIp(request);

		formBean.setCreateDate(currDate);
		formBean.setCreator(creatorId);
		formBean.setCreateIp(clientIp);
		formBean.setModifyDate(currDate);
		formBean.setModifier(creatorId);
		formBean.setModifyIp(clientIp);

		menuService.add(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@RequestMapping(value = "/editmenu", method = RequestMethod.GET)
	public String edit(int id, Model model) {
		model.addAttribute("id", id);

		MenuBean menu = menuService.getMenu(id);
		model.addAttribute("menu", menu);
		return "rbac/menuadd";
	}

	@ResponseBody
	@RequestMapping(value = "/editmenu", method = RequestMethod.POST)
	public String edit(MenuBean formBean, HttpServletRequest request) {
		LOGGER.info(String.format("go into MenuController.editMenu, id:%d, name:%s, show:%b, description:%s",
				formBean.getId(), formBean.getName(), formBean.isShow(), formBean.getDescription()));
		int error = 0;
		String msg = "";
		JSONObject jsonResult = new JSONObject();

		StaffBean staff = webUtils.getCurrentLoginInfo(request);
		int modifierId = null != staff ? staff.getId() : 0;
		formBean.setModifyDate(webUtils.getCurrentDateStr());
		formBean.setModifier(modifierId);
		formBean.setModifyIp(webUtils.getClientIp(request));
		menuService.updateMenu(formBean);

		jsonResult.put(ERROR, error);
		jsonResult.put(MSG, msg);
		return jsonResult.toJSONString();
	}

	@ResponseBody
	@RequestMapping(value = "/getallmenusfortree", method = RequestMethod.POST)
	public String getAllMenusForTree(int roleId) {
		List<MenuGroupBean> menuGroups = menuGroupService.getAllMenuGroups();
		List<MenuBean> menus = menuService.getAllMenus();

		List<Integer> ownedMenuIds = roleMenuService.getRoleMenuRelation(roleId);

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

		for (MenuGroupBean mg : menuGroups) {
			// 判断该功能组下面有没有功能菜单，如果没有则继续下一个功能组
			if (menus.stream().filter(x -> x.getMenuGroupId() == mg.getId()).collect(Collectors.toList()).size() == 0) {
				continue;
			}

			// 当前功能组下面所包含的功能菜单数量
			int menuSum = menus.stream().filter(x -> x.getMenuGroupId() == mg.getId()).collect(Collectors.toList())
					.size();

			DTree root = new DTree();
			root.setId("root" + Integer.toString(mg.getId()));
			root.setTitle(mg.getName());
			// root.setLevel("1");//这个level貌似没多大意义
			root.setSpread(false);
			root.setParentId("0");

			DTree child = null;
			List<DTree> children = new ArrayList<DTree>();

			int n = 0;
			for (MenuBean menu : menus.stream().filter(x -> x.getMenuGroupId() == mg.getId())
					.collect(Collectors.toList())) {
				child = new DTree();
				child.setId(Integer.toString(menu.getId()));
				child.setTitle(menu.getName());
				// child.setLevel("2");
				child.setParentId("root" + Integer.toString(mg.getId()));
				if (ownedMenuIds.contains(menu.getId())) {
					child.setCheckArr(checkArrs1);
					n++;
				} else {
					child.setCheckArr(checkArrs0);
				}

				child.setIsLast(true);
				children.add(child);
			}

			if (n == 0) {
				root.setCheckArr(checkArrs0);
			}
			if (n > 0 && n < menuSum) {
				root.setCheckArr(checkArrs2);
			}
			if (n == menuSum) {
				root.setCheckArr(checkArrs1);
			}

			root.setChildren(children);
			rootD.add(root);
		}

		DTreeResponse resp = new DTreeResponse();
		resp.setData(rootD);

		return JSONObject.toJSON(resp).toString();
	}

	@ResponseBody
	@RequestMapping(value = "/getmenusbymenugroupid", method = RequestMethod.POST)
	public String getMenusByMenuGroupId(int menuGroupId) {
		LOGGER.info(String.format("go into MenuController.getMenusByMenuGroupId, menuGroupId:%d", menuGroupId));
		List<MenuBean> menus = menuService.getMenusByMenuGroupId(menuGroupId);
		MenuGroupBean menuGroupBean = menuGroupService.getMenuGroup(menuGroupId);

		List<DTree> rootD = new ArrayList<DTree>();
		DTree root = new DTree();
		root.setId("root");
		root.setTitle(menuGroupBean.getName());
		root.setLevel("1");
		root.setParentId("0");

		DTree child = null;
		List<DTree> children = new ArrayList<DTree>();
		for (MenuBean menu : menus) {
			child = new DTree();
			child.setId(String.valueOf(menu.getId()));
			child.setTitle(menu.getName());
			child.setLevel("2");
			child.setParentId("root");
			child.setIsLast(true);
			children.add(child);
		}

		root.setChildren(children);
		rootD.add(root);
		DTreeResponse resp = new DTreeResponse();
		resp.setData(rootD);

		return JSONObject.toJSON(resp).toString();
	}

}
