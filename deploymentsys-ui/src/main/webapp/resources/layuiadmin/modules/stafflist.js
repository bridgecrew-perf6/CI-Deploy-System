/** layuiAdmin.std-v1.2.1 LPPL License By http://www.layui.com/admin/ */
;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;
	i.render({
		elem: "#LAY-app-content-list",
		url: "/staff/listdata",
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				type: "checkbox",
				fixed: "left"
			}, {
				field: "id",
				width: 100,
				title: "ID",
				hide: true
			}, {
				field: "loginName",
				title: "登录账号",
				minWidth: 100
			}, {
				field: "createDate",
				title: "创建时间",
				minWidth: 100
			}, {
				title: "操作",
				minWidth: 150,
				align: "center",
				fixed: "right",
				toolbar: "#table-content-list"
			}]
		],
		page: !0,
		limit: 15,
		limits: [10, 15, 20, 25, 30],
		text: {
		    none: '暂无相关数据' //默认：无数据。
		  }
	}), i.on("tool(LAY-app-content-list)", function(t) {
		var e = t.data;
		var id=e.id;
		if ("del" === t.event) {
			layer.confirm("确定删除此人员？", function(e) {
				//t.del();
				//layer.close(e);
				
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/staff/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: { //设定异步数据接口的额外参数，任意设
					        		  loginName: null
					        		  }
					        		  ,page: {
					        		    curr: 1 //重新从第 1 页开始
					        		  }
					        		});
					        layer.msg('删除成功');
						}else {
							layer.msg(result.msg, {
					            offset: '15px'
					            ,icon: 2
					            ,time: 3000
					          });
						}		
					});
			});
		}
		
		if ("changepwd" === t.event) {
			layer.open({
		          type: 2
		          ,title: '修改人员登录密码'
		          ,content: '/staff/changestaffpwd?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['550px', '350px']
		          ,btn: ['确定', '取消']
		          ,yes: function(index, layero){
		            //点击确认触发 iframe 内容中的按钮提交
		            var submit = layero.find('iframe').contents().find("#layuiadmin-app-form-submit");
		            submit.click();
		          }
		        });
		}
		
		if ("assignRoles" === t.event) {
			layer.open({
		          type: 2
		          ,title: '人员分配角色'
		          ,content: '/staff/staffassignroles?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['550px', '750px']
		          ,btn: ['确定', '取消']
		          ,yes: function(index, layero){
		            //点击确认触发 iframe 内容中的按钮提交
		            var submit = layero.find('iframe').contents().find("#layuiadmin-app-form-submit");
		            submit.click();
		          }
		        });
		}
		
		
	}), t("stafflist", {})
});