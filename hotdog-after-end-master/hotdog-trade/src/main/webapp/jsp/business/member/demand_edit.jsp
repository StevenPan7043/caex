<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}/member/auth_identity_edit_do" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">	
						<div class="div_block">
							<p class="p_block"><label class="label_block">账号：</label>
								<input id="m_name" type="text" name="m_name" class="txt txt_readonly" value="${info.m_name}" readonly="readonly" />
							</p>
							<p class="p_block"><label class="label_block">姓名：</label>
								<input id="real_name" type="text" name="real_name" class="txt txt_readonly" value="${info.given_name} | ${info.middle_name} | ${info.family_name}" readonly="readonly" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">证件信息：</label>
								<input type="text" class="txt required txt_readonly" value="${info.id_type}|${info.id_number}|${info.id_begin_date}|${info.id_end_date}" readonly="readonly" style="width: 567px; font-size:16px;" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">证件颁发：</label>
								<input type="text" class="txt required txt_readonly" value="${info.nationality}|${info.province}|${info.city}" readonly="readonly" style="width: 567px; font-size:16px;" />
							</p>
						</div>
											
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">状态：</label>
								<select name="id_status" id="id_status" class="sel">
									<option value="1">审核通过</option>
									<option value="2">审核不通过</option>
								</select>
							</p>
							<p class="p_block reject_reason_p" style="display: none;"><label class="label_block">拒绝原因：</label>
								<input id="reject_reason" type="text" name="reject_reason" maxlength="20" class="txt required" value="" />
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" style="${info.id_status == 0 ? '' : 'display: none;'}"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
					<div class="div_block">
						<p class="p_block" style="width: 100%; text-align: center;">
							<img alt="正面" src="${curIDCardDomain }${info.id_front_img}">
							<img alt="反面" src="${curIDCardDomain }${info.id_back_img}">
							<img alt="手持" src="${curIDCardDomain }${info.id_handheld_img}">
						</p>
					</div>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
$("#id_status").change(function () {  
    var id_status = $(this).children('option:selected').val();
    if (id_status == "1") {  
		$(".reject_reason_p").hide();
		$("#reject_reason").val("");
    } else if (id_status == "2") {  
    	$(".reject_reason_p").show();
    }  
});
</script>