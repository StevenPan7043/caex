<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input id="member_id" name="member_id" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">时间范围</div>
		<div class="searchFieldCtr">
			<input type="text" name="startDate" id="startDate" maxlength="16" readonly="readonly"
				   class="txt txt_datetime required"/>
			--
			<input type="text" name="endDate" id="endDate" maxlength="16" readonly="readonly"
				   class="txt txt_datetime required"/>
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
                {display:'会员ID',name:'member_id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
                {display:'会员账号',name:'m_name',width:180},
                {display:'登录时间',name:'oper_time',width:180},
                {display:'登录IP',name:'oper_ip',width:100},
                {display:'登录设备',name:'login_device',width:80},
            ],
            delayLoad: true,
            rownumbers:true,
            sortName: 'id',
			sortOrder: 'desc',
			isSort: true,
			url: "${rootPath}/member/loginHistory",
			method: "get"
		});
        $('#btnOK').trigger('click');
    });

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