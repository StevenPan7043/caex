<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div class="main_content">
		<div id="gridBox"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog;
$(function(){
	 
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'虚拟币',name:'currency',width:300, align:'center'},
			{display:'剩余地址',name:'num',width:300, align:'right'}
		],
        delayLoad: false, 
		rownumbers:true,
		url: "${rootPath}/trade/addr/addr_pool",
		method: "get",
	});
});

</script>

<%@ include file="../../bottom.jsp"%>