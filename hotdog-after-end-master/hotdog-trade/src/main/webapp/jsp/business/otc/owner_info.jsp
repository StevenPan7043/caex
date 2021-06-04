<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<style type="text/css">
table.gridtable {
	width: 500px;
	font-family: verdana, arial, sans-serif;
	font-size: 11px;
	color: #333333;
	border-width: 1px;
	border-color: #666666;
	border-collapse: collapse;
	margin-left: 40px;
}

table.gridtable th {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #dedede;
	text-align: center;
}

table.gridtable td {
	border-width: 1px;
	padding: 8px;
	border-style: solid;
	border-color: #666666;
	background-color: #ffffff;
}
</style>

<c:if test="${sessionScope.SYS_SESSION_USER.id == 1}">
	<div class="main_content">
		<div class="contbox_a1">
			<h1>管理员没有商户信息, 请在广告主管理中查看</h1>
		</div>
	</div>
</c:if>

<c:if test="${sessionScope.SYS_SESSION_USER.id != 1}">
	<div class="main_content">
		<div class="contbox_a1">
			<div class="box_a1">
				<div class="itembox">
					<div class="div_block">
						<p class="p_block" style="width: 100%;">
							<label class="label_block">广告主姓名：</label>${result.user.oname}
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width: 100%;">
							<label class="label_block">最后在线时间：</label>${result.user.lastTime}
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width: 100%;">
							<label class="label_block">银行卡信息：</label>${result.user.wxpayInfo}
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width: 100%;">
							<label class="label_block">支付宝信息：</label>${result.user.alipayInfo}
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width: 100%;">
							<label class="label_block">微信支付信息：</label>${result.user.wxpayInfo}
						</p>
					</div>
				</div>

				<table class="gridtable">
					<tr>
						<th>币种</th>
						<!-- <th>冻结额度</th> -->
						<th>可用额度</th>
						<!-- <th>总额度</th> -->
					</tr>
					<c:if test="${not empty result.list}">
						<c:forEach items="${result.list}" var="l">
							<tr>
								<td>${l.currency}</td>
								<!-- <td>0</td> -->
								<%-- <td>${l.max}</td> --%>
								<td style="text-align: center">${l.max}</td>
							</tr>
						</c:forEach>
					</c:if>
					<c:if test="${ empty result.list}">
						<tr>
							<td colspan="4" style="text-align: center;">没有数据</td>
						</tr>
					</c:if>
				</table>


				<hr class="hrStyle">
				<div class="div_block">
					<p class="p_block_all" style="margin-left: 15px;">
						<label class="label_block_info" style="display: block; color: blue; width: 100%; text-align: left;">1、最后上线时间、平均放行时间，创建时填写，后面系统会自动刷新</label>
						<label class="label_block_info" style="display: block; color: blue; width: 100%; text-align: left;">2、银行卡信息格式：户名▲卡号▲开户行▲手机</label>
						<label class="label_block_info" style="display: block; color: blue; width: 100%; text-align: left;">3、支付宝信息格式：户名▲账号▲二维码URL▲手机</label>
						<label class="label_block_info" style="display: block; color: blue; width: 100%; text-align: left;">4、微信信息格式：户名▲账号▲二维码URL▲手机▲微信号二维码URL</label>
					</p>
				</div>
			</div>
		</div>

	</div>

</c:if>
<%@ include file="/jsp/bottom.jsp"%>
