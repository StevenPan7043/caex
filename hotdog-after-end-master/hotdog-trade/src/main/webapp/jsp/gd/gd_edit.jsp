<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.all.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/lang/zh-cn/zh-cn.js"></script>
<style>
.p_block {
	width: 32% !important;
}
</style>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/gd/gd_add_do' : '/gd/gd_edit_do'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
<%--					<textarea id="particular" name="particular" style="display: none;">${info.particular}</textarea>--%>
<%--					<textarea id="allotDesc" name="allotDesc" style="display: none;">${info.allotDesc}</textarea>--%>
<%--					<textarea id="question" name="question" style="display: none;">${info.question}</textarea>--%>
<%--					<textarea id="riskWarning" name="riskWarning" style="display: none;">${info.riskWarning}</textarea>--%>
<%--					<textarea id="particularE" name="particularE" style="display: none;">${info.particularE}</textarea>--%>
<%--					<textarea id="allotDescE" name="allotDescE" style="display: none;">${info.allotDescE}</textarea>--%>
<%--					<textarea id="questionE" name="questionE" style="display: none;">${info.questionE}</textarea>--%>
<%--					<textarea id="riskWarningE" name="riskWarningE" style="display: none;">${info.riskWarningE}</textarea>--%>
<%--					<input type="hidden" name="particular_txt" id="particular_txt" value="" />--%>
<%--					<input type="hidden" name="allotDesc_txt" id="allotDesc_txt" value="" />--%>
<%--					<input type="hidden" name="question_txt" id="question_txt" value="" />--%>
<%--					<input type="hidden" name="riskWarning_txt" id="riskWarning_txt" value="" />--%>


					<div class="itembox">
						<div class="div_block">
							<p class="p_block"><label class="label_block">算力每日产出币种：</label>
								<input id="outputCurrency" type="text" name="outputCurrency" maxlength="50" class="txt required " } value="${info.outputCurrency}" />
							</p>
							<p class="p_block"><label class="label_block">购买算力的货币：</label>
								<input id="quoteCurrency" type="text" name="quoteCurrency" maxlength="50" class="txt required " } value="${info.quoteCurrency}" />
							</p>
						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block" >每日产出的下限：</label>
								<input id="outputFloor" type="text" style="width: 145px;" name="outputFloor" maxlength="12" class="txt required number" value="${info.outputFloor}" />
							</p>
							<p class="p_block"><label class="label_block" >每日产出的上限：</label>
								<input id="outputUpper" type="text" style="width: 145px;" name="outputUpper" maxlength="12" class="txt required number" value="${info.outputUpper}" />
							</p>
						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block">跟单价格：</label>
								<input id="price" type="text" name="price" maxlength="20" class="txt required" value="${info.price}" />
							</p>
							<p class="p_block"><label class="label_block" >跟单详情描述：</label>
								<input id="particular" type="text" style="width: 145px;" name="particular" maxlength="12" class="txt required " value="${info.particular}" />
							</p>
						</div>



						<div class="div_block">
							<p class="p_block"><label class="label_block">认购开始时间：</label>
								<input type="text" id="createTime" name="createTime" class="txt txt_datetime " value="${info.createTime}" readonly="readonly"/>
<%--								<input type="text" id="saleStartTime" name="saleStartTime" class="txt dateinput dateicon required" readonly="readonly">--%>
							</p>
							<p class="p_block"><label class="label_block">认购结束时间：</label>
								<input type="text" id="modifyTime" name="modifyTime" class="txt txt_datetime " value="${info.modifyTime}" readonly="readonly"/>
							</p>
						</div>



						<div class="div_block">
							<p class="p_block"><label class="label_block">运行状态：</label>
								<select name="runStatus" class="sel">
<%--									<option value="">请选择...</option>--%>
									<option <c:if test='${"1"==info.runStatus || info.runStatus == null}'>selected=selected</c:if> value="1">未开始</option>
									<option <c:if test='${"2"==info.runStatus}'>selected=selected</c:if> value="2">启动</option>
									<option <c:if test='${"3"==info.runStatus}'>selected=selected</c:if> value="3">停止</option>
								</select>
							</p>
						</div>

<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">项目详情</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor1" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">产出分配</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor2" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">常见问题</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor3" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">风险提示</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor4" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>

<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">项目英文详情</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor11" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">产出分配英文说明</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor22" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">常见问题英文说明</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor33" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<p class="p_block" style="width:100% !important;"><label class="label_block" style="font-size: 16px; color: blue; font-weight: bold;">风险英文提示</label></p>--%>
<%--						</div>--%>
<%--						<div class="div_block">--%>
<%--							<script id="editor44" type="text/plain" style="width:1000px;height:300px;"></script>--%>
<%--						</div>--%>
					</div>
					<p class="p_block p_btn" style="width:100% !important;">
						<input type="button" onclick="return pCheck();" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
// var ue1, ue2, ue3, ue4, ue11, ue22, ue33, ue44;

// $(function(){
// 	//实例化编辑器
// 	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
// 	ue1 = UE.getEditor('editor1');
// 	ue2 = UE.getEditor('editor2');
// 	ue3 = UE.getEditor('editor3');
// 	ue4 = UE.getEditor('editor4');
// 	ue11 = UE.getEditor('editor11');
// 	ue22 = UE.getEditor('editor22');
// 	ue33 = UE.getEditor('editor33');
// 	ue44 = UE.getEditor('editor44');
//
// 	ue1.addListener("ready", function () {
// 		ue1.setContent($("#particular").val());
// 	});
// 	ue2.addListener("ready", function () {
// 		ue2.setContent($("#allotDesc").val());
// 	});
// 	ue3.addListener("ready", function () {
// 		ue3.setContent($("#question").val());
// 	});
// 	ue4.addListener("ready", function () {
// 		ue4.setContent($("#riskWarning").val());
// 	});
//
// 	ue11.addListener("ready", function () {
// 		ue11.setContent($("#particularE").val());
// 	});
// 	ue22.addListener("ready", function () {
// 		ue22.setContent($("#allotDescE").val());
// 	});
// 	ue33.addListener("ready", function () {
// 		ue33.setContent($("#questionE").val());
// 	});
// 	ue44.addListener("ready", function () {
// 		ue44.setContent($("#riskWarningE").val());
// 	});
// });

function pCheck(){
	// var bool = true;

			// <input type="hidden" name="particular_txt" id="particular_txt" value="" />
			// <input type="hidden" name="allotDesc_txt" id="allotDesc_txt" value="" />
			// <input type="hidden" name="question_txt" id="question_txt" value="" />
			// <input type="hidden" name="riskWarning_txt" id="riskWarning_txt" value="" />

	// $("#particular").val(UE.getEditor('editor1').getContent());
	// $("#particular_txt").val(UE.getEditor('editor1').getContentTxt());

	// $("#allotDesc").val(UE.getEditor('editor2').getContent());
	// $("#allotDesc_txt").val(UE.getEditor('editor2').getContentTxt());

	// $("#question").val(UE.getEditor('editor3').getContent());
	// $("#question_txt").val(UE.getEditor('editor3').getContentTxt());

	// $("#riskWarning").val(UE.getEditor('editor4').getContent());
	// $("#riskWarning_txt").val(UE.getEditor('editor4').getContentTxt());

	// $("#particularE").val(UE.getEditor('editor11').getContent());
	// $("#allotDescE").val(UE.getEditor('editor22').getContent());
	// $("#questionE").val(UE.getEditor('editor33').getContent());
	// $("#riskWarningE").val(UE.getEditor('editor44').getContent());

	// if ($("#c_intro_en").val() == '' || $('#c_intro_cn').val() == '') {
	// 	layer.alert("请输入中英文简述。");
	// 	bool = false;
	// }
	//
	// if (bool) {
	// 	$("#wanwuForm").validate();
		$("#wanwuForm").submit();
	// }
}
</script>