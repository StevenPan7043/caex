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
			{display:'货币',name:'currency',width:60},
			{display:'总金额',name:'total_balance',width:170, align:'right'},
			{display:'总冻结额',name:'frozen_balance',width:170, align:'right'},
			{display:'总可用额',name:'can_use_balance',width:170, align:'right'},
            {display:'锁仓总金额',name:'lock_total',width:170, align:'right'},


            {display:'币币总金额',name:'total',width:170, align:'right'},
            {display:'币币冻结额',name:'frozen',width:170, align:'right'},
            {display:'币币总可用额',name:'can_use',width:170, align:'right'},

            {display:'OTC总金额',name:'otc_total',width:170, align:'right'},
            {display:'OTC冻结额',name:'otc_frozen',width:170, align:'right'},
            {display:'OTC总可用额',name:'otc_can_use',width:170, align:'right'},

		],
		 
        delayLoad: false, 
		rownumbers:true,
		url: "${rootPath}/trade/total_balance_list",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc'
	});
});

</script>

<%@ include file="../../bottom.jsp"%>