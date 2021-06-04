<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}/member/member_edit_do" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">
						<div class="div_block">
							<p class="p_block"><label class="label_block">账号：</label>
								<input id="m_name" type="text" name="m_name" maxlength="40"
									   class="txt required txt_readonly" value="${info.m_name}" readonly="readonly"/>
							</p>

                            <p class="p_block ${info.auth_grade == 0 ? "hide" : "" }">
								<label class="label_block">姓：</label>
								<input id="m_name1" type="text" name="family_name" maxlength="10"
									   value="${authIdentity.family_name}"
									   style="width: 50px;border-top: none;border-left: none;border-right: none;outline: #2c2c2c;border-bottom-style: solid;"/>
								<label>名：</label>
								<input id="m_name2" type="text" name="given_name" maxlength="10"
									   value="${authIdentity.given_name}"
									   style="width: 80px;border-top: none;border-left: none;border-right: none;outline: #2c2c2c;border-bottom-style: solid"/>
							</p>
						</div>

						<div class="div_block">
							<p class="p_block"><label class="label_block">状态：</label>
								<select name="m_status" id="m_status" class="sel">
									<option value="0" ${info.m_status == 0 ? "selected" : "" }>手机未验证</option>
									<option value="1" ${info.m_status == 1 ? "selected" : "" }>正常</option>
									<option value="2" ${info.m_status == 2 ? "selected" : "" }>冻结</option>
								</select>
							</p>
							<p class="p_block"><label class="label_block">API刷单：</label>
								<select name="api_limit" id="api_limit" class="sel">
									<option value="0" ${info.api_limit == 0 ? "selected" : "" }>限制频率</option>
									<option value="1" ${info.api_limit == 1 ? "selected" : "" }>不限制频率</option>
								</select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">认证级别：</label>
								<select name="auth_grade" id="auth_grade" class="sel">
									<option value="0" ${info.auth_grade == 0 ? "selected" : "" }>未认证</option>
									<option value="1" ${info.auth_grade == 1 ? "selected" : "" }>1级</option>
									<option value="2" ${info.auth_grade == 2 ? "selected" : "" }>2级</option>
								</select>
							</p>
							<p class="p_block"><label class="label_block">API状态：</label>
								<select name="api_status" id="api_status" class="sel">
									<option value="1" ${info.api_status == 1 ? "selected" : "" }>正常</option>
									<option value="2" ${info.api_status == 2 ? "selected" : "" }>冻结</option>
								</select>
							</p>
						</div>
						
						
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">佣金折扣：</label>
								<select name="trade_commission" id="trade_commission" class="sel">
									<option value="0" ${info.trade_commission eq 0 ? "selected" : "" }>0</option>
									<option value="0.05" ${info.trade_commission eq 0.05 ? "selected" : "" }>0.05</option>
									<option value="0.1" ${info.trade_commission eq 0.1 ? "selected" : "" }>0.1</option>
									<option value="0.2" ${info.trade_commission eq 0.2 ? "selected" : "" }>0.2</option>
									<option value="0.3" ${info.trade_commission eq 0.3 ? "selected" : "" }>0.3</option>
									<option value="0.4" ${info.trade_commission eq 0.4 ? "selected" : "" }>0.4</option>
									<option value="0.5" ${info.trade_commission eq 0.5 ? "selected" : "" }>0.5</option>
									<option value="0.6" ${info.trade_commission eq 0.6 ? "selected" : "" }>0.6</option>
									<option value="0.7" ${info.trade_commission eq 0.7 ? "selected" : "" }>0.7</option>
									<option value="0.8" ${info.trade_commission eq 0.8 ? "selected" : "" }>0.8</option>
									<option value="0.9" ${info.trade_commission eq 0.9 ? "selected" : "" }>0.9</option>
									<option value="1" ${info.trade_commission eq 1 ? "selected" : "" }>1</option>
								</select>&nbsp;1表示不折扣，0表示不收，0.5表示打5折
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

<script>
</script>