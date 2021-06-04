<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils" %>
<%@ include file="/jsp/topForList.jsp" %>

<div id="searchDiv" class="searchDiv">
    <div class="searchField">
        <div class="searchFieldLbl">虚拟币</div>
        <div class="searchFieldCtr">
            <input id="currency" name="currency" type="text" class="txt enterAsSearch" />
        </div>
        <div class="searchField">
            <div class="searchFieldLbl">用户名</div>
            <div class="searchFieldCtr">
                <input id="m_name" name="m_name" type="text" class="txt enterAsSearch"/>
            </div>
        </div>
        <div class="searchField">
            <div class="searchFieldLbl">UID</div>
            <div class="searchFieldCtr">
                <input id="member_id" name="member_id" type="text" class="txt enterAsSearch"/>
            </div>
        </div>
        <div class="searchField">
            <div class="searchFieldCtr">
                <select id="f_status" name="f_status" class="sel">
                    <option value="">--状态--</option>
                    <option value="0">未解冻</option>
                    <option value="1">已解冻</option>
                </select>
            </div>
        </div>
        <div class="searchFieldLbl">设定解冻时间</div>
        <div class="searchFieldCtr">
            <input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${startDate}"/>
            --
            <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${endDate}"/>
        </div>
    </div>

    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>

<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
    var grid;
    var itemsStr = "[ "
        + "{ text: '手动解冻', click: dicOper, icon: 'modify'}]";
    $(function () {
        menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
        grid = $("#gridBox").ligerGrid({
            columns: [
                {display:'UID',name:'member_id',width:80},
                {display:'用户名',name:'m_name',width:200},
                {display:'币种',name:'currency',width:100},
                {display:'冻结数量',name:'frozen_balance',width:150},
                {display:'设定解冻时间',name:'unfrozen_time',width:150},
                {display:'状态',name:'f_status',width:100,
                    render:function(r,n,v){return v=='1'&&"<span style='color: red; font-weight: bold;'>" + '已解冻' + "</span>"||v=='0'&&"<span style='color: green; font-weight: bold;'>" + '未解冻' + "</span>";}
                },
            ],
            sortName: 'id',
            sortOrder: 'desc',
            method: 'get',
            checkbox: true,
            url: "${rootPath}/member/frozen_release",
            rownumbers: true,
            delayLoad: true,
            onContextmenu : function (parm,e)
            {
                menu.show({ top: e.pageY, left: e.pageX });
                return false;
            },
            toolbar:{  items: eval(itemsStr)  }
        });
        $('#btnOK').trigger('click');
    });

    function dicOper(item) {
        var gm = $("#gridBox").ligerGetGridManager();
        var row = gm.getSelected();

        switch (item.text) {
            case "手动解冻":
                f_common_edit($("#gridBox"), "${rootPath}/member/frozen_release_edit?id={id}", false, 750, 300, "手动解冻");
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
