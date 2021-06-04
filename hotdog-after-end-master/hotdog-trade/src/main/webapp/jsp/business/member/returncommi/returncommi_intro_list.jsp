<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">

		<div class="searchField">
			<div class="searchFieldLbl">邀请人账号</div>
			<div class="searchFieldCtr">
				<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>

		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
	<div class="main_content" style="width: 60%;  float: left; margin:3px; overflow-y: scroll;">
		<div id="gridBox"></div>
	</div>
	<%--<div id="main_content2"style="width: 35%;  float: left; margin:3px; overflow-y: scroll;">--%>
		<%--<div id="gridBox2"></div>--%>
	<%--</div>--%>
	<div id="main_content3" style="width: 35%; float: right; margin:3px;  overflow-y: scroll;">
		<div id="gridBox3"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog ,firstLoad = true;
$(function(){
	 
	grid = $("#gridBox").ligerGrid({
        columns: [
            {display:'会员ID',name:'mid',width:80},
            {display:'会员账号',name:'mname',width:180},
            {display:'注册时间',name:'reg_time',width:180},
            {display:'邀请人ID',name:'intro_id',width:100},
            {display:'邀请人账号',name:'intro_mname',width:100},
            {display:'返佣详情',name:'intro_id',width:220,render:
                function(row){
                    return "<a href='javascript:void(0);' onclick='javascript:loadReturnCommiAmount(" + row.intro_id + ")'>查看总数&nbsp;&nbsp;</a>"
                    + "<a href='javascript:void(0);' onclick='javascript:loadReturnCommiCurrencyAmount(" + row.intro_id + ")'>&nbsp;&nbsp;查看币种明细</a>";
                }
            },
        ],
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/member/returncommi/intro",
		method: "get",
		sortName: 'm.id',
	   	sortOrder: 'desc',
	   	isSort: false
	});

	$('#btnOK').trigger('click');
    if(firstLoad){
        firstLoad = false;
		loadReturnCommiAmount();
		loadReturnCommiCurrencyAmount();
	}


});

// 加载返佣总数
var loadReturnCommiAmount = function (member_id) {
    grid = $("#gridBox3").ligerGrid({
        columns: [
            {display:'会员ID',name:'member_id',width:80},
            {display:'返佣数量',name:'return_commi_amount',width:100},
            {display:'返佣时间',name:'create_time',width:150}
        ],
        delayLoad: true,
        rownumbers:true,
        url: "${rootPath}/member/returncommi/detail?member_id="+member_id,
        method: "get",
        sortName: 'create_time',
        sortOrder: 'asc',
        isSort: false
    });
}
// 加载返佣币种明细
var loadReturnCommiCurrencyAmount = function (member_id) {
    grid = $("#gridBox3").ligerGrid({
        columns: [
            {display:'会员ID',name:'member_id',width:80},
            {display:'返佣币种',name:'currency',width:80},
            {display:'返佣汇率',name:'rate',width:80},
            {display:'返佣数量',name:'amount',width:80},
            {display:'返佣时间',name:'create_time',width:150}
        ],
        delayLoad: true,
        rownumbers:true,
        url: "${rootPath}/member/returncommi/currency/detail?member_id="+member_id,
        method: "get",
        sortName: 'create_time',
        sortOrder: 'asc',
        isSort: false
    });
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

<%@ include file="../../../bottom.jsp"%>