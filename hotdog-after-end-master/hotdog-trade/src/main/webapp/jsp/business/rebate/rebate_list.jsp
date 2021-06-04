<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">会员ID</div>
			<div class="searchFieldCtr">
				<input  id="memberId" name="memberId" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">邀请人ID</div>
			<div class="searchFieldCtr">
				<input  id="introduceMId" name="introduceMId" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
		<div class="searchFieldCtr">
			<select id="rStatus" name="rStatus" class="sel">
				<option value="">划转状态</option>
				<option value="0">未划转</option>
				<option value="1">已划转</option>
			</select>
		</div>
		</div>
		<div class="searchField">
		<div class="searchFieldCtr">
			<select id="rType" name="rType" class="sel">
				<option value="">奖励类型</option>
				<option value="0">静态持仓</option>
				<option value="1">动态邀请</option>
			</select>
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
            {display:'会员ID',name:'memberId',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
			{display:'币种',name:'currency',width:60},
			{display:'总资产',name:'totalBalance',width:140},
			{display:'冻结资产',name:'frozenBalance',width:140},
			{display:'可用资产',name:'availableBalance',width:140},
			{display:'返利资产',name:'rebateBalance',width:140},
			{display:'状态',name:'rStatus',width:60,render:function(r,n,v){return v=='0'&&'未划转'||v=='1'&&'已划转'}},
			{display:'返利类型',name:'rType',width:80,render:function(r,n,v){return v=='0'&&'静态持仓'||v=='1'&&'邀请返利'}},
			{display:'介绍人ID',name:'introduceMId',width:80},
			{display:'创建时间',name:'createTime',width:120},
            // {display:'是否停止释放',name:'isRelease',width:120,render:function(r,n,v){return v=='0'&&'正在释放'||v=='1'&&'释放结束'||v=='2'&&'待释放' || v=='3'&&'作废'}},
            {display:'操作时间',name:'updateTime',width:120},
		],
        delayLoad: true,
		rownumbers:true,
		url: "${rootPath}/r/getAccountRebateList",
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