<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>部署文件列表</title>
  <meta name="renderer" content="webkit">
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache, must-revalidate">
  <meta http-equiv="expires" content="0">
  
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all">
  <link rel="stylesheet" href="/resources/layuiadmin/style/admin.css" media="all">
</head>
<body>
  <div class="layui-fluid">
    <div class="layui-card">
      <div class="layui-form layui-card-header layuiadmin-card-header-auto">
        <div class="layui-form-item">
          <div class="layui-inline">
           <span class="layui-breadcrumb">
			  <a href="javascript:void(0);" id="appName">${appName}</a>
			  <a><cite>${batchNo}</cite></a>
			</span>
			 <span style="color:red;padding-left:50px;">
			 注意：这里所下载的文件是位于部署系统服务器临时目录的文件（即待部署的文件），而不是已部署到目标服务器上的文件
			</span>
          </div>          
        </div>
        
        <div class="layui-form-item">
          <div class="layui-inline">
            <label class="layui-form-label">文件名</label>
            <div class="layui-input-inline">
              <input type="text" name="fileName" id="fileName" placeholder="请输入" autocomplete="off" class="layui-input">
              <input type="hidden" id="appId" value="${appId}">
			  <input type="hidden" id="batchNo" value="${batchNo}">
            </div>
          </div>
          <div class="layui-inline">
            <button class="layui-btn layuiadmin-btn-list" lay-submit lay-filter="LAY-app-contlist-search">
              <i class="layui-icon layui-icon-search layuiadmin-button-btn"></i>
            </button>
          </div>
        </div>
      </div>

      <div class="layui-card-body">
        <div style="padding-bottom: 10px;">
          <button class="layui-btn layuiadmin-btn-list" data-type="batchDownload">批量打包下载</button>
        </div>
        <table id="LAY-app-content-list" lay-filter="LAY-app-content-list"></table> 

        <script type="text/html" id="table-content-list">
			<a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="download"><i class="layui-icon layui-icon-download-circle"></i>下载</a>
        </script>
      </div>
    </div>
  </div>

  <script src="/resources/layuiadmin/layui/layui.js"></script>  
  <script>
  layui.config({
	    base: '/resources/layuiadmin/', //静态资源所在路径
	    version: '9c6e26476e834606b54575a37ddbab39'
	}).extend({
	    index: 'lib/index',
	    //主入口模块
	    taskfilelist: 'deploy/taskfilelist'
	}).use(['index', 'taskfilelist', 'table'],
	function() {
	    var table = layui.table,
	    form = layui.form;

	    //监听搜索
	    form.on('submit(LAY-app-contlist-search)',
	    function(data) {
	        var field = data.field;

	        //执行重载
	        table.reload('LAY-app-content-list', {
	            where: field,
	            page: {
	                curr: 1 //重新从第 1 页开始
	            }
	        });
	    });

	    var $ = layui.$,
	    active = {
	        batchDownload: function() {
	            var checkStatus = table.checkStatus('LAY-app-content-list'),
	            checkData = checkStatus.data; //得到选中的数据
	            if (checkData.length === 0) {
	                return layer.msg('请选择要下载的文件');
	            }

	            var url = "/deploy/taskfile/download"; //下载文件url
	            var form = $("<form></form>").attr("action", url).attr("method", "post");
	            // 封装参数
	            $.each(checkData,
	            function(index, value) {
	                form.append($("<input></input>").attr("type", "hidden").attr("name", "relativePaths").attr("value", value.relativePath));

	            });

	            form.append($("<input></input>").attr("type", "hidden").attr("name", "appName").attr("value", "${appName}"));
	            form.append($("<input></input>").attr("type", "hidden").attr("name", "batchNo").attr("value", "${batchNo}"));
	            // 提交
	            form.appendTo('body').submit().remove();
	        }
	    };

	    $('.layui-btn.layuiadmin-btn-list').on('click',
	    function() {
	        var type = $(this).data('type');
	        active[type] ? active[type].call(this) : '';
	    });

	});
  </script>
</body>
</html>    