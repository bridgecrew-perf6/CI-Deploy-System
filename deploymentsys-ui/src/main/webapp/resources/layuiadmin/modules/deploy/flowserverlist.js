;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;

	var deployFlowId=$("#deployFlowId").val();

	i.render({
		elem: "#LAY-app-content-list",
		url: "/deploy/flowserver/listdata",
		where: {deployFlowId: deployFlowId},
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				field: "id",
				title: "ID",
				hide: true
			}, {
				field: "targetServerIp",
				title: "目标服务器ip",
			}, {
				field: "targetServerPort",
				title: "端口号",
				width: 100
			}, {
				field: "serverOrder",
				title: "部署执行顺序",
				width: 120
			}, {
				field: "deployDir",
				title: "应用部署目录",
				width: 500
			}, {
				title: "操作",
				align: "center",
				fixed: "right",
				toolbar: "#table-content-list"
			}]
		],
		page: true,
		limit: 10,
		limits: [10, 15, 20, 25, 30],
		text: {
		    none: '暂无相关数据' //默认：无数据。
		  }
	}), i.on("tool(LAY-app-content-list)", function(t) {
		var e = t.data;
		var id=e.id;
		if ("del" === t.event) {
			layer.confirm("确定删除此服务器？", function(e) {
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/deploy/flowserver/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: { //设定异步数据接口的额外参数，任意设
					        		  deployFlowId: deployFlowId
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
		
		if ("edit" === t.event) {
			layer.open({
		          type: 2
		          ,title: '编辑流程目标服务器'
		          ,content: '/deploy/flowserver/edit?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['800px', '250px']
		          ,btn: ['确定', '取消']
		          ,yes: function(index, layero){
		        	  var editiframe = window["layui-layer-iframe" + index],
						a = layero.find("iframe").contents().find("#layuiadmin-app-form-edit");
		        	  editiframe.layui.form.on("submit(layuiadmin-app-form-edit)", function(i) {
						$.ajax({
				  			type: "POST",
				  			url: "/deploy/flowserver/edit",
				  			data: i.field,
				  			dataType: "json"
				  		}).done(
				  		function(result) {
				  			if (result.error==0) {
				  				layer.msg('编辑成功', {
				  				            offset: '15px'
				  				            ,icon: 1
				  				            ,time: 1000
				  				          },function(){
				  				        	layui.table.reload('LAY-app-content-list', {
									        	  where: {
									        		  deployFlowId: deployFlowId
									        		  }
									        		  ,page: {
									        		    curr: 1 //重新从第 1 页开始
									        		  }
									        		});
				  				        	layer.close(index); //再执行关闭
				  				          });
				  			}else {
								layer.msg(result.msg, {
						            offset: '15px'
						            ,icon: 2
						            ,time: 3000
						          });
							}
				  		});
					}), a.trigger("click");
		          }
		        });
			
		}
		
		if ("configFlowServerTodo" === t.event) {
			var configFlowServerTodoLayer = layer.open({
		          type: 2
		          ,title: '配置服务器执行明细'
		          ,content: '/deploy/flowservertodo/list?flowServerId=' + id + '&v='+Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']
		        });			
			layer.full(configFlowServerTodoLayer);
		}
		
		
	}), t("flowserverlist", {})
});