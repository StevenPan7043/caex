<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<div class="main_content">
    <div class="contbox_a1">
        <div class="box_a1">
            <form class="wanwu_form"  method="post" action="${rootPath}/member/frozen_release_edit_do" id="wanwuForm">
                <input type="hidden" name="id" id="id" value="${info.id}" />
                <div class="itembox">
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">账号：</label>
                            <input id="m_name" type="text" name="m_name" class="txt txt_readonly" value="${info.m_name}" readonly="readonly" />
                        </p>
                        <p class="p_block"><label class="label_block">UID：</label>
                            <input id="member_id" type="text" name="member_id" class="txt txt_readonly" value="${info.member_id}" readonly="readonly" />
                        </p>
                    </div>

                    <div class="div_block">
                        <p class="p_block"><label class="label_block">币种：</label>
                            <input id="currency" type="text" name="currency" class="txt txt_readonly" value="${info.currency}" readonly="readonly" />
                        </p>
                        <p class="p_block"><label class="label_block">冻结数量：</label>
                            <input id="frozen_balance" type="text" name="frozen_balance" class="txt txt_readonly" value="${info.frozen_balance}" readonly="readonly" />
                        </p>
                    </div>


                    <div class="div_block">
                        <p class="p_block"><label class="label_block">设定解冻时间：</label>
                            <input id="unfrozen_time" type="text" name="unfrozen_time" class="txt txt_readonly" value="${info.unfrozen_time}" readonly="readonly" />
                        </p>
                    </div>
                </div>
                <p class="p_block p_btn">
                    <input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="解 冻" style="${info.f_status == 0 ? '' : 'display: none;'}"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
                </p>

            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

