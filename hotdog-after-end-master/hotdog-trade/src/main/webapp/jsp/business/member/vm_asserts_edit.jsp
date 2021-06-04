<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form"  method="post" action="${rootPath}/member/vm_asserts_edit_do" id="dwanwuForm">
				<input type="hidden" name="old_balance" id="old_total_balance" value="${info.balance}" />
				<input type="hidden" name="old_zcbalance" id="old_frozen_balance" value="${info.zcbalance}" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block"><label class="label_block">会员id：</label><input  id="id"  type="text" name="id" maxlength="30" class="txt required" value="${info.id }" readonly="true" /></p>
						<p class="p_block"><label class="label_block">会员账号：</label><input type="text"  name="phone" id="phone" class="txt required" value="${info.phone}" maxlength="32" readonly="true" /></p>
						<p class="p_block"><label class="label_block">币种：</label><input  id="currency" type="text" name="currency"  class="txt" maxlength="30" class="txt" value="${info.currency }"  readonly="true" /></p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">全仓资产：</label><input  id="balance" type="text" name="balance"  class="txt" class="txt required" value="${info.balance }"  /></p>
						<p class="p_block"><label class="label_block">逐仓资产：</label><input  id="zcbalance" type="text" name="zcbalance"  class="txt" class="txt required" value="${info.zcbalance }"  /></p>
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

