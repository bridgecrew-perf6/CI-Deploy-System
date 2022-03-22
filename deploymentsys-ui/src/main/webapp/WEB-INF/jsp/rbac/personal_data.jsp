<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
    
<!DOCTYPE html>
<html>
 <head> 
  <meta charset="utf-8" /> 
  <title>修改个人资料</title> 
  <meta http-equiv="Pragma" content="no-cache" /> 
  <meta http-equiv="cache-control" content="no-cache, must-revalidate" /> 
  <meta http-equiv="expires" content="0" /> 
  <meta name="renderer" content="webkit" /> 
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" /> 
  <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=0" /> 
  <link rel="stylesheet" href="/resources/layuiadmin/layui/css/layui.css" media="all" /> 
  <link rel="stylesheet" href="/resources/layuiadmin/style/admin.css" media="all" /> 
 </head> 
 <body> 
  <div class="layui-fluid"> 
   <div class="layui-row layui-col-space15"> 
    <div class="layui-col-md12"> 
     <div class="layui-card"> 
      <div class="layui-card-header">
       个人资料
      </div> 
      <div class="layui-card-body" pad15=""> 
       <div class="layui-form" lay-filter=""> 
        <div class="layui-form-item"> 
         <label class="layui-form-label">真实姓名</label> 
         <div class="layui-input-inline"> 
          <input type="text" name="trueName" lay-verify="required" value="${staff.trueName}" class="layui-input" /> 
          <input type="hidden" name="id" value="${staff.id}" />
         </div> 
        </div> 
        <div class="layui-form-item"> 
         <label class="layui-form-label">出生年月</label> 
         <div class="layui-input-inline"> 
          <input type="text" name="birthday" id="birthday" value="" lay-verify="required" class="layui-input" /> 
         </div>        
        </div> 
        <div class="layui-form-item">
    <label class="layui-form-label">个人照片</label>
    <div class="layui-input-inline uploadHeadImage">
        <div class="layui-upload-drag" id="headImg">
            <i class="layui-icon"></i>
            <p>点击上传图片，或将图片拖拽到此处</p>
        </div>
    </div>
    <div class="layui-input-inline">
        <div class="layui-upload-list">
            <img class="layui-upload-img headImage" style="width:200px;height:200px;" src="${staff.virtualPhotoUrl}" id="demo1" />
            <input type="hidden" name="localPhotoPath" value="${staff.localPhotoPath}" />
            <input type="hidden" name="virtualPhotoUrl" value="${staff.virtualPhotoUrl}" />
            <p id="demoText"></p>
        </div>
    </div>
</div> 
        <div class="layui-form-item"> 
         <div class="layui-input-block"> 
          <button class="layui-btn" lay-submit="" lay-filter="setmydata">确认修改</button>
         </div> 
        </div> 
       </div> 
      </div> 
     </div> 
    </div> 
   </div> 
  </div> 
  <script src="/resources/layuiadmin/layui/layui.js"></script> 
  <script>
  layui.config({
	    base: '/resources/layuiadmin/',
	    //静态资源所在路径
	    version: '4411997116ac4315b23cbbc88b2613f8'
	}).extend({
	    index: 'lib/index' //主入口模块
	}).use(['index', 'set', 'laydate', 'upload', "layer", "element"],
	function() {
	    var $ = layui.$,
	    laydate = layui.laydate,
	    element = layui.element,
	    layer = layui.layer,
	    upload = layui.upload,
	    form = layui.form; 
	    
	    if ('${staff.birthday}' != '') {
	    	$("#birthday").val('${staff.birthday}'.substring(0,7));
		}

	    //执行一个laydate实例
	    laydate.render({
	        elem: '#birthday',
	        //指定元素
	        //format: 'yyyy-MM-dd HH:mm:ss',
	        format: 'yyyy-MM',
	        type: 'month'
	    });

	    form.render();
	    
	    var loadingLayer;
	    //拖拽上传
	    var uploadInst = upload.render({
	        elem: '#headImg',
	        url: '/upload/uploadimage',
	        accept: 'images',
	        acceptMime: 'image/*',
	        auto: true,
	        //bindAction: 'button[lay-filter="setmypass"]',
	        size: 512,
	        choose: function(obj) {
	            //预读本地文件示例，不支持ie8
	            //console.log("进来choose");
	            obj.preview(function(index, file, result) {
	                $('#demo1').attr('src', result); //图片链接（base64）
	            });
	        },
	        before: function(obj) {
	            //console.log("进来before了");
	            //console.log(obj);
	        	loadingLayer = layer.load(2, {
					  shade: [0.1,'#000']
				});
	        },
	        done: function(res) {
	        	layer.close(loadingLayer); //关闭loading
	        	//console.log("res: "+res);
	            //如果上传失败
	            if (res.error > 0 || res.code > 0) {
	                layer.msg(res.msg);
	            } else {
	            	$("input[name='localPhotoPath']").val(res.data.localPhotoPath);
	            	$("input[name='virtualPhotoUrl']").val(res.data.virtualPhotoUrl);
	                //layer.msg('更新成功');
	            }
	            //上传成功
	            //打印后台传回的地址: 把地址放入一个隐藏的input中, 和表单一起提交到后台, 此处略..
	        },
	        error: function() {
	        	layer.close(loadingLayer); //关闭loading
	            //演示失败状态，并实现重传
	            var demoText = $('#demoText');
	            demoText.html('<span style="color: #FF5722;">上传失败</span> <a class="layui-btn layui-btn-mini demo-reload">重试</a>');
	            demoText.find('.demo-reload').on('click',
	            function() {
	                uploadInst.upload();
	            });
	        }
	    });
	    element.init();

	    //提交
	    form.on('submit(setmydata)',
	    function(obj) {
	        $.ajax({
	            type: "POST",
	            url: "/staff/personaldata",
	            data: obj.field,
	            dataType: "json"
	        }).done(function(result) {
	            if (result.error == 0) {
	                //登入成功的提示与跳转
	                layer.msg('修改成功', {
	                    offset: '15px',
	                    icon: 1,
	                    time: 1000
	                });
	            } else {
	                layer.msg(result.msg, {
	                    offset: '15px',
	                    icon: 2,
	                    time: 1000
	                });
	            }
	        });
	    });
	});
  </script>  
 </body>
</html>