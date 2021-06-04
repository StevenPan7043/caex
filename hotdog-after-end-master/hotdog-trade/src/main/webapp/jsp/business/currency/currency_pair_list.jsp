<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">关键词</div>
		<div class="searchFieldCtr">
			<input id="keyword" name="keyword" type="text" class="txt enterAsSearch"  placeholder="如BTC、BTCLTC" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">交易区</div>
		<div class="searchFieldCtr">
			<input id="area_id" name="area_id" type="text" class="txt enterAsSearch"  placeholder="如1、2、3" />
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
	+ "{ text: '修改', click: dicOper, icon: 'modify'},"
	+ "{ text: '数据同步', click: dicOper, icon: 'refresh'}]";
$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'展示名称',name:'dsp_name',width:100},
			{display:'基础货币',name:'base_currency',width:100},
			{display:'计价货币',name:'quote_currency',width:100},	
			{display:'吃单手续费',name:'taker_fee',width:90, align:'right'},
			{display:'挂单手续费',name:'maker_fee',width:90, align:'right'},
			{display:'最低卖出限价',name:'min_price',width:90, align:'right'},
			{display:'最高买入限价',name:'max_price',width:90, align:'right'},
            {display:'涨跌幅是否开启',name:'is_ups_downs_limit',width:90, align:'right',render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
            {display:'涨跌幅比例(%)',name:'ups_downs_limit',width:130, align:'right'},
            {display:'交易区',name:'area_id',width:70},
            {display:'排序',name:'p_order',width:60, align:'right'},
			{display:'交易区第一',name:'is_area_first',width:100,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'开启交易时间',name:'open_time',width:120},
			{display:'状态',name:'p_status_name',width:80},
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/currency/currency_pair",
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

// 同步交易对
function syncData() {
    var gm = $("#gridBox").ligerGetGridManager();
    var url="${rootPath}/currency/sync/currency-pair";
    $.reqUrlEx(url, null, function(rst) {
        if (rst && rst.state)gm.loadData(true);
    }, '确定同步数据？');

}
function dicOper(item){
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
        
	switch (item.text) {
		case "新增":
			openAddWindow("增加", "${rootPath}/currency/currency_pair_add", 950, 600);
			break;
		case "修改":
	        f_common_edit($("#gridBox"), "${rootPath}/currency/currency_pair_edit?id={id}", false, 950, 600, "修改");
	        break;
        case "数据同步":
            syncData();
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
