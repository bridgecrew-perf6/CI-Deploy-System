;
layui.define([ "table", "form" ], function(t) {
	var $ = layui.$, i = layui.table, n = layui.form;

	var taskId = $("#taskId").val();
	var taskServerId = $("#taskServerId").val();
	var serverTodoId = $("#serverTodoId").val();

	i.render({
		elem : "#LAY-app-content-list",
		url : "/deploy/log/getlist",
		where : {
			taskId : taskId,
			taskServerId : taskServerId,
			serverTodoId : serverTodoId
		},
		method : 'post', // 本系统ajax异步请求都统一用post请求
		cols : [ [  {
			field : "logContent",
			title : "日志内容"
		}, {
			field : "createDate",
			title : "部署时间",
			width : 200
		} ] ],
		page : true,
		limit : 20,
		limits : [ 10, 15, 20, 25, 30 ],
		text : {
			none : '暂无相关数据' // 默认：无数据。
		}
	}), i.on("tool(LAY-app-content-list)", function(t) {


	}), t("taskloglist", {})
});