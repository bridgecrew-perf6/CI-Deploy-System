<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>编辑流程目标服务器</title>
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache, must-revalidate">
  <meta http-equiv="expires" content="0">
  
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all">
   <style>
  .layui-form-label {
    width: 100px;
}
  </style>
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
 <div class="layui-form" lay-filter="layuiadmin-app-form-list" id="layuiadmin-app-form-list" style="padding: 20px 30px 0 0;">
 <input type="hidden" name="id" value="${flowServer.id}" />
	
	<div class="layui-form-item">
		<label class="layui-form-label">部署执行顺序</label>
		<div class="layui-input-inline">
			<input type="text" name="serverOrder" value="${flowServer.serverOrder}" lay-verify="required" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入执行顺序" class="layui-input">
		</div>
		<div class="layui-form-mid layui-word-aux">数字越小，越优先执行</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">应用部署目录</label>
		<div class="layui-input-inline">
			<input type="text" name="deployDir" value="${flowServer.deployDir}" style="width:550px;" lay-verify="required" placeholder="请输入应用部署目录" class="layui-input">
		</div>
	</div>
	
	<div class="layui-form-item layui-hide">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-submit" id="layuiadmin-app-form-submit" value="确认添加">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-edit" id="layuiadmin-app-form-edit" value="确认编辑">
	</div>
</div>
	</div>
</div>

  <script src="/resources/layuiadmin/layui/layui.js"></script>  
  <script>
  layui.config({
    base: '/resources/layuiadmin/', //静态资源所在路径
    version: '20190829'
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['index', 'form', 'set'], function(){
    var $ = layui.$
    ,form = layui.form;
  
    form.render();    
   
  });
  </script>
</body>
</html>