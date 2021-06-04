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
				<form class="wanwu_form"  method="post" action="${rootPath}/ipfs/hashrate_edit_do" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />

					<div class="itembox">
						<div class="div_block">
							<p class="p_block"><label class="label_block">项目id：</label>
								<input  disabled type="text"  maxlength="50" class="txt required " } value="${info.projectId}" />
							</p>
							<p class="p_block"><label class="label_block">项目编码：</label>
								<input  disabled type="text"  maxlength="50" class="txt required " } value="${info.projectCode}" />
							</p>
							<p class="p_block"><label class="label_block">项目名称：</label>
								<input  disabled type="text"  maxlength="20" class="txt required" value="${info.projectName}" />
							</p>

						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block" >用户id：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required number" value="${info.memberId}" />
							</p>
							<p class="p_block"><label class="label_block" >产出币种：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required number" value="${info.outputCurrency}" />
							</p>
							<p class="p_block"><label class="label_block" >单价：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required number" value="${info.price}" />
							</p>
						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block" >购买币种：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required" value="${info.quoteCurrency}" />
							</p>
							<p class="p_block"><label class="label_block" >购买数量：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required" value="${info.num}" />
							</p>
							<p class="p_block"><label class="label_block" >购买总价：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required number" value="${info.total}" />
							</p>
						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block" >购买时间：</label>
								<input  disabled type="text" style="width: 145px;"  maxlength="12" class="txt required number" value="<fmt:formatDate  value="${info.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
							</p>
							<p class="p_block"><label class="label_block">状态：</label>
								<select name="status" id="status" class="sel">
									<%--									<option value="">请选择...</option>--%>
									<option <c:if test='${"1"==info.status || info.status == null}'>selected=selected</c:if> value="1">有效</option>
									<option <c:if test='${"2"==info.status}'>selected=selected</c:if> value="2">无效</option>
								</select>
							</p>
							<p class="p_block"><label class="label_block">类型：</label>
								<select  class="sel">
									<%--									<option value="">请选择...</option>--%>
									<option <c:if test='${"1"==info.type || info.type == null}'>selected=selected</c:if> disabled value="1">算力</option>
									<option <c:if test='${"2"==info.type}'>selected=selected</c:if> disabled value="2">矿机</option>
								</select>
							</p>
						</div>
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

function pCheck(){
	var bool = true;


	// if ($("#c_intro_en").val() == '' || $('#c_intro_cn').val() == '') {
	// 	layer.alert("请输入中英文简述。");
	// 	bool = false;
	// }
	//
	if (bool) {
		$("#wanwuForm").validate();
		$("#wanwuForm").submit();
	}
}
</script>