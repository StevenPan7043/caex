<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form" method="post" action="${rootPath}/trade/set_trade_ranking_isopen" id="wanwuForm">
				<input type="hidden" name="ttrsid" id="ttrsid" value="${info.ttrsid}" />
				<input type="hidden" name="dcpid" id="dcpid" value="${info.dcpid}" />
				<input type="hidden" name="keyname" id="keyname" value="${info.keyname}" />
				<div class="itembox">
					<div class="div_block">
						<p class="p_block">
							<label class="label_block">交易对名称：</label>${info.dspname}
						</p>
						<!-- 						<p class="p_block"> -->
						<!-- 							<label class="label_block">开启交易时间：</label> -->
						<%-- 							<input type="text" class="txt txt_datetime required" value="${info.opentime}" readonly="readonly" /> --%>
						<!-- 						</p> -->
					</div>

					<div class="div_block">
						<p class="p_block">
							<label class="label_block">开始排名时间：</label>
							<input type="text" id="startdate" name="startdate" class="txt txt_datetime required" value="${info.startdate}"
								readonly="readonly" />
						</p>
						<p class="p_block">
							<label class="label_block">结束排名时间：</label>
							<input type="text" id="enddate" name="enddate" class="txt txt_datetime required" value="${info.enddate}"
								readonly="readonly" />
						</p>
					</div>

					<div class="div_block">
						<p class="p_block">
							<label class="label_block">状态 ：</label>${info.isopen == 1 ? '开启' : '关闭'}
						</p>
						<p class="p_block">
							<label class="label_block">是否开启：</label>
							<select name="isopen" class="sel ww_select" data="{code:'bool|id',initValue:'${info.isopen }'}"></select>
						</p>
					</div>
				</div>
				<p class="p_block p_btn">
					<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" />
					<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" value="关 闭" />
				</p>
			</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>
