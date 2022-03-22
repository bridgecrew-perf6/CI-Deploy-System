;
layui.define(["table", "form"],
function(t) {
    var $ = layui.$,
    i = layui.table,
    n = layui.form;

    i.render({
        elem: "#LAY-app-content-list",
        url: "/deploy/task/test/listdata",
        method: 'post',
        //本系统ajax异步请求都统一用post请求
        cols: [[
        {
            field: "id",
            title: "ID",
            hide: true
        },
        {
            field: "appId",
            title: "appId",
            hide: true
        },
        {
            field: "appName",
            title: "应用名称",
            width: 180
        },
        {
            field: "batchNo",
            title: "批次号",
            width: 200
        },
        {
            field: "flowType",
            title: "任务类型",
            width: 90
        },
        {
            field: "flowOrder",
            title: "任务顺序",
            width: 90
        },
        {
            field: "status",
            title: "任务状态",
            width: 90
        },
        {
            field: "deploymentApplicant",
            title: "申请人",
            width: 120
        },
        {
			field: "createDate",
			title: "申请时间",
			width: 180
		},
        {
            field: "description",
            title: "部署说明",
        },
        {
            title: "操作",
            align: "left",
            width: 470,
            fixed: "right",
            toolbar: "#table-content-list"
        }]],
        page: !0,
        limit: 15,
        limits: [10, 15, 20, 25, 30],
        text: {
            none: '暂无相关数据' //默认：无数据。
        }
    }),
    i.on("tool(LAY-app-content-list)",
    function(t) {
        var e = t.data;
        var id = e.id;
        var appId = e.appId;
        var appName = e.appName;
        var flowType = e.flowType;
        var batchNo = e.batchNo;
        
        if ("deploy" === t.event) {
            layer.confirm("确定部署此任务？",
            function(e) {
                $.ajax({
                    type: "POST",
                    url: "/deploy/task/test/deploy",
                    data: {
                    	taskId: id
                    },
                    dataType: "json"
                }).done(function(result) {
                    if (result.error == 0) {
                        //执行 Ajax 后重载         
                        i.reload('LAY-app-content-list', {
                            where: { //设定异步数据接口的额外参数，任意设
                                appId: null,
                                batchNo: null
                            },
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                        layer.msg('操作成功');
                    } else {
                        layer.msg(result.msg, {
                            offset: '15px',
                            icon: 2,
                            time: 1000
                        });
                    }
                });
            });
        }
        
        if ("abortTask" === t.event) {
            layer.confirm("确定终止此任务？",
            function(e) {
                $.ajax({
                    type: "POST",
                    url: "/deploy/task/test/abort",
                    data: {
                    	taskId: id
                    },
                    dataType: "json"
                }).done(function(result) {
                    if (result.error == 0) {
                        //执行 Ajax 后重载         
                        i.reload('LAY-app-content-list', {
                            where: { //设定异步数据接口的额外参数，任意设
                                appId: null,
                                batchNo: null
                            },
                            page: {
                                curr: 1 //重新从第 1 页开始
                            }
                        });
                        layer.msg('操作成功');
                    } else {
                        layer.msg(result.msg, {
                            offset: '15px',
                            icon: 2,
                            time: 1000
                        });
                    }
                });
            });
        }
        
        if ("taskDetails" === t.event) {
			var taskDetailsLayer = layer.open({
		          type: 2
		          ,title: '任务明细'
		          ,content: '/deploy/taskserver/list?taskId=' + id + '&appName=' + appName + '&flowType=' + flowType + '&v=' + Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(taskDetailsLayer);
		}
        
        if ("taskFiles" === t.event) {
			var taskFilesLayer = layer.open({
		          type: 2
		          ,title: '部署文件列表'
		          ,content: '/deploy/taskfile/list?appId=' + appId + '&appName=' + appName + '&batchNo=' + batchNo + '&v=' + Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(taskFilesLayer);
		}
        
        if ("taskLogs" === t.event) {
			var taskLogsLayer = layer.open({
		          type: 2
		          ,title: '部署日志'
		          ,content: '/deploy/log/list?taskId=' + id + '&v=' + Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(taskLogsLayer);
		}
    }),
    t("tasktestlist", {})
});