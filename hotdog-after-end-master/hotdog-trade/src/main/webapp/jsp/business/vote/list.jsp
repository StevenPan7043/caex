<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="div_block">
		<p class="p_block">
			<label class="label_block" style="width: 200px; font-size: 24px; font-weight: bold; color: #ff6600;">赞成：${data.like}</label>
		</p>
		<p class="p_block">
			<label class="label_block" style="width: 200px; font-size: 24px; font-weight: bold; color: #437dbd;">反对：${data.oppose}</label>
		</p>
	</div>
</div>
<div class="main_content">
	<div id="gridBox" style="margin: 0; padding: 0"></div>
</div>
<script type="text/javascript">
	var grid;
	$(function() {
		grid = $("#gridBox").ligerGrid({
			columns : [ {
				display : '会员ID',
				name : 'mid',
				width : 100
			}, {
				display : '币种',
				name : 'currency',
				width : 100
			}, {
				display : '投币数量',
				name : 'currencyNum',
				width : 120
			}, {
				display : '类型',
				name : 'state',
				width : 120,
				render : function(r, n, v) {
					return v == '1' ? '赞成' : '反对';
				}
			}, {
				display : '票数',
				name : 'pollNum',
				width : 120
			}, {
				display : '每票多少币',
				name : 'num',
				width : 120
			}, {
				display : '投票时间',
				name : 'createtime',
				width : 200
			}, ],
			url : "${rootPath}/voteb/list_data",
			method : "get",
			rownumbers : true,
			sortName : 'createtime',
			sortOrder : 'desc',
			isSort : false,
			autoRefresh : false
		});
	});
</script>
<%@ include file="../../bottom.jsp"%>
