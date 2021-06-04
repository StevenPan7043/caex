<%@ page contentType="text/html; charset=utf-8"%>
<style>
.layim-chat-main {
    padding: 5px;
    overflow-x: hidden;
    overflow-y: auto;
}
.layim-chat-main ul .layim-chat-mine {
    text-align: right;
    padding-left: 0;
    padding-right: 60px;
}
.layim-chat-main ul li {
    position: relative;
    font-size: 0;
    margin-bottom: 10px;
    padding-left: 60px;
    min-height: 68px;
}
.layim-chat-mine .layim-chat-user {
    left: auto;
    right: 1px;
}
.layim-chat-user {
    position: absolute;
    left: 5px;
}
.layim-chat-text, .layim-chat-user {
    display: inline-block;
    vertical-align: top;
    font-size: 14px;
}
.layim-chat-mine .layim-chat-text {
    margin-left: 0;
    text-align: left;
    background-color: #5FB878;
    color: #fff;
    left: auto;
    right: 50px;
}
.layim-chat-text {
	left: 55px;
    position: relative;
    line-height: 22px;
    margin-top: 25px;
    padding: 8px 15px;
    background-color: #e2e2e2;
    border-radius: 3px;
    color: #333;
    word-break: break-all;
    max-width: 300px;
}
.layim-chat-text, .layim-chat-user {
    display: inline-block;
    vertical-align: top;
    font-size: 14px;
}
.layim-chat-mine .layim-chat-text:after {
    left: auto;
    right: -10px;
    border-top-color: #5FB878;
}
.layim-chat-text:after {
    content: '';
    position: absolute;
    left: -10px;
    top: 13px;
    width: 0;
    height: 0;
    border-style: solid dashed dashed;
    border-color: #e2e2e2 transparent transparent;
    overflow: hidden;
    border-width: 10px;
}
.layim-chat-mine {
	text-align: right;
	position: relative;
}
.layim-chat-mine .layim-chat-user cite i {
    padding-left: 0;
    padding-right: 15px;
}
.layim-chat-user cite i {
    font-style: normal;
}
.layim-chat-mine .layim-chat-user cite {
    left: auto;
    right: 1px;
    text-align: right;
}
.layim-chat-user cite {
    position: absolute;
    left: 60px;
    top: -2px;
    width: 200px;
    line-height: 24px;
    font-size: 12px;
    white-space: nowrap;
    color: #999;
    text-align: left;
    font-style: normal;
}
.chat-user-me {
    margin-top: 15px;
    background: #00B2EE;
    border-radius: 40px;
    height: 40px;
    width: 40px;
    line-height: 40px;
    color: #FFFFFF;
    text-align: center;
}
.chat-user-plat {
    margin-top: 15px;
    background: #568AAD;
    border-radius: 40px;
    height: 40px;
    width: 40px;
    line-height: 40px;
    color: #FFFFFF;
    text-align: center;
}
.layim-chat-system {
	margin: 10px 0;
	text-align: left;
	position: relative;
}

.layim-chat-system span {
	display: inline-block;
	line-height: 30px;
	padding: 0 15px;
	border-radius: 3px;
	background-color: #e2e2e2;
	cursor: default;
	font-size: 14px
}
.layui-btn {
    display: inline-block;
    height: 30px;
    line-height: 30px;
	font-weight: bold;
    padding: 0 18px;
    background-color: #2E2A2A;
    color: #fff;
    white-space: nowrap;
    text-align: center;
    font-size: 14px;
    border: none;
    border-radius: 2px;
    cursor: pointer;
    -moz-user-select: none;
    -webkit-user-select: none;
    -ms-user-select: none;
    margin-right: 30px;
}
.send {
	width: 140px;
    height: 50px;
    line-height: 50px;
    font-size: 16px;
    border-radius: 4px 4px;
    border: 1px solid #e5e5e5;
    background: #fbfbfb;
    text-align: center;
    cursor: pointer;
    float: right; 
    background: #00B2EE; 
    color: #FFFFFF;
}
</style>
<%@ include file="/jsp/topForList.jsp"%>


<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">关键词</div>
		<div class="searchFieldCtr">
			<input  id="keyword" name="keyword" placeholder="模糊查询" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<bp:dicCheckBox dicId="demand_status" controlName="d_status" initValue="0,1" controlId="d_status"></bp:dicCheckBox>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
	
<div class="main_content" style="width: 680px; float: left;">
	<div id="gridBox" style="margin:0; padding:0;"></div>
</div>
<div style="width:450px; text-align: left; margin:3px; padding:5px; border: 1px solid #D6D6D6; font-size:14px; line-height: 24px; overflow-y: scroll;">
	<div class="layim-chat-main">
		<div id="userDetailDiv">
			
		</div>
	</div>
	<textarea id="l_memo" style="display: none; width: 100%; height: 60px; margin-top:10px;" id="l_memo" placeholder="回复内容"></textarea>
	<select id="d_status" name="d_status" class="sel" style="display: none;">
		<option value="">--请选择需求状态--</option>
		<option value="1">已回复</option>
		<option value="2">关闭</option>
	</select>
	<button id="sendAnswer" style="display: none; color: #FFFFFF; background: #00B2EE; padding: 5px 40px; float: right; margin-bottom: 40px;">回复</button>
	
</div>

<script type="text/javascript">
function setVal(str) {
	$("#l_memo").val(str); 
}
var grid;
var itemsStr = "[ "
	+ "{ text: '处理', click: dicOper, icon: 'modify'}]";
$(function () {
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'会员账号',name:'m_name',width:160},
			{display:'提交时间',name:'create_time',width:125},
			{display:'工单标题',name:'d_title',width:260},
			{display:'状态',name:'d_status',width:80,render:function(r,n,v){return v=='0'&&'待处理'||v=='1'&&'已回复'||v=='2'&&'已关闭';}}
		],
		sortName: 'id',
	   	sortOrder: 'desc',
		url: "${rootPath}/member/demand",
		method: "get",
		rownumbers: true,
		onSelectRow: showUserDetail,
		delayLoad: true
	});
      
	$('#btnOK').trigger('click');
});

$("#userDetailDiv").height($(window).height() - 300);
$(".main_content").height($(window).height() - 100);

var curDemandId = 0;
function showUserDetail() {
	var obj = grid.getSelected();
	
	var id = obj.id;
	curDemandId = id;
	$.reqUrl("${rootPath}/member/showDemand", {id: id}, function(rst) {
		var talks = "";
		for(var i = 0; i < rst.demandLogList.length; i++) {
			var logObj = rst.demandLogList[i];
			
			talks += genOneTalk(logObj.log_type, logObj.create_time, logObj.l_memo, logObj.l_memo_type);
		}
		$("#userDetailDiv").html(talks);
		$("#l_memo").show();
		$("#sendAnswer").show();
		$("#d_status").show();
		$(".yrzCls").show();
		$('.layim-chat-main').scrollTop(1000000000);
	});
}


function genOneTalk(log_type, create_time, l_memo, l_memo_type) {
	var talks = "";
	talks += '<li class="' + (log_type == 1 ? 'layim-chat-mine' : 'layim-chat-system') + '"><div class="layim-chat-user"><div class=';
	talks += (log_type == 1 ? '"chat-user-me">' + '我' : '"chat-user-plat">' + '客');
	talks += '</div><cite><i>' + create_time + '</i></cite></div><div class="layim-chat-text">';
	talks += l_memo;
	talks += (l_memo_type * 1 == 2 ? "<br><br><a target='_blank' href='" + l_memo + "'>查看附件[!!!请开启杀毒软件，同时跟财务隔离!!!]</a>" : "");
	talks += '</div></li>';
	return talks;
}

$("#sendAnswer").click(function(){
	sendDemand();				
});
function sendDemand() {
	var l_memo = $("#l_memo").val();
	
	if (isNull(l_memo)) {
		layer.tips("内容必填", '#l_memo', {
		  tips: [1, '#3595CC'],
		  time: 4000
		});
		$("#l_memo")[0].focus()
		return;
	}
	
	
	var d_status = $("#d_status").val();
	
	if (isNull(d_status)) {
		layer.tips("状态必选", '#d_status', {
		  tips: [1, '#3595CC'],
		  time: 4000
		});
		$("#d_status")[0].focus()
		return;
	}
	
	layer.load(2, {shade: false});
	$.ajax({
		type : "POST",
		async : true,   //异步请求（同步请求将会锁住浏览器，用户其他操作必须等待请求完成才可以执行）
		url : "${rootPath}/member/addDemandLog",    //请求发送到TestServlet处
		data : "{\"demand_id\": \"" + curDemandId + "\", " 
				+ "\"d_status\": " + d_status + ", " 
				+ "\"l_memo\": \"" + l_memo.replace(/\n/g,"<br />").replace(/\"/g,"'")  + "\"}",
		dataType : "json",        //返回数据形式为json
		contentType: "application/json",
		success : function(result) {
			layer.closeAll();
			//请求成功时执行该函数内容，result即为服务器返回的json对象
			if (result) {
				if (result.state != 1) {
		    		layer.tips(result.msg, '.send', {
		  			  tips: [1, '#3595CC'],
		  			  time: 4000
		  			});
					return;
				} else {
					//layer.msg("回复成功");
					$("#l_memo").val("")
					// 把最新的信息append上去
					var newTalk = genOneTalk(1, getNowFormatDate(), l_memo.replace(/\n/g,"<br />"));
					$("#userDetailDiv").append(newTalk);
					$('.layim-chat-main').scrollTop(1000000000);
				}
			}
				
        },
		error : function(errorMsg) {
			layer.closeAll();
			layer.msg(JSON.stringify(errorMsg), {
				anim: 6
			});
		}
	})
}
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
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
