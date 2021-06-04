<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">成交时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value=""/>
			    --
			    <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value=""/>
			    币种:
				<c:choose>
					<c:when test="${currencys!=null}">
						<select name="currency" >
							<option value="">--所有币种--</option>
						<c:forEach var="currency" items="${currencys}">
								<option value="${currency}">${currency}</option>
						</c:forEach>
						</select>
					</c:when>
					<c:otherwise>
						<input type="text" name="currency" id="currency" class="txt required" value="" placeholder="如BTCZC、BTCETH"/>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" onclick="f_validate()"/>
		<input type="button" style="display: none" class="btn_a wanwu_search" id="select" data="{grid:'gridBox',scope:'searchDiv'}" >
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
<center><div id="message">请点击上方查询按钮进行搜索</div></center>

	<div class="main_content">
		<div id="gridBox"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog;
$(function(){

	$("#gridBox").ligerGrid({
		columns: [
			{display:'所在交易对',name:'key_name',width:300, align:'right'},
			{display:'手续费金额',name:'fee',width:300, align:'right'},
			{display:'手续费货币',name:'fee_currency',width:200, align:'left'}
		],

        delayLoad: true,
		rownumbers:true,
		pageSize:"1000",
		url: "${rootPath}/trade/fee_list_list",
		method: "get"

	});

	// $('#btnOK').trigger('click');
});
function f_validate(){
	if ($("#startDate").val()==""){
		$("#message").html("请选择成交时间:起始时间")
		return false;
	}
	if ($("#endDate").val()==""){
		$("#message").html("请选择成交时间:结束时间")
		return false;
	}

	$("#message").html("请点击上方查询按钮进行搜索")
	$('#select').trigger('click');

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