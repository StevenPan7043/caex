<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form"  method="post" action="${rootPath}/api/addOrEditThirdParty" id="dwanwuForm">
				<input type="hidden" name="id" id="id" value="${info.id}" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block"><label class="label_block">客户端回调地址：</label><input  id="c_ip" type="text" name="c_ip"  class="txt required"  value="${info.c_ip }"  /></p>
						<p class="p_block"><label class="label_block">项目方名称：</label><input  id="c_name" type="text" name="c_name"  class="txt required"  value="${info.c_name }"  /></p>
					</div>

					<div class="div_block">
						<p class="p_block"><label class="label_block">可充值币种：</label><input  id="can_deposit_currency" type="text" name="can_deposit_currency"  class="txt required" class="number" value="${info.can_deposit_currency }"  /></p>
						<p class="p_block"><label class="label_block">可提币币种：</label><input  id="can_withdraw_currency" type="text" name="can_withdraw_currency"  class="txt required" class="number" value="${info.can_withdraw_currency }"  /></p>
					</div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">客户端成功状态码：</label><input  id="c_code" type="text" name="c_code"  class="txt required" maxlength="16" class="number" value="${info.c_code }"  /></p>
                        <p class="p_block"><label class="label_block">项目方UID：</label><input  id="ext" type="text" name="ext"  class="txt" value="${info.ext }"  /></p>
                    </div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">客户端appKey：</label><input  id="c_appKey" type="text" name="c_appKey"  class="txt" class="txt required" value="${info.c_appKey }"  /></p>
						<p class="p_block"><label class="label_block">是否可用：</label>
                            <input type="hidden" id="select_c_flag" value="${info.c_flag}" />
							<select name="c_flag" class="sel">
                                <option value="0" >可用</option>
                                <option value="1">不可用</option>
                            </select>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">白名单：</label><input  id="whiteIp" type="text" name="whiteIp"  class="txt required"   value="${info.whiteIp }"  /></p>
						<p class="p_block"><label class="label_block">项目方批次：</label>
							<input type="hidden" id="c_name_type" value="${info.c_name_type}"/>
							<select name="c_name_type" class="sel">
								<c:choose>
									<c:when test="${info.c_name_type == null}">
										<option value="1">第一批</option>
										<option value="2">第二批</option>
									</c:when>
									<c:when test="${info.c_name_type != null}">
										<c:if test="${info.c_name_type == '1'}">
											<option value="1">第一批</option>
										</c:if>
										<c:if test="${info.c_name_type == '2'}">
											<option value="2">第二批</option>
										</c:if>
									</c:when>
								</c:choose>
							</select>
						</p>
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

<script type="text/javascript">
    $(function () {
        $('.div_block select').val($('#select_c_flag').val() == undefined ? '0' : $('#select_c_flag').val());
    })
</script>