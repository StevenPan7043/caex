<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils" %>
<%@ include file="../../topForList.jsp" %>
<div id="searchDiv" class="searchDiv">
    <div class="searchField">
        <div class="searchFieldLbl">UID</div>
        <div class="searchFieldCtr">
            <input id="id" name="id" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">会员账号</div>
        <div class="searchFieldCtr">
            <input id="m_name" name="m_name" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">币种</div>
        <div class="searchFieldCtr">
            <input id="type" name="type" type="text" class="txt enterAsSearch"/>
        </div>
    </div>

    <input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}"/>
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置"/>
</div>
<div class="main_content">
    <div id="gridBox" style="margin:0; padding:0;"></div>
</div>
<script type="text/javascript">
    var grid;
    var itemsStr = "[{ text: '导出合约资产', click: dicOper, icon: 'logout'}]";
    $(function () {
        menu = $.ligerMenu({width: 120, items: eval(itemsStr)});
        grid = $("#gridBox").ligerGrid({
            columns: [
                {
                    display: '会员ID', name: 'member_id', width: 80, render: function (r, n, v) {
                        return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;
                    }
                },
                {display: '会员账号', name: 'm_name', width: 180},
                {display: '币种', name: 'currency', width: 80},
                {display: '全仓资产', name: 'balance', width: 150},
                {display: '逐仓资产', name: 'zcbalance', width: 150},
                {display: '周期资产', name: 'gdbalance', width: 150},
            ],
            rownumbers: true,
            sortName: 'id',
            sortOrder: 'desc',
            isSort: false,
            url: "${rootPath}/member/contractBalance",
            method: "get",
            onContextmenu : function (parm,e)
            {
                menu.show({ top: e.pageY, left: e.pageX });
                return false;
            },
            // onDblClickRow: function () {
            //     dicOper();
            // },
            delayLoad: true,
            toolbar: {items: eval(itemsStr)},
        });
        $('#btnOK').trigger('click');
    });

    function dicOper() {
        window.location.href = "${rootPath}/member/contractBalanceExport";
    }

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

<%@ include file="../../bottom.jsp" %>