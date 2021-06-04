<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style>
	#otcBankQrcode img {
		display: inline !important;
	}
</style>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}/member/coin_withdraw_edit_do" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">	
						<div class="div_block">
							<p class="p_block"><label class="label_block">货币代码：</label>
								<input id="currency" type="text" name="currency" maxlength="20" class="txt required txt_readonly" value="${info.currency}" readonly="readonly" />
								<input id="member_id" type="hidden" name="member_id" value="${info.member_id }">
							</p>
							<p class="p_block"><label class="label_block">会员账号：</label>
								<input id="m_name" type="text" name="m_name" maxlength="100" class="txt required txt_readonly" value="" readonly="readonly" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">提币地址：</label>
								<input id="member_coin_addr" type="text" name="member_coin_addr" class="txt required txt_readonly" value="${displayaddr}" readonly="readonly" style="width: 592px;" />
								<br>
								<div style="font-size:80px; float:right; padding:5px 180px 0 0; color:red; font-weight:bold;">${info.currency}</div>
								<div id="withdrawAddrQrcode" style="padding: 30px 0 0 110px;">
								</div>
							</p>
						</div>
						
						<div class="div_block" style="${is_in_eth == '2' ? '' : 'display: none;'}">
							<p class="p_block" style="width: 100%;"><label class="label_block" style="color: blue; font-weight: bold;">终端标签：</label>
								<input type="text" class="txt required txt_readonly" value="${info.member_coin_addr_label}" readonly="readonly" style="width: 592px;" />
							</p>
						</div>
						
						<div class="div_block" style="padding: 10px 0 0 0; margin-bottom: 15px; border-bottom: 1px solid #ccc; ${info.otc_ads_id > 0 ? '' : 'display: none;'}">
							<p class="p_block" style="width: 100%; text-align: left; padding-left:112px;">
								<div style="padding: 3px; border: 1px solid #ccc; margin: 2px 60px;">
									OTC对方姓名：${info.otc_owner_name}。卖出数量：<span style="font-weight: bold; color: #0000FF; font-size: 16px;">${info.otc_volume}</span>${info.otc_oppsite_currency}<br>
								</div>
								<div style="width: 100%; text-align: center;">
									<div style="width: 240px; border: 1px solid #0000FF; margin: 5px; padding: 5px; font-size:14px; display: none;" id="bankDiv">
										<span style="color: blue; font-weight: bold;">银行卡</span>
										<br><br>
										<div style="width: 100%; text-align: left;" id="otcBankCardNo"></div>
										<div style="width: 100%; text-align: left;" id="otcBankCardName"></div>
										<div style="width: 100%; text-align: left;" id="otcBankCardBank"></div>
										<br>
										<div id="otcBankQrcode" style="display: none;">
										</div>
										<label style="cursor: pointer; color: blue; text-decoration: underline;" onclick="showBankBigQr()">查看二维码</label>
									</div>
									<div style="width: 240px; border: 1px solid #0000FF; margin: 5px; padding: 5px; font-size:14px; display: none;" id="aliDiv">
										<span style="color: blue; font-weight: bold;">支付宝 </span>
										<br><br>
										<div style="width: 100%; text-align: left;" id="otcAlipayNo"></div>
										<div style="width: 100%; text-align: left;" id="otcAlipayName"></div>
										<br>
										<div id="otcAlipayQrcode" style="display: none;">
										</div>
										<label style="cursor: pointer; color: blue; text-decoration: underline;" onclick="showAlipayBigQr()">查看二维码</label>
									</div>
									<div style="width: 240px; border: 1px solid #0000FF; margin: 5px; padding: 5px; font-size:14px; display: none;" id="wxDiv">
										<span style="color: blue; font-weight: bold;">微信</span>
										<br><br>
										<div style="width: 100%; text-align: left;" id="otcWxpayNo"></div>
										<div style="width: 100%; text-align: left;" id="otcWxpayName"></div>
										<br>
										<div id="otcWxpayQrcode" style="display: none;">
										</div>
										<label style="cursor: pointer; color: blue; text-decoration: underline;" onclick="showWxBigQr()">查看二维码</label>
									</div>
								</div>
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">提币数量：</label>
								<input id="w_amount" type="text" name="w_amount" maxlength="20" class="txt required txt_readonly" value="<fmt:formatNumber value ="${info.w_amount}" pattern="#.###" minFractionDigits="8" />" readonly="readonly" />
							</p>
							<p class="p_block"><label class="label_block">手续费：</label>
								<input id="w_fee" type="text" name="w_fee" maxlength="20" class="txt required txt_readonly" value="<fmt:formatNumber value ="${info.w_fee}" pattern="#.###" minFractionDigits="8" />" readonly="readonly" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">实际到账：</label>
								<input id="actualRecive" type="text" name="actualRecive" maxlength="20" style="font-size: 16px; font-weight: bold; color: blue;" class="txt required txt_readonly" value="<fmt:formatNumber value ="${info.w_amount - info.w_fee}" pattern="#.###" minFractionDigits="8" />" readonly="readonly" />
							</p>
						</div>
											
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">状态：</label>
								<select name="w_status" id="w_status" class="sel">
									<%--<c:if test="${user.id ==1}"><option value="1">已完成</option></c:if>--%>
									<option value="1">已完成</option>
									<c:if test="${info.otc_ads_id > 0}"><option value="4">已付款</option></c:if>
									<option value="2">已取消</option>
									<option value="3">处理中</option>
								</select>
							</p>
							<p class="p_block reject_reason_p" style="display: none;"><label class="label_block">取消原因：</label>
								<input id="reject_reason" type="text" name="reject_reason" maxlength="20" class="txt required" value="" />
							</p>
						</div>
						
						<div class="div_block w_txid_div">
							<p class="p_block" style="width: 100%;"><label class="label_block">TXID：</label>
								<input id="w_txid" type="text" name="w_txid" maxlength="100" class="txt required" value ="${info.w_txid}" style="width: 592px;" />
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="button" class="btn_a btn_a1" name="btn_submit" id="transferAccounts" value="转账"
                               onclick="transfer(${info.id})" style="${(info.w_txid == '' or info.w_txid == null)? '' : 'display: none;'}" />
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" style="${info.w_status == 0 or info.w_status == 3 or (user.id==1 and info.w_status == 4) ? '' : 'display: none;'}" />
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
						<input type="button" class="btn_a btn_a1" onclick="transferAddrCheck(${info.id});" name="btn_reset"  value="地址校验" />
					</p>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script src="/resources/js/qrcode.min.js" type="text/javascript"></script>
<script>
$("#w_status").change(function () {  
    var w_status = $(this).children('option:selected').val();
    if (w_status == "1") {  
		$(".reject_reason_p").hide();
		$("#reject_reason").val("");
		$(".w_txid_div").show();
    } else if (w_status == "2") {  
    	$(".reject_reason_p").show();
		$(".w_txid_div").hide();
		$("#w_txid").val("");
    } else if (w_status == "3") { 
    	$(".reject_reason_p").hide();
		$(".w_txid_div").hide();
		$("#w_txid").val("");
		$("#reject_reason").val("");
    }  else if (w_status == "4") {  
		$(".reject_reason_p").hide();
		$("#reject_reason").val("");
		$(".w_txid_div").show();
    }
});

function transfer(id) {
    $.ajax({
        type:"get",
        url:"${rootPath}/member/transfer",
        dataType: 'Json',
        contentType: "application/json",
        data:{
            "id":id
        },
        success:function(dataa) {
            var objs=eval(dataa); //解析json对象
            layer.alert(objs.msg);
        },
        error:function() {
            layer.alert(objs.msg);
        }
    });
}

function transferAddrCheck(id) {
    $.ajax({
        type: "get",
        url: "${rootPath}/member/transfer/addrCheck",
        dataType: 'Json',
        contentType: "application/json",
        data: {
            "id": id,
            "addr": $("#member_coin_addr").val(),
        },
        success: function (dataa) {
            //解析json对象
            var objs = eval(dataa);
            if (objs.state != 1) {
                layer.alert(objs.msg);
            }else {
                layer.msg(objs.msg, 2, 1);
            }

        },
        error: function () {
            layer.alert(objs.msg);
        }
    });
}


if ("${info.currency}" == "WKC") {
	$(".w_from_addr_div").show();
}
$("#m_name").val(window.parent.curMName);

try{
	new QRCode(document.getElementById("withdrawAddrQrcode"),{text: "${displayaddr}",width:100,height:100});
}catch(err){}

<c:if test="${not empty error }">
layer.alert("${error}");
</c:if>

<c:if test="${info.otc_ads_id > 0 }">
	var coinAddr = "${info.member_coin_addr}";
	var coinAddrArr = coinAddr.split("→");
	for (var i = 0; i < coinAddrArr.length; i++) {
		if (coinAddrArr[i]) {
			var coinAddrArrArr = coinAddrArr[i].split("▲");
			if (coinAddrArrArr[0] == "bank") {
				$("#otcBankCardNo").html("卡号：" + coinAddrArrArr[2]);
				$("#otcBankCardName").html("姓名：" + coinAddrArrArr[1]);
				$("#otcBankCardBank").html("银行：" + coinAddrArrArr[3]);
				try{
					new QRCode(document.getElementById("otcBankQrcode"),{text: coinAddrArrArr[2],width:300,height:300});
				}catch(err){}
				$("#bankDiv").css('display','inline-block');
			} else if (coinAddrArrArr[0] == "wxpay") {
				$("#otcWxpayNo").html("卡号：" + coinAddrArrArr[2]);
				$("#otcWxpayName").html("姓名：" + coinAddrArrArr[1]);
				$("#otcWxpayQrcode").html("<img src='" + coinAddrArrArr[3] + "' style='width: 300px;' />");
				$("#wxDiv").css('display','inline-block');
			} else if (coinAddrArrArr[0] == "alipay") {
				$("#otcAlipayNo").html("卡号：" + coinAddrArrArr[2]);
				$("#otcAlipayName").html("姓名：" + coinAddrArrArr[1]);
				$("#otcAlipayQrcode").html("<img src='" + coinAddrArrArr[3] + "' style='width: 300px;' />");
				$("#aliDiv").css('display','inline-block');
			}
		}
	}
</c:if>

function showAlipayBigQr() {
	layer.alert($("#otcAlipayQrcode").html());
}
function showWxBigQr() {
	layer.alert($("#otcWxpayQrcode").html());
}
function showBankBigQr() {
	layer.alert($("#otcBankQrcode").html());
}
</script>