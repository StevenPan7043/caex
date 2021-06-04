<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils" %>
<%@ include file="/jsp/topForList.jsp" %>

<div id="searchDiv" class="searchDiv">
    <div class="searchField">
        <div class="searchFieldLbl">UID</div>
        <div class="searchFieldCtr">
            <input id="member_id" name="member_id" type="text" class="txt enterAsSearch"/>
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
            <input id="currency" name="currency" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">时间范围</div>
        <div class="searchFieldCtr">
            <input type="text" name="startTime" id="startTime" maxlength="16" readonly="readonly"
                   class="txt txt_date required"/>
            --
            <input type="text" name="endTime" id="endTime" maxlength="16" readonly="readonly"
                   class="txt txt_date required"/>
        </div>
    </div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询"/>
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置"/>
</div>


<div class="main_content" style="width: 600px; float: left;">
    <div id="gridBox" style="margin:0; padding:0;"></div>
</div>
<div ID="userDetailDiv"
     style="width:auto; text-align: left; margin:3px; padding:5px; border: 1px solid #D6D6D6; font-size:14px; line-height: 30px; overflow-y: scroll;">

</div>

<script type="text/javascript">
    var grid;
    var itemsStr = "[ "
        + "{ text: '导出历史余额', click: dicOper, icon: 'logout' <bp:buttonAndColumnPermission functionId='HYZCGL-YHLSYE-DC'/>}]";
    $(function () {
        menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
        grid = $("#gridBox").ligerGrid({
            columns: [
                {
                    display: 'UID', name: 'member_id', width: 70, render: function (r, n, v) {
                        return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;
                    }
                },
                {display: '会员账号', name: 'm_name', width: 150},
                {display: '会员姓名', name: 'real_name', width: 90},
                {display: '币种', name: 'currency', width: 60},
                {display: '资产结余', name: 'totalMoney', width: 130},
                {display: '日期', name: 'record_date', width: 70},
            ],
            sortName: 'record_date',
            sortOrder: 'desc',
            isSort: true,
            url: "${rootPath}/member/everyTotalBalance",
            onSelectRow: showUserDetail,
            method: "get",
            rownumbers: true,
            onContextmenu : function (parm,e)
            {
                menu.show({ top: e.pageY, left: e.pageX });
                return false;
            },
            onDblClickRow : function (data, rowindex, rowobj)
            {
                var item = {text: '设置'};
                <bp:displayPermission functionId='HYZCGL-YHLSYE-DC'>dicOper(item);</bp:displayPermission>
            },
            delayLoad: true,
            toolbar:{  items: eval(itemsStr)  },
        });

        $('#btnOK').trigger('click');

        $("#userDetailDiv").height($(window).height() - 100);
    });

    function showUserDetail() {
        var obj = grid.getSelected();
        var id = obj.member_id;
        var m_name = obj.m_name;
        var record_date = obj.record_date;
        $.reqUrl("${rootPath}/member/balanceHistoryDetails", {
            m_name: m_name,
            record_date: record_date
        }, function (rst) {
            console.log(rst);
            var talks = "UID|账号：<%=HelpUtils.PRE_INTRODUCE_ID%>" + id + " | " + m_name + "<br>";
            talks += "<br><br>合约资产：<br>";
            for (var i = 0; i < rst.wallet.length; i++) {
                var logObj = rst.wallet[i];
                talks += "<div style='width: 80px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>";
                talks += logObj.currency + " 全仓：</div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>";
                talks += logObj.balance;
                talks += "</span>&nbsp;&nbsp;&nbsp;&nbsp;逐仓：</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>";
                talks += logObj.zcbalance + "</span></div><br>";
            }

            talks += "<br><br>币币资产：<br>";
            for (var i = 0; i < rst.mAccount.length; i++) {
                var logObj = rst.mAccount[i];
                talks += "<div style='width: 80px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>";
                talks += logObj.currency + " 总：</div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>";
                talks += logObj.total_balance;
                talks += "</span>&nbsp;&nbsp;&nbsp;&nbsp;冻结：</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>";
                talks += logObj.frozen_balance + "</span></div><br>";
            }

            talks += "<br><br>法币资产：<br>";
            for (var i = 0; i < rst.oAccount.length; i++) {
                var logObj = rst.oAccount[i];
                talks += "<div style='width: 80px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'>";
                talks += logObj.currency + " 总：</div><div style='width: 140px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>";
                talks += logObj.total_balance;
                talks += "</span>&nbsp;&nbsp;&nbsp;&nbsp;冻结：</div><div style='width: 100px; text-align: right; display: inline-block; border-bottom: 1px solid #ccc;'><span style='color: blue; font-weight: bold;'>";
                talks += logObj.frozen_balance + "</span></div><br>";
            }

            $("#userDetailDiv").html(talks);
        });

    }

    function dicOper(item){
        debugger
        var gm = $("#gridBox").ligerGetGridManager();
        switch (item.text) {
            case "导出历史余额":
                f_common_edit($("#gridBox"), "${rootPath}/member/member_balance_history_export?id={member_id}&m_name={m_name}", false, 750, 400, "导出历史余额");
                break;
        }
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
