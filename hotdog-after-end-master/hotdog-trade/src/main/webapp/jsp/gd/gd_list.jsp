<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">关键词</div>
		<div class="searchFieldCtr">
			<input id="keyword" name="keyword" type="text" class="txt enterAsSearch"/>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>


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
			{display:'项目Id',name:'id',width:100},
			{display:'算力每日产出币种',name:'outputCurrency',width:120},
			{display:'购买算力的货币',name:'quoteCurrency',width:150},
			{display:'跟单价格',name:'price',width:100},
			{display:'每日产出的下限',name:'outputFloor',width:120},
			{display:'每日产出的上限',name:'outputUpper',width:120},
			{display:'跟单详情描述',name:'particular',width:90},
			{display:'创建时间',name:'createTime',width:110},
			{display:'修改时间',name:'modifyTime',width:110},
			{display:'运行状态',name:'runStatus',width:90,
				render : function(r, n, v) {
					return v == '1' && '未开始'
							|| v == '2' && '启动'
							|| v == '3' && '停止';
				}}
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/gd/queryGdList",
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
			openAddWindow("增加", "${rootPath}/gd/gd_add", 1000, 700);
			break;
		case "修改":
	        f_common_edit($("#gridBox"), "${rootPath}/gd/gd_edit?id={id}", false, 1000, 700, "修改");
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
