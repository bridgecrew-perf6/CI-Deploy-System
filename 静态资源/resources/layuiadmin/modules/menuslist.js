;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;
	i.render({
		elem: "#LAY-app-content-list",
		url: "/menu/listdata",
		method:'post',//本系统ajax异步请求都统一用post请求
		cols: [
			[{
				type: "checkbox",
				fixed: "left"
			}, {
				field: "id",
				title: "ID",
				hide: true
			}, {
				field: "name",
				title: "功能名称",
				width: 300
			}, {
				field: "url",
				title: "功能url",
				width: 580
			}, {
				field: "sort",
				title: "排序",
			}, {
				field: "menuGroupName",
				title: "所属功能组",
			}, {
				field: "show",
				title: "是否在左侧菜单显示",
				templet: "#buttonTpl",
				align: "center"
			}, {
				title: "操作",
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
			layer.confirm("确定删除此功能？", function(e) {
				
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/menu/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: { //设定异步数据接口的额外参数，任意设
					        		  name: null,
					        		  url: null,
					        		  menuGroupId: null
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
		
		if ("editMenu" === t.event) {
			layer.open({
		          type: 2
		          ,title: '编辑功能'
		          ,content: '/menu/editmenu?id=' + id + '&v='+Math.random()
		          ,maxmin: true
		          ,area: ['550px', '550px']
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
				  			url: "/menu/editmenu",
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
				  				        	  where: { //设定异步数据接口的额外参数，任意设
				  				        		  name: null,
				  				        		  url: null,
				  				        		  menuGroupId: null
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
		
	}), t("menuslist", {})
});