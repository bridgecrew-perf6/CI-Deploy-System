/** layuiAdmin.std-v1.2.1 LPPL License By http://www.layui.com/admin/ */
;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;
	var taskId=$("#taskId").val();
	
	i.render({
		elem: "#LAY-app-content-list",
		url: "/excelsyncimporttest/listdata",
		where: {taskIdStr: taskId},
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				field: "id",
				width: 100,
				title: "ID",
				hide: true
			}, {
				field: "userName",
				title: "用户名"
			}, {
				field: "age",
				title: "年龄",
				width: 100
			}, {
				field: "cellPhone",
				title: "手机号"
			}, {
				field: "createDate",
				title: "导入时间",
				minWidth: 100
			}, {
				field: "creatorName",
				title: "操作人",
				minWidth: 100
			}]
		],
		page: !0,
		limit: 15,
		limits: [10, 15, 20, 25, 30],
		text: {
		    none: '暂无相关数据' //默认：无数据。
		  }
	}), i.on("tool(LAY-app-content-list)", function(t) {		
		
	}), t("ExcelSyncImportTest", {})
});