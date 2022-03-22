;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;
	i.render({
		elem: "#LAY-app-content-list",
		url: "/operationlog/listdata",
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				field: "url",
				title: "url"
			},{
				field: "requestMethod",
				title: "请求方式",
				width: 90
			},{
				field: "contentType",
				title: "ContentType",
				width: 300
			},{
				field: "requestParameters",
				title: "请求参数"
			},{
				field: "staffName",
				title: "操作人",
				width: 100
			}, {
				field: "createDate",
				title: "操作时间",
				width: 180
			},{
				field: "createIp",
				title: "操作ip",
				width: 110
			}]
		],
		page: !0,
		limit: 15,
		limits: [10, 15, 20, 25, 30],
		text: {
		    none: '暂无相关数据' //默认：无数据。
		  }
	}), t("operationloglist", {})
});