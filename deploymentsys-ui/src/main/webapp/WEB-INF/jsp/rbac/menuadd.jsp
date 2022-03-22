<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <title>添加功能</title>
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
    <form lay-filter="formfilter">
 <div class="layui-form" lay-filter="layuiadmin-app-form-list" id="layuiadmin-app-form-list" style="padding: 20px 30px 0 0;">
 <input type="hidden" name="id" value="${menu.id}" />
	<div class="layui-form-item">
		<label class="layui-form-label">功能名称</label>
		<div class="layui-input-inline">
			<input type="text" name="name" value="${menu.name}" placeholder="请输入功能名称" lay-verify="required" autocomplete="off" class="layui-input">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">功能url</label>
		<div class="layui-input-block">
			<input type="text" name="url" value="${menu.url}" placeholder="请输入功能url" lay-verify="required" autocomplete="off" class="layui-input">
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">所属功能组</label>
		<div class="layui-input-inline">			
		<select name="menuGroupId" id="menuGroupId" lay-verify="required" lay-search>                
        </select>  
		</div>
	</div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">排序</label>
		<div class="layui-input-inline">
			<input type="text" name="sort" id="sort" lay-verify="required" value="0" placeholder="请输入排序" class="layui-input">
		</div>
		<div class="layui-form-mid layui-word-aux">数字越大，排序越前</div>
	</div>
	
	<div class="layui-form-item">
      <label class="layui-form-label">是否在左侧菜单显示</label>
      <div class="layui-input-block">
        <input type="radio" name="show" value="1" title="是">
        <input type="radio" name="show" value="0" title="否">
      </div>
    </div>
	
	<div class="layui-form-item">
		<label class="layui-form-label">描述</label>
		<div class="layui-input-inline">
		<textarea name="description" placeholder="请输入描述" class="layui-textarea">${menu.description}</textarea>
		</div>
	</div>
	
	<div class="layui-form-item layui-hide">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-submit" id="layuiadmin-app-form-submit" value="确认添加">
		<input type="button" lay-submit lay-filter="layuiadmin-app-form-edit" id="layuiadmin-app-form-edit" value="确认编辑">
	</div>
	</div>
	</form>

	</div>
</div>

<script id="menuGroupSelectTmpl" type="text/x-jsrender">
<option value="{{:id}}">{{:name}}</option>
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
    var form = layui.form;
    
    $.ajax({
		type: "POST",
		url: "/menugroup/getallmenugroups",
		dataType: "json",
		async: false
	}).done(
	function(result) {
			var tpl = $.templates("#menuGroupSelectTmpl");
			var html = tpl.render(result);
			html='<option value="">请选择，可模糊搜索</option>' + html; //注意，第一个option value不能有值，只能置为""，否则表现效果不是placeholder的效果
			$("select").html(html);

			form.render('select');
	}); 
    
    if ("${menu.show}".length != 0) {
    	$("input[name=show][value=1]").attr("checked", "${menu.show}" == "true" ? true : false);
    	$("input[name=show][value=0]").attr("checked", "${menu.show}" == "false" ? true : false);
	}
    
    if ("${menu.menuGroupId}".length != 0) {
		$('#menuGroupId').val("${menu.menuGroupId}");
		form.render('select');
	}
    
    if ("${menu.sort}".length != 0) {
		$('#sort').val("${menu.sort}");
	}
    
    form.render();
    
    //监听提交
    form.on('submit(layuiadmin-app-form-submit)', function(data){
      data.field.id = 0;
      var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引  
      //提交 Ajax 成功后，关闭当前弹层并重载表格
      $.ajax({
			type: "POST",
			url: "/menu/menuadd",
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
					        	  where: { //设定异步数据接口的额外参数，任意设
					        		  name: null,
					        		  url: null,
					        		  menuGroupId: null
					        		  }
					        		  ,page: {
					        		    curr: 1 //重新从第 1 页开始
					        		  }
					        		}); //重载表格
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