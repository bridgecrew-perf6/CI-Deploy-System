<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>修改人员登录密码</title>
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
 <input type="hidden" name="id" value="${id}" />
	<div class="layui-form-item">
		<label class="layui-form-label">登录密码</label>
		<div class="layui-input-inline">
			<input type="password" name="password" lay-verify="pass" autocomplete="off" id="LAY_password" class="layui-input">
		</div>
		<div class="layui-form-mid layui-word-aux">
			6到16个字符
		</div>
	</div>
	<div class="layui-form-item">
		<label class="layui-form-label">确认密码</label>
		<div class="layui-input-inline">
			<input type="password" name="repassword" lay-verify="repass" autocomplete="off" class="layui-input">
		</div>
	</div>
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
    index: 'lib/index' //主入口模块
  }).use(['index', 'form', 'set'], function(){
    var $ = layui.$
    ,form = layui.form;
    
    form.render();
    
    //监听提交
    form.on('submit(layuiadmin-app-form-submit)', function(data){
      var field = data.field; //获取提交的字段
      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
	  console.log(data.field);	
      //提交 Ajax 成功后，关闭当前弹层并重载表格
      $.ajax({
			type: "POST",
			url: "/staff/changestaffpwd",
			data: data.field,
			dataType: "json"
		}).done(
		function(result) {
			console.log(result);
			if (result.error==0) {
				//登入成功的提示与跳转
				          layer.msg('修改成功', {
				            offset: '15px'
				            ,icon: 1
				            ,time: 1000
				          },function(){
				        	  parent.layui.table.reload('LAY-app-content-list'); //重载表格
					          parent.layer.close(index); //再执行关闭 	
				          });				          			
			}else {
                layer.msg(result.msg, {
                    offset: '15px'
                    ,icon: 2
                    ,time: 3000
                  });
            }		
		});
      
    });
  })
  </script>
</body>
</html>