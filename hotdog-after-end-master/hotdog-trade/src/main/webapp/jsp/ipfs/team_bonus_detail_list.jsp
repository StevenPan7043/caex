<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">

	<div class="searchField">
		<div class="searchFieldLbl">用户Id</div>
		<div class="searchFieldCtr">
			<input id="memberId" name="memberId" type="text"
				   class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">分红日期</div>
		<div class="searchFieldCtr">
			<input id="bonusDate" name="bonusDate" type="text"
				   class="txt enterAsSearch" />
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>


<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
var grid;

$(function () {
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'用户账号',name:'phone',width:90},
			{display:'用户Id',name:'id',width:90},
			{display:'用户等级',name:'LEVEL',width:90,
				render : function(r, n, v) {
					return v == '0' && '普通'
							|| v == '1' && '黄金'
							|| v == '2' && '铂金'
							|| v == '3' && '钻石'
							|| v == '4' && '荣耀';
				}},
			{display:'个人团队奖励',name:'team_bonus',width:100},
			{display:'伞下总产量',name:'sub_bonus_base',width:100},
			{display:'伞下总业绩',name:'sub_hash',width:100}
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/ipfs/teamDetailBonus",
		method: "get",
		rownumbers: true,
		delayLoad: false
	});
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

function datetimeFormat(longTypeDate){
	var datetimeType = "";
	var date = new Date();
	date.setTime(longTypeDate);
	datetimeType+= date.getFullYear();  //年
	datetimeType+= "-" + getMonth(date); //月
	datetimeType += "-" + getDay(date);  //日
	datetimeType+= "&nbsp;&nbsp;" + getHours(date);  //时
	datetimeType+= ":" + getMinutes(date);   //分
	datetimeType+= ":" + getSeconds(date);   //分
	return datetimeType;
}
function getMonth(date){
	var month = "";
	month = date.getMonth() + 1; //getMonth()得到的月份是0-11
	if(month<10){
		month = "0" + month;
	}
	return month;
}
//返回01-30的日期
function getDay(date){
	var day = "";
	day = date.getDate();
	if(day<10){
		day = "0" + day;
	}
	return day;
}
//返回小时
function getHours(date){
	var hours = "";
	hours = date.getHours();
	if(hours<10){
		hours = "0" + hours;
	}
	return hours;
}
//返回分
function getMinutes(date){
	var minute = "";
	minute = date.getMinutes();
	if(minute<10){
		minute = "0" + minute;
	}
	return minute;
}
//返回秒
function getSeconds(date){
	var second = "";
	second = date.getSeconds();
	if(second<10){
		second = "0" + second;
	}
	return second;
}
</script>
