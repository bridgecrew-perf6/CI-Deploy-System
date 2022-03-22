<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>添加流程目标服务器</title>
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
 <input type="hidden" name="id" />
 <input type="hidden" name="flowId" value="${flowId}" />
 <input type="hidden" name="deployAppName" value="${deployAppName}" />
 
	<div class="layui-form-item">
		<label class="layui-form-label">目标服务器ip</label>
		<div class="layui-input-inline">
			<input type="text" name="targetServerIp" id="targetServerIp" readonly="readonly" lay-verify="required" placeholder="请选择目标服务器" class="layui-input">
			<input type="hidden" name="targetServerId" id="targetServerId" />
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">部署执行顺序</label>
		<div class="layui-input-inline">
			<input type="text" name="serverOrder" id="serverOrder" value="0" lay-verify="required" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" placeholder="请输入执行顺序" class="layui-input">
		</div>
		<div class="layui-form-mid layui-word-aux">默认0，数字越小，越优先执行</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">应用部署目录</label>
		<div class="layui-input-inline">
			<input type="text" name="deployDir" lay-verify="required" style="width:550px;" placeholder="请输入应用部署目录" class="layui-input">
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
  <script src="/resources/layuiadmin/layui_ext/tableSelect/tableSelect.js"></script>
  <script>
  layui.config({
    base: '/resources/layuiadmin/', //静态资源所在路径
    version: '20190829'
  }).extend({
    index: 'lib/index' //主入口模块
  }).use(['index', 'form','table', 'set'], function(){
    var $ = layui.$
    ,table = layui.table
    ,form = layui.form;
 
    form.render();
	var tableSelect = layui.tableSelect;
	
	tableSelect.render({
		elem: '#targetServerIp',
		checkedKey: 'id',
		searchKey: 'serverIp',	//搜索输入框的name值 默认keyword
		searchPlaceholder: '输入ip模糊搜索',	//搜索输入框的提示文字 默认关键词搜索
		table: {
			url: "/deploy/targetserver/getlistdata",
			method:'post',//本系统ajax异步请求都统一用post请求
			cols: [[
				{ type: 'radio' },
				, {
					field: "id",
					title: "ID",
					hide: true
				}, {
					field: "serverIp",
					title: "服务器ip"
				}
			]],
			limit: 10,
			limits: [10, 15, 20, 25, 30],
			text: {
			    none: '暂无相关数据' //默认：无数据。
			  }
		},
		done: function (elem, data) {
			var NEWJSON = [];
			var NEWJSON2 = [];
			layui.each(data.data, function (index, item) {
				NEWJSON.push(item.serverIp);
				NEWJSON2.push(item.id);
			});
			
			elem.val(NEWJSON.join(","));
			$("#targetServerId").val(NEWJSON2.join(","));
		}
	});
    
    //监听提交
    form.on('submit(layuiadmin-app-form-submit)', function(data){
      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
      data.field.id = 0;
      $.ajax({
			type: "POST",
			url: "/deploy/flowserver/add",
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
					        		  deployFlowId: '${flowId}'
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
		            ,time: 1600
		          });
			}		
		});
      
    });
  })
  </script>
</body>
</html>