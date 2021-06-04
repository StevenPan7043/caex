<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv" >
	<input id="btnOK" type="button" value="刷新" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
</div>
<div class="main_content">
	<div id="gridBox"></div>
</div>
<script type="text/javascript">
	var grid,selectDialog;
	$(function(){

		grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'来源',name:'source',width:150},
				{display:'币种',name:'currency',width:150},
				{display:'余额',name:'banlance',width:250},
				{display:'简介',name:'desc',width:400}
			],
			delayLoad: true,
			rownumbers:true,
			url: "${rootPath}/manual/feeBanlanceList",
			method: "get",
			sortName: 'member_id',
			sortOrder: 'asc',
			isSort: false
		});
		$('#btnOK').trigger('click');
	});

	function commonOpenDialog1(operDesc){
		layer.alert(operDesc);
	}
</script>

<%@ include file="../../bottom.jsp"%>