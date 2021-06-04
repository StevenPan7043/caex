<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils" %>
<%@ include file="/jsp/topForList.jsp" %>

<div id="searchDiv" class="searchDiv">
    <input id="OTC" name="OTC" type="hidden" value="${OTC }" class="txt enterAsSearch"/>
    <input id="otc_owner_id" name="otc_owner_id" type="hidden" value="${otc_owner_id }" class="txt enterAsSearch"/>
    <div class="searchField">
        <div class="searchFieldLbl">交易对</div>
        <div class="searchFieldCtr">
            <input id="symbol" name="symbol" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">会员账号</div>
        <div class="searchFieldCtr">
            <input id="mName" name="mName" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">会员UID</div>
        <div class="searchFieldCtr">
            <input id="memberId" name="memberId" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <%--<div class="searchField">--%>
        <%--<div class="searchFieldCtr">--%>
            <%--<select id="r_status" name="r_status" class="sel">--%>
                <%--<option value="">--状态--</option>--%>
                <%--<option value="-1">待付款[OTC]</option>--%>
                <%--<option value="0">未确认</option>--%>
                <%--<option value="1">已确认</option>--%>
                <%--<option value="2">已取消</option>--%>
                <%--<option value="3">回写待确认</option>--%>
            <%--</select>--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="searchField">
        <div class="searchFieldCtr">
            <select id="isFree" name="isFree" class="sel">
                <option value="">--是否冻结--</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select>
        </div>
    </div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询"/>
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置"/>
</div>

<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
    var grid, manage;
    var itemsStr = "[ ";

    itemsStr += "{ text: '增加权限', click: dicOper, icon: 'add' <bp:buttonAndColumnPermission functionId='DATALAB-CAUTH—ADD'/>},";
    itemsStr += "{ text: '修改', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='DATALAB-CAUTH—EDIT'/>}";
    itemsStr += "]";

    $(function () {
        menu = $.ligerMenu({width: 120, items: eval(itemsStr)});
        grid = manage = $("#gridBox").ligerGrid({
            columns: [
                {
                    display: 'UID', name: 'memberId', width: 80, render: function (r, n, v) {
                        return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;
                    }
                },
                {display: '会员账号', name: 'mName', width: 150},
                {display: '交易对', name: 'symbol', width: 100},
                // {display: '权限', name: 'authority', width: 150},
                {display: '手续费比例(%)', name: 'feeScale', width: 120, align: 'right',render:function (r, n, v) {
                        return v+"%";
                    }},
                {display: '基础货币提现限额', name: 'baseWQuota', width: 125},
                {display: '计价货币提现限额', name: 'valuationWQuota', width: 125},
                {
                    display: '是否冻结', name: 'isFree', width: 125, render: function (r, n, v) {
                        return v == 0 && "否" || v == 1 && "是"
                    }
                },
                {display: '备注', name: 'remark', width: 200},
                {display: '创建时间', name: 'createTime', width: 180}
            ],
            sortName: 'id',
            // checkbox: true,
            sortOrder: 'desc',
            url: "${rootPath}/ca/getCurrencyAuthList",
            method: "get",
            rownumbers: true,
            onContextmenu: function (parm, e) {
                menu.show({top: e.pageY, left: e.pageX});
                return false;
            },
            delayLoad: true,
            toolbar: {items: eval(itemsStr)}
        });

        $('#btnOK').trigger('click');
    });

    function dicOper(item) {
        switch (item.text) {
            case "增加权限":
                openAddWindow("增加权限", "${rootPath}/ca/toAddCurrencyAuth", 750, 500);
                break;
            case "修改":
                f_common_edit($("#gridBox"), "${rootPath}/ca/toEditCurrencyAuth?id={id}", false, 1000, 700, "修改");
                break;
        }
    }


    // 删除
    <%--function deleteRecharge() {--%>
        <%--var obj = manage.selected;--%>
        <%--if (obj.length <= 0) {--%>
            <%--layer.alert('至少选择一个！');--%>
            <%--return;--%>
        <%--}--%>

        <%--$util.sure("您确定删除【" + obj.length + "】笔充值记录吗？", function () {--%>
            <%--var url = '${rootPath}/ca/toAddCurrencyAuth';--%>
            <%--$.ajax({--%>
                <%--method: 'post',--%>
                <%--url: url,--%>
                <%--dataType: "json",--%>
                <%--contentType: "application/json",--%>
                <%--data: JSON.stringify(obj),--%>
                <%--success: function (res) {--%>
                    <%--$.submitShowResult(res);--%>
                    <%--try {--%>
                        <%--$hook.opeRefresh("ope_refresh", "gridBox", "searchDiv");--%>
                    <%--} catch (e) {--%>
                        <%--layer.alert(e);--%>
                    <%--}--%>
                <%--},--%>
                <%--error: function (res) {--%>
                    <%--$.submitShowResult(res);--%>
                <%--}--%>
            <%--})--%>
        <%--});--%>

    <%--}--%>

    /**
     * 清空条件查询选项
     */
    function btnClearInput() {
        $("#searchDiv").find("option:selected").each(function () {
            this.selected = false;
        })
        $("#searchDiv input[type=text]").each(function () {
            this.value = null;
        })

    }
</script>
