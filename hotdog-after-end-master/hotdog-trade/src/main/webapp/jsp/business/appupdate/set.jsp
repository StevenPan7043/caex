<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<style type="text/css">
.box {
	width: 360px;
	border: 1px solid #ddd;
	padding: 10px;
}

.box div {
	padding: 10px;
}

h3 {
	padding-bottom: 10px;
}

.android {
	top: 10px;
	left: 10px;
	position: absolute;
}

.ios {
	top: 10px;
	left: 400px;
	position: absolute;
}

.p_block {
	width: 100% !important;
}
</style>

<div class="main_content">
	<div class="box android">
		<h3>Android更新设置:</h3>
		<form method="post" action="${rootPath}/aub/edit" onsubmit="return submitForm(this)">
			<input type="hidden" id="id" name="id" value="${android.id}" />
			<div class="itembox">
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">下载URL：</label>
						<textarea class="txta required" name="url" style="width: 160px; height: 150px;">${android.url}</textarea>
					</p>
				</div>
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">更新描述：</label>
						<textarea class="txta required" name="description" style="width: 160px; height: 150px;">${android.description}</textarea>
					</p>
				</div>
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">版本号：</label>
						<input name="version" type="text" maxlength="20" class="txt required" value="${android.version}" />
					</p>
				</div>
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">是否更新：</label>
						<select name="update" class="sel ww_select required" data="{code:'bool|id',initValue:'${android.update}'}"></select>
					</p>
				</div>
			</div>
			<p class="p_block p_btn" style="text-align: left; padding-left: 60px;">
				<input type="submit" class="btn_a btn_a1" value="确 定" style="width: 200px" />
			</p>
		</form>
	</div>
	<div class="box ios">
		<h3>IOS更新设置:</h3>
		<form method="post" action="${rootPath}/aub/edit" onsubmit="return submitForm(this)">
			<input type="hidden" id="id" name="id" value="${ios.id}" />
			<div class="itembox">
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">下载URL：</label>
						<textarea class="txta required" name="url" style="width: 160px; height: 150px;">${ios.url}</textarea>
					</p>
				</div>
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">更新描述：</label>
						<textarea class="txta required" name="description" style="width: 160px; height: 150px;">${ios.description}</textarea>
					</p>
				</div>
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">版本号：</label>
						<input name="version" type="text" maxlength="20" class="txt required" value="${ios.version}" />
					</p>
				</div>
				<div class="div_block">
					<p class="p_block">
						<label class="label_block">是否更新：</label>
						<select name="update" class="sel ww_select required" data="{code:'bool|id',initValue:'${ios.update}'}"></select>
					</p>
				</div>
			</div>
			<p class="p_block p_btn" style="text-align: left; padding-left: 60px;">
				<input type="submit" class="btn_a btn_a1" value="确 定" style="width: 200px" />
			</p>
		</form>
	</div>

</div>
<script>
	function submitForm(form) {
		$(form)
				.validate(
						{
							errorPlacement : function(lable, element) {
								if (lable.html() != "<b>undefined</b>") {
									if (element.hasClass("txta")) {
										element.parent().addClass(
												"l-textarea-invalid");
									} else if (element.hasClass("txt")
											|| element.hasClass("sel")
											|| element.hasClass("number")) {
										element.parent().addClass(
												"l-text-invalid");
									}
									$(element).removeAttr("title")
											.ligerHideTip();
									$(element).attr("title", lable.html())
											.ligerTip();
								}
							},
							success : function(lable, element) {
								element.parent().removeClass("l-text-invalid");
								element.parent().removeClass(
										"l-textarea-invalid");
								element.ligerHideTip();
							},
							submitHandler : function(vform) {
								var name = $(this.submitButton).attr("tip")
										|| "您确定要提交吗？";
								var action = $(this.submitButton)
										.attr("action")
										|| vform.action;

								if (name == "noConfirm"
										|| ($("_noConfirmKey") && $(
												"#_noConfirmKey").val() == "noConfirm")) {
									layer.closeAll();
									params = $(vform).vals();
									$.reqUrlEx(action, params, null, false);
									return false;
								}
								layer.confirm(name, function() {
									layer.closeAll();
									params = $(vform).vals();
									$.reqUrlEx(action, params, null, false);
								}, '确认');
								return false;
							}
						});
		return false;
	}
</script>
<%@ include file="/jsp/bottom.jsp"%>
