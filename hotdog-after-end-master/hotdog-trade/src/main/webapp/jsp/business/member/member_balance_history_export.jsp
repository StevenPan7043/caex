<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/topForForm.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<div class="main_content">
    <div class="contbox_a1">
        <div class="box_a1">
            <form class="wanwu_form" method="post" action="${rootPath}/member/balanceHistoryExport" id="wanwuForm">
                <div class="itembox">
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">UID：</label>
                            <input id="member_id" type="text" name="member_id" maxlength="40"
                                   class="txt required txt_readonly" value="${info.id}" readonly="readonly"/>
                        </p>
                    </div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">账号：</label>
                            <input id="m_name" type="text" name="m_name" maxlength="40"
                                   class="txt required txt_readonly" value="${info.m_name}" readonly="readonly"/>
                        </p>
                    </div>
                    <div class="div_block">
                        <p class="p_block_all"><label class="label_block">时间范围：</label>
                            <input type="text" name="startTime" id="startTime" maxlength="20" readonly="readonly"
                                   class="txt txt_date"/>
                            --
                            <input type="text" name="endTime" id="endTime" maxlength="20" readonly="readonly"
                                   class="txt txt_date"/>
                        </p>
                    </div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">币种：</label>
                    <input id="currency" name="currency" type="text" maxlength="40"
                    class="txt enterAsSearch"/>
                </div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">资产类型：</label>
                            <select name="m_status" id="m_status" class="sel">
                                <option value="0" ${info.m_status == 0 ? "selected" : "" }>合约资产</option>
                                <option value="1" ${info.m_status == 1 ? "selected" : "" }>币币资产</option>
                                <option value="2" ${info.m_status == 2 ? "selected" : "" }>法币资产</option>
                            </select>
                        </p>
                    </div>
                </div>
                <p class="p_block p_btn">
                    <input type="button" onclick="historyExport()" class="btn_a btn_a1" name="btn_submit" id="ok"
                           value="确 定"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"
                           value="关 闭"/>
                </p>
            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp" %>
<script>
    function historyExport() {
        var m_name = $("#m_name").val() === undefined ? '' : $("#m_name").val();
        var startTime = $("#startTime").val() === undefined ? '' : $("#startTime").val();
        var endTime = $("#endTime").val() === undefined ? '' : $("#endTime").val();
        var m_status = $("#m_status").val() === undefined ? '' : $("#m_status").val();
        var currency = $("#currency").val() === undefined ? '' : $("#currency").val();
        var params = "m_name=" + m_name
            + "&startTime=" + startTime
            + "&endTime=" + endTime
            + "&m_status=" + m_status
            + "&currency=" + currency;
        window.location.href = "${rootPath}/member/balanceHistoryExport?" + params;
        <%--var obj = manage.selected;--%>
        <%--var url = '${rootPath}/member/balanceHistoryExport';--%>
        <%--$.ajax({--%>
        <%--    method: 'post',--%>
        <%--    url: url,--%>
        <%--    dataType: "json",--%>
        <%--    contentType: "application/json",--%>
        <%--    data: JSON.stringify(obj),--%>
        <%--    success: function (res) {--%>
        <%--    },--%>
        <%--    error: function (res) {--%>
        <%--        $.submitShowResult(res);--%>
        <%--    }--%>
        <%--})--%>
    }
</script>