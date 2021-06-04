<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>

<%-- 搜索栏--%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input id="id" name="id" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">ApiKey</div>
		<div class="searchFieldCtr">
			<input id="api_key" name="api_key" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">ApiSecret</div>
		<div class="searchFieldCtr">
			<input id="api_secret" name="api_secret" type="text" class="txt enterAsSearch" />
		</div>
	</div>

	<input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>

	<div class="main_content">
		<div id="gridBox"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog;
$(function(){
	 
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'UID',name:'id',width:120},
			{display:'会员账号',name:'m_name',width:120},
            {display:'ApiKey',name:'api_key',width:350},
            {display:'ApiSecret',name:'api_secret',width:350},
            {display:'佣金比例',name:'trade_commission',width:80},
			{display:'创建时间',name:'create_time',width:125},
			{display:'Api权限',name:'api_privilege',width:200},
			{display:'IP地址',name:'trusted_ip',width:120},
			{display:'状态',name:'t_status',width:80}
		],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/member/member_api_token",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc',
	   	isSort: false
	});
	$('#btnOK').trigger('click');
});

function commonOpenDialog1(operDesc){
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