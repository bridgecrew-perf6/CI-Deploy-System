;
layui.define(["table", "form"],
function(t) {
    var $ = layui.$,
    i = layui.table,
    n = layui.form;

    var taskId = $("#taskId").val();
    var taskServerId = $("#taskServerId").val();

    i.render({
        elem: "#LAY-app-content-list",
        url: "/deploy/taskservertodo/listdata",
        where: {
            taskServerId: taskServerId
        },
        method: 'post',
        //本系统ajax异步请求都统一用post请求
        cols: [[{
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
            field: "todoType",
            title: "执行类型",
            width: 90
        },
        {
            field: "todoOrder",
            title: "执行顺序",
            width: 90
        },
        {
            field: "param1",
            title: "执行参数1"
        },
        {
            field: "param2",
            title: "执行参数2"
        },
        {
            field: "param3",
            title: "执行参数3"
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
            width: 150,
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

        if ("taskLogs" === t.event) {
			var taskLogsLayer = layer.open({
		          type: 2
		          ,title: '部署日志'
		          ,content: '/deploy/log/list?taskId=' + taskId + '&taskServerId=' + taskServerId + '&serverTodoId=' + id + '&v=' + Math.random()
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(taskLogsLayer);
		}
    }),
    t("taskservertodolist", {})
});