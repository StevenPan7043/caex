<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
<div class="main_content">
	<div id="gridBox" style="margin: 0; padding: 0"></div>
</div>
<script type="text/javascript">
	var grid;
	var itemsStr = "[{ text: '修改', click: dicOper, icon: 'modify'}]";
	$(function () {
		menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
		grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'交易对名称',name:'keyname',width:100},
// 				{display:'开启交易时间',name:'opentime',width:120},
				{display:'开始排名时间', name: 'startdate', width: 120},
				{display:'结束排名时间', name: 'enddate', width: 120},
				{display:'状态',name:'isopen',width:100,render:function(r,n,v){return v=='1'? '已启用' : '未启用';}},	
// 				{display:'是否开启',width:90,render:
// 					function(row){
// 						var html = '<a href=\'javascript:void(0);\' onclick="isopen('+row.dcpid+','+row.ttrsid+','+row.isopen+');">是否开启</a>';
// 						if(row.isopen == 1){
// 							html = '<a href=\'javascript:void(0);\' onclick="isopen('+row.dcpid+','+row.ttrsid+','+row.isopen+');">是否关闭</a>';
// 						}else{
// 							html = '<a href=\'javascript:void(0);\' onclick="isopen('+row.dcpid+','+row.ttrsid+','+row.isopen+');">是否开启</a>';
// 						}
// 						return html;
// 					}
// 				<bp:buttonAndColumnPermission functionId="XTGL-YHGL-CSHMM"/>}
			],
			url: "${rootPath}/trade/set_trade_ranking_data",
			method: "get",
			rownumbers: true,
			sortName: 'id',
		   	sortOrder: 'asc',
		   	onDblClickRow : function (data, rowindex, rowobj){
				var item = {text: '修改'};
				dicOper(item);
			},
		   	isSort: false,
		   	autoRefresh:false,
		   	toolbar:{  items: eval(itemsStr)  }
		});
	});
	
// 	function isopen(dcpid,ttrsid,state) {
// 		$.ligerDialog.confirm('确定开启交易排名吗？', function (yes) {
// 			if(yes) {
// 				var param = {
// 						dcpid:dcpid,
// 						ttrsid:ttrsid,
// 						isopen:state
// 				};
// 				$.post("/trade/set_trade_ranking_isopen",param,function(data){
// 					if(data == 1){
// 						$.ligerDialog.success('开启成功!');
// 						location.reload();
// 					}else{
// 						$.ligerDialog.error('开启失败!请联系管理员!');
// 					}
// 				});
// 			}
// 		});
// 	}
	
	function dicOper(item){
		console.log(item)
		switch (item.text) {
			case "修改":
		        f_common_edit($("#gridBox"), "${rootPath}/trade/trade_ranking_edit?dcpid={dcpid}", false, 950, 400, "修改");
		        break; 
	    }
	}
	
</script>
<%@ include file="../../bottom.jsp"%>
