<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">会员ID</div>
			<div class="searchFieldCtr">
				<input id="memberId" name="memberId" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<select id="tableName" name="tableName" class="sel" >
				<option value="" selected>---项目方---</option>
				<c:forEach items="${info}" var="m">
					<option value="${m.c_name}">${m.c_name}</option>
				</c:forEach>
			</select>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField" style="margin-top: 10px">
			<div class="searchFieldLbl">划转类型：</div>
			<div class="searchFieldCtr">
				<select id="transferType" name="transferType" >
					<option value="deposit">转入</option>
					<option value="withdraw" selected>转出</option>
				</select>
			</div>
		</div>
		<div class="searchField" style="margin-top: 10px">
			<div class="searchFieldCtr">
				<div class="searchFieldLbl">划转状态：</div>
				<select id="transferStatus" name="transferStatus" >
						<option value="fail" selected>失败</option>
						<option value="success">成功</option>
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
            {display: '会员ID', name: 'memberId', width: 80},
            {display: '会员账号', name: 'mName', width: 120},
            {display: '币种', name: 'currency', width: 80},
            // {display:'操作类型',name:'type',width:80,render:function(r,n,v){return v=='1'&&'充值'||v=='2'&&'释放'||v=='3'&&'还原' || v=='4'&&'作废';}},
            {
                display: '划转类型', name: 'transferType', width: 120, render: function (r, n, v) {
                    return v == 'withdraw' && '转出' || v == 'deposit' && '转入';
                }
            },
            {
                display: '划转状态', name: 'transferStatus', width: 120, render: function (r, n, v) {
                    return v == 'fail' && '失败' || v == 'success' && '成功';
                }
            },
			{display: '划转资产', name: 'currentNum', width: 150},

            {display: '请求体', name: 'body', width: 250},
            {display: '划转时间', name: 'transferTime', width: 150},
            {display: '备注', name: 'remark', width: 150},
        ],
		 
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/api/getThirdLogList",
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