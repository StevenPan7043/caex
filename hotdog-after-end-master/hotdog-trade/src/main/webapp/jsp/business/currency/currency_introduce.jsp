<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>




<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
var grid;
var itemsStr = "[ "
	+ "{ text: '新增', click: dicOper, icon: 'add'},"
	+ "{ text: '修改', click: dicOper, icon: 'modify'}]";
$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'货币代码',name:'currency',width:100},
			{display:'货币名称',name:'name',width:100},
			{display:'官网',name:'site',width:180},
			{display:'发行总量',name:'release_amount',width:90},
			{display:'发行时间',name:'release_time',width:120},
			{display:'创建时间',name:'create_time',width:120},
			{display:'更新时间',name:'update_time',width:120},
			{display:'发行价格',name:'release_price',width:90},
			{display:'流通总量',name:'circulation_amount',width:90},
			{display:'区块查询',name:'block_search',width:180},
			{display:'白皮书',name:'white_paper',width:200},
			{display:'是否显示',name:'is_show',width:60,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/currency/currency_introduce/list",
		method: "get",
		rownumbers: true,
		onContextmenu : function (parm,e)
        {
            menu.show({ top: e.pageY, left: e.pageX });
            return false;
        },
        onDblClickRow : function (data, rowindex, rowobj)
		{
			var item = {text: '修改'};
			dicOper(item);
		},
		delayLoad: false,
		toolbar:{  items: eval(itemsStr)  }
	});
});


function dicOper(item){
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
        
	switch (item.text) {
		case "新增":
			openAddWindow("增加", "${rootPath}/currency/currency_introduce/edit", 1000, 700);
			break;
		case "修改":
            f_common_edit($("#gridBox"), "${rootPath}/currency/currency_introduce/edit?id={id}", false, 1000, 700);
	        break;
    }
}
</script>
