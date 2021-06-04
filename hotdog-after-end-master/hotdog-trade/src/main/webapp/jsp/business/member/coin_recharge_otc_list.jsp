<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<input id="OTC" name="OTC" type="hidden" value="${OTC }" class="txt enterAsSearch" />
	<input id="otc_owner_id" name="otc_owner_id" type="hidden" value="${otc_owner_id }" class="txt enterAsSearch" />
	<div class="searchField">
		<div class="searchFieldLbl">币种</div>
		<div class="searchFieldCtr">
			<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input id="id" name="id" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">地址</div>
		<div class="searchFieldCtr">
			<input id="_r_address" name="_r_address" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="r_status" name="r_status" class="sel">
				<option value="">--状态--</option>
				<option value="-1">待付款[OTC]</option>
				<option value="0">未确认</option>
				<option value="1">已确认</option>
				<option value="2">已取消</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="r_address" name="r_address" class="sel">
				<option value="">--来源--</option>
				<option value="INVITE_REWARD">推荐奖励</option>
				<option value="MAN_RECHARGE">平台人工充值</option>
				<option value="REG_REWARD">注册奖励</option>
				<option value="SYS_REWARD">系统奖励</option>
				<option value="TRADE_RANKING_REWARD">交易排名奖励</option>
				<option value="TRADE_RETURN_FEE_REWARD">交易返还手续费</option>
			</select>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
	
<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
var grid;
var itemsStr = "[ ";

if ("${OTC }" == 0) {
	itemsStr += "{ text: '手动充值', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='CZTX-XNBCZ-SDCZ'/>},";	
}
if ("${OTC }" == "1") {
	itemsStr += "{ text: '确认入账', click: dicOper, icon: 'ok' <bp:buttonAndColumnPermission functionId='JYOTC-XNBCZ-SDCZ'/>}]";
} else {
	itemsStr += "{ text: '确认入账', click: dicOper, icon: 'ok' <bp:buttonAndColumnPermission functionId='CZTX-XNBCZ-SDCZ'/>}]";
}

$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'虚拟币',name:'currency',width:80},
			{display:'UID',name:'member_id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
			{display:'会员账号',name:'m_name',width:150},
			{display:'充值数量',name:'r_amount',width:80, align:'right'},
			{display:'充值时间',name:'r_create_time',width:125},
			// r: 所有字段，n：第几条，v：该字段的值
			{display:'我方地址',name:'r_address',width:330,align: 'left',render:function (r,n,v) {
                var tmp = v.split('→');
                var addr = '';
                for(var i=0; i<tmp.length; i++) {
                    if(tmp[i]) {
                        var info = tmp[i].split('▲');
                        if(info[0] == 'bank') {
                            addr = '支付类型：'+ info[0] + '<br>卡号：' + info[2] + '<br>姓名：' + info[1] + '<br>银行：' + info[3];
                        }else if(info[0] == 'alipay' ){
                            addr = '支付类型：'+ info[0] + '<br>卡号：' + info[2] + '<br>姓名：' + info[1] + '<br>支付宝';
                        }else if(info[0] == 'wxpay') {
                            addr = '支付类型：'+ info[0] + '<br>卡号：' + info[2] + '<br>姓名：' + info[1] + '<br>微信';
						}
                    }
                }
                if(addr == ''){
                    return v;
				}else {
                    return addr;
				}
            }},
			//{display:'确认数',name:'r_confirmations',width:70},
			{display:'来源',name:'r_address',width:80,render:function(r,n,v){return v=='INVITE_REWARD'&&'推荐奖励'||v=='MAN_RECHARGE'&&'平台人工充值'||v=='REG_REWARD'&&'注册奖励'||v=='SYS_REWARD'&&'系统奖励'||'币充值';}},
			{display:'状态',name:'r_status',width:70,render:function(r,n,v){return (v=='-1'&&'待付款'||v=='1'&&'已确认'||v=='0'&&'未确认'||v=='2'&&'已取消') + ("<br><span style='color: blue;' title='" + (v=='2'?r.reject_reason:'') + "'>" + (v=='2'?r.reject_reason:'') + "</span>");}},
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/member/coin_recharge",
		method: "get",
		rownumbers: true,
		onContextmenu : function (parm,e)
        {
            menu.show({ top: e.pageY, left: e.pageX });
            return false;
        },
        onDblClickRow : function (data, rowindex, rowobj)
		{
			var item = {text: '确认入账'};
			<bp:displayPermission functionId='CZTX-XNBCZ-SDCZ'>dicOper(item);</bp:displayPermission>
		},
		delayLoad: true,
		toolbar:{  items: eval(itemsStr)  }
	});
      
	$('#btnOK').trigger('click');
});

function dicOper(item){
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
        
	switch (item.text) {
		case "手动充值":
			openAddWindow("手动充值", "${rootPath}/member/coin_recharge_add", 750, 500);
			break;
		case "确认入账":
			if (row.r_status != "0") {
				//layer.alert("只有未确认的订单才可以确认");
				//return;
			}
			f_common_edit($("#gridBox"), "${rootPath}/member/coin_recharge_add?id={id}", false, 750, 620, "确认入账");
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
