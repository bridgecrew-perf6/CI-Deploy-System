<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>添加目标服务器todo</title>
  <meta http-equiv="Pragma" content="no-cache" />
  <meta http-equiv="cache-control" content="no-cache, must-revalidate">
  <meta http-equiv="expires" content="0">
  
  <meta name="renderer" content="webkit">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0">
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all">
</head>
<body>
<div class="layui-fluid">
    <div class="layui-row layui-col-space15">
 <div class="layui-form" lay-filter="layuiadmin-app-form-list" id="layuiadmin-app-form-list" style="padding: 20px 30px 0 0;">
 <input type="hidden" name="id" value="${todo.id}" />
 <input type="hidden" name="flowServerId" value="${flowServerId}${todo.flowServerId}" />
	<div class="layui-form-item">
		<label class="layui-form-label">执行类型</label>
		<div class="layui-input-inline">			
		<select name="todoType" id="todoType" lay-verify="required">                
        </select>  
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">执行顺序</label>
		<div class="layui-input-inline">
			<input type="text" name="todoOrder" id="todoOrder" value="${todo.todoOrder}" lay-verify="required" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入执行顺序" class="layui-input">
		</div>
		<div class="layui-form-mid layui-word-aux">数字越小，越优先执行</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">参数1</label>
		<div class="layui-input-inline">
			<input type="text" name="param1" value="${todo.param1}" style="width:560px;" placeholder="至少要输入一个参数" lay-verify="required" autocomplete="off" class="layui-input">
		</div>
	</div>    
	<div class="layui-form-item">
		<label class="layui-form-label">参数2</label>
		<div class="layui-input-inline">
			<input type="text" name="param2" value="${todo.param2}" style="width:560px;" autocomplete="off" class="layui-input">
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">参数3</label>
		<div class="layui-input-inline">
			<input type="text" name="param3" value="${todo.param3}" style="width:560px;" autocomplete="off" class="layui-input">
		</div>
	</div>
	
	<div class="layui-form-item layui-hide">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-submit" id="layuiadmin-app-form-submit" value="确认添加">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-edit" id="layuiadmin-app-form-edit" value="确认编辑">
	</div>
</div>
	</div>
</div>

<script id="todoTypeSelectTmpl" type="text/x-jsrender">
<option value="{{:toDoType}}">{{:toDoType}}</option>
</script>
<script src="/resources/script/jquery-3.3.1.js"></script>
  <script src="/resources/layuiadmin/layui/layui.js"></script>  
  <script src="/resources/script/jsrender.js"></script>  
  <script>
  layui.config({
    base: '/resources/layuiadmin/', //静态资源所在路径
    version: '20190829'
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['index', 'form', 'set'], function(){
    var $ = layui.$
    ,form = layui.form;
    
    var tpl = $.templates("#todoTypeSelectTmpl");
	var html = tpl.render($.parseJSON('${toDoTypes}'));
	html='<option value="">请选择</option>' + html; //注意，第一个option value不能有值，只能置为""，否则表现效果不是placeholder的效果
	$("select").html(html);

	form.render('select');    
    form.render();
    
    if ("${todo.todoType}".length != 0) {
		$('#todoType').val("${todo.todoType}");
		form.render('select');
	}
    
    //监听提交
    form.on('submit(layuiadmin-app-form-submit)', function(data){
      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
      data.field.id = 0;
      $.ajax({
			type: "POST",
			url: "/deploy/flowservertodo/add",
			data: data.field,
			dataType: "json"
		}).done(
		function(result) {
			if (result.error==0) {
				//登入成功的提示与跳转
				          layer.msg('添加成功', {
				            offset: '15px'
				            ,icon: 1
				            ,time: 1000
				          },function(){
				        	  parent.layui.table.reload('LAY-app-content-list', {
					        	  where: {
					        		  flowServerId: '${flowServerId}'
					        		  }
					        		  ,page: {
					        		    curr: 1 //重新从第 1 页开始
					        		  }
					        		});
					          parent.layer.close(index); //再执行关闭 	
				          });				          			
			}else {
				layer.msg(result.msg, {
		            offset: '15px'
		            ,icon: 2
		            ,time: 1000
		          });
			}		
		});
      
    });
  })
  </script>
</body>
</html>