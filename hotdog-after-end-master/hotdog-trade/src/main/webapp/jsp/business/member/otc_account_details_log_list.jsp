<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">会员ID</div>
			<div class="searchFieldCtr">
				<input id="memberId" name="memberId" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
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
			{display:'会员ID',name:'memberId',width:80},
			{display:'币种',name:'currency',width:80},
			{display:'操作类型1',name:'procType',width:80,hide:true},
			{display:'操作类型',name:'procType',width:80,render:function(r,n,v){
			    return v=='1'&&'增加总额'||v=='2'&&'冻结'||v=='3'&&'还原'||v=='4'&&'解冻'||v=='5'&&'转入'||v=='6'&&'转出'||v=='7'&&'申请商家'||v=='8'&&'申请通过'||v=='9'&&'申请驳回'||v=='10'&&'退出商家资格';}},
			{display:'操作数量',name:'num',width:150},
			{display:'操作前信息',name:'beforeInfo',width:800},
			{display:'操作时间',name:'createTime',width:150},
			{display:'操作结果',name:'operResult',width:80}
		],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/member/otc_account_oper_log",
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