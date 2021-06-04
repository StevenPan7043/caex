<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<style>
<!--
.div_block .txt, .div_block .number {
    width: 160px;
}
select {
    width: 160px;
}
-->
</style>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form"  method="post" action="/noau_updatePassword" id="wanwuForm">
				<input type="hidden" name="id" id="id" value="${info.id}" />
				<input type="hidden" name="uType" id="uType" value="${uType}" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block_all"><label class="label_block">当前密码：</label>
							<input type="password" name="oldPassword" id="oldPassword" class="txt required" value="${info.w_name}" maxlength="20" />
							</p>
					</div>
					<div class="div_block">	
						<p class="p_block_all"><label class="label_block">新密码：</label>
							<input type="password" name="newPassword" id="newPassword" class="txt required" value="${info.w_name}" maxlength="20" />
						</p>
						<!-- class="txt required {required:true,remote:'/common/checkDicCode',messages:{remote:'已存在该名称'}}" -->
					</div>
					<div class="div_block">
						<p class="p_block_all"><label class="label_block">新密码确认：</label>
							<input type="password" name="confirm_password" id="confirm_password" class="txt required" value="${info.w_name}" maxlength="20" />
						</p>
					</div>
				</div>
				<p class="p_block p_btn">
					<input type="button" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
					<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
				</p>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript">
$(function(){
	 
	$("#ok").click(function(){
		if(!$("#wanwuForm").valid()) return false;
		if($("#confirm_password").val() != $("#newPassword").val()) {
			layer.alert("您输入的新密码和新密码确认不一样！");
			return false;
		}
		$("#wanwuForm").submit();
	});
	 
});

</script>
<%@ include file="/jsp/bottom.jsp"%>