<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>
<div id="searchDiv" class="searchDiv" style='${sessionScope.SYS_SESSION_USER.id == 1 ? "" : "display: none;"}'>
	<div class="searchField">
		<div class="searchFieldLbl">是否展示</div>
		<div class="searchFieldCtr">
			<select name="c_status" id="c_status" class="sel ww_select" data="{code:'bool|id'}">
				<option value="">请选择...</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">广告主</div>
		<div class="searchFieldCtr">
			<c:if test="${sessionScope.SYS_SESSION_USER.id == 1}">
				<select name="owner_id" id="owner_id" class="sel ww_select" data="{code:'otc_owner|id'}">
					<option value="">请选择...</option>
				</select>
			</c:if>
			<c:if test="${sessionScope.SYS_SESSION_USER.id != 1}">
				<input id="owner_id" name="owner_id" type="hidden" value="${sessionScope.SYS_SESSION_USER.otc_owner_id}" />
			</c:if>
		</div>
	</div>
	<input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content">
	<div id="gridBox" style="margin: 0; padding: 0"></div>
</div>

<script type="text/javascript">
	var grid;
	var itemsStr = "";
	<c:if test="${sessionScope.SYS_SESSION_USER.id == 1}">
	itemsStr = "[{ text: '增加', click: dicOper, icon: 'add'},{ text: '修改', click: dicOper, icon: 'modify'}]";
	</c:if>
	$(function() {
		menu = $.ligerMenu({
			width : 120,
			items : eval(itemsStr)
		});
		grid = $("#gridBox").ligerGrid({
			columns : [ {
				display : '广告主',
				name : 'o_name',
				width : 100
			}, {
				display : '类型',
				name : 'ad_type',
				width : 60,
				render : function(r, n, v) {
					return v == '1' && '买入' || v == '2' && '卖出';
				}
			}, {
				display : '基础货币',
				name : 'base_currency',
				width : 70
			}, {
				display : '计价货币',
				name : 'quote_currency',
				width : 70
			}, {
				display : '价格',
				name : 'price',
				width : 70
			}, {
				display : '交易限额',
				name : 'min_quote',
				width : 150,
				render : function(r, n, v) {
					return v + "-" + r.max_quote;
				}
			}, {
				display : '银行卡信息',
				name : 'bank_info',
				width : 150
			}, {
				display : '支付宝信息',
				name : 'alipay_info',
				width : 150
			}, {
				display : '微信支付信息',
				name : 'wxpay_info',
				width : 150
			}, {
				display : '总成交量',
				name : 'total_volume',
				width : 100
			}, {
				display : '总成交额',
				name : 'total_amount',
				width : 100
			}, {
				display : '排序',
				name : 'a_order',
				width : 60
			}, {
				display : '状态',
				name : 'c_status',
				width : 60,
				render : function(r, n, v) {
					return v == '1' && '正常' || v == '0' && '暂停展示';
				}
			} ],
			sortName : 'id',
			sortOrder : 'desc',
			url : "${rootPath}/otc/listAds",
			method : "get",
			rownumbers : true,
			onContextmenu : function(parm, e) {
				menu.show({
					top : e.pageY,
					left : e.pageX
				});
				return false;
			},
			<c:if test="${sessionScope.SYS_SESSION_USER.id == 1}">
			onDblClickRow : function(data, rowindex, rowobj) {
				var item = {
					text : '修改'
				};
				dicOper(item);
			},
			</c:if>
			toolbar : {
				items : eval(itemsStr)
			},
			delayLoad : true
		});
		$("#btnOK").click();
	});

	function dicOper(item) {
		switch (item.text) {
		case "增加":
			openAddWindow("增加", "${rootPath}/otc/toAddAds", 950, 450);
			break;

		case "修改":
			f_common_edit($("#gridBox"), "${rootPath}/otc/toEditAds?id={id}", false, 950,
					450);
			break;

		case "删除":
			f_common_del($("#gridBox"), "${rootPath}/otc/delAds?id={id}", false);
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
