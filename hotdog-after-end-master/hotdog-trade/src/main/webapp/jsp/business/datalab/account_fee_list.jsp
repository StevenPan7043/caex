<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="feeCurrency" name="feeCurrency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input  id="mName" name="mName" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
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
			{display:'会员ID',name:'id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
			{display:'会员账号',name:'mName',width:180},
			{display:'币种',name:'feeCurrency',width:100},
			{display:'总余额',name:'totalAmount',width:150},
			{display:'冻结余额',name:'forzenAmount',width:150}
		],
		onDblClickRow : function (data, rowindex, rowobj)
		{
			f_common_edit($("#gridBox"), "${rootPath}/member/asserts_edit?id={id}&currency={currency}", false, 750, 320);
		}, 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/ca/accountFeeList",
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