<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>
<div class="main_content">
	<div id="gridBox" style="margin:0; padding:0"></div>
</div>

<script type="text/javascript">
	var grid;
	var itemsStr = "[ "
	+ "{ text: '增加', click: dicOper, icon: 'add'},"
	+ "{ text: '修改', click: dicOper, icon: 'modify'},"
	+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	$(function () {
		  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
	      grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'栏目名',name:'c_name',width:250},	
				{display:'父栏目',name:'c_parent_name',width:150},
				{display:'栏目排序',name:'c_order',width:80},
				{display:'是否启用',name:'is_use',width:80,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}}
			],
			sortName: 'c_order',
		   	sortOrder: 'asc',
			url: "${rootPath}/cms/listColumn",
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
			toolbar:{  items: eval(itemsStr)  }
		});
	});
 
	function dicOper(item){
		switch (item.text) {
			case "增加":
				openAddWindow("增加", "${rootPath}/cms/toAddColumn", 850, 430);
				break;
	        
			case "修改":
		        f_common_edit($("#gridBox"), "${rootPath}/cms/toEditColumn?id={id}", false, 850, 430);
		        break;
		        
	        case "删除":
	        	f_common_del($("#gridBox"), "${rootPath}/cms/delColumn?id={id}", false);
	            break;
	    }
	}
</script>
