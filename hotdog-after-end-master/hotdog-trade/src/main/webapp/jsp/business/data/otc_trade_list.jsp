<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text"
					class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">订单号</div>
			<div class="searchFieldCtr">
				<input id="tNumber" name="tNumber" type="text"
					class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">基础货币</div>
			<div class="searchFieldCtr">
				<input id="baseCurrency" name="baseCurrency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchFieldLbl">买卖</div>
		<div class="searchFieldCtr">
			<select id="o_type" name="tType" class="sel">
				<option value="">--请选择--</option>
				<option value="BUY">买</option>
				<option value="SELL">卖</option>
			</select>
		</div>
		<div class="searchFieldLbl">状态</div>
		<div class="searchFieldCtr">
			<select id="status" name="status" class="sel">
				<option value="">--请选择--</option>
				<option value="NP">待支付</option>
				<option value="UNCONFIRMED">待确认</option>
				<option value="DONE">已完成</option>
				<option value="CANCELED">撤销</option>
				<option value="COMPLAINING">申诉</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">下单时间</div>
		<div class="searchFieldCtr">
			<input type="text" name="create_start" id="create_start"
				maxlength="16" readonly="readonly" class="txt txt_datetime required"
				value="${curDateAndReportTime}" /> -- <input type="text"
				name="create_end" id="create_end" maxlength="16" readonly="readonly"
				class="txt txt_datetime required"
				value="${tomorrowDateAndReportTime}" />
		</div>
	</div>
	<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search"
		data="{grid:'gridBox',scope:'searchDiv'}" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content" style="width: 100%; float: left;">
	<div id="gridBox"></div>
</div>

<script type="text/javascript">
	var grid, selectDialog, manage;
	$(function() {

		grid = manage = $("#gridBox")
				.ligerGrid(
						{
							columns : [
									{
										display : '订单id',
										name : 'id',
										width : 100
									},
									{
										display : '订单号',
										name : 'tNumber',
										width : 150
									},
									{
										display : '会员id',
										name : 'memberId',
										width : 120
									},
									{
										display : '会员账号',
										name : 'mName',
										width : 120
									},
									{
										display : '基础货币',
										name : 'baseCurrency',
										width : 80
									},
									{
										display : '计价货币',
										name : 'quoteCurrency',
										width : 80
									},
									{
										display : '买卖',
										name : 'tType',
										width : 80
									},
									{
										display : '价格',
										name : 'price',
										width : 100,
										align : 'right'
									},
									{
										display : '数量',
										name : 'volume',
										width : 100,
										align : 'right'
									},
									{
										display : '成交额',
										name : 'done',
										width : 100,
										align : 'right'
									},
									{
										display : '是否吃单方',
										name : 'taker',
										width : 80,
										render : function(r, n, v) {
											return v == 'SELF' && '是'
													|| v == 'OPPOSITE' && '否';
										}
									},
									{
										display : '下单时间',
										name : 'createTime',
										width : 125
									},
									{
										display : '状态',
										name : 'status',
										width : 80,
										render : function(r, n, v) {
											return v == 'NP' && '待支付'
													|| v == 'UNCONFIRMED'
													&& '待确认' || v == 'DONE'
													&& '已完成' || v == 'CANCELED'
													&& '撤销'
													|| v == 'COMPLAINING'
													&& '申诉';
										}
									},
									{
										display : '支付方式',
										name : 'payType',
										width : 100,
										render : function(r, n, v) {
											return v == 'BANK' && '待支付'
													|| v == 'ALIPAY' && '支付宝'
													|| v == 'WXPAY' && '微信';
										}
									},
									{
										display : '商户账号',
										name : 'payAccount',
										width : 100
									},
									{
										display : '聊天记录',
										name : '',
										width : 100,
										render : function(row) {
											return "<a href='javascript:void(0);' onclick='charRecord("
													+ row.id + "," + row.memberId 
													+ ")'><div>聊天记录</div></a>";
										}
									}, {
										display : '交易备注',
										name : 'memo',
										width : 200
									} ],

							delayLoad : true,
							rownumbers : true,
							url : "${rootPath}/member/otcTrade",
							method : "get",
							sortName : 'id',
							sortOrder : 'desc'
						});
		$('#btnOK').trigger('click');
	});
	//撤单
	function charRecord(id, memberId) {
		var url = '${rootPath}/member/otcTradeChatRecord?tradeId=' + id + "&memberId=" + memberId;
		$.ajax({
			method : 'get',
			url : url,
			dataType : "json",
			contentType : "application/json",
			success : function(res) {
				$div = $('<div style="width: 300px; height:500px;"></div>').html(res.data);
				$.ligerDialog.open({ target: $div })
			},
			error : function(res) {
			}
		})
	}
	function commonOpenDialog1(operDesc) {
		layer.alert(operDesc);
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