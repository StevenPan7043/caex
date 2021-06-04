<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<input id="OTC" name="OTC" type="hidden" value="${OTC }" class="txt enterAsSearch" />
	<input id="otc_owner_id" name="otc_owner_id" type="hidden" value="${otc_owner_id }" class="txt enterAsSearch" />
	<div class="searchField">
		<div class="searchFieldLbl">虚拟币</div>
		<div class="searchFieldCtr">
			<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input id="id" name="id" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">地址</div>
		<div class="searchFieldCtr">
			<input id="member_coin_addr" name="member_coin_addr" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">TXID</div>
		<div class="searchFieldCtr">
			<select id="w_txid" name="w_txid" class="sel">
				<option value="">--请选择--</option>
				<option value="1">为空</option>
				<option value="2">非空</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">划转状态</div>
		<div class="searchFieldCtr">
			<select id="transfer_no" name="transfer_no" class="sel">
				<option value="">--请选择--</option>
				<option value="0">未划转</option>
				<option value="1">待划转</option>
				<option value="2">划转成功</option>
				<option value="3">划转失败</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">状态</div>
		<div class="searchFieldCtr">
			<select id="w_status" name="w_status" class="sel">
				<option value="">--请选择--</option>
				<option value="0">待处理</option>
				<option value="1">已完成</option>
				<option value="2">已取消</option>
				<option value="3">处理中</option>
				<option value="4">已付款</option>
			</select>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
	
<div class="main_content" style="width: 730px; float: left;">
	<div id="gridBox" style="margin:0; padding:0;"></div>
</div>
<div ID="userDetailDiv" style="width:460px; text-align: left; margin:3px; padding:5px; border: 1px solid #D6D6D6; font-size:14px; line-height: 30px; overflow-y: scroll;">
	
</div>

<script type="text/javascript">
var grid,manage;
var itemsStr = "[ ";
	if ("${OTC }" == "1") {
		itemsStr += "{ text: '审核', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='JYOTC-XNBTX-SH'/>},{ text: '批量转账', click: transfer, icon: 'ok' <bp:buttonAndColumnPermission functionId='JYOTC-XNBTX-SH'/>},";
	} else {
		itemsStr += "{ text: '审核', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='CZTX-XNBTX-SH'/>},{ text: '批量转账', click: transfer, icon: 'ok' <bp:buttonAndColumnPermission functionId='JYOTC-XNBTX-SH'/>},";
	}
itemsStr += "{ text: '报表导出', click: dicOper, icon: 'logout' <bp:buttonAndColumnPermission functionId='CZTX-XNBTX-SH'/>}]";

$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = manage= $("#gridBox").ligerGrid({
		columns: [
			{display:'id',name:'id',width:50},
			{display:'虚拟币',name:'currency',width:60},
			{display:'会员账号',name:'m_name',width:120},
			{display:'提现/到账数量',name:'w_amount',width:100, align:'right',render:function(r,n,v){return v + " / <span style='color: blue; font-weight: bold;'>" + (r.w_amount - r.w_fee).toFixed(4) + "</span>";}},
			//{display:'提现地址',name:'member_coin_addr',width:260},	
			{display:'提现时间',name:'w_create_time',width:120},
			{display:'状态',name:'w_status',width:70,render:function(r,n,v){return v=='1'&&"<span style='color: red; font-weight: bold;'>"+'已完成'+ "</span>"||v=='0'&&"<span style='color: green; font-weight: bold;'>"+'待处理'+ "</span>"||v=='2'&&'已取消'||v=='3'&&'处理中'||v=='4'&&'已付款';}},
			{display:'TXID',name:'w_txid',width:70},
            {display:'划转状态',name:'transfer_no',width:70,render:function(r,n,v){return (v=='0'&&'未划转'||v=='1'&&'待划转'||v=='2'&&'划转成功'||v=='3'&&'划转失败')}},

            //{display:'取消原因',name:'reject_reason',width:150}
		],
		sortName: 'id',
	  	checkbox: true,
	   	sortOrder: 'desc',
		url: "${rootPath}/member/coin_withdraw",
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
			var item = {text: '审核'};
			<bp:displayPermission functionId='CZTX-XNBTX-SH'>dicOper(item);</bp:displayPermission>
		},
          onTransferlClickRow : function (data, rowindex, rowobj)
          {
              var item = {text: '批量转账'};
              <bp:displayPermission functionId='CZTX-XNBTX-SH'>transfer(item);</bp:displayPermission>
          },
		delayLoad: true,
		toolbar:{  items: eval(itemsStr)  },
	});
      
	$('#btnOK').trigger('click');
	
	$("#userDetailDiv").height($(window).height() - 100);
});

var curMName = "";
function dicOper(item){
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
        
	switch (item.text) {
		case "审核":
			if (!row) {
				layer.alert("请先选择一条记录");
				return;
			}
			curMName = row.m_name;
	        f_common_edit($("#gridBox"), "${rootPath}/member/coin_withdraw_edit?id={id}", false, 950, 720, "审核");
	        break;
        case "报表导出":

            var m_name = $("#m_name").val() === undefined ? '' : $("#m_name").val();
            var currency = $("#currency").val() === undefined ? '' : $("#currency").val();
            var id = $("#id").val() === undefined ? '' : $("#id").val();
            var member_coin_addr = $("#member_coin_addr").val() === undefined ? '' : $("#member_coin_addr").val();
            var w_txid = $("#w_txid").val() === undefined ? '' : $("#w_txid").val();
            var transfer_no = $("#transfer_no").val() === undefined ? '' : $("#transfer_no").val();
            var w_status = $("#w_status").val() === undefined ? '' : $("#w_status").val();
			var OTC = "${OTC}"
            var params = "m_name="+m_name
                + "&currency="+currency
                + "&member_id="+id
                + "&member_coin_addr="+member_coin_addr
                + "&w_txid="+w_txid
                + "&transfer_no="+transfer_no
                + "&OTC="+OTC
                + "&w_status="+w_status;
            window.location.href = "${rootPath}/member/coinWithdraw/export?"+params;

            break;
    }
}
function transfer(item){
    debugger
    var obj = manage.selected;
    if(obj.length <= 0) {
        layer.alert('至少选择一个！');
        return;
    }
    var ids = "";
    for (var i = 0; i < obj.length; i++) {
        if (obj[i].w_txid != "" && obj[i].w_txid != null) {
            layer.alert('批量转账需筛选TXID字段为空的数据！');
            return;
        }
        ids += obj[i].id + ",";
    }
	ids=ids.substring(0,ids.length-1);
   console.log(ids);
    $util.sure("您确定对【"+obj.length+"】笔进行批量转账吗？", function() {
        var url = '${rootPath}/member/transfer';
        $.ajax({
            method: 'get',
            url: url,
            dataType: "json",
            contentType:"application/json",
            data: {
                "ids":ids
            },
            success: function (res) {
                $.submitShowResult(res);
                try {
                    $hook.opeRefresh("ope_refresh","gridBox","searchDiv");
                } catch (e) {
                    layer.alert(e);
                }
            },
            error: function (res) {
                $.submitShowResult(res);
            }
        })
    });
}
function showUserDetail() {
	var obj = grid.getSelected();

	var tmp = obj.member_coin_addr.split('→');
	var withdrawAddress = '';
	for(var i=0; i<tmp.length; i++) {
	    if(tmp[i]) {
            var info = tmp[i].split('▲');
            if(info[0] == 'bank') {
                withdrawAddress = '卡号：' + info[2] + '<br>姓名：' + info[1] + '<br> 银行：' + info[3];
			}else if(info[0] == 'alipay' || info[0] == 'wxpay'){
                withdrawAddress = '卡号：' + info[2] + '<br>姓名：' + info[1] + '<br><img src=" ：' + info[3] + '">';
			}
            $("#userDetailDiv").html('提现地址：<br>' + withdrawAddress);
		}
	}


	if (obj.reject_reason) {
		$("#userDetailDiv").append("取消原因：" + obj.reject_reason + "<br>");
	}
	$("#userDetailDiv").append("<br><hr><br>");
	
	var id = obj.member_id;
	$.reqUrl("${rootPath}/member/member_trace", {memberId: id}, function(rst) {
		var talks = "UID|账号：<%=HelpUtils.PRE_INTRODUCE_ID%>" + rst.id + " | " + rst.m_name + "<br>";
		if (rst.id_status * 1 > 0) {
			talks +=  "姓名：" + rst.family_name + " | " + rst.middle_name + " | " + rst.given_name + "<br>";
			talks +=  "身份证：" + rst.id_number + "<br>";
			talks +=  "照片：<a href='${curIDCardDomain }" + rst.id_front_img + "' target='_blank'>正面</a> | <a href='${curIDCardDomain }" + rst.id_back_img + "' target='_blank'>反面</a> | <a href='${curIDCardDomain }" + rst.id_handheld_img + "' target='_blank'>手持</a><br>";
			
		}
		talks +=  "注册/最后登录时间：" + rst.reg_time + " | " + rst.last_login_time + "<br>";
		talks +=  "最后IP/介绍人：" + rst.last_login_ip + " | " + (rst.introduce_m_id ? ("<%=HelpUtils.PRE_INTRODUCE_ID%>" + rst.introduce_m_id) : "无") + "<br>";
		talks +=  "认证状态：" + (rst.id_status == 1 ? rst.id_status : 0) + "级认证(1级为已身份认证)";
		
		talks += "<br><br>资产情况：<br>";
		for(var i = 0; i < rst.accountsLst.length; i++) {
			var logObj = rst.accountsLst[i];
			
			talks += "<div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" 
			+ logObj.currency + "总：</div><div style='width: 160px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.total_balance 
			+ "</span>&nbsp;&nbsp;&nbsp;&nbsp;冻结：</div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.frozen_balance + "</span></div><br>";
		}


/* 		talks += "<br><br>充值汇总：<br>";
		for(var i = 0; i < rst.rechargeSum.length; i++) {
			var logObj = rst.rechargeSum[i];

			talks += "<div style='width: 70px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>"
					+ logObj.currency + "</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.sum + "</span></div><br>";
		}
		if (rst.rechargeSum.length == 0) {
			talks += "无";
		} */

		
		talks += "<br><br>充值记录：<br>";
		for(var i = 0; i < rst.rechargeLst.length; i++) {
			var logObj = rst.rechargeLst[i];
			
			talks += "<div style='width: 70px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" 
			+ logObj.currency + "</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.r_amount + "</span></div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>" + logObj.r_create_time + "</div><br>";
		}
		if (rst.rechargeLst.length == 0) {
			talks += "无";
		}

		talks += "<br><br>提现汇总：<br>";
		for(var i = 0; i < rst.withdrawSum.length; i++) {
			var logObj = rst.withdrawSum[i];

			talks += "<div style='width: 70px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>"
					+ logObj.currency + "</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>" + logObj.sum + "</span></div><br>";
		}
		if (rst.withdrawSum.length == 0) {
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
		
		$("#userDetailDiv").append(talks);
	});
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
