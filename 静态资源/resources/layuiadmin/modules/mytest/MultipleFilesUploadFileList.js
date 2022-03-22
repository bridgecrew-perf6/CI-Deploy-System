;
layui.define(["table", "form"],
function(t) {
    var $ = layui.$,
    i = layui.table,
    n = layui.form;

    i.render({
        elem: "#LAY-app-content-list",
        url: "/multiplefilesupload/listdata",
        method: 'post',
        //本系统ajax异步请求都统一用post请求
        cols: [[
        {
            field: "id",
            title: "ID",
            hide: true
        },
        {
            field: "fileLocalPath",
            title: "文件路径"
        },             
        {
            title: "操作",
            align: "center",
            width: 200,
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
        var fileLocalPath = e.fileLocalPath;
        
        if ("download" === t.event) {       	
        	var url="/multiplefilesupload/download?fileLocalPath=" + fileLocalPath + '&v='+Math.random();//下载文件url
		    window.location.href=url;
        }   

    }),
    t("MultipleFilesUploadFileList", {})
});