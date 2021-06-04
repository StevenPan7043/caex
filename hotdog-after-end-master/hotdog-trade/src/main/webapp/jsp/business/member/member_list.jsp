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
		<div class="searchFieldLbl">姓名</div>
		<div class="searchFieldCtr">
			<input  id="real_name" name="real_name" type="text" class="txt enterAsSearch" placeholder="模糊查询" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input id="id" name="id" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="auth_grade" name="auth_grade" class="sel">
				<option value="">--认证等级--</option>
				<option value="0">未认证</option>
				<option value="1">1</option>
				<option value="2">2</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="m_status" name="m_status" class="sel">
				<option value="">--会员状态--</option>
				<option value="0">手机未验证</option>
				<option value="1">正常</option>
				<option value="2">冻结</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="api_limit" name="api_limit" class="sel">
				<option value="">--API刷单--</option>
				<option value="0">频率限制</option>
				<option value="1">不限制频率</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="api_status" name="api_status" class="sel">
				<option value="">--API状态--</option>
				<option value="1">正常</option>
				<option value="2">冻结</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="trade_commission" name="trade_commission" class="sel">
				<option value="">--手续费折扣--</option>
				<option value="0">0</option>
				<option value="0.05">0.05</option>
				<option value="0.1">0.1</option>
				<option value="0.2">0.2</option>
				<option value="0.3">0.3</option>
				<option value="0.4">0.4</option>
				<option value="0.5">0.5</option>
				<option value="0.6">0.6</option>
				<option value="0.7">0.7</option>
				<option value="0.8">0.8</option>
				<option value="0.9">0.9</option>
				<option value="1">1</option>
			</select>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
	

<div class="main_content" style="width: 540px; float: left;">
	<div id="gridBox" style="margin:0; padding:0;"></div>
</div>
<div ID="userDetailDiv" style="width:460px; text-align: left; margin:3px; padding:5px; border: 1px solid #D6D6D6; font-size:14px; line-height: 30px; overflow-y: scroll;">
	
</div>

<script type="text/javascript">
var grid;
var itemsStr = "[ "
	+ "{ text: '设置', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='HYGL-HYGL-SZ'/>}]";
$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = $("#gridBox").ligerGrid({
		columns: [
			{display:'UID',name:'id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
			{display:'会员账号',name:'m_name',width:150},
			{display:'会员姓名',name:'real_name',width:100},
			//{display:'身份证明号',name:'id_number',width:200},
			//{display:'注册时间',name:'reg_time',width:125},
			//{display:'最后登录时间',name:'last_login_time',width:125},
			//{display:'最后登录IP',name:'last_login_ip',width:120},
			//{display:'认证等级',name:'auth_grade',width:70},
			{display:'佣金折扣',name:'trade_commission',width:70},
			{display:'会员状态',name:'m_status',width:70,render:function(r,n,v){return v=='1'&&'正常'||v=='0'&&'未验证'||v=='2'&&'冻结';}},
			{display:'API刷单',name:'api_limit',width:70,render:function(r,n,v){return v=='0'&&'频率限制'||v=='1'&&'不限制频率';}},
			//{display:'API状态',name:'api_status',width:80,render:function(r,n,v){return v=='1'&&'正常'||v=='2'&&'冻结';}}
		],
		sortName: 'm.id',
	   	sortOrder: 'desc',
		url: "${rootPath}/member/member",
		onSelectRow: showUserDetail,
		method: "get",
		rownumbers: true,
		onContextmenu : function (parm,e)
        {
            menu.show({ top: e.pageY, left: e.pageX });
            return false;
        },
        onDblClickRow : function (data, rowindex, rowobj)
		{
			var item = {text: '设置'};
            <bp:displayPermission functionId='HYGL-HYGL-SZ'>dicOper(item);</bp:displayPermission>
		},
		delayLoad: true,
		toolbar:{  items: eval(itemsStr)  },
	});
      
	$('#btnOK').trigger('click');
	
	$("#userDetailDiv").height($(window).height() - 100);
});
function showUserDetail() {
	var obj = grid.getSelected();
	
	var id = obj.id;
    $.reqUrl("${rootPath}/member/member_trace", {memberId: id}, function (rst) {
        var talks = "UID|账号：<%=HelpUtils.PRE_INTRODUCE_ID%>" + rst.id + " | " + rst.m_name + "<br>";
        if (rst.id_status * 1 > 0) {
            talks += "姓名：" + rst.family_name + " | " + rst.middle_name + " | " + rst.given_name + "<br>";
            talks += "身份证：" + rst.id_number + "<br>";
            talks += "照片：<a href='" + rst.id_front_img + "' target='_blank'>正面</a> | <a href='" + rst.id_back_img + "' target='_blank'>反面</a> | <a href='" + rst.id_handheld_img + "' target='_blank'>手持</a><br>";

        }
        talks += "注册/最后登录时间：" + rst.reg_time + " | " + rst.last_login_time + "<br>";
        talks += "安全密码修改时间：" + (rst.last_mod_pwd_time ? rst.last_mod_pwd_time : "未设置") + "<br>";
        talks += "最后IP/介绍人：" + rst.last_login_ip + " | " + (rst.introduce_m_id ? ("<%=HelpUtils.PRE_INTRODUCE_ID%>" + rst.introduce_m_id) : "无") + "<br>";
        talks += "认证状态：" + (rst.id_status == 1 ? rst.id_status : 0) + "级认证(1级为已身份认证)<br><br>";
        talks += "<br>支付信息：<br>";
        for (var i = 0; i < rst.otcAccountInfoLst.length; i++) {
            var logObj = rst.otcAccountInfoLst[i];
            talks += "<div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>";
            if (logObj.type == 1) {
                talks += "银行卡:&nbsp;&nbsp;" + logObj.name + "</div>";
            } else if (logObj.type == 2) {
                talks += "支付宝:&nbsp;&nbsp;" + logObj.name + "</div>";
            } else if (logObj.type == 3) {
                talks += "微信:&nbsp;&nbsp;" + logObj.name + "</div>";
            }
            talks += "<div style='width: 160px; text-align: center; display: inline-block; border-bottom: 1px solid #ccc;'>账号：<span style='color: blue; font-weight: bold;'>" + logObj.account
                + "</span></div>";
            if (logObj.type == 1) {
                talks += "<div style='width: 140px; text-align: left; display: inline-block; border-bottom: 1px solid #ccc;'>开户行：<span style='color: blue; font-weight: bold;'>" + logObj.bank_or_img + "</span></div>";
            }
            talks += "<br>";
        }
		talks +=  "<div style='color:gray;'>最近验证码：" + obj.sms_code + "<br></div>";
		
		talks += "<br><br>资产情况：<br>";
		for(var i = 0; i < rst.accountsLst.length; i++) {
			var logObj = rst.accountsLst[i];
			
			talks += "<div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" 
			+ logObj.currency + "总：</div><div style='width: 160px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.total_balance 
			+ "</span>&nbsp;&nbsp;&nbsp;&nbsp;冻结：</div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.frozen_balance + "</span></div><br>";
		}
		talks += "<br><br>法币资产情况：<br>";
		for(var i = 0; i < rst.otcAccountsLst.length; i++) {
			var logObj = rst.otcAccountsLst[i];
			
			talks += "<div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" 
			+ logObj.currency + "总：</div><div style='width: 160px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.total_balance 
			+ "</span>&nbsp;&nbsp;&nbsp;&nbsp;冻结：</div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.frozen_balance + "</span></div><br>";
		}
		
		
		talks += "<br><br>充值记录：<br>";
		for(var i = 0; i < rst.rechargeLst.length; i++) {
			var logObj = rst.rechargeLst[i];
			
			talks += "<div style='width: 70px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" 
			+ logObj.currency + "</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.r_amount + "</span></div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" + logObj.r_create_time + "</div><br>";
		}
		if (rst.rechargeLst.length == 0) {
			talks += "无";
		}
		
		talks += "<br><br>提现记录：<br>";
		for(var i = 0; i < rst.withdrawLst.length; i++) {
			var logObj = rst.withdrawLst[i];
			
			talks += "<div style='width: 70px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" 
				+ logObj.currency + "</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.w_amount + "</span></div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" + logObj.w_create_time + "</div><div style='width: 80px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" + (logObj.w_status == 0 ? "待处理" : (logObj.w_status == 2 ? "<span style='color: gray;'>已取消</a>" : (logObj.w_status == 3 ? "处理中" : "<span style='color: blue;'>已完成</a>"))) + "</div><br>";
		}
		if (rst.withdrawLst.length == 0) {
			talks += "无";
		}
		
		$("#userDetailDiv").html(talks);
	});

}

function dicOper(item){
    debugger
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
	switch (item.text) {
		case "设置":
	        f_common_edit($("#gridBox"), "${rootPath}/member/member_edit?id={id}", false, 750, 400, "设置");
	        break; 
    }
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
