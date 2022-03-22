<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>Excel异步导入任务明细</title>
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache, must-revalidate">
  <meta http-equiv="expires" content="0">
  
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all">
  <link rel="stylesheet" href="/resources/layuiadmin/style/admin.css" media="all">
</head>
<body>
  <div class="layui-fluid">
    <div class="layui-card">
      <div class="layui-card-body">
        <table id="LAY-app-content-list" lay-filter="LAY-app-content-list"></table> 
		<input type="hidden" id="taskId" value="${taskId}">
      </div>
    </div>
  </div>

  <script src="/resources/layuiadmin/layui/layui.js"></script>  
  <script>
  layui.config({
    base: '/resources/layuiadmin/', //静态资源所在路径
    version: '91c4331729f549358d13c773ab074e21'
  }).extend({
    index: 'lib/index', //主入口模块
    ExcelSyncImportTest: 'mytest/ExcelSyncImportTest'
  }).use(['index', 'ExcelSyncImportTest', 'table'], function(){
    var table = layui.table
    ,form = layui.form;

  });
  </script>
</body>
</html>
    