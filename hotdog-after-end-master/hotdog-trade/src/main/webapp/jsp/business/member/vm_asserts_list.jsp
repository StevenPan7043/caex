<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
<%--	<div class="searchField">--%>
<%--		<div class="searchFieldCtr">--%>
<%--			<select id="m_status" name="m_status" class="sel">--%>
<%--				<option value="">会员状态</option>--%>
<%--				<option value="0">手机未验证</option>--%>
<%--				<option value="1">正常</option>--%>
<%--				<option value="2">冻结</option>--%>
<%--			</select>--%>
<%--		</div>--%>
<%--	</div>--%>

	<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content">
	<div id="gridBox"></div>
</div>
<script type="text/javascript">
    var grid,selectDialog;
    var itemsStr = "[ "
        + "{ text: '设置', click: dicOper, icon: 'modify'}]";
    $(function(){
        menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
        grid = $("#gridBox").ligerGrid({
            columns: [
                {display:'会员ID',name:'id',width:80},
				{display:'会员账号',name:'phone',width:150},
                {display:'币种',name:'currency',width:100},
                {display:'全仓资产',name:'balance',width:150},
                {display:'逐仓资产',name:'zcbalance',width:150}
            ],
            onContextmenu : function (parm,e)
            {
                menu.show({ top: e.pageY, left: e.pageX });
                return false;
            },
            onDblClickRow : function (data, rowindex, rowobj)
            {
                var item = {text: '设置'};
                dicOper(item);
            },
            delayLoad: true,
            rownumbers:true,
            url: "${rootPath}/member/vm_asserts",
            method: "get",
            sortName: 'id',
            sortOrder: 'asc',
            isSort: false
        });
        $('#btnOK').trigger('click');
    });

    function commonOpenDialog1(operDesc){
        layer.alert(operDesc);
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
    function dicOper(item) {
        debugger
        var gm = $("#gridBox").ligerGetGridManager();
        var row = gm.getSelected();
        switch (item.text) {
            case "设置":
                f_common_edit($("#gridBox"), "${rootPath}/member/vm_asserts_edit?id={id}&currency={currency}", false, 750, 320);
                break;
        }
    }

</script>

<%@ include file="../../bottom.jsp"%>