<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员ID</div>
			<div class="searchFieldCtr">
				<input  id="memberId" name="memberId" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField" style="margin-top: 10px">
			<div class="searchFieldCtr">
				<select id="isRelease" name="isRelease" >
					<c:forEach items="${ReleaseTypes}" var="m">
						<option value="${m.type}" <c:if test="${0 == m.type}">selected </c:if>>${m.codeCn}</option>
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
            {display:'锁仓账户ID',name:'id',width:100},
            {display:'会员ID',name:'memberId',width:100,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
			{display:'币种',name:'currency',width:80},
			{display:'锁仓总资产',name:'warehouseAmount',width:120},
			{display:'锁仓已释放资产',name:'warehouseReleaseAmount',width:120},
			{display:'释放次数',name:'warehouseCount',width:100},
			{display:'已释放次数',name:'warehouseReleaseCount',width:100},
            {display:'下次释放时间',name:'nextReleaseTime',width:150,
				render:function(r,n,v){
                    var date1 = new Date(v/1000*1000);
                    var date = (date1.getFullYear()) + "-" +
                        (date1.getMonth() + 1) + "-" +
                        (date1.getDate()) + " " +
                        (date1.getHours()) + ":" +
                        (date1.getMinutes()) + ":" +
                        (date1.getSeconds());
                return date;
				}},
            {display:'是否停止释放',name:'isRelease',width:120,render:function(r,n,v){return v=='0'&&'正在释放'||v=='1'&&'释放结束'||v=='2'&&'待释放' || v=='3'&&'作废'}},
            {display:'操作时间',name:'updateTime',width:150},
            // {display:'充值地址',name:'rAddress',width:500},
		],
		// onDblClickRow : function (data, rowindex, rowobj)
		// {
		// 	f_common_edit($("#gridBox"), "/member/asserts_edit?id={id}&currency={currency}", false, 750, 320);
		// },
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/a/getWarehouseList",
		method: "get",
		sortName: 'id',
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