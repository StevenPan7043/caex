<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.all.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/lang/zh-cn/zh-cn.js"></script>
<style>
.p_block {
	width: 32% !important;
}
</style>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/currency/currency_add_do' : '/currency/currency_edit_do'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<textarea id="c_intro_cn" name="c_intro_cn" style="display: none;">${info.c_intro_cn}</textarea>
					<textarea id="c_intro_en" name="c_intro_en" style="display: none;">${info.c_intro_en}</textarea>
					<input type="hidden" name="c_intro_cn_txt" id="c_intro_cn_txt" value="" />
					<input type="hidden" name="c_intro_en_txt" id="c_intro_en_txt" value="" />
					
					<div class="itembox">	
						<div class="div_block">
							<p class="p_block"><label class="label_block">货币代码：</label>
								<input id="currency" type="text" name="currency" maxlength="20" class="txt required ${info.id == null ? '': 'txt_readonly'}" value="${info.currency}" ${info.id == null ? '': 'readonly="readonly"'} />
							</p>
							<p class="p_block"><label class="label_block">货币名称：</label>
								<input id="currency_name" type="text" name="currency_name" maxlength="20" class="txt txt_readonly" readonly="readonly" value="${info.currency_name}" />
							</p>
							<p class="p_block"><label class="label_block">是否数字货币：</label>
								<select name="is_coin" class="sel ww_select" data="{code:'bool|id',initValue:'${info.is_coin }'}"></select>
							</p>
						</div>
						
						<div class="div_block" id ="isInitValue" <c:if test="${info.currency == 'USDT'}">hidden</c:if>>
							<p class="p_block"><label class="label_block">是否可充值：</label>
								<select name="can_recharge" class="sel ww_select" data="{code:'bool|id',initValue:'${info.can_recharge }'}"></select>
							</p>
							<p class="p_block"><label class="label_block">是否可提现：</label>
								<select name="can_withdraw" class="sel ww_select" data="{code:'bool|id',initValue:'${info.can_withdraw }'}"></select>
							</p>
							<p class="p_block"><label class="label_block">是否展示：</label>
								<select name="is_show" class="sel ww_select" data="{code:'bool|id',initValue:'${info.is_show }'}"></select>
							</p>
						</div>
						<div class="div_block" id ="vertical" <c:if test="${info.currency != 'USDT'}">hidden</c:if>>
							<p class="p_block"><label class="label_block">ERC20是否可充值：</label>
								<select name="usdtRechargeERC20" class="sel ww_select"
										data="{code:'bool|id',initValue:'${usdt_recharge_ERC20 }'}"></select>
							</p>
							<p class="p_block"><label class="label_block">ERC20是否可提现：</label>
								<select name="usdtWithdrawERC20" class="sel ww_select"
										data="{code:'bool|id',initValue:'${usdt_withdraw_ERC20}'}"></select>
							</p>
							<p class="p_block"><label class="label_block">是否展示：</label>
								<select name="is_show" class="sel ww_select" data="{code:'bool|id',initValue:'${info.is_show }'}"></select>
							</p>
						</div>
						<div class="div_block" <c:if test="${info.currency != 'USDT'}">hidden</c:if>>
							<p class="p_block"><label class="label_block">OMNI是否可充值：</label>
								<select name="usdtRechargeOMNI" class="sel ww_select"
										data="{code:'bool|id',initValue:'${usdt_recharge_OMNI }'}"></select>
							</p>
							<p class="p_block"><label class="label_block">OMNI是否可提现：</label>
								<select name="usdtWithdrawOMNI" class="sel ww_select"
										data="{code:'bool|id',initValue:'${usdt_withdraw_OMNI}'}"></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">充值开启时间：</label>
								<input type="text" id="recharge_open_time" name="recharge_open_time" class="txt txt_datetime required" value="${info.recharge_open_time}" readonly="readonly"/>
							</p>
							<p class="p_block"><label class="label_block">提现开启时间：</label>
								<input type="text" id="withdraw_open_time" name="withdraw_open_time" class="txt txt_datetime required" value="${info.withdraw_open_time}" readonly="readonly"/>
							</p>
                            <p class="p_block"><label class="label_block">自动提币上限：</label>
                                <input id="auto_withdraw_up_limit" type="text" name="auto_withdraw_up_limit" maxlength="12" class="txt number" value="<fmt:formatNumber
                                value="${info.auto_withdraw_up_limit}" pattern="#.########" minFractionDigits="8" > </fmt:formatNumber>"/>
                            </p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block" style="color: blue; text-decoration: underline;" title="如果是按百分比，则单位为百分号，如果不按百分比，则单位为个">提现手续费：</label>
								<input id="withdraw_fee" type="text" style="width: 145px;" name="withdraw_fee" maxlength="12" class="txt required number" value="<fmt:formatNumber value="${info.withdraw_fee}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								<span id="withdraw_fee_unit" style="display: ${info.withdraw_fee_percent == 0 ? 'none' : 'Inline'}">%</span>
							</p>
							<p class="p_block"><label class="label_block">手续费按百分比：</label>
								<select id="withdraw_fee_percent" name="withdraw_fee_percent" class="sel ww_select" data="{code:'bool|id',initValue:'${info.withdraw_fee_percent }'}"></select>
							</p>
							<p class="p_block"><label class="label_block">提现最低手续费：</label>
								<input id="withdraw_fee_min" type="text" name="withdraw_fee_min" maxlength="12" class="txt required number" value="<fmt:formatNumber value="${info.withdraw_fee_min}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" style="width: 145px;" /><span style="color: blue; font-weight: bold">个</span>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">提现最高手续费(0表示不封顶)：</label>
								<input id="withdraw_fee_max" type="text" name="withdraw_fee_max" maxlength="12" class="txt required number" value="<fmt:formatNumber value="${info.withdraw_fee_max}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" style="width: 145px;" /><span style="color: blue; font-weight: bold">个</span>
							</p>
							<p class="p_block"><label class="label_block" style="color: blue; font-weight: bold;">是否以太坊架构：</label>
								<select name="is_in_eth" class="sel ww_select required" data="{code:'bool|id',initValue:'${info.is_in_eth }'}"><option value="">--请选择--</option></select>
							</p>
							<p class="p_block"><label class="label_block">最小充值额度：</label>
								<input id="c_min_recharge" type="text" name="c_min_recharge" maxlength="12" class="txt required number" value="<fmt:formatNumber value="${info.c_min_recharge}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">最小提现额度：</label>
								<input id="c_min_withdraw" type="text" name="c_min_withdraw" maxlength="12" class="txt number required" value="<fmt:formatNumber value="${info.c_min_withdraw}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>"/>
							</p>
							<p class="p_block"><label class="label_block">每天最大提现额度：</label>
								<input id="c_max_withdraw" type="text" name="c_max_withdraw" maxlength="12" class="txt number required" value="<fmt:formatNumber value="${info.c_max_withdraw}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>"/>
							</p>
							<p class="p_block"><label class="label_block">每天提币次数(0不限制)：</label>
								<input type="text" id="c_limit_withdraw" name="c_limit_withdraw" maxlength="5" class="txt number required" value="${info.c_limit_withdraw}"/>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">排序：</label>
								<input id="c_order" type="text" name="c_order" maxlength="4" class="txt required digits" value="${info.c_order}" />
							</p>
							<p class="p_block"><label class="label_block">小数位：</label>
								<input type="text" id="c_precision" name="c_precision" maxlength="1" class="txt digits required {max: 8}" value="${info.c_precision}"/>
							</p>
							<p class="p_block"><label class="label_block">是否otc交易(1支持,0不支持)：</label>
								<input id="is_otc" type="text" name="is_otc" maxlength="12" class="txt number required" value="${info.is_otc}"/>
							</p>
						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block">地址池预留数量：</label>
								<input id="c_num" type="text" name="num" maxlength="10" class="txt required digits" value="${info.num}" />
							</p>
							<p class="p_block"><label class="label_block">创建地址数量：</label>
								<input type="text" id="c_createNum" name="createNum" maxlength="10" class="txt digits required " value="${info.createNum}"/>
							</p>
							<p class="p_block"><label class="label_block">调度是否启动：</label>
								<select name="status" class="sel ww_select" data="{code:'bool|id',initValue:'${info.status }'}"></select>
							</p>
						</div>


						<div class="div_block">

							<p class="p_block" ><label class="label_block">合约地址：</label>
								<input id="tokenaddr" type="text" name="tokenaddr"  class="txt  " value="${info.tokenaddr}"/>
							</p>
							<p class="p_block"><label class="label_block" style="color: blue; text-decoration: underline;">代币精度：</label>
								<input id="decimal" type="text" name="decimal"  class="txt" maxlength="10" value="${info.decimal == null ? '1e18' : info.decimal}"/>

							</p>
							<p class="p_block"><label class="label_block" style="color: blue; text-decoration: underline;">交易查询链接：</label>
								<input id="currency_img" type="text" name="currency_img"  class="txt"  value="${info.currency_img}"/>

							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block" style="color: blue; text-decoration: underline;" title="见下方">
								提币地址校验：</label>
								<input id="withdraw_rule" type="text" name="withdraw_rule"  class="txt" value="${info.withdraw_rule}" />
							</p>
							<p class="p_block"><label class="label_block">是否自动入账：</label>
								<select name="auto_confirm" class="sel ww_select required" data="{code:'bool|id',initValue:'${info.auto_confirm }'}"><option value="">--请选择--</option></select>
							</p>
							<p class="p_block"><label class="label_block">是否支持内部划转(1支持,0不支持)(USDT不支持)：</label>
								<input id="can_internal_transfer" type="text" name="can_internal_transfer" maxlength="12" class="txt number required" value="${info.can_internal_transfer}"/>
							</p>
						</div>

						<div class="div_block">
							<p class="p_block" style="width:25% !important;"><label class="label_block">不能充值描述（中文）：</label>
								<textarea class="txta" id="c_cannot_recharge_desc_cn" name="c_cannot_recharge_desc_cn" style="width:220px; height:200px;">${info.c_cannot_recharge_desc_cn }</textarea>
							</p>
							<p class="p_block" style="width:25% !important;"><label class="label_block">不能充值描述（英文）：</label>
								<textarea class="txta" id="c_cannot_recharge_desc_en" name="c_cannot_recharge_desc_en" style="width:220px; height:200px;">${info.c_cannot_recharge_desc_en }</textarea>
							</p>
							<p class="p_block" style="width:25% !important;"><label class="label_block">提现描述（中文）：</label>
								<textarea class="txta" id="c_cannot_withdraw_desc_cn" name="c_cannot_withdraw_desc_cn" style="width:220px; height:200px;">${info.c_cannot_withdraw_desc_cn }</textarea>
							</p>
							<p class="p_block" style="width:25% !important;"><label class="label_block">提现描述（英文）：</label>
								<textarea class="txta" id="c_cannot_withdraw_desc_en" name="c_cannot_withdraw_desc_en" style="width:220px; height:200px;">${info.c_cannot_withdraw_desc_en }</textarea>
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">简述（中文）</label></p>
						</div>
						<div class="div_block">
							<script id="editor" type="text/plain" style="width:1000px;height:300px;"></script>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">简述（英文）</label></p>
						</div>
						<div class="div_block">
							<script id="editor1" type="text/plain" style="width:1000px;height:300px;"></script>
						</div>
					</div>

					<p class="p_block p_btn" style="width:100% !important;">
						<input type="button" onclick="return pCheck();" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
					<hr class="hrStyle">
					<div class="div_block">
						<p class="p_block_all" style="margin-left:15px;">
							<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">1、<b>[提币地址校验]：</b>minlen:5,表示地址长度最小为5，maxlen:18,表示地址长度最大为18,pre:0x,表示地址开头必须为0x；多个条件用“,”分隔，例如：minlen:5,maxlen:18</label>
						</p>
					</div>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
var ue, ue1; 

$(function(){
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	ue = UE.getEditor('editor');
	ue1 = UE.getEditor('editor1');
	
	ue.addListener("ready", function () {
		ue.setContent($("#c_intro_cn").val());
		ue1.setContent($("#c_intro_en").val());
    });    
	
	if ("${info.withdraw_fee_percent }" == "0") {
		withdrawFeeMinMax("${info.withdraw_fee_percent }");
	}
});

$("#withdraw_fee_percent").change(function(){
	withdrawFeeMinMax($(this).val());
	withdrawFeeUnit($(this).val());
});

function withdrawFeeMinMax(fee_percent) {
	if (fee_percent == 0) {
		$("#withdraw_fee_min").val(0.000);
		$("#withdraw_fee_max").val(0.000);
		$("#withdraw_fee_min").addClass("txt_readonly");
		$("#withdraw_fee_max").addClass("txt_readonly");
		$("#withdraw_fee_min").attr("readonly","readonly");
		$("#withdraw_fee_max").attr("readonly","readonly");
	} else {
		$("#withdraw_fee_min").removeClass("txt_readonly");
		$("#withdraw_fee_max").removeClass("txt_readonly");
		$("#withdraw_fee_min").removeAttr("readonly");
		$("#withdraw_fee_max").removeAttr("readonly");
	}
}
// 提现手续费单位判断
function withdrawFeeUnit(fee_percent) {
	if (fee_percent == 0) {
		$("#withdraw_fee_unit").css("display","none");
	} else {
		$("#withdraw_fee_unit").css("display","Inline");
	}
}

function pCheck(){
	var bool = true;
	
	$("#c_intro_cn").val(UE.getEditor('editor').getContent());
	$("#c_intro_cn_txt").val(UE.getEditor('editor').getContentTxt());
	
	$("#c_intro_en").val(UE.getEditor('editor1').getContent());
	$("#c_intro_en_txt").val(UE.getEditor('editor1').getContentTxt());
	
	if ($("#c_intro_en").val() == '' || $('#c_intro_cn').val() == '') {
		layer.alert("请输入中英文简述。");
		bool = false;
	}
	
	if (bool) {
		$("#wanwuForm").validate();
		$("#wanwuForm").submit();
	}
}

function checkURL(URL){
	var str=URL;
	//判断URL地址的正则表达式为:http(s)?://([\w-]+\.)+[\w-]+(/[\w- ./?%&=]*)?
	//下面的代码中应用了转义字符"\"输出一个字符"/"
	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp=new RegExp(Expression);
	if(objExp.test(str)==true){
		return true;
	}else{
		return false;
	}
}
</script>