<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="/otc/trade/complainAudit" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${complainTradeInfo.id}" />
					<div class="itembox">	
						<div class="div_block">
							<p class="p_block"><label class="label_block">会员ID：</label>
								<input id="memberId" type="text" name="memberId" class="txt txt_readonly" value="${complainTradeInfo.memberId}" readonly="readonly" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">申诉类型：</label>
								<input id="complainType" type="text" name="complainType" class="txt txt_readonly" value="${complainTradeInfo.complainType == 'NP' ? '对方未付款' : complainTradeInfo.complainType == 'UNCONFIRMED' ? '对方未放行' :  complainTradeInfo.complainType == 'CANCELED' ? '恶意取消订单' : '其他'} " readonly="readonly" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">申诉理由：</label>
								<input id="memo" type="text" name="memo" class="txt txt_readonly" value="${complainTradeInfo.memo} " readonly="readonly" style="width: 567px; font-size:16px;"/>
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">交易币种：</label>
								<input type="text" class="txt required txt_readonly" value="${complainTradeInfo.baseCurrency}" readonly="readonly" style="width: 567px; font-size:16px;" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width: 100%;"><label class="label_block">交易数量：</label>
								<input type="text" class="txt required txt_readonly" value="${complainTradeInfo.volume}" readonly="readonly" style="width: 567px; font-size:16px;" />
							</p>
						</div>
						
						<div class="div_block">
							<p class="p_block"><label class="label_block">处理结果：</label>
								<select name="opera" id="opera" class="sel">
									<option value="0">申诉驳回</option>
									<option value="1">交易撤消</option>
									<option value="2">交易完成</option>
								</select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block reject_reason_p" style="width: 100%; display: none;"><label class="label_block">驳回原因：</label>
								<input id="memo_result" style="width: 567px" type="text" name="memo_result" maxlength="200" class="txt required" value="" />
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定" />
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
$("#status").change(function () {  
    var status = $(this).children('option:selected').val();
    if (status == "APPLY_PASSED" || status == "SECEDE_PASSED") {  
		$(".reject_reason_p").hide();
		$("#memo").val("");
    } else if (status == "APPLY_REJECT" || status == "SECEDE_REJECT") {  
    	$(".reject_reason_p").show();
    }  
});
</script>