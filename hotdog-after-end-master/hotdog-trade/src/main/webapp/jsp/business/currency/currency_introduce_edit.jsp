<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/topForForm.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="/resources/js/commcomponent.js?v=6" type="text/javascript"></script>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form" method="post" action="${rootPath}/currency/currency_introduce/add" id="wanwuForm">
				<input type="hidden" name="id" id="id" value="${info.id != null ? info.id : null}"/>
				<input type="hidden" name="d_currency_id" id="d_currency_id" value="${info.d_currency_id != null ? info.d_currency_id : null}"/>
				<input type="hidden" name="create_time" id="create_time" value="${info.create_time != null ? info.create_time : null}"/>
				<div class="itembox">

					<div class="div_block">
						<p class="p_block"><label class="label_block">币种：</label>
							<select name="currency" id="currency" class="sel ww_select ww_select_text required"
									data="{code:'currency|id',initValue:'${info.currency }'}" ${info.currency == null ? '': 'disabled="disabled"'}>
								<option value="">--请选择币种--</option>
							</select>
						</p>
						<p class="p_block"><label class="label_block">货币名称：</label>
							<input type="text" name="name" id="name"  maxlength="16"  class="txt required" value="${info.name}"/>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">发行价格：</label>
							<input id="release_price" type="text" name="release_price"
								   class="txt required" value="${info.release_price}"/>
						</p>
						<p class="p_block"><label class="label_block">发行总量：</label>
							<input id="release_amount" type="text" name="release_amount"
								   class="txt required" value="${info.release_amount}"/>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">流通总量：</label>
							<input id="circulation_amount" type="text" name="circulation_amount"
								   class="txt required" value="${info.circulation_amount}"/>
						</p>
						<p class="p_block"><label class="label_block">官网：</label>
							<input id="site" type="text" name="site"
								   class="txt " value="${info.site}"/>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">白皮书：</label>
							<input id="white_paper" type="text" name="white_paper"
								   class="txt required" value="${info.white_paper}"/>
						</p>
						<p class="p_block"><label class="label_block">区块查询：</label>
							<input id="block_search" type="text" name="block_search"
								   class="txt required" value="${info.block_search}"/>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">是否显示：</label>
							<select name="is_show" id="is_show" class="sel ww_select required" data="{code:'info|r_status',initValue:'${info.is_show }'}">
								<option value="1" ${info.is_show == 1 ? "selected" : "" }>是</option>
								<option value="0" ${info.is_show == 0 ? "selected" : "" }>否</option>
							</select>
						</p>
						<p class="p_block"><label class="label_block">发行时间：</label>
							<input type="text" name="release_time" id="release_time"  maxlength="16"  class="txt required" value="${info.release_time}"/>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" ><label class="label_block">币种描述(中文)：</label>
							<textarea class="txta" id="introduce_cn" name="introduce_cn" style="width:220px; height:200px;">${info.introduce_cn }</textarea>
						</p>
						<p class="p_block" ><label class="label_block">币种描述(英文)：</label>
							<textarea class="txta" id="introduce_en" name="introduce_en" style="width:220px; height:200px;">${info.introduce_en }</textarea>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" ><label class="label_block">币种描述(韩文)：</label>
							<textarea class="txta" id="introduce_ko" name="introduce_ko" style="width:220px; height:200px;">${info.introduce_ko }</textarea>
						</p>
						<p class="p_block" ><label class="label_block">币种描述(日文)：</label>
							<textarea class="txta" id="introduce_jp" name="introduce_jp" style="width:220px; height:200px;">${info.introduce_jp }</textarea>
						</p>
					</div>
				</div>
				<p class="p_block p_btn">
					<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
					<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset" value="关 闭"/>
				</p>
			</form>
			<hr class="hrStyle">
			<div class="div_block">
				<p class="p_block_all" style="margin-left:15px;">
					<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">1、是否显示：是，显示该币种资料；否，隐藏该币种资料</label>
				</p>
			</div>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp" %>

<script>

</script>