;
layui.define(["table", "form"],
function(t) {
    var $ = layui.$,
    i = layui.table,
    n = layui.form;
    
    var appId = $("#appId").val();
    var appName = $("#appName").text();
    var batchNo = $("#batchNo").val();

    i.render({
        elem: "#LAY-app-content-list",
        url: "/deploy/taskfile/listdata",
        where: {appId: appId,batchNo: batchNo},
        method: 'post',
        //本系统ajax异步请求都统一用post请求
        cols: [[{
            type: "checkbox",
            fixed: "left"
        },
        {
            field: "id",
            title: "ID",
            hide: true
        },
        {
            field: "relativePath",
            title: "文件相对目录"
        },
        {
            field: "md5",
            title: "文件md5值",
            width: 300
        },       
        {
            title: "操作",
            align: "center",
            width: 200,
            fixed: "right",
            toolbar: "#table-content-list"
        }]],
        page: true,
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
        var relativePath = e.relativePath;
        
        if ("download" === t.event) {
        	var arrayRelativePaths = new Array();
        	arrayRelativePaths.push(relativePath);
        	
        	$.each(arrayRelativePaths, function(index, value) {
        		var url="/deploy/taskfile/download?relativePath=" + relativePath + "&appName=" + appName + "&batchNo=" + batchNo + '&v='+Math.random();//下载文件url
    		    window.location.href=url;
        	});
        }   

    }),
    t("taskfilelist", {})
});