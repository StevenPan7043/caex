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
			{display:'项目描述',name:'projectName',width:100},
			{display:'项目编码',name:'code',width:150},
			{display:'期数',name:'periods',width:100},
			{display:'发行数量',name:'publishNum',width:90},
			{display:'产出币种',name:'outputCurrency',width:90},
			{display:'单价',name:'price',width:90},
			{display:'认购币种',name:'quoteCurrency',width:90},
			{display:'产出下限',name:'outputFloor',width:100},
			{display:'产出上限',name:'outputUpper',width:60},
			{display:'认购开始时间',name:'saleStartTime',width:110},
			{display:'预计上线时间',name:'runTime',width:110},
			{display:'项目销售状态',name:'saleStatus',width:90,
				render : function(r, n, v) {
					return v == '1' && '待售'
							|| v == '2' && '销售中'
							|| v == '3' && '售罄'
							|| v == '4' && '结束';
				}},
			{display:'运行状态',name:'runStatus',width:90,
				render : function(r, n, v) {
					return v == '1' && '未开始'
							|| v == '2' && '启动'
							|| v == '3' && '停止';
				}},
			{display:'类型',name:'type',width:100,
				render : function(r, n, v) {
					return v == '1' && '算力'
							|| v == '2' && '矿机';
				}},
			{display:'兑换率',name:'exchangeRate',width:60}
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/ipfs/ipfs",
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
			openAddWindow("增加", "${rootPath}/ipfs/ipfs_add", 1000, 700);
			break;
		case "修改":
	        f_common_edit($("#gridBox"), "${rootPath}/ipfs/ipfs_edit?id={id}", false, 1000, 700, "修改");
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
