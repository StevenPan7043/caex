<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form" method="post" action="${rootPath}/ca/addCurrencyAuth" id="wanwuForm">
				<input id="id" type="hidden" name="id" value="${info.id }" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block " style="width: 100%; ">
							<label class="label_block">会员：</label>
							<input id="m_name" type="text" name="mName" maxlength="200" style="width: 495px;"
								   class="txt required txt_readonly txt_choose" readonly="readonly"
								   value="${member.id > 0 ? member.m_name : "" }"/>
							<input id="member_id" type="hidden" name="memberId"
								   value="${member.memberId > 0 ? member.id : "" }"/>
						</p>
					</div>

					<div class="div_block">
						<p class="p_block"><label class="label_block">基础货币：</label>
							<select name="baseCurrencye" id="baseCurrency" class="sel ww_select ww_select_text required"
									data="{code:'currency|id',initValue:'${currencyPair.base_currency }'}" ${empty currencyPair.id ? '' : 'disabled="disabled"'}>
								<option value="">请选择...</option>
							</select>
						</p>
						<p class="p_block"><label class="label_block">计价货币：</label>
							<select name="valuationCurrency" id="valuationCurrency" class="sel ww_select ww_select_text required"
									data="{code:'currency|id',initValue:'${currencyPair.quote_currency }'}" ${empty currencyPair.id ? '' : 'disabled="disabled"'}>
								<option value="">请选择...</option>
							</select>
						</p>
					</div>
					<div class="div_block" style="margin-top: 10px" id ="feeScale">
						<div class="searchFieldCtr p_block">
							<label class="label_block">手续费比例(%)：</label>
							<input name="feeScale" type="text" style="height: 20px;width: 120px" class="txt number required" value="${info == null ? 0 : info.feeScale}" /> %
						</div>
						<div class="p_block"><label class="label_block">是否冻结：</label>
							<select id="isFree" name="isFree" class="sel ww_select" data="{code:'bool|id',initValue:'0'}" value="${info == null ? 0 : info.isFree}"></select>
						</div>
					</div>
					<div class="div_block" style="margin-top: 10px" id ="baseWQuota">
						<div class="searchFieldCtr p_block">
							<label class="label_block">基础货币提现限额：</label>
							<input name="baseWQuota" type="text" style="height: 20px;width: 167px" class="txt number required" value="${info == null ? 0 : info.baseWQuota}" />
						</div>
						<div class="searchFieldCtr p_block">
							<label class="label_block">计价货币提现限额：</label>
							<input name="valuationWQuota" type="text" style="height: 20px;width: 167px" class="txt number required" value="${info == null ? 0 : info.valuationWQuota}" />
						</div>
					</div>
					<div class="div_block" style="margin-top: 10px" id ="remark">
						<div class="searchFieldCtr p_block">
							<label class="label_block">备注：</label>
							<input name="remark" type="text" style="height: 20px;width: 120px" class="txt " value="${info == null ? "" : info.remark}" />
						</div>
					</div>
					<iv class="div_block" style="margin-top: 10px" id ="baseWQuota1">
						<label class="label_block">权限设置：</label>
						<c:forEach var="item" items="${dateAuthList}">
							<input style="margin-left: 20px" type="checkbox" name="${item.codeEn}" value="${item.type}"
							<c:if test="${item.flag == true}">checked</c:if>/>  ${item.code}
						</c:forEach>
					</div>

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