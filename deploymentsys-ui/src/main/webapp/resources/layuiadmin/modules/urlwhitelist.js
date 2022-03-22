;
layui.define(["table", "form"], function(t) {
	var $ = layui.$,
		i = layui.table,
		n = layui.form;
	i.render({
		elem: "#LAY-app-content-list",
		url: "/urlwhitelist/listdata",
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
				field: "url",
				title: "url"
			}, {
				field: "description",
				title: "描述"
			}, {
				title: "操作",
				align: "center",
				fixed: "right",
				width: 300,
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
			layer.confirm("确定删除此数据？", function(e) {
				
				var arrayIds = new Array();
		        arrayIds.push(id);
		          
		          $.ajax({
						type: "POST",
						url: "/urlwhitelist/delete",
						data: {ids: arrayIds},
						dataType: "json"
					}).done(
					function(result) {
						if (result.error == 0) {
							//执行 Ajax 后重载         
							i.reload('LAY-app-content-list', {
					        	  where: {
					        		  url: null
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
		
	}), t("urlwhitelist", {})
});