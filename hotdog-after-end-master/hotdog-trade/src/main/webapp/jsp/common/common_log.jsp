<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ include file="/jsp/topForForm.jsp"%>
<style>
<!--
.div_block .txt, .div_block .number {
    width: 130px;
}
.div_block .p_block {
    white-space: nowrap; overflow: visible;
}
select {
    width: 130px;
}
-->
</style>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<div class="itembox">
					<div class="div_block" id="OrderDiv">
						<p class="p_block_all">
							<label class="label_block">信息：</label>
							<input id="info" type="text" name="info" style="width:650px;" class="txt txt_readonly" readonly="readonly" value="${info.info }"/>
						</p>
					</div>
					<hr class="hrStyle">
					<div class="div_block">
						<p class="p_block_all"><label class="label_block">操作记录：</label></p>
					</div>
					<div class="div_block" style="width:700px; margin-left:100px;">
						${info.memo }
					</div>
					<hr class="hrStyle">
					<p class="p_block_260 p_btn">
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
			</div>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>