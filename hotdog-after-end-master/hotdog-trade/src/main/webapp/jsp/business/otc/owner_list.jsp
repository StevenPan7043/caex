<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>
<div class="main_content">
	<div id="gridBox" style="margin: 0; padding: 0"></div>
</div>

<script type="text/javascript">
	var grid;
	var itemsStr = "[ " + "{ text: '增加', click: dicOper, icon: 'add'},"
			+ "{ text: '修改', click: dicOper, icon: 'modify'},"
			+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	$(function() {
		menu = $.ligerMenu({
			width : 120,
			items : eval(itemsStr)
		});
		grid = $("#gridBox").ligerGrid({
			columns : [ {
				display : '广告主',
				name : 'o_name',
				width : 250
			}, {
				display : '银行卡信息',
				name : 'bank_info',
				width : 160
			}, {
				display : '支付宝信息',
				name : 'alipay_info',
				width : 160
			}, {
				display : '微信支付信息',
				name : 'wxpay_info',
				width : 160
			}, {
				display : '最后在线时间',
				name : 'last_time',
				width : 200
			}, {
				display : '平均放行时间',
				name : 'avg_time',
				width : 120
			} ],
			sortName : 'id',
			sortOrder : 'desc',
			url : "${rootPath}/otc/listOwner",
			method : "get",
			rownumbers : true,
			onContextmenu : function(parm, e) {
				menu.show({
					top : e.pageY,
					left : e.pageX
				});
				return false;
			},
			onDblClickRow : function(data, rowindex, rowobj) {
				var item = {
					text : '修改'
				};
				dicOper(item);
			},
			toolbar : {
				items : eval(itemsStr)
			}
		});
	});

	function dicOper(item) {
		switch (item.text) {
		case "增加":
			openAddWindow("增加", "${rootPath}/otc/toAddOwner", 850, 600);
			break;

		case "修改":
			f_common_edit($("#gridBox"), "${rootPath}/otc/toEditOwner?id={id}", false,
					850, 600);
			break;

		case "删除":
			f_common_del($("#gridBox"), "${rootPath}/otc/delOwner?id={id}", false);
			break;
		}
	}
</script>
