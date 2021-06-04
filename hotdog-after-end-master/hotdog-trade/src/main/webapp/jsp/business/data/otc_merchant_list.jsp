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
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">登录时间</div>
		<div class="searchFieldCtr">
			<input type="text" name="login_start" id="login_start" maxlength="16"
				readonly="readonly" class="txt txt_datetime required"
				value="${curDateAndReportTime}" /> -- <input type="text"
				name="login_end" id="login_end" maxlength="16" readonly="readonly"
				class="txt txt_datetime required"
				value="${tomorrowDateAndReportTime}" />
		</div>
	</div>
	<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content" style="width: 100%; float: left;">
	<div id="gridBox"></div>
</div>

<script type="text/javascript">
	var grid, selectDialog, manage;
	$(function() {

		grid = manage = $("#gridBox").ligerGrid({
			columns : [ 
			{ display : '会员账号',
				name : 'mName',
				width : 120
			}, {
				display : '银行卡信息',
				name : 'bankAccount',
				width : 150
			}, {
				display : '支付宝信息',
				name : 'alipayAccount',
				width : 100
			}, {
				display : '微信信息',
				name : 'wxAccount',
				width : 100
			}, {
				display : '平均放行时间',
				name : 'consumingTime',
				width : 100
			}, {
				display : '最后在线时间',
				name : 'last_login_time',
				width : 150
			},{
				display: 'vip',
					name:'vip',
					width :150,
				} ],

			delayLoad : true,
			rownumbers : true,
			onDblClickRow : function (data, rowindex, rowobj)
			{
				var item = {text: '修改'};
				dicOper(item);
			},
			url : "${rootPath}/member/otcMerchant?status=2",
			method : "get",
			sortName : 'id',
			sortOrder : 'desc'
		});
		$('#btnOK').trigger('click');
	});

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

//修改vip
	function dicOper(item){
		switch (item.text) {
			case "修改":
				f_common_edit($("#gridBox"), "${rootPath}/member/otc_merchant_edit?id={id}", false, 500, 250);
				break;
		}
	}
</script>

<%@ include file="../../bottom.jsp"%>