<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form"  method="post" action="${rootPath}/member/edit_OtcAsserts" id="dwanwuForm">
				<input type="hidden" name="old_total_balance" id="old_total_balance" value="${info.total_balance}" />
				<input type="hidden" name="old_frozen_balance" id="old_frozen_balance" value="${info.frozen_balance}" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block"><label class="label_block">会员id：</label><input  id="member_id"  type="text" name="member_id" maxlength="30" class="txt required" value="${info.id }" readonly="true" /></p>
						<p class="p_block"><label class="label_block">会员账号：</label><input type="text"  name="m_name" id="m_name" class="txt required" value="${info.m_name}" maxlength="32" readonly="true" /></p>
						<p class="p_block"><label class="label_block">币种：</label><input  id="currency" type="text" name="currency"  class="txt" maxlength="30" class="txt" value="${info.currency }"  readonly="true" /></p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">总余额：</label><input  id="total_balance" type="text" name="total_balance"  class="txt" class="txt required" value="${info.total_balance }"  /></p>
						<p class="p_block"><label class="label_block">冻结余额：</label><input  id="frozen_balance" type="text" name="frozen_balance"  class="txt" class="txt required" value="${info.frozen_balance }"  /></p>
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

