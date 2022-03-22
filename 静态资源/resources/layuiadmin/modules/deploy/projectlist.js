;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;

	i.render({
		elem: "#LAY-app-content-list",
		url: "/deploy/project/listdata",
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
				field: "projectName",
				title: "项目名称",
				width: 240
			}, {
				field: "description",
				title: "描述"
			}, {
				field: "createDate",
				title: "创建时间",
				width: 180
			}, {
				title: "操作",
				width: 270,
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
			layer.confirm("确定删除此项目？", function(e) {
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/deploy/project/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: { //设定异步数据接口的额外参数，任意设					        		  
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
					            ,time: 1000
					          });
						}		
					});
			});
		}
		
		if ("edit" === t.event) {
			layer.open({
		          type: 2
		          ,title: '编辑项目'
		          ,content: '/deploy/project/edit?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['600px', '450px']
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
				  			url: "/deploy/project/edit",
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
						            ,time: 1000
						          });
							}
				  		});
					}), a.trigger("click");
		          }
		        });
			
		}
		
		if ("getProjectVersions" === t.event) {
			var projectVersionsLayer = layer.open({
		          type: 2
		          ,title: '项目版本管理'
		          ,content: '/deploy/projectversion/list?projectId=' + id + '&v='+Math.random()
		          //,maxmin: true
		          ,area: ['1300px', '950px']		          
		        });			
			layer.full(projectVersionsLayer);
		}		
		
	}), t("projectlist", {})
});