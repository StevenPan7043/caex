<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@page import="com.pmzhongguo.ex.business.entity.Currency"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
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
	<%
    //取出java后台设置好的userList
    List<Currency> currencyLst = (List<Currency>)application.getAttribute(HelpUtils.CURRENCYLST);
	%>
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'会员ID',name:'id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
			{display:'会员账号',name:'m_name',width:100}
			 <%
		        for (Currency c : currencyLst) {
		    %>
		    ,{display:'<%=c.getCurrency()%>',columns: [
		    	{display:'总余额',name:'<%=c.getCurrency()%>_total_balance',width:100,render:function(r,n,v){
		    		if(v == null || v == undefined || v == ''){
		    			return '0';
		    		}
		    		return v
					}
		    	},
				{display:'冻结余额',name:'<%=c.getCurrency()%>_frozen_balance',width:100,render:function(r,n,v){
					if(v == null || v == undefined || v == ''){
		    			return '0';
		    		}
		    		return v
					}
				}
		    	]}
		    <%
		        }
		    %>
		],
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/member/asserts_collection",
		method: "get",
		sortName: 'member_id',
	   	sortOrder: 'asc',
	   	isSort: false
	});
	$('#btnOK').trigger('click');
});

//function commonOpenDialog1(operDesc){
//	layer.alert(operDesc);
//}
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