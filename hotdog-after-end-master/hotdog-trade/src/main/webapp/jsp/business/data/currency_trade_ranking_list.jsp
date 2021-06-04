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
				{display:'币种名称',name:'currency',width:100},
				{display:'开始排名时间', name: 'startdate', width: 120},
				{display:'结束排名时间', name: 'enddate', width: 120},
				{display:'创建时间', name: 'create_time', width: 120},
				{display:'修改时间', name: 'update_time', width: 120},
				{display:'状态',name:'is_open',width:100,render:function(r,n,v){return v=='1'? '已启用' : '未启用';}},
// 				<bp:buttonAndColumnPermission functionId="XTGL-YHGL-CSHMM"/>}
			],
			url: "/trade/get_currency_trade_ranking_data",
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

	
	function dicOper(item){
		console.log(item)
		switch (item.text) {
			case "修改":
		        f_common_edit($("#gridBox"), "/trade/currency_trade_ranking_set?id={id}", false, 950, 400, "修改");
		        break; 
	    }
	}
	
</script>
<%@ include file="../../bottom.jsp"%>
