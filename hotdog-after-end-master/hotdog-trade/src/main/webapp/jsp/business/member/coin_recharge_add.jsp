<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}/member/coin_recharge_add_do" id="wanwuForm">
					<input id="id" type="hidden" name="id" value="${coinRecharge2.id }" />
					<div class="itembox">	
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">会员：</label>
								<input id="m_name" type="text" name="m_name" maxlength="200" style="width: 495px;" class="txt required txt_readonly txt_choose" readonly="readonly" value="${member.id > 0 ? member.m_name : "" }${member.id > 0 ? "--" : "" }${member.id > 0 ? member.id : "" }" />
								<input id="member_id" type="hidden" name="member_id" value="${member.id > 0 ? member.id : "" }" />
							</p>
						</div>
						
						<div class="div_block" style="padding: 10px 0 0 0; margin-bottom: 15px; border-bottom: 1px solid #ccc; ${authIdentity.id > 0 ? '' : 'display: none;'}">
							<p class="p_block" style="width: 100%; text-align: center; color: blue;">
								当前客户认证姓名：${authIdentity.family_name}-${authIdentity.middle_name}-${authIdentity.given_name}
							</p>
						</div>
						<div class="div_block" style="padding: 10px 0 0 0; margin-bottom: 15px; border-bottom: 1px solid #ccc; ${coinRecharge2.otc_ads_id > 0 ? '' : 'display: none;'}">
							<p class="p_block" style="width: 100%; text-align: left; padding-left:112px;">
								OTC信息：<br>
								对手币种：${coinRecharge2.otc_oppsite_currency}，价格：${coinRecharge2.otc_price}，OTC对方姓名：${coinRecharge2.otc_owner_name}。<br>
								应收金额(收到该金额才能入账)：<span style="font-weight: bold; color: #0000FF; font-size: 16px;">${coinRecharge2.otc_money}</span>
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">币种：</label>
								<select name="currency" id="currency" class="sel ww_select ww_select_text required" data="{code:'currency|id',initValue:'${coinRecharge2.currency }'}" ${empty coinRecharge2.id ? '' : 'disabled="disabled"'}><option value="">请选择...</option></select>
							</p>
							<p class="p_block"><label class="label_block">充值金额：</label>
								<input id="r_amount" type="text" name="r_amount" maxlength="18" class="txt required number ${member.id > 0 ? 'txt_readonly' : '' }" ${member.id > 0 ? 'readonly="readonly"' : '' } value="${member.id > 0 ? coinRecharge2.r_amount : "" }" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">充值类型：</label>
								<select name="r_address" id="r_address" class="sel required" ${empty coinRecharge2.id ? '' : 'disabled="disabled"'}>
									<option value="">--请选择--</option>
									<option value="SYS_RECHARGE" ${!empty coinRecharge2.id && coinRecharge2.r_address != 'MAN_RECHARGE' && coinRecharge2.r_address != 'DATALAB' && coinRecharge2.r_address != 'SYS_REWARD' && coinRecharge2.r_address != 'REG_REWARD'? 'selected="selected"' : '' }>正常充值（钱包漏扫等情况使用）</option>
									<option value="MAN_RECHARGE" ${coinRecharge2.r_address == 'MAN_RECHARGE' ? 'selected="selected"' : '' }>人工充值</option>
									<option value="SYS_REWARD" ${coinRecharge2.r_address == 'SYS_REWARD' ? 'selected="selected"' : '' }>系统奖励</option>
									<option value="REG_REWARD" ${coinRecharge2.r_address == 'REG_REWARD' ? 'selected="selected"' : '' }>注册/推荐奖励</option>
									<option value="REG_REWARD" ${coinRecharge2.r_address == 'DATALAB' ? 'selected="selected"' : '' }>数据实验室</option>
								</select>
							</p>
						</div>
						
						<div class="div_block sysRechargeShow" style="${!empty coinRecharge2.id && coinRecharge2.r_address != 'MAN_RECHARGE' && coinRecharge2.r_address != 'SYS_REWARD' && coinRecharge2.r_address != 'REG_REWARD' ? '' : 'display: none;' }">
							<p class="p_block" style="width: 100%;"><label class="label_block">地址[正常充值填我方地址，OTC是对方转出地址]：</label>
								<textarea id="r_address_" type="text" name="r_address_" maxlength="200" class="txt required ${empty coinRecharge2.id ? '' : 'txt_readonly'}" style="width: 495px; height: 75px;"   ${empty coinRecharge2.id ? '' : 'readonly="readonly"'}>
									<%--${!empty coinRecharge2.id && coinRecharge2.r_address != 'MAN_RECHARGE' && coinRecharge2.r_address != 'SYS_REWARD' ? coinRecharge2.r_address : '' }--%>
								</textarea>
							</p>
							<div id="qrcode" style="display: none">
							</div>
						</div>
						<div class="div_block" style="padding: 10px 0 0 0; margin-bottom: 15px; border-bottom: 1px solid #ccc;">
							<p class="p_block" style="width: 100%; text-align: center; color: blue; cursor: pointer;"  onclick="showRechargeQrcode()">
								查看二维码
							</p>
						</div>
						<c:if test="${coinRecharge2.id > 0 }">
							<div class="div_block">
								<p class="p_block"><label class="label_block" style="color: blue; font-weight: bold;">状态：</label>
									<select name="r_status" id="r_status" class="sel">
										${coinRecharge2.r_status == -1 ? '' : '<option value="1" style="">已确认入账</option>'}
										<option value="2">已取消</option>
									</select>
								</p>
								<p class="p_block reject_reason_p" style="display: none;"><label class="label_block">取消原因：</label>
									<input id="reject_reason" type="text" name="reject_reason" maxlength="20" class="txt required" value="" />
								</p>
							</div>
						</c:if>
						
						<c:if test="${empty coinRecharge2.id }">
							<br><br><br><br>
							<hr class="hrStyle">
							<br><br>
							<div class="div_block">
								<p class="p_block"><label class="label_block">是否冻结：</label>
									<select id="is_frozen" name="is_frozen" class="sel ww_select" data="{code:'bool|id',initValue:'0'}"></select>
								</p>
								<p class="p_block frozenShow" style="display: none;"><label class="label_block">解冻时间：</label>
									<input type="text" id="unfrozen_time" name="unfrozen_time" class="txt txt_datetime required" value="" readonly="readonly"/>
								</p>
							</div>
							<br><br>
							<hr class="hrStyle">
						</c:if>
						
					</div>
					<p class="p_block p_btn">
						<input style="${empty coinRecharge2.r_status || coinRecharge2.r_status == 0 || (coinRecharge2.r_status == -1 && coinRecharge2.otc_ads_id > 0) ? '' : 'display: none;'}" type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>
<script src="/resources/js/qrcode.min.js" type="text/javascript"></script>
<script>
$("#m_name").click(function(){
	openSelectDialog("会员", "m_member", "", 620, 470, false,"1");
});

$(function () {
	var address = '${!empty coinRecharge2.id && coinRecharge2.r_address != 'MAN_RECHARGE' && coinRecharge2.r_address != 'SYS_REWARD' ? coinRecharge2.r_address : '' }';

    var tmp = address.split('→');
    var rechargeAddress = '';
    for(var i=0; i<tmp.length; i++) {
        if(tmp[i]) {
            var info = tmp[i].split('▲');
            if(info[0] == 'bank') {
                rechargeAddress = '卡号：' + info[2] + '\n姓名：' + info[1] + '\n银行：' + info[3];
                try{
                    new QRCode(document.getElementById("qrcode"),{text: info[2],width:300,height:300});
                }catch(err){}
            }else if(info[0] == 'alipay' || info[0] == 'wxpay'){
                rechargeAddress = '卡号：' + info[2] + '\n姓名：' + info[1] + '\n图片链接' + info[3];
                try{
                    new QRCode(document.getElementById("qrcode"),{text: info[3],width:300,height:300});
                }catch(err){}
            }
            $("#r_address_").val('提现地址：\n' + rechargeAddress);
        }
    }
})

function f_selectCommonOK(data, code) {
	if(code == "m_member") {
		$("#member_id").val(data.id);
		$("#m_name").val(data.id + "---" + data.m_name);
		
		if (data.currency) {
			$("#currency").val(data.currency);
		}
	}
	selectDialog.close();
}

$("#r_address").change(function(){
	if ($(this).val() == "SYS_RECHARGE") {
		$(".sysRechargeShow").show();
	} else {
		$(".sysRechargeShow").hide();
	}
});

$('#is_frozen').change(function(){
	if ($('#is_frozen').val() * 1 == 1) {
		$(".frozenShow").show();
	} else {
		$(".frozenShow").hide();
	}
});

$("#r_status").change(function () {  
    var r_status = $(this).children('option:selected').val();
    if (r_status == "1") {  
		$(".reject_reason_p").hide();
		$("#reject_reason").val("");
    } else if (r_status == "2") {  
    	$(".reject_reason_p").show();
    }  
});

function showRechargeQrcode() {
    layer.alert($("#qrcode").html());
}

<c:if test="${member.id > 0 && coinRecharge2.r_address != 'MAN_RECHARGE' && coinRecharge2.r_address != 'SYS_REWARD'}">
	$(".sysRechargeShow").show();
</c:if>
</script>