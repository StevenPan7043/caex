<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/otc/addAds' : '/otc/editAds'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">
						<div class="div_block">
							<p class="p_block"><label class="label_block">广告主：</label>
								<select name="owner_id" id="owner_id" class="sel ww_select required" data="{code:'otc_owner|id',initValue:'${info.owner_id }'}"><option value="">请选择...</option></select>
							</p>
							<p class="p_block"><label class="label_block">类型：</label>
								<select name="ad_type" id="ad_type" class="sel ww_select required" data="{code:'ad_type|id',initValue:'${info.ad_type}'}"><option value="">请选择...</option></select><span style="color: blue;">从用户角度买入还是卖出</span>
							</p>
							
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">基础货币：</label>
								<select name="base_currency" id="base_currency" class="sel ww_select ww_select_text required" data="{code:'currency|id',initValue:'${info.base_currency }'}" ${info.id == null ? '': 'disabled="disabled"'}>
									<option value="">--请选择--</option>
								</select>
								选择后不能修改
							</p>
							<p class="p_block"><label class="label_block">计价货币：</label>
								<select name="quote_currency" id="quote_currency" class="sel required">
									<option value="CNY">CNY</option>
								</select>
								选择后不能修改
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">价格：</label>
								<input id="price" type="text" name="price" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.price ? 0 : info.price}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
							</p>
							<p class="p_block"><label class="label_block">是否展示：</label>
								<select name="c_status" id="c_status" class="sel ww_select required" data="{code:'bool|id',initValue:'${info.c_status}'}"><option value="">请选择...</option></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">最低交易额：</label>
								<input id="min_quote" type="text" name="min_quote" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.min_quote ? 0 : info.min_quote}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								单位：基础货币
							</p>
							<p class="p_block"><label class="label_block">最高交易额：</label>
								<input id="max_quote" type="text" name="max_quote" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.max_quote ? 0 : info.max_quote}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								单位：基础货币
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">初始化成交量：</label>
								<input id="total_volume" type="text" name="total_volume" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.total_volume ? 0 : info.total_volume}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								后面系统会自动刷新
							</p>
							<p class="p_block"><label class="label_block">初始化成交额：</label>
								<input id="total_amount" type="text" name="total_amount" maxlength="20" class="txt number required" value="<fmt:formatNumber value="${empty info.total_amount ? 0 : info.total_amount}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" />
								后面系统会自动刷新
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">排序：</label>
								<input id="a_order" type="text" name="a_order" maxlength="20" class="txt digits required" value="${empty info.a_order ? 0 : info.a_order}" />
								越小越靠前
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>