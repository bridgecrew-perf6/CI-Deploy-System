<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>添加部署流程</title>
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
 <input type="hidden" name="id" value="${flow.id}" />
<input type="hidden" name="deployConfigId" value="${configId}${flow.deployConfigId}" />	
	<div class="layui-form-item">
      <label class="layui-form-label">流程类型</label>
      <div class="layui-input-block">
        <input type="radio" name="flowType" value="${FlowType_TEST}" title="${FlowType_TEST}" checked>
        <input type="radio" name="flowType" value="${FlowType_FORMAL}" title="${FlowType_FORMAL}">
      </div>
    </div>
    
    <div class="layui-form-item">
      <label class="layui-form-label">服务器执行顺序</label>
      <div class="layui-input-block">
        <input type="radio" name="targetServerOrderType" value="${TargetServerOrderType_CONCURRENCE}" title="${TargetServerOrderType_CONCURRENCE}" checked>
        <input type="radio" name="targetServerOrderType" value="${TargetServerOrderType_QUEUE}" title="${TargetServerOrderType_QUEUE}">
      </div>
    </div>
    
    <div class="layui-form-item">
		<label class="layui-form-label">流程顺序</label>
		<div class="layui-input-inline">
			<input type="text" name="flowOrder" value="0" lay-verify="required" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入流程顺序" class="layui-input">
		</div>
		<div class="layui-form-mid layui-word-aux">默认0，数字越小，越优先执行</div>
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
    
    if ("${flow.flowType}".length != 0) {
    	$("input[name=flowType][value=${FlowType_TEST}]").attr("checked", "${flow.flowType}" == "${FlowType_TEST}" ? true : false);
    	$("input[name=flowType][value=${FlowType_FORMAL}]").attr("checked", "${flow.flowType}" == "${FlowType_FORMAL}" ? true : false);
	}
    if ("${flow.targetServerOrderType}".length != 0) {
    	$("input[name=targetServerOrderType][value=${TargetServerOrderType_CONCURRENCE}]").attr("checked", "${flow.targetServerOrderType}" == "${TargetServerOrderType_CONCURRENCE}" ? true : false);
    	$("input[name=targetServerOrderType][value=${TargetServerOrderType_QUEUE}]").attr("checked", "${flow.targetServerOrderType}" == "${TargetServerOrderType_QUEUE}" ? true : false);
	}
    
    if ("${flow.flowOrder}".length != 0) {
		$('input[name=flowOrder]').val("${flow.flowOrder}");
	}
    
    form.render();
    
    //监听提交
    form.on('submit(layuiadmin-app-form-submit)', function(data){
      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
      data.field.id = 0;
      $.ajax({
			type: "POST",
			url: "/deploy/flow/add",
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
					        		  deployConfigId: '${configId}'
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