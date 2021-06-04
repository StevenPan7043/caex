<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<select name="table_name" id="table_name" class="sel ww_select required" data="{code:'currency_pair|id'}">
				<option value="">--请选择--</option>
			</select>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">买卖</div>
			<div class="searchFieldCtr">
				<select id="t_type" name="t_type" class="sel">
					<option value="">--请选择--</option>
					<option value="buy">买</option>
					<option value="sell">卖</option>
				</select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">成交时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${curDateAndReportTime}"/>
			    --
			    <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${tomorrowDateAndReportTime}"/>
			</div>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />

		<%-- 用户交易查询--%>
		<input id="btnOK2" type="button" value="交易总数查询" style="width: 150px;" onclick="showUserTrade();" class="btn_a" data="{grid:'gridBox',scope:'searchDiv'}" />

		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
	<div class="main_content" style="width: 65%; float: left;">
		<div id="gridBox"></div>
	</div>
	<div id="userTrade" style="width: 30%; text-align: left; margin:3px; padding:5px; border: 1px solid #D6D6D6; font-size:14px; line-height: 30px; overflow-y: scroll;">
		<div id="gridBox2"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog,firstLoad = true;
$(function(){
	 
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'基础货币',name:'base_currency',width:120},
			{display:'计价货币',name:'quote_currency',width:120},
			{display:'会员账号',name:'m_name',width:120},
			{display:'买卖',name:'t_type',width:80},
			{display:'成交价格',name:'price',width:100, align:'right'},
			{display:'数量',name:'volume',width:100, align:'right'},
			{display:'成交时间',name:'done_time',width:125},
			{display:'吃单方',name:'taker',width:100,render:function(r,n,v){return v=='self'&&'本'||v=='opposite'&&'对方';}},
			{display:'手续费',name:'fee',width:100, align:'right'},
			{display:'手续费单位',name:'fee_currency',width:125, align:'left'}
		],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/trade/trade",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc'
	});
	if(firstLoad) {
	    firstLoad = false;
        loadUserTread();
	}
	$('#btnOK').trigger('click');
});

function commonOpenDialog1(operDesc){
	layer.alert(operDesc);
}
// 会员交易数据
function showUserTrade() {
    if(isNull($("#m_name").val())) {
        layer.alert("请输入会员账号！");
        return;
    }
    var params = '?m_name='+$("#m_name").val();
    // 买/卖
    var t_type = $("#t_type option:selected");
    if(!isNull(t_type.val())) {
        params = params + '&t_type=' + t_type.val();
    }
    // 日期
    if(!isNull($("#startDate").val())) {
        params = params + '&startDate=' + $("#startDate").val();
    }
    if(!isNull($("#endDate").val())) {
        params = params + '&endDate=' + $("#endDate").val();
    }

    loadUserTread(params);
}
// 加载会员交易数据
var loadUserTread = function (params) {
    var grid2 = $("#gridBox2").ligerGrid({
        columns: [
            {display:'会员账号',name:'m_name',width:120},
            {display:'交易对',name:'table_name',width:120},
            {display:'交易数量',name:'count',width:120},
        ],

        delayLoad: true,
        rownumbers:true,
        url: "${rootPath}/trade/member_trade_list"+params,
        method: "get",
    });
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