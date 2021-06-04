<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/otc/addOwner' : '/otc/editOwner'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">广告主姓名：</label>
								<input id="o_name" type="text" name="o_name" maxlength="16" class="txt required" value="${info.o_name}" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">最后在线时间：</label>
								<input type="text" name="last_time" class="txt txt_datetime" value="${info.last_time == null ? curDateAndCurTime :  info.last_time}" readonly="readonly"/>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">平均放行时间：</label>
								<input id="avg_time" type="text" name="avg_time" maxlength="4" class="txt digits required" value="${info.avg_time == null ? 1 : info.avg_time}" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">银行卡信息：</label>
								<input id="bank_info" type="text" name="bank_info" maxlength="100" class="txt" value="${info.bank_info == null ? '' : info.bank_info}" style="width: 590px;" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">支付宝信息：</label>
								<textarea id="alipay_info" type="text" name="alipay_info" maxlength="400" class="txta" style="width: 420px; height: 70px;">${info.alipay_info == null ? '户名▲账号▲二维码URL▲手机' : info.alipay_info}</textarea>
								<a style="text-decoration:underline;position:absolute;padding-top:12px;padding-left:5px;font-size:15px;" href="javascript:void(0);" onclick="setQrCode('alipay_pay');">设置收款码</a>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">微信支付信息：</label>
								<textarea id="wxpay_info" type="text" name="wxpay_info" maxlength="400" class="txta" style="width: 420px; height: 100px;" >${info.wxpay_info == null ? '户名▲账号▲二维码URL▲手机▲微信号二维码URL' : info.wxpay_info}</textarea>
								<a style="text-decoration:underline;position:absolute;padding-top:12px;padding-left:5px;font-size:15px;" href="javascript:void(0);" onclick="setQrCode('wxpay_pay');">设置收款码</a>
								<a style="text-decoration:underline;position:absolute;padding-top:12px;margin-left:95px;font-size:15px;" href="javascript:void(0);" onclick="setQrCode('wxpay_friend');">设置好友码</a>
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
				<hr class="hrStyle">
				<div class="div_block">
					<p class="p_block_all" style="margin-left:15px;">
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">1、最后上线时间、平均放行时间，创建时填写，后面系统会自动刷新</label>
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">2、银行卡信息格式：户名▲卡号▲开户行▲手机，不支持该项时，不填</label>
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">3、支付宝信息格式：户名▲账号▲二维码URL▲手机，不支持该项时，不填</label>
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">4、微信信息格式：户名▲账号▲二维码URL▲手机▲微信号二维码URL，不支持该项时，不填</label>
					</p>
				</div>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
function setQrCode(sType) {
	var title = "";
	var alreadyImg = "";
	if (sType === "alipay_pay") {
		title = "设置支付宝图片";
		try {
			alreadyImg = $("#alipay_info").val().split("▲")[2]
		} catch (e) {
			
		}		
	} else if (sType === "wxpay_pay") {
		title = "设置微信支付图片";
		try {
			alreadyImg = $("#wxpay_info").val().split("▲")[2]
		} catch (e) {
			
		}
	} else if (sType === "wxpay_friend") {
		title = "设置微信好友图片";
		try {
			alreadyImg = $("#wxpay_info").val().split("▲")[4]
		} catch (e) {
			
		}
	}
	openAddWindow(title, "${rootPath}/otc/toAddImg?image=" + alreadyImg + "&sType=" + sType, 500, 500);
}

function backQrCode(sType, imgUrl) {
	if (sType === "alipay_pay") {
		//设置支付宝图片
		var all = $("#alipay_info").val();
		if ("" === all || all.indexOf("▲") === -1) {
			all = "户名▲账号▲" + imgUrl + "▲手机"
		} else { //其他情况，认为他已经有
			var allArr = all.split("▲");
			all = allArr[0] + "▲" + allArr[1] + "▲" +imgUrl + "▲" + allArr[3]
		}
		$("#alipay_info").val(all)
	} else if (sType === "wxpay_pay") {
		//设置微信支付图片
		var all = $("#wxpay_info").val();
		if ("" === all || all.indexOf("▲") === -1) {
			all = "户名▲账号▲" + imgUrl + "▲手机▲微信号二维码URL"
		} else { //其他情况，认为他已经有
			var allArr = all.split("▲");
			all = allArr[0] + "▲" + allArr[1] + "▲" +imgUrl + "▲" + allArr[3] + "▲" + allArr[4]
		}
		$("#wxpay_info").val(all)
	} else if (sType === "wxpay_friend") {
		//设置微信好友图片
		var all = $("#wxpay_info").val();
		if ("" === all || all.indexOf("▲") === -1) {
			all = "户名▲账号▲" + imgUrl + "▲手机▲微信号二维码URL"
		} else { //其他情况，认为他已经有
			var allArr = all.split("▲");
			all = allArr[0] + "▲" + allArr[1] + "▲" +allArr[2] + "▲" + allArr[3] + "▲" + imgUrl
		}
		$("#wxpay_info").val(all)
	}
}
</script>