<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/topForForm.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="/resources/js/commcomponent.js?v=6" type="text/javascript"></script>
<div class="main_content">
    <div class="contbox_a1">
        <div class="box_a1">
            <form class="wanwu_form" method="post" action="${rootPath}/member/lock_recharge_update" id="wanwuForm">
                <input type="hidden" name="id" id="id" value="${info.id}"/>
                <div class="itembox">
                    <input id="member_id" type="hidden" name="member_id" value="${info.member_id}"/>
                    <input id="currency_id" type="hidden" name="currency_id" value="${info.currency_id}"/>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">币种：</label>
                            <input id="currency" type="text" name="currency" readonly
                                   class="txt txt_readonly" value="${info.currency}"/>
                        </p>
                        <p class="p_block"><label class="label_block">锁仓数量：</label>
                            <input name="r_amount" id="r_amount" class="txt txt_readonly" readonly
                                   value="${info.r_amount}"/>
                        </p>
                    </div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">充值地址：</label>
                            <input id="r_address" type="text" name="r_address"  readonly
                                   class="txt txt_readonly" value="${info.r_address}"/>
                        </p>
                        <p class="p_block"><label class="label_block">是否确认：</label>
                            <select name="r_status" id="r_status" class="sel ww_select required" data="{code:'info|r_status',initValue:'${info.r_status }'}">
                                <option value="0" ${info.r_status == 0 ? "selected" : "" }>未确认</option>
                                <option value="1" ${info.r_status == 1 ? "selected" : "" }>确认</option>
                            </select>
                        </p>
                    </div>
                </div>
                <p class="p_block p_btn">
                    <input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"
                           value="关 闭"/>
                </p>
            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp" %>

<script>

</script>