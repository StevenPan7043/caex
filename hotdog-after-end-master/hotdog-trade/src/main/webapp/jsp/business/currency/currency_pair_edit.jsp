<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/currency/currency_pair_add_do' : '/currency/currency_pair_edit_do'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">	
						<div class="div_block">
							<p class="p_block"><label class="label_block">基础货币：</label>
								<select name="base_currency" id="base_currency" class="sel ww_select ww_select_text required" data="{code:'currency|id',initValue:'${info.base_currency }'}" ${info.id == null ? '': 'disabled="disabled"'}>
									<option value="">--请选择--</option>
								</select>
							</p>
							<p class="p_block"><label class="label_block">计价货币：</label>
								<select name="quote_currency" id="quote_currency" class="sel ww_select ww_select_text required" data="{code:'currency|id',initValue:'${info.quote_currency }'}" ${info.id == null ? '': 'disabled="disabled"'}>
									<option value="">--请选择--</option>
								</select>
							</p>
						</div>
											
						<div class="div_block">
							<p class="p_block"><label class="label_block">展示名称：</label>
								<input id="dsp_name" type="text" name="dsp_name" maxlength="50" class="txt required" value="${info.dsp_name}" />
								如：LTC/BTC
							</p>
							<p class="p_block"><label class="label_block">开启交易时间：</label>
								<input type="text" id="open_time" name="open_time" class="txt txt_datetime required" value="${info.open_time}" readonly="readonly"/>
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">吃单手续费：</label>
								<input id="taker_fee" type="text" name="taker_fee" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${info.taker_fee}" pattern="#.###" minFractionDigits="8" > </fmt:formatNumber>" />
								如0.002表示千二
							</p>
							<p class="p_block"><label class="label_block">挂单手续费：</label>
								<input id="maker_fee" type="text" name="maker_fee" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${info.maker_fee}" pattern="#.###" minFractionDigits="8" > </fmt:formatNumber>" />
								如0.002表示千二
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">价格小数位：</label>
								<input type="text" id="price_precision" name="price_precision" maxlength="1" class="txt digits required" value="${info.price_precision}"/>
							</p>
							<p class="p_block"><label class="label_block">数量小数位：</label>
								<input type="text" id="volume_precision" name="volume_precision" maxlength="1" class="txt digits required" value="${info.volume_precision}"/>
							</p>
						</div>
						
						<div class="div_block"">
							<p class="p_block"><label class="label_block">最低卖出限价：</label>
								<input id="min_price" type="text" name="min_price" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.min_price ? 0 : info.min_price}" pattern="#.###" minFractionDigits="8" > </fmt:formatNumber>" />
							</p>
							<p class="p_block"><label class="label_block">最高买入限价：</label>
								<input id="max_price" type="text" name="max_price" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.max_price ? 0 : info.max_price}" pattern="#.###" minFractionDigits="8" > </fmt:formatNumber>" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">状态：</label>
								<select name="p_status" class="sel ww_select" data="{code:'pair_status|id',initValue:'${info.p_status }'}">
									<option value="">请选择...</option>
								</select>
							</p>
							<p class="p_block"><label class="label_block">排序：</label>
								<input type="text" id="p_order" name="p_order" class="txt digits required" value="${info.p_order}"/>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">是否可买入：</label>
								<select name="can_buy" class="sel ww_select" data="{code:'bool|id',initValue:'${info.can_buy }'}"></select>
							</p>
							<p class="p_block"><label class="label_block">是否可卖出：</label>
								<select name="can_sell" class="sel ww_select" data="{code:'bool|id',initValue:'${info.can_sell }'}"></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">最低买入量：</label>
								<input id="min_buy_volume" type="text" name="min_buy_volume" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.min_buy_volume ? 0 : info.min_buy_volume}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								0表示不限【限价买单】
							</p>
							<p class="p_block"><label class="label_block">最低买入金额：</label>
								<input id="min_buy_amount" type="text" name="min_buy_amount" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.min_buy_amount ? 0 : info.min_buy_amount}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								0表示不限【市价买单】
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">最低卖出量：</label>
								<input id="min_sell_volume" type="text" name="min_sell_volume" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.min_sell_volume ? 0 : info.min_sell_volume}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								0表示不限【卖单】
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">暂停说明：</label>
								<input id="stop_desc" type="text" name="stop_desc" maxlength="50" class="txt" value="${info.stop_desc}" style="width: 260px;" placeholder="暂停时，展示在按钮上，英文在|前面" />中英文竖线（|）分隔，如【Suspend trading|暂停交易】
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">所在交易区：</label>
								<input type="text" id="area_id" name="area_id" class="txt digits required" value="${info.area_id}"/>
							</p>
							</p>
							<p class="p_block"><label class="label_block">交易区第一个：</label>
								<select name="is_area_first" class="sel ww_select" data="{code:'bool|id',initValue:'${info.is_area_first}'}"></select>
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">刷量数量级：</label>
								<input id="fraud_magnitude" type="text" name="fraud_magnitude" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.fraud_magnitude ? 0 : info.fraud_magnitude}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								0表示不刷，填入数字后，在该数字和该数字/10中随机变动，如10，则在1-10中间随机变换
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">涨跌幅是否开启：</label>
								<select name="is_ups_downs_limit" class="sel ww_select"
										data="{code:'bool|id',initValue:'${info.is_ups_downs_limit==null?0:info.is_ups_downs_limit}'}"></select>
							</p>
							<p class="p_block"><label class="label_block">涨跌幅比例：</label>
								<input id="ups_downs_limit" type="text" name="ups_downs_limit" maxlength="20"
									   value="${info.ups_downs_limit==null?0:info.ups_downs_limit}"/>
							</p>
						</div>
						<div class="div_block" style="display: none;">
							<p class="p_block" style="width: 100%;"><label class="label_block">深度步长：</label>
								<input id="p_depth" type="text" name="p_depth" maxlength="10" class="txt number required" value="${empty info.p_depth ? 0 : info.p_depth}" />
							</p>
						</div>

						<hr class="hrStyle">
						<div class="div_block">
							<div>&nbsp;</div>
							&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;抢购功能设置：
							<div>&nbsp;</div>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%"><label class="label_block">是否开启(0代表关闭,1开启)：</label>
								<input id="is_flash_sale_open" type="text" name="is_flash_sale_open"  class="txt number required" value="${empty info.is_flash_sale_open ? 0 : info.is_flash_sale_open}"/>
								关闭时请确认交易对状态(正常/未启用/已上线)，若开启则只能用固定买入价买入
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">开启交易时间：</label>
								<input type="text" id="flash_sale_open_time" name="flash_sale_open_time" class="txt txt_datetime" value="${empty info.flash_sale_open_time ? '2099-09-09 09:09' : info.flash_sale_open_time}" readonly="readonly"/>
							</p>
							<p class="p_block"><label class="label_block">固定买入价：</label>
								<input id="fixed_buy_price" type="text" name="fixed_buy_price" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.fixed_buy_price ? 0 : info.fixed_buy_price}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">关闭交易时间：</label>
								<input type="text" id="flash_sale_close_time" name="flash_sale_close_time" class="txt txt_datetime" value="${empty info.flash_sale_close_time ? '2099-09-09 09:09' : info.flash_sale_close_time }" readonly="readonly"/>
							</p>
							<p class="p_block"><label class="label_block">最大购买量：</label>
								<input id="max_buy_volume" type="text" name="max_buy_volume" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.max_buy_volume ? 0 : info.max_buy_volume}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								0表示不限
							</p>
						</div>
					</div>



					<p class="p_block p_btn">
						<input type="button" onclick="return pCheck();" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
				<hr class="hrStyle">
				<div class="div_block">
					<p class="p_block_all" style="margin-left:15px;">
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">1、<b>[排序 ]</b>不能重复</label>
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">2、<b>[交易区 ]</b>目前1表示主交易区，2表示创新区</label>
					</p>
				</div>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
	function pCheck(){
		var bool = true;
		$("#wanwuForm").validate();
		var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
		if (reg.test($("#base_currency").val())) {
			layer.alert("交易对名称不支持中文");
			bool = false;
		}
		if (reg.test($("#quote_currency").val())) {
			layer.alert("交易对名称不支持中文");
			bool = false;
		}
		if ($("#volume_precision").val() * 1 + $('#price_precision').val() * 1 > 8) {
			layer.alert("价格小数位和数量小数位之和不能大于8");
			bool = false;
		}

		if ($("#is_flash_sale_open").val() != 0 && $("#is_flash_sale_open").val() != 1){
			layer.alert("请输入正确的抢购功能是否开启值(0或1)");
			bool = false;
		}

		if ($("#is_flash_sale_open").val() == 1 && $("#fixed_buy_price").val() == 0){
			layer.alert("开启抢购时请输入不为零的固定买入价");
			bool = false;
		}

		if (bool) {
			$("#wanwuForm").validate();
			$("#wanwuForm").submit();
		}
	}
</script>