<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>配置流程服务器列表</title>
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
			  <a href="javascript:void(0);">${deployAppName}</a>
			  <a href="javascript:void(0);">${deployConfigName}</a>
			  <a href="javascript:void(0);">${deployFlow}</a>
			  <a><cite>配置流程服务器</cite></a>
			</span>
          </div>
        </div>
      </div>

<input type="hidden" id="deployFlowId" value="${deployFlowId}">
<input type="hidden" id="deployAppName" value="${deployAppName}">
      <div class="layui-card-body">
        <div style="padding-bottom: 10px;">
          <button class="layui-btn layuiadmin-btn-list" data-type="add">添加流程服务器</button>
        </div>
        <table id="LAY-app-content-list" lay-filter="LAY-app-content-list"></table> 

        <script type="text/html" id="table-content-list">
          <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="edit"><i class="layui-icon layui-icon-edit"></i>编辑</a>
		  <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="configFlowServerTodo"><i class="layui-icon layui-icon-set-fill"></i>配置服务器执行明细</a>
          <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del"><i class="layui-icon layui-icon-delete"></i>删除</a>
        </script>
      </div>
    </div>
  </div>

  <script src="/resources/layuiadmin/layui/layui.js"></script>  
  <script>
  layui.config({
    base: '/resources/layuiadmin/', //静态资源所在路径
    version: 'c5a9434d142a45c189b6ea936c375a88'
  }).extend({
    index: 'lib/index', //主入口模块
    flowserverlist: 'deploy/flowserverlist'
  }).use(['index', 'flowserverlist', 'table'], function(){
    var table = layui.table
    ,form = layui.form;
    
    var $ = layui.$, active = {
     add: function(){
        layer.open({
          type: 2
          ,title: '添加流程目标服务器'
          ,content: '/deploy/flowserver/add?flowId=' + $("#deployFlowId").val() +'&deployAppName='+ $("#deployAppName").val() +'&v='+Math.random()
          ,maxmin: true
          ,area: ['800px', '550px']
          ,btn: ['确定', '取消']
          ,yes: function(index, layero){
            //点击确认触发 iframe 内容中的按钮提交
            var submit = layero.find('iframe').contents().find("#layuiadmin-app-form-submit");
            submit.click();
          }
        }); 
      }
    }; 

    $('.layui-btn.layuiadmin-btn-list').on('click', function(){
      var type = $(this).data('type');
      active[type] ? active[type].call(this) : '';
    });

  });
  </script>
</body>
</html>    