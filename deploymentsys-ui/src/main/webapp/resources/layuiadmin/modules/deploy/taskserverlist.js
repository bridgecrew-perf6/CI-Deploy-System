;
layui.define(["table", "form"],
function(t) {
    var $ = layui.$,
    i = layui.table,
    n = layui.form;
    
    var taskId = $("#taskId").val();

    i.render({
        elem: "#LAY-app-content-list",
        url: "/deploy/taskserver/listdata",
        where: {taskId: taskId},
        method: 'post',
        //本系统ajax异步请求都统一用post请求
        cols: [[
        {
            field: "id",
            title: "ID",
            hide: true
        },        
        {
            field: "batchNo",
            title: "批次号",
            width: 200
        },
        {
            field: "targetServerIp",
            title: "目标服务器",
            width: 300
        },
        {
            field: "serverOrder",
            title: "部署顺序",
            width: 100
        },
        {
            field: "deployDir",
            title: "部署目录"
        },
        {
            field: "status",
            title: "任务状态",
            width: 135
        },
        {
            title: "操作",
            align: "center",
            fixed: "right",
            width: 300,
            toolbar: "#table-content-list"
        }]],
        page: !0,
        limit: 10,
        limits: [10, 15, 20, 25, 30],
        text: {
            none: '暂无相关数据' //默认：无数据。
        }
    }),
    i.on("tool(LAY-app-content-list)",
    function(t) {
        var e = t.data;
        var id = e.id;
        var serverIp = e.targetServerIp;
        var appName = $("#appName").text();
        var flowType = $("#flowType").text();
        
        if ("toDoDetails" === t.event) {
			var toDoDetailsLayer = layer.open({
		          type: 2
		          ,title: '执行明细'
		          ,content: '/deploy/taskservertodo/list?taskServerId=' + id + '&taskId=' + taskId + '&appName=' + appName + '&flowType=' + flowType + '&serverIp=' + serverIp + '&v=' + Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(toDoDetailsLayer);
		}
        
        if ("taskLogs" === t.event) {
			var taskLogsLayer = layer.open({
		          type: 2
		          ,title: '部署日志'
		          ,content: '/deploy/log/list?taskId=' + taskId + '&taskServerId=' + id + '&v=' + Math.random()
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(taskLogsLayer);
		}
    }),
    t("taskserverlist", {})
});