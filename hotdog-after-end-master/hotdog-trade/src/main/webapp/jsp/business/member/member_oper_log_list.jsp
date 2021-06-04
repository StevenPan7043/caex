<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">类型</div>
			<div class="searchFieldCtr">
				<select id="oper_type" name="oper_type" class="sel">
					<option value="">--请选择--</option>
					<option value="1">登录</option>
					<option value="2">其他</option>
				</select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">操作人</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">操作内容</div>
			<div class="searchFieldCtr">
				<input id="oper_memo" name="oper_memo" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">操作时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${curDateAndReportTime}"/>
			    --
			    <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${tomorrowDateAndReportTime}"/>
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
			{display:'操作人',name:'m_name',width:120},
			{display:'操作内容',name:'oper_memo',width:300,render:$render.link('<a href="javascript:void(0);" onclick="commonOpenDialog1(\'{0}\');"><li style="width:340px;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">{0}</li></a>',["oper_memo"])},
			{display:'操作时间',name:'oper_time',width:125},
			{display:'操作人IP',name:'oper_ip',width:120}
		],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/member/member_oper_log",
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