;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;

	i.render({
		elem: "#LAY-app-content-list",
		url: "/deploy/config/listdata",
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
				field: "configName",
				title: "配置名称",
				minWidth: 100
			}, {
				field: "appName",
				title: "所属应用",
				minWidth: 100
			}, {
				field: "description",
				title: "描述",
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
		page: true,
		limit: 15,
		limits: [10, 15, 20, 25, 30],
		text: {
		    none: '暂无相关数据' //默认：无数据。
		  }
	}), i.on("tool(LAY-app-content-list)", function(t) {
		var e = t.data;
		var id=e.id;
		if ("del" === t.event) {
			layer.confirm("确定删除此配置？", function(e) {
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/deploy/config/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: { //设定异步数据接口的额外参数，任意设
					        		  appId: $("#appId").val()
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
		          ,title: '编辑部署配置'
		          ,content: '/deploy/config/edit?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['550px', '450px']
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
				  			url: "/deploy/config/edit",
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
									        		  appId: $("#appId").val()
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
		
		if ("configDeployFlows" === t.event) {
			var configDeployFlowsLayer = layer.open({
		          type: 2
		          ,title: '配置部署流程'
		          ,content: '/deploy/flow/list?deployConfigId=' + id + '&v='+Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(configDeployFlowsLayer);
		}		
		
	}), t("configlist", {})
});