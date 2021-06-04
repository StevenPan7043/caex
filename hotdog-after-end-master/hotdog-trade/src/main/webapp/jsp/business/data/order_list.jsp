<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<select name="table_name" id="table_name" class="sel ww_select required" data="{code:'currency_pair|id'}">
				<option value="">--请选择--</option>
			</select>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">状态</div>
			<div class="searchFieldCtr">
				<select id="o_status" name="o_status" class="sel">
					<option value="">--请选择--</option>
					<option value="watting,partial-done">挂单中</option>
					<option value="watting">未成交</option>
					<option value="partial-done">部分成交未完成</option>
					<option value="done">已成交</option>
					<option value="partial-canceled">部分成交并取消</option>
					<option value="canceled">已取消</option>
				</select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">买卖</div>
			<div class="searchFieldCtr">
				<select id="o_type" name="o_type" class="sel">
					<option value="">--请选择--</option>
					<option value="buy">买</option>
					<option value="sell">卖</option>
				</select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">下单时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${curDateAndReportTime}"/>
			    --
			    <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${tomorrowDateAndReportTime}"/>
			</div>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
		<input onclick="cancelOrder()" type="button" value="撤单" data="{grid:'gridBox',scope:'searchDiv'}" class="btn_a ope_refresh" />
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
var grid,selectDialog,manage,firstLoad = true;
$(function(){
	 
	grid = manage = $("#gridBox").ligerGrid({
		columns: [
			{display:'基础货币',name:'base_currency',width:120},
			{display:'计价货币',name:'quote_currency',width:120},
			{display:'会员账号',name:'m_name',width:120},
			{display:'买卖',name:'o_type',width:80},
			{display:'限价/市价',name:'o_price_type',width:100},
			{display:'价格',name:'price',width:100, align:'right'},
			{display:'数量',name:'volume',width:100, align:'right'},
			{display:'已成交',name:'done_volume',width:100, align:'right'},
			{display:'下单时间',name:'create_time',width:125},
			{display:'操作人IP',name:'oper_ip',width:100},
			{display:'状态',name:'o_status',width:80}
		],
		 
        delayLoad: true,
		checkbox: true,
		rownumbers:true,
		url: "${rootPath}/trade/order",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc'
	});
	console.log(firstLoad)
    if(firstLoad) {
        firstLoad = false;
        loadUserTread();
    }
	$('#btnOK').trigger('click');
});
//撤单
function cancelOrder() {
    var obj = manage.selected;
    if(obj.length <= 0) {
        alert('至少选择一个单号！');
        return;
	}

    $util.sure("您确定撤销该【"+obj.length+"】订单吗？", function() {
        var url = '${rootPath}/trade/order/cancel';
        $.ajax({
            method: 'post',
            url: url,
            dataType: "json",
            contentType:"application/json",
            data: JSON.stringify(obj),
            success: function (res) {
                $.submitShowResult(res);
                try {
                    $hook.opeRefresh("ope_refresh","gridBox","searchDiv");
                } catch (e) {
                    layer.alert(e);
                }
            },
            error: function (res) {
                $.submitShowResult(res);
            }
        })
    });


}
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
    // 状态
    var o_status = $("#o_status option:selected");
    if(!isNull(o_status.val())) {
        params = params + '&o_status=' + o_status.val();
    }
    // 买/卖
    var o_type = $("#o_type option:selected");
    if(!isNull(o_type.val())) {
        params = params + '&o_type=' + o_type.val();
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
        url: "${rootPath}/trade/order/member_order_list"+params,
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