/** layuiAdmin.std-v1.2.1 LPPL License By http://www.layui.com/admin/ */
;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;
	i.render({
		elem: "#LAY-app-content-list",
		url: "/excelasyncimporttask/listdata",
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				field: "id",
				width: 100,
				title: "ID",
				hide: true
			}, {
				field: "taskName",
				title: "任务名"
			}, {
				field: "originalFileName",
				title: "原始文件名"
			}, {
				field: "status",
				title: "任务状态",
				width: 150
			}, {
				field: "failureCause",
				title: "失败原因"
			}, {
				field: "createDate",
				title: "导入时间",
				minWidth: 100
			}, {
				field: "creatorName",
				title: "操作人",
				minWidth: 100
			},
        {
            title: "操作",
            align: "center",
            //width: 470,
            fixed: "right",
            toolbar: "#table-content-list"
        }]
		],
		page: !0,
		limit: 15,
		limits: [10, 15, 20, 25, 30],
		text: {
		    none: '暂无相关数据' //默认：无数据。
		  }
	}), i.on("tool(LAY-app-content-list)", function(t) {
		var e = t.data;
        var id = e.id;
		
		if ("taskDetails" === t.event) {
			var taskDetailsLayer = layer.open({
		          type: 2
		          ,title: '数据明细'
		          ,content: '/excelasyncimporttask/taskdetails?taskId=' + id + '&v=' + Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(taskDetailsLayer);
		}
	}), t("ExcelAsyncImportTaskList", {})
});