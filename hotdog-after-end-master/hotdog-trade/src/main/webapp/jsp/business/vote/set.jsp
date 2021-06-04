<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<input type="hidden" id="temp" value="${vote.currency}" />
			<form class="wanwu_form" method="get" action="${rootPath}/voteb/edit_set" id="wanwuForm">
				<input type="hidden" id="id" name="id" value="${vote.id}" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">投票币种：</label>
							<select name="currency" id="currency" class="sel required"></select>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">每票多少币：</label>
							<input name="num" type="text" maxlength="20" class="txt required" value="${vote.num}" />
						</p>
					</div>
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">开始排名时间：</label>
							<input type="text" name="begintime" class="txt txt_datetime required" value="${vote.begintime}" readonly="readonly" />
						</p>
					</div>
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">结束排名时间：</label>
							<input type="text" name="endtime" class="txt txt_datetime required" value="${vote.endtime}" readonly="readonly" />
						</p>
					</div>
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">是否开启：</label>
							<select name="isopen" class="sel ww_select" data="{code:'bool|id',initValue:'${vote.isopen}'}"></select>
						</p>
					</div>
				</div>
				<p class="p_block p_btn" style="text-align: left; padding-left: 60px;">
					<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" style="width: 200px" />
				</p>
			</form>
		</div>
	</div>
</div>
<script>
	$.get("${rootPath}/voteb/coins", function(data) {
		var optionstring = "<option>--请选择币种--</option>";

		var currency = $("#temp").val();
		$.each(data.data, function(k, v) { //循环遍历后台传过来的json数据
			var selected = "";
			if (currency == v.text) {
				selected = 'selected="selected"';
			}
			optionstring += '<option value="' + v.text + '" '+ selected +'>'
					+ v.text + '</option>';
		});
		$("#currency").html(optionstring);
	});
</script>
<%@ include file="/jsp/bottom.jsp"%>
