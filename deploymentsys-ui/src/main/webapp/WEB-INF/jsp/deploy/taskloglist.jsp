<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
 <head> 
  <meta charset="utf-8" /> 
  <title>部署日志列表</title> 
  <meta name="renderer" content="webkit" /> 
  <meta http-equiv="Pragma" content="no-cache" /> 
  <meta http-equiv="cache-control" content="no-cache, must-revalidate" /> 
  <meta http-equiv="expires" content="0" /> 
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0" /> 
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all" /> 
  <link rel="stylesheet" href="/resources/layuiadmin/style/admin.css" media="all" /> 
 </head> 
 <body> 
  <div class="layui-fluid"> 
   <div class="layui-card"> 
    <div class="layui-form layui-card-header layuiadmin-card-header-auto"> 
     <div class="layui-form-item"> 
      <div class="layui-inline"> 
       <input type="checkbox" name="autoRefresh" lay-skin="switch" lay-text="自动刷新|自动刷新" lay-filter="autoRefresh" checked="" /> 
       <input type="hidden" id="taskId" value="${taskId}" /> 
       <input type="hidden" id="taskServerId" value="${taskServerId}" /> 
       <input type="hidden" id="serverTodoId" value="${serverTodoId}" /> 
      </div> 
     </div> 
    </div> 
    <div class="layui-card-body"> 
     <table id="LAY-app-content-list" lay-filter="LAY-app-content-list"></table> 
    </div> 
   </div> 
  </div>  

<script src="/resources/layuiadmin/layui/layui.js"></script>
<script>
	layui.config({
	    base: '/resources/layuiadmin/',  //静态资源所在路径
	    version: 'd47f799779754ecf8c03e8afdeadd1f2'
	}).extend({
	    index: 'lib/index',
	    //主入口模块
	    taskloglist: 'deploy/taskloglist'
	}).use(['index', 'taskloglist', 'table'],
	function() {
	    var table = layui.table,
	    $ = layui.$,
	    form = layui.form;

	    var autoRefresh;
	    if ($("input[name='autoRefresh']").attr("checked") == "checked") {
	        console.log("开启自动刷新");
	        autoRefresh = self.setInterval(function() {
	            //执行重载
	            table.reload('LAY-app-content-list', {
	                page: {
	                    curr: 1 //重新从第 1 页开始
	                }
	            });
	        },
	        2000);
	    }

	    form.on('switch(autoRefresh)',
	    function(data) {
	        if (data.elem.checked) {
	            autoRefresh = self.setInterval(function() {
	                //执行重载
	                table.reload('LAY-app-content-list', {
	                    page: {
	                        curr: 1 //重新从第 1 页开始
	                    }
	                });
	            },
	            2000);
	        } else {
	            autoRefresh = window.clearInterval(autoRefresh);
	        }
	    });

	});
</script>
</body>
</html>
