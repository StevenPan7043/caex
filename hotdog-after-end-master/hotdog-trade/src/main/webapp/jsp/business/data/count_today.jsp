<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<style type="text/css">
.box {
	width: 500px;
	/* 	height: 400px; */
	border: 1px solid #ddd;
	padding: 10px;
}

.box div {
	padding: 10px;
}

h3 {
	padding-bottom: 10px;
}

.recharge {
	top: 50px;
	left: 10px;
	background: #009688;
	position: absolute;
}

.withdraw {
	top: 50px;
	left: 540px;
	background: #FF5722;
	position: absolute;
}

table.gridtable {
	width: 100%;
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
}

table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
	text-align: center;
}

table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${startDate}"/>
			    --
			    <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${endDate}"/>
			</div>
	</div>
	<input type="button" value="查询" class="btn_a wanwu_search" onclick="search()" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content">
	<div class="box recharge">
		<h3>充值:</h3>
		<table class="gridtable">
			<tr>
				<th>币种</th>
				<th>总金额</th>
			</tr>
			<c:if test="${not empty map.rList}">
				<c:forEach items="${map.rList}" var="m">
					<tr>
						<td>${m.currency}</td>
						<td><fmt:formatNumber type="number" groupingUsed="false" value="${m.amount}" /></td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${ empty map.rList}">
				<tr>
					<td colspan="2" style="text-align: center;">没有数据</td>
				</tr>
			</c:if>
		</table>
	</div>
	<div class="box withdraw">
		<h3>提现:</h3>
		<table class="gridtable">
			<tr>
				<th>币种</th>
				<th>总金额</th>
				<th>总手续费</th>
			</tr>
			<c:if test="${not empty map.wList}">
				<c:forEach items="${map.wList}" var="m">
					<tr>
						<td>${m.currency}</td>
						<td><fmt:formatNumber type="number" groupingUsed="false" value="${m.amount}" /></td>
						<td><fmt:formatNumber type="number" groupingUsed="false" value="${m.fee}" /></td>
					</tr>
				</c:forEach>
			</c:if>
			<c:if test="${ empty map.wList}">
				<tr>
					<td colspan="3" style="text-align: center;">没有数据</td>
				</tr>
			</c:if>
		</table>
	</div>
</div>
<script type="text/javascript">
	function search() {
		window.location.href = "${rootPath}/count/today?startDate=" + $("#startDate").val() + "&endDate=" + $("#endDate").val();
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