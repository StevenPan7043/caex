<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<select name="type" id="type" class="sel  required" >

			</select>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
		 <%--todo 该功能暂未完善--%>
		<%--<input id="sendreward" type="button" onclick="sendreward()" value="发送奖励" class="btn_a wanwu_search"/>--%>
	</div>
	<div class="main_content">
		<div id="gridBox"></div>
	</div>
<script type="text/javascript">
var grid,selectDialog;
$(function(){
	grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'名次',name:'no',width:120},
			{display:'UID',name:'uid',width:120},
			{display:'账号',name:'account',width:120},
			{display:'24h成交数量',name:'day_trade',width:150},
			{display:'总成交数量',name:'num',width:150}
		],
        delayLoad: true, 
		rownumbers:true,
		url: "${rootPath}/trade/trade_ranking_list_query",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc'
	});
	$('#btnOK').trigger('click');
});

/**
 * 发送奖励
 */
function sendreward(){
	var type = $("#type").val();
	console.log(type)
	if(type == ""){
		$.ligerDialog.warn("请选择交易对");
	}else{
		$.ligerDialog.confirm('确定发送奖励', function (yes) {
			console.log(yes)
			if(yes){
				$.get("${rootPath}/trade/send_reward?type=" + type,function(data){
					console.log(data);
					if(data.state == 1){
						$.ligerDialog.success("奖励发送成功");
					}else{
						$.ligerDialog.warn(data.msg)
					}
				});
			}
		});
	}
}
$.get("${rootPath}/voteb/coins", function(data) {
    var optionstring = "<option>--请选择币种--</option>";


    $.each(data.data, function(k, v) { //循环遍历后台传过来的json数据
        var selected = "";
        optionstring += '<option value="' + v.text + '" '+ selected +'>'
            + v.text + '</option>';
    });
    $("#type").html(optionstring);
});

function commonOpenDialog1(operDesc){
	layer.alert(operDesc);
}

</script>

<%@ include file="../../bottom.jsp"%>