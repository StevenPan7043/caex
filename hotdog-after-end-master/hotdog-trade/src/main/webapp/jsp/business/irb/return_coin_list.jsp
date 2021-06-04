 <%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<select name="table_name" id="table_name" class="sel ww_select required" data="{code:'currency_pair|id'}">
				<option value="">--请选择--</option>
			</select>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">日期</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_date required" value="${curDateAndReportTime}"/>
			</div>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
	<div style="padding: 15px 20px;">
		<h3>返还币种总数量: <span id="count" style="color: #00b07c;">10000</span></h3>
	</div>
	<div class="main_content">
		<div id="gridBox"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog;
$(function(){
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'会员账号',name:'XXX',width:120},
			{display:'持有比例',name:'XXX',width:120},
			{display:'返还币种类型',name:'XXX',width:120},
			{display:'返还数量',name:'XXX',width:120}
		],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "/trade/trade",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc'
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