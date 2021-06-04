<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">项目Id</div>
		<div class="searchFieldCtr">
			<input id="projectId" name="projectId" type="text" class="txt enterAsSearch"/>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">算力Id</div>
		<div class="searchFieldCtr">
			<input id="hashrateId" name="hashrateId" type="text" class="txt enterAsSearch"/>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员Id</div>
		<div class="searchFieldCtr">
			<input id="memberId" name="memberId" type="text"
				   class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">推荐人Id</div>
		<div class="searchFieldCtr">
			<input id="introId" name="introId" type="text"
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
			{display:'项目id',name:'projectId',width:100},
			{display:'项目编码',name:'projectCode',width:150},
			{display:'算力id',name:'hashrateId',width:100},
			{display:'用户Id',name:'memberId',width:90},
			{display:'推荐人Id',name:'introId',width:90},
			{display:'奖励币种',name:'bonusCurrency',width:90},
			{display:'奖励数量',name:'bonusNum',width:100},
			{display:'奖励时间',name:'createTime',width:250,
				render : function(r, n, v) {
					return datetimeFormat(v) ;
				}}
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/ipfs/introBonus",
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
