<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">

	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">钱包地址</div>
		<div class="searchFieldCtr">
			<input id="address" name="address" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">币种</div>
		<div class="searchFieldCtr">
			<select name="currency" id="currency" class="sel required"></select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">状态</div>
		<div class="searchFieldCtr">
			<select id="hasMember" name="hasMember" class="sel">
				<option value="">--请选择--</option>
				<option value="true">已分配</option>
				<option value="false">未分配</option>
			</select>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
	
<div class="main_content">
<div id="gridBox"></div>
</div>

<script type="text/javascript">


$.get("${rootPath}/voteb/coins", function(data) {
    var optionstring = "<option value=''>--请选择币种--</option>";

    var currency = $("#temp").val();
    $.each(data.data, function(k, v) { //循环遍历后台传过来的json数据
        var selected = "";
        if (currency == v.text) {
            selected = 'selected="selected"';
        }
        optionstring += '<option value="' + v.text + '" '+ selected +'>'
            + v.text + '</option>';
    });
    $("#currency").html(optionstring);
});
var grid;
$(function () {
    grid = $("#gridBox").ligerGrid({
        columns: [
            {display:'用户名',name:'m_name',width:180,render:function(res){
                var option = $("#hasMember").val();
            	if(option=='' || option=='false') {
            	    return '---未分配---'
                }else {
            	    return res.m_name;
				}}},
            {display:'币种',name:'currency',width:180},
            {display:'钱包地址',name:'address',width:300},
        ],

        delayLoad: true,
        rownumbers:true,
        url: "${rootPath}/member/member_coin_recharge_addr",
        method: "get",
        sortName: 'id',
        sortOrder: 'desc',
        isSort: false
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