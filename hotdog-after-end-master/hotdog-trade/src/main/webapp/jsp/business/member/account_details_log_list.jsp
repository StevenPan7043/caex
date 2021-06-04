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
<%--		<div class="searchField">--%>
<%--			<div class="searchFieldLbl">操作来源</div>--%>
<%--			<div class="searchFieldCtr">--%>
<%--				<select id="optSource" name="optSource" class="sel">--%>
<%--					<option value="">--请选择--</option>--%>
<%--					<option value="JSC">JSC</option>--%>
<%--					<option value="GSTTProject">GSTTProject</option>--%>
<%--					<option value="lastWiner">lastWiner</option>--%>
<%--					<option value="gmc">gmc</option>--%>
<%--					<option value="lockAccount">lockAccount</option>--%>
<%--					<option value="otc">otc</option>--%>
<%--					<option value="zzex">zzex</option>--%>
<%--					<option value="currencylock">currencylock</option>--%>
<%--				</select>--%>
<%--			</div>--%>
<%--		</div>--%>

		<div class="searchField">
			<div class="searchFieldLbl">操作来源</div>
			<div class="searchFieldCtr">
				<select id="optSource" name="optSource" class="sel">
					<option value="">--请选择--</option>
					<c:forEach var="optSource" items="${optSources}">
						<option value="${optSource}">${optSource}</option>
					</c:forEach>
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
			{display:'会员ID',name:'memberId',width:80},
			{display:'币种',name:'currency',width:80},
			{display:'操作类型',name:'procType',width:80,render:function(r,n,v){return v=='1'&&'冻结'||v=='2'&&'解冻'||v=='3'&&'增加'||v=='4'&&'还原' ||v=='11'&&'失败';}},
			{display:'操作数量',name:'num',width:150},
			{display:'操作前信息',name:'beforeInfo',width:800},
			{display:'操作时间',name:'createTime',width:150},
			{display:'操作结果',name:'operResult',width:80},
			{display:'操作来源',name:'optSource',width:150}
		],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/member/account_oper_log",
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