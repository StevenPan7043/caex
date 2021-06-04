<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/topForForm.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="main_content">
    <div class="contbox_a1">
        <div class="box_a1">
            <form class="wanwu_form" method="post" action="/otc/merchant/audit" id="wanwuForm">
                <input type="hidden" name="id" id="id" value="${merchantinfo.id}"/>
                <div class="itembox">
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">会员ID：</label>
                            <input id="memberId" type="text" name="memberId" class="txt txt_readonly"
                                   value="${merchantinfo.memberId}" readonly="readonly"/>
                        </p>
                        <p class="p_block"><label class="label_block">押金币种：</label>
                            <input id="depositCurrency" type="text" name="depositCurrency" class="txt txt_readonly"
                                   value="${merchantinfo.depositCurrency} " readonly="readonly"/>
                        </p>
                    </div>

                    <div class="div_block">
                        <p class="p_block" style="width: 100%;"><label class="label_block">币种数量：</label>
                            <input type="text" class="txt required txt_readonly" value="${merchantinfo.depositVolume}"
                                   readonly="readonly" style="width: 567px; font-size:16px;"/>
                        </p>
                    </div>
                    <div class="div_block">
                        <p class="p_block" style="width: 100%;"><label class="label_block">最后操作时间：</label>
                            <input type="text" class="txt required txt_readonly" value="${merchantinfo.modifyTime}"
                                   readonly="readonly" style="width: 567px; font-size:16px;"/>
                        </p>
                    </div>


                    <div class="div_block">
                        <p class="p_block"><label class="label_block">状态：</label>
                            <select name="status" id="status">

                                <option value="APPLY_REJECT"
                                        style="${merchantinfo.status == 'APPLY_AUDITING'  ? '' : 'display: none;'}"
                                        <c:if test="${merchantinfo.status == 'APPLY_AUDITING' }">selected</c:if> >申请驳回
                                </option>
                                <option value="APPLY_PASSED"
                                        style="${merchantinfo.status == 'APPLY_AUDITING'  ? '' : 'display: none;'}"
                                        <c:if test="${merchantinfo.status == 'APPLY_AUDITING' }">selected</c:if> >申请通过
                                </option>

                                <option value="SECEDE_REJECT"
                                        style="${merchantinfo.status == 'SECEDE_AUDITING' ? '' : 'display: none;'}"
                                        <c:if test="${merchantinfo.status == 'SECEDE_AUDITING'}">selected</c:if> >退商驳回
                                </option>
                                <option value="SECEDE_PASSED"
                                        style="${merchantinfo.status == 'SECEDE_AUDITING' ? '' : 'display: none;'}"
                                        <c:if test="${merchantinfo.status == 'SECEDE_AUDITING'}">selected</c:if> >退商通过
                                </option>

                            </select>
                        </p>
                    </div>
                    <div class="div_block">
                        <p class="p_block reject_reason_p" style="width: 100%; display: none;"><label
                                class="label_block">驳回原因：</label>
                            <input id="memo" style="width: 567px" type="text" name="memo" maxlength="200"
                                   class="txt required" value=""/>
                        </p>
                    </div>
                </div>
                <p class="p_block p_btn">
                    <input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"
                           style="${merchantinfo.status == 'APPLY_AUDITING' || merchantinfo.status == 'SECEDE_AUDITING' ? '' : 'display: none;'}"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"
                           value="关 闭"/>
                </p>
            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp" %>

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