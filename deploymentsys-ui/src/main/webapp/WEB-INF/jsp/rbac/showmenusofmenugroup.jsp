<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>查看所包含的功能</title>
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache, must-revalidate">
  <meta http-equiv="expires" content="0">
  
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all">
  <link rel="stylesheet" href="/resources/layuiadmin/layui_ext/dtree/dtree.css">
  <link rel="stylesheet" href="/resources/layuiadmin/layui_ext/dtree/font/dtreefont.css">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
 <div class="layui-form" lay-filter="layuiadmin-app-form-list" id="layuiadmin-app-form-list" style="padding: 20px 30px 0 0;">
	<ul id="menusTree" class="dtree" data-id="0"></ul>
	
	<div class="layui-form-item layui-hide">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-submit" id="layuiadmin-app-form-submit" />
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-edit" id="layuiadmin-app-form-edit" />
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
	  dtree: 'layui_ext/dtree/dtree',
	  index: 'lib/index' //主入口模块
  }).use(['dtree', 'index', 'form', 'set'], function(){
	  var $ = layui.$
	  	,dtree = layui.dtree
	    ,form = layui.form;
	    
	    form.render();
	    
	  dtree.render({
		    elem: "#menusTree",
		    url: "/menu/getmenusbymenugroupid",
		    method: "post",
		    request:{menuGroupId:"${menuGroupId}"},
		    dot: false,  // 隐藏小圆点
		    icon: "-1",
		    skin: "layui"
		  });
	  
  });
  </script>
</body>
</html>