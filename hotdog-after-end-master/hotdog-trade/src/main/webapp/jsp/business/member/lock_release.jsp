<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">币种</div>
		<div class="searchFieldCtr">
			<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">释放时间</div>
		<div class="searchFieldCtr">
			<input id="undata" name="undata" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>

	<input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content">
	<div id="gridBox" style="margin:0; padding:0"></div>
</div>
<script type="text/javascript">
	var grid;
//	var itemsStr = "[ "
//	+ "{ text: '增加', click: dicOper, icon: 'add'},"
//	+ "{ text: '修改', click: dicOper, icon: 'modify'},"
//	+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	$(function () {
//		  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
	      grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'会员账号',name:'m_name',width:150},
				{display:'释放币种',name:'currency',width:150},
				{display:'释放时间',name:'untime',width:200},
				{display:'释放期数',name:'qishu',width:150},
				{display:'释放数量',name:'u_num',width:150}
			],
			sortName: 'id',
			method: 'get',
		   	sortOrder: 'asc',
			url: "${rootPath}/member/lock_release_list",
			rownumbers: true,
			onContextmenu : function (parm,e)
	        {
//	            menu.show({ top: e.pageY, left: e.pageX });
	            return false;
	        },
	        onDblClickRow : function (data, rowindex, rowobj)
			{
				var item = {text: '修改'};
			    dicOper(item);
			},
//			toolbar:{  items: eval(itemsStr)  }
		});
	});

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
