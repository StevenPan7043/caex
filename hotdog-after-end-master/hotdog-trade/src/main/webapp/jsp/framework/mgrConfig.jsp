<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<style>
body{
	overflow:auto;
}
.div_block {
	border-bottom:1px solid #9f9f9f;
	margin-bottom:10px;
	padding-bottom:10px;
}
.div_block .label_block{
	width:150px;
}
.div_block .txt, .div_block .number {
    width: 300px;
}
select {
    width: 308px;
}
.div_block .p_block{
	width:460px;
}
p{
	padding-left:2px;
}
</style>

<body>
<div class="main_content">
	<div class="box_a1">
		<form class="wanwu_form"  method="post"  id="wanwuForm" action="${rootPath}/frm/modifyMgrConfig">
			<div class="itembox">
				<div class="div_block">
					<p class="p_block"><label class="label_block">公司名称：</label>
						<input type="text" value="${mgrConfig.comp_name }" name="comp_name" id="comp_name" maxlength="100" class="txt required" />
					</p>
					<p>
						公司名称，标题栏等处，均使用该字段内容
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">网站英文名称：</label>
						<input type="text" value="${mgrConfig.comp_en_name }" name="comp_en_name" id="comp_en_name" maxlength="100" class="txt required" />
					</p>
					<p>
						发送邮件时使用
					</p>
				</div>
				
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">启用大日志模式：</label>
						
						<select name="is_use_big_log" id="is_use_big_log" <c:if test="${user.user_name != 'admin' }">disabled</c:if> class="sel">
							<option value="1" <c:if test="${mgrConfig.is_use_big_log == '1' }">selected</c:if> >是</option>
							<option value="0" <c:if test="${mgrConfig.is_use_big_log == '0' }">selected</c:if>>否</option>
						</select>
							
					</p>
					<p>
						启用大日志模式时，日志文件会变大
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">启用登录验证码：</label>
						
						<select name="is_use_validate" id="is_use_validate"
								<c:if test="${user.user_name != 'admin' }">disabled</c:if> class="sel">
							<option value="1" <c:if test="${mgrConfig.is_use_validate == '1' }">selected</c:if> >是</option>
							<option value="0" <c:if test="${mgrConfig.is_use_validate == '0' }">selected</c:if>>否</option>
						</select>
							
					</p>
					<p>
						登录时，是否启用验证码
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">用户初始密码：</label>
						<input type="text" value="${mgrConfig.def_passwd }" name="def_passwd" id="def_passwd" maxlength="32"  class="txt required" />
					</p>
					<p>
						新增用户、初始化密码时默认的密码
					</p>
				
				</div>
				
				<div class="div_block" style="display: none;">
					<p class="p_block"><label class="label_block">邮件接收人：</label>
						<input type="text" value="${mgrConfig.mail_reciver }" name="mail_reciver" id="mail_reciver" maxlength="200" class="txt required" />
					</p>
					<p>
						邮件通知接收人，多个以逗号分隔
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">文章默认作者：</label>
						<input type="text" value="${mgrConfig.cms_def_author }" name="cms_def_author" id="cms_def_author" maxlength="8" class="txt  " />
					</p>
					<p>
						为空时，取当前登录用户
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">注册送币类型：</label>
						<input type="text" value="${mgrConfig.reg_reward_currencys }" name="reg_reward_currencys" id="reg_reward_currencys" maxlength="100" class="txt" />
					</p>
					<p>
						逗号分割多个
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">注册送币数量：</label>
						<input type="text" value="${mgrConfig.reg_reward_volume }" name="reg_reward_volume" id="reg_reward_volume" maxlength="100" class="txt" />
					</p>
					<p>
						逗号分割多个
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">推荐送币类型：</label>
						<input type="text" value="${mgrConfig.invite_reward_currencys }" name="invite_reward_currencys" id="invite_reward_currencys" maxlength="100" class="txt" />
					</p>
					<p>
						逗号分割多个
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">推荐送币数量：</label>
						<input type="text" value="${mgrConfig.invite_reward_volume }" name="invite_reward_volume" id="invite_reward_volume" maxlength="100" class="txt" />
					</p>
					<p>
						逗号分割多个
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">server_url：</label>
						<input type="text" value="${mgrConfig.server_url }" name="server_url" id="server_url" maxlength="50" class="txt required" />
					</p>
					<p>
						格式：https://www.aaa.com
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">redis_ip：</label>
						<input type="text" value="${mgrConfig.redis_ip }" name="redis_ip" id="redis_ip" maxlength="200" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">redis_port：</label>
						<input type="text" value="${mgrConfig.redis_port }" name="redis_port" id="redis_port" maxlength="200" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">redis_pwd：</label>
						<input type="text" value="${mgrConfig.redis_pwd }" name="redis_pwd" id="redis_pwd" maxlength="200" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">是否停止交易：</label>
						<input type="text" value="${mgrConfig.is_stop_ex }" name="is_stop_ex" id="is_stop_ex" maxlength="1" class="txt digits required" />
					</p>
					<p>
						1表示停止交易，0表示正常交易
					</p>
				</div>
				
				<div class="div_block" style="display: none;">
					<p class="p_block"><label class="label_block">交易量放大几倍：</label>
						<input type="text" value="${mgrConfig.volume_multiply }" name="volume_multiply" id="volume_multiply" maxlength="3" class="txt digits required {min:1}" />
					</p>
					<p>
						最小填1
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">刷数据频率：</label>
						<input type="text" value="${mgrConfig.fraud_seconds }" name="fraud_seconds" id="fraud_seconds" maxlength="3" class="txt digits required {min:1}" />
					</p>
					<p>
						单位秒，最小填1
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">提现是否需实名认证：</label>
						<input type="text" value="${mgrConfig.withdraw_need_identity }" name="withdraw_need_identity" id="withdraw_need_identity" maxlength="1" class="txt digits required" />
					</p>
					<p>
						1表示需要，0表示不需要
					</p>
				</div>
				
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">OTC商家申请押金币种：</label>
						<input type="text" value="${mgrConfig.otc_deposit_currency }" name="otc_deposit_currency" id="otc_deposit_currency" maxlength="200" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">OTC商家申请押金数量：</label>
						<input type="text" value="${mgrConfig.otc_deposit_volume }" name="otc_deposit_volume" id="otc_deposit_volume" maxlength="200" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				
				<div class="div_block">
					<p class="p_block"><label class="label_block">邮件NAME：</label>
						<input type="text" value="${mgrConfig.mail_name }" name="mail_name" id="mail_name" maxlength="50" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">邮件nickName：</label>
						<input type="text" value="${mgrConfig.mail_nick_name }" name="mail_nick_name" id="mail_nick_name" maxlength="50" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">邮件PASSWORD：</label>
						<input type="text" value="${mgrConfig.mail_password }" name="mail_password" id="mail_password" maxlength="50" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">数据交换Key：</label>
						<input type="text" value="${mgrConfig.ex_secreat }" name="ex_secreat" id="ex_secreat" maxlength="50" class="txt required" />
					</p>
					<p>
						
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">API访问平率限制：</label>
						<input type="text" value="${mgrConfig.api_access_limit_rule }" name="api_access_limit_rule" id="api_access_limit_rule" maxlength="500" class="txt required" />
					</p>
					<p>
						api访问限制规则，格式：order,60,100|account,60,80，表示order接口60秒内限制访问100次，account接口60秒内限制访问80次
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">代币充值地址：</label>
						<input type="text" value="${mgrConfig.eoslist }" name="eoslist" id="eoslist" maxlength="500" class="txt required" />
					</p>
					<p>

					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">邀请返佣率：</label>
						<input type="text" value="<fmt:formatNumber value="${mgrConfig.return_commi_rate}" pattern="#.###" minFractionDigits="4" > </fmt:formatNumber>" name="return_commi_rate" id="return_commi_rate" maxlength="500" class="txt required" />
					</p>
					<p>
						1,即返佣100%；0.25即返佣25%
					</p>
				</div>
				<div class="div_block">
					<p class="p_block"><label class="label_block">邀请返佣有效期：</label>
						<input type="text" value="${mgrConfig.return_commi_time }" name="return_commi_time" id="return_commi_time" maxlength="500" class="txt required" />
					</p>
					<p>
						单位：月（每月按30天）
					</p>
				</div>
			</div>
	
			<p class="p_block p_btn">
				 <input type="submit" value="确 定" name="btn_submit" class="btn_a btn_a1">
			</p>
		</form>
	</div>
</div>