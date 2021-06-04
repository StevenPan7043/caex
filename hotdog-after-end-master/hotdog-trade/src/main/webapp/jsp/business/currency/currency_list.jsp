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
	+ "{ text: '修改', click: dicOper, icon: 'modify'},"
	+ "{ text: '数据同步', click: dicOper, icon: 'refresh'}]";
$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'代币代码',name:'currency',width:100},
			{display:'代币名称',name:'currency_name',width:150},
			{display:'是否代币',name:'is_coin',width:100,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},	
			{display:'可充值',name:'can_recharge',width:90,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'可提现',name:'can_withdraw',width:90,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'是否展示',name:'is_show',width:90,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'是否otc交易',name:'is_otc',width:90,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'是否充值锁仓',name:'is_lock',width:90,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'小数位',name:'c_precision',width:100},
			{display:'排序',name:'c_order',width:60},
			{display:'地址池预留数量',name:'num',width:110},
			{display:'创建地址数量',name:'createNum',width:110},
			{display:'归集',name:'guijiCron',width:90},
			{display:'回写',name:'rechargeCron',width:90},
			{display:'提现内部划转',name:'can_internal_transfer',width:90,render:function(r,n,v){return v=='1'&&'是'||v=='0'&&'否';}},
			{display:'操作',name:'status',width:120,render:function(r,n,v){return v=='1'&&'启动'||'关闭';}}
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/currency/currency",
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

// 同步交易币种
function syncData() {
    var gm = $("#gridBox").ligerGetGridManager();
    var url="${rootPath}/currency/sync/currency";
    $.reqUrlEx(url, null, function(rst) {
        if (rst && rst.state)gm.loadData(true);
    }, '确定同步数据？');

}
function dicOper(item){
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
        
	switch (item.text) {
		case "新增":
			openAddWindow("增加", "${rootPath}/currency/currency_add", 1000, 700);
			break;
		case "修改":
	        f_common_edit($("#gridBox"), "${rootPath}/currency/currency_edit?id={id}", false, 1000, 700, "修改");
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
