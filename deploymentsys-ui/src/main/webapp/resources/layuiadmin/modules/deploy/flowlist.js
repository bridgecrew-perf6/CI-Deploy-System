;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;

	var deployConfigId=$("#deployConfigId").val();

	i.render({
		elem: "#LAY-app-content-list",
		url: "/deploy/flow/listdata",
		where: {deployConfigId: deployConfigId},
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				field: "id",				
				title: "ID",
				hide: true
			}, {
				field: "flowType",
				title: "流程类型",
			}, {
				field: "flowOrder",
				title: "流程顺序",
			}, {
				field: "targetServerOrderType",
				title: "目标服务器执行顺序",
			}, {
				field: "deployConfigName",
				title: "所属配置",
			}, {
				field: "deployAppName",
				title: "所属应用",
				width: 400
			}, {
				title: "操作",
				width: 400,
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
			layer.confirm("确定删除此数据？", function(e) {
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/deploy/flow/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: { //设定异步数据接口的额外参数，任意设
					        		  deployConfigId: deployConfigId
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
		          ,title: '编辑部署流程'
		          ,content: '/deploy/flow/edit?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['650px', '450px']
		          ,btn: ['确定', '取消']
		          ,yes: function(index, layero){
		        	  var editiframe = window["layui-layer-iframe" + index],
						a = layero.find("iframe").contents().find("#layuiadmin-app-form-edit");
		        	  editiframe.layui.form.on("submit(layuiadmin-app-form-edit)", function(i) {
//						t.update({
//							name: i.field.name,
//							show: i.field.show
//						});
						
						$.ajax({
				  			type: "POST",
				  			url: "/deploy/flow/edit",
				  			data: i.field,
				  			dataType: "json"
				  		}).done(
				  		function(result) {
				  			console.log(result);
				  			if (result.error==0) {
				  				layer.msg('编辑成功', {
				  				            offset: '15px'
				  				            ,icon: 1
				  				            ,time: 1000
				  				          },function(){
				  				        	layui.table.reload('LAY-app-content-list', {
									        	  where: {
									        		  deployConfigId: deployConfigId
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
		
		if ("configFlowServers" === t.event) {
			var flowServersLayer = layer.open({
		          type: 2
		          ,title: '配置流程服务器'
		          ,content: '/deploy/flowserver/list?deployFlowId=' + id + '&v='+Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(flowServersLayer);
		}
		
		
	}), t("flowlist", {})
});