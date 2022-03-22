<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>部署任务目标服务器列表</title>
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
			  <a><cite id="flowType">${flowType}</cite></a>
			</span>
          </div>
        </div>
      </div>

<input type="hidden" id="taskId" value="${taskId}">
      <div class="layui-card-body">
        <table id="LAY-app-content-list" lay-filter="LAY-app-content-list"></table> 

        <script type="text/html" id="table-content-list">
          <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="toDoDetails"><i class="layui-icon layui-icon-list"></i>执行明细</a>
		  <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="taskLogs"><i class="layui-icon layui-icon-log"></i>查看日志</a>
        </script>
      </div>
    </div>
  </div>

  <script src="/resources/layuiadmin/layui/layui.js"></script>  
  <script>
  layui.config({
    base: '/resources/layuiadmin/', //静态资源所在路径
    version: '92a5f063b93e43d09ee66e2de984d36c'
  }).extend({
    index: 'lib/index', //主入口模块
    taskserverlist: 'deploy/taskserverlist'
  }).use(['index', 'taskserverlist', 'table'], function(){
    var table = layui.table
    ,form = layui.form;
    
    //监听搜索
    form.on('submit(LAY-app-contlist-search)', function(data){
      var field = data.field;
      
      //执行重载
      table.reload('LAY-app-content-list', {
        where: field,
        page: {
		    curr: 1 //重新从第 1 页开始
		  }
      });
    });
  });
  </script>
</body>
</html>    