<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">备份日期</div>
		<div class="searchFieldCtr">
			<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${curDateAndReportTime}"/>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">币种</div>
		<div class="searchFieldCtr">
			<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input  id="member_id" name="member_id" type="text" class="txt enterAsSearch" />
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
                {display:'会员UID',name:'member_id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
                // {display:'会员账号',name:'m_name',width:180},
                {display:'币种',name:'currency',width:100},
                {display:'总余额',name:'total_balance',width:150},
                {display:'冻结余额',name:'frozen_balance',width:150}
            ],
            delayLoad: true,
            rownumbers:true,
            url: "${rootPath}/account/getAccountList",
            method: "get",
            sortName: 'member_id',
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