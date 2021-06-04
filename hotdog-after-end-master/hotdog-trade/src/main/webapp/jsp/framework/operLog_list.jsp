<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">操作人</div>
			<div class="searchFieldCtr">
				<input id="user_real_name" name="user_real_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">操作内容</div>
			<div class="searchFieldCtr">
				<input id="oper_desc" name="oper_desc" type="text" class="txt enterAsSearch" />
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

var itemsStr = "[{ text: '报表导出', click: dicOper, icon: 'logout' <bp:buttonAndColumnPermission functionId='XTGL-CZRZ'/>}] ";
$(function(){
	 
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'操作人',name:'user_real_name',width:80},
			{display:'操作内容',name:'oper_desc',width:350,render:$render.link('<a href="javascript:void(0);" onclick="commonOpenDialog1(\'{0}\');"><li style="width:340px;display:block;white-space:nowrap; overflow:hidden; text-overflow:ellipsis;">{0}</li></a>',["oper_desc"])},
			{display:'操作时间',name:'timestr',width:120},
			{display:'操作人IP',name:'user_ip',width:100},
			{display:'执行URL',name:'oper_url',width:160}
		],
        toolbar:{  items: eval(itemsStr)  },
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/frm/listOperLog",
		method: "get",
		sortName: 'oper_time',
	   	sortOrder: 'desc'
	});
	$('#btnOK').trigger('click');
});

	function dicOper(item){
		var gm = $("#gridBox").ligerGetGridManager();
		var row = gm.getSelected();

		switch (item.text) {

			case "报表导出":

			var user_real_name = $("#user_real_name").val() === undefined ? '' : $("#user_real_name").val();
			var oper_desc = $("#oper_desc").val() === undefined ? '' : $("#oper_desc").val();
			var startDate = $("#startDate").val() === undefined ? '' : $("#startDate").val();
			var endDate = $("#endDate").val() === undefined ? '' : $("#endDate").val();

			var params = "user_real_name="+user_real_name
			+ "&oper_desc="+oper_desc
			+ "&startDate="+startDate
			+ "&endDate="+endDate;
			window.location.href = "${rootPath}/frm/operlog/export?"+params;

			break;
		}
	}
function commonOpenDialog1(operDesc){

    var title = null;
	if (operDesc.indexOf("修改用户资产") !== -1) {
        title = "<h3 style='text-align: center'>修改用户资产<h3>";

	}else if (operDesc.indexOf("修改用户OTC资产") !== -1) {
        title = "<h3 style='text-align: center'>修改用户OTC资产<h3>";
    }
    if (title === null) {
	    // 不处理直接显示
        layer.alert(operDesc);
        return;
	}

    operDesc = getModifyMemberAssetsJSON(operDesc);
    var data = showModifyMemberAssets(operDesc);
	layer.alert(title + data);
}

/**
 *
 * @param originalStr 要替换的字符串
 * @param replaceStr  要替换的字符串中的某个子字符串
 * @param targetStr   要替换成哪个
 */
var myReplaceAll = function (originalStr,replaceStr,targetStr) {
    var reg = new RegExp(replaceStr,"g");
    return originalStr.replace(reg,targetStr);
}
/**
 * 获取【修改用户资产,修改用户OTC资产】的JSON对象
 * @param operDesc 字符串
 */
var getModifyMemberAssetsJSON = function (operDesc) {
    operDesc = operDesc.substring(6,operDesc.length);
    operDesc = myReplaceAll(operDesc,"”","\"");
    operDesc = myReplaceAll(operDesc,"\\[","");
    operDesc = myReplaceAll(operDesc,"\\]","");
    var head = operDesc.indexOf("old_total_balance") - 2;
    var tail = operDesc.length - 2;
    operDesc = operDesc.substring(head,tail);
    return JSON.parse(operDesc);
}

/**
 * 显示 【修改用户资产,修改用户OTC资产】
 * @param obj
 * @returns {string}
 */
function showModifyMemberAssets(obj) {
	var data =
		"<p>用户ID："+obj.member_id+"</p>" +
		"<p>会员账号："+obj.m_name+"</p>" +
		"<p>币种："+obj.currency+"</p>" +
		"<p>操作前资产："+obj.old_total_balance+"</p>" +
		"<p>操作前冻结资产："+obj.old_frozen_balance+"</p>" +
		"<p>操作后资产："+obj.total_balance+"</p>" +
		"<p>操作后冻结资产："+obj.frozen_balance+"</p>";
	return data;
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

<%@ include file="../bottom.jsp"%>