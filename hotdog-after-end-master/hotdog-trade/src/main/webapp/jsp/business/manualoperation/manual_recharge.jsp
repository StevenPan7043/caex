<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form" method="post" action="${rootPath}/manual/recharge" id="wanwuForm">
				<div class="itembox">
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">币种：</label>
							<select id="currency" name="currency" class="sel">
								<c:forEach var="currency" items="${currencys}">
									<option value="${currency}">${currency}</option>
								</c:forEach>
							</select>
						</p>

					</div>

					<div class="div_block">
						<p class="p_block">
							<label class="label_block">哈希/高度：</label>
							<input type="text" id="info" name="info" style="width: 500px" class="txt required"/>
						</p>
					</div>


				</div>
				<p class="p_block p_btn" style="text-align: left; padding-left: 60px;">
					<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" style="width: 200px" />
				</p>
			</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>
