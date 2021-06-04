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
		<div class="searchFieldLbl">买卖</div>
		<div class="searchFieldCtr">
			<select id="o_type" name="type" class="sel">
				<option value="">--请选择--</option>
				<option value="BUY">买</option>
				<option value="SELL">卖</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">下单时间</div>
		<div class="searchFieldCtr">
			<input type="text" name="create_start" id="create_start" maxlength="16"
				readonly="readonly" class="txt txt_datetime required"
				value="${curDateAndReportTime}" /> -- <input type="text"
				name="create_end" id="create_end" maxlength="16" readonly="readonly"
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
			{ display : '广告id',
				name : 'id',
				width : 100
			},{ display : '会员账号',
				name : 'mName',
				width : 120
			},{
				display : '基础货币',
				name : 'baseCurrency',
				width : 80
			}, {
				display : '计价货币',
				name : 'quoteCurrency',
				width : 80
			}, {
				display : '买卖',
				name : 'type',
				width : 80
			}, {
				display : '价格',
				name : 'price',
				width : 100,
				align : 'right'
			}, {
				display : '数量',
				name : 'volume',
				width : 100,
				align : 'right'
			}, {
				display : '已成交',
				name : 'done_volume',
				width : 100,
				align : 'right'
			}, {
				display : '交易中',
				name : 'lockVolume',
				width : 100,
				align : 'right'
			}, {
				display : '交易限额',
				name : 'temp',
				width : 100
			}, {
				display : '下单时间',
				name : 'createTime',
				width : 125
			}, {
				display : '状态',
				name : 'status',
				width : 80,
				render : function(r, n, v){
					return v == 'WATTING' && '挂单中'
					|| v == 'TRADING' && '交易中'
					|| v == 'DONE' && '已完成'
					|| v == 'PC' && '部分撤销'
					|| v == 'CANCELED' && '撤销';
				}
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
				display : '操作',
				name : '',
				width : 100,
				render : function(row){
					return "<a href='javascript:void(0);' onclick='cancelOrder(" + row.id + ")'><div>撤销</div></a>";
				}
			} ],

			delayLoad : true,
			rownumbers : true,
			url : "${rootPath}/member/otcOrder",
			method : "get",
			sortName : 'id',
			sortOrder : 'desc'
		});
		$('#btnOK').trigger('click');
	});
	//撤单
	function cancelOrder(id) {

		$util.sure("您确定撤销该【" + id + "】广告吗？", function() {
			var url = '${rootPath}/member/otcOrderCancel/' + id;
			$.ajax({
				method : 'get',
				url : url,
				dataType : "json",
				contentType : "application/json",
				success : function(res) {
					$.submitShowResult(res);
					try {
						$hook.opeRefresh("ope_refresh", "gridBox",
								"searchDiv");
					} catch (e) {
						layer.alert(e);
					}
				},
				error : function(res) {
					$.submitShowResult(res);
				}
			})
		});

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