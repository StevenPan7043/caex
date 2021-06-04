<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>

	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">项目方名称</div>
			<div class="searchFieldCtr">
				<input  id="c_name" name="c_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
	    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
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
				{display:'服务端apiKey',name:'s_apiKey',width:400},
				{display:'服务端secretKey',name:'s_secretKey',width:250},
				{display:'是否可用',name:'c_flag',width:100,render:function(r,n,v){return v=='0'&&'已启用'||v=='1'&&'未启用';}},
				{display:'客户端回调地址',name:'c_ip',width:140},
				{display:'客户端appKey',name:'c_appKey',width:140},
				{display:'项目方名称',name:'c_name',width:140},
				{display:'项目方批次',name:'c_name_type',width:140},
				{display:'可充值币种',name:'can_deposit_currency',width:140},
				{display:'可提币币种',name:'can_withdraw_currency',width:140},
				{display:'客户端成功状态码',name:'c_code',width:140},
				{display:'项目方UID',name:'ext',width:140},
				{display:'创建时间',name:'create_time',width:140},
				{display:'更新时间',name:'update_time',width:140}
			],
			url: "${rootPath}/api/listThirdParty",
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
			toolbar:{  items: eval(itemsStr)  },
			sortName: 'id',
		   	sortOrder: 'asc',
		   	isSort: false,
		   	autoRefresh:false
		});
	});
  
	function dicOper(item){
		switch (item.text) {
			case "增加":
				openAddWindow("增加", "${rootPath}/api/toAddThirdParty", 750, 320);
				break;
			case "修改":
		         f_common_edit($("#gridBox"), "${rootPath}/api/toAddThirdParty?id={id}", false, 750, 320);
		        break;
	        case "删除":
	        	f_common_del($("#gridBox"), "${rootPath}/api/delThirdParty?id={id}", false);
	            break;
	    }
	}
    /**
     * 清空条件查询选项
     */
    function btnClearInput() {
        $("#searchDiv").find("option:selected").each(function () {
            this.selected = false;
        })
        $("#searchDiv input[type=text]").each(function () {
            this.value = null;
        })

    }
</script>
<%@ include file="../../bottom.jsp"%>
