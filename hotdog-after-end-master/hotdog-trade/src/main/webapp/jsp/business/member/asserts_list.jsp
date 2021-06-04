<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">币种</div>
		<div class="searchFieldCtr">
			<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="m_status" name="m_status" class="sel">
				<option value="">会员状态</option>
				<option value="0">手机未验证</option>
				<option value="1">正常</option>
				<option value="2">冻结</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="trade_commission" name="trade_commission" class="sel">
				<option value="">佣金折扣</option>
				<option value="0">0</option>
				<option value="0.05">0.05</option>
				<option value="0.1">0.1</option>
				<option value="0.2">0.2</option>
				<option value="0.3">0.3</option>
				<option value="0.4">0.4</option>
				<option value="0.5">0.5</option>
				<option value="0.6">0.6</option>
				<option value="0.7">0.7</option>
				<option value="0.8">0.8</option>
				<option value="0.9">0.9</option>
				<option value="1">1</option>
			</select>
		</div>
	</div>
	<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content">
	<div id="gridBox"></div>
</div>
<script type="text/javascript">
    var grid,selectDialog;
    var itemsStr = "[ "
        + "{ text: '设置', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='HYZCGL-ZCGL-SZ'/>}]";
    $(function(){
        menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
        grid = $("#gridBox").ligerGrid({
            columns: [
                {display:'会员ID',name:'id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},
                {display:'会员账号',name:'m_name',width:180},
                {display:'佣金折扣',name:'trade_commission',width:70},
                {display:'会员状态',name:'m_status',width:70,render:function(r,n,v){return v=='1'&&'正常'||v=='0'&&'未验证'||v=='2'&&'冻结';}},
                {display:'币种',name:'currency',width:100},
                {display:'总余额',name:'total_balance',width:150},
                {display:'冻结余额',name:'frozen_balance',width:150}
            ],
            onContextmenu : function (parm,e)
            {
                menu.show({ top: e.pageY, left: e.pageX });
                return false;
            },
            onDblClickRow : function (data, rowindex, rowobj)
            {
                var item = {text: '设置'};
                <bp:displayPermission functionId='HYZCGL-ZCGL-SZ'>dicOper(item);</bp:displayPermission>
            },
            delayLoad: true,
            rownumbers:true,
            url: "${rootPath}/member/asserts",
            method: "get",
            sortName: 'member_id',
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
                f_common_edit($("#gridBox"), "${rootPath}/member/asserts_edit?id={id}&currency={currency}", false, 750, 320);
                break;
        }
    }

</script>

<%@ include file="../../bottom.jsp"%>