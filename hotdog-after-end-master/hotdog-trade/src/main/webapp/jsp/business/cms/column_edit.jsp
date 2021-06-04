<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/cms/addColumn' : '/cms/editColumn'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">
						<%
							//管理员可以选择站点，非管理员默认管理自己的站点。
						%>
						<c:choose>
							<c:when test="${user_site_id == '' || user_site_id == null}">
								<div class="div_block">
									<p class="p_block"><label class="label_block">站点ID：</label>
										<select name="site_id" id="site_id" class="sel ww_select required" data="{code:'site|id',initValue:'${info.site_id }'}"><option value="">请选择...</option></select>
									</p>
								</div>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="site_id" id="site_id" value="${info.site_id == '' || info.site_id == null ? user_site_id : info.site_id}" />
							</c:otherwise>
						</c:choose>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">栏目名称：</label>
								<input id="c_name" type="text" name="c_name" maxlength="16" class="txt required" value="${info.c_name}" />
							</p>
							<p class="p_block"><label class="label_block">父栏目：</label>
								<select name="parent_id" id="parent_id" class="sel required"><option value="0">***一级栏目***</option></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">栏目模板：</label>
								<input id="c_template_file" type="text" name="c_template_file" maxlength="100" class="txt" value="${info.c_template_file}" />
							</p>
							<p class="p_block"><label class="label_block">展示方式：</label>
								<select name="c_type" id="c_type" class="sel ww_select" data="{code:'cms_column_type|id',initValue:'${info.c_type }'}"><option value="">请选择</option></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">内容模板：</label>
								<input id="c_article_template_file" type="text" name="c_article_template_file" maxlength="100" class="txt" value="${info.c_article_template_file}" />
							</p>
							<p class="p_block"><label class="label_block">菜单上展示：</label>
								<select name="is_display_in_menu" id="is_display_in_menu" class="sel ww_select required" data="{code:'bool|id',initValue:'${info.is_display_in_menu }'}"><option value="">请选择</option></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">缩略图宽度：</label>
								<input id="thumb_width" type="text" name="thumb_width" maxlength="3" class="txt digits required" value="${info.thumb_width == null ? 0 : info.thumb_width}" />
							</p>
							<p class="p_block"><label class="label_block">缩略图高度：</label>
								<input id="thumb_height" type="text" name="thumb_height" maxlength="3" class="txt digits required" value="${info.thumb_height == null ? 0 : info.thumb_height}" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">排序：</label>
								<input id="c_order" type="text" name="c_order" maxlength="8" class="txt digits required" value="${info.c_order == null ? 1 : info.c_order}" />
							</p>
							<p class="p_block"><label class="label_block">是否启用：</label>
								<select name="is_use" id="is_use" class="sel ww_select required" data="{code:'bool|id',initValue:'${info.is_use }'}"><option value="">请选择</option></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">栏目说明：</label>
								<input id="c_desc" type="text" name="c_desc" maxlength="500" class="txt" style="width:543px;" value="${info.c_desc}" />
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
				<hr class="hrStyle">
				<div class="div_block">
					<p class="p_block_all" style="margin-left:15px;">
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">1、<b>[展示方式]</b>为直接展示内容时，直接显示该栏目下第一篇文章。</label>
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">2、<b>[排序]</b>数字越小越靠前。</label>
					</p>
				</div>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>

$(function(){
	if ('${user_site_id}' != '') {
		funcGetParentColumnBySiteId("${user_site_id}", 'parent_id', "${info.parent_id}", "${info.id}");
	} else {
		funcGetParentColumnBySiteId("${info.site_id}", 'parent_id', "${info.parent_id}", "${info.id}");
	}
});

$('#site_id').live("change", (function() {
	//联动栏目
	funcGetParentColumnBySiteId(this.value, 'parent_id', "${info.parent_id}", "${info.id}");
}));
</script>