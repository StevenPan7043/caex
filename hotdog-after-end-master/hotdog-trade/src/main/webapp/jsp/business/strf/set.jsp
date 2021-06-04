<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form" method="post" action="/strf/edit_set" id="wanwuForm">
				<div class="itembox">
					<c:forEach var="info" items="${list }" varStatus="i">
						<div class="div_block">
							<p class="p_block" style="width: 400px;">
								<label class="label_block" style="width: 200px;">${info.title} : </label>
								<input type="hidden" name='lists[${i.index}].id' value="${info.id}" />
								<input name='lists[${i.index}].rate' type="text" maxlength="20" class="txt required" value="${info.rate}" />
							</p>
						</div>
					</c:forEach>
				</div>
				<p class="p_block p_btn" style="text-align: left; padding-left: 110px;">
					<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" style="width: 200px" />
				</p>
			</form>
		</div>
	</div>
</div>
<script>
	
</script>
<%@ include file="/jsp/bottom.jsp"%>
