<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<input id="otc_owner_id" name="otc_owner_id" type="hidden" value="${otc_owner_id }" class="txt enterAsSearch" />
	<div class="searchField">
		<div class="searchFieldLbl">虚拟币</div>
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
		<div class="searchFieldLbl">UID</div>
		<div class="searchFieldCtr">
			<input id="member_id" name="member_id" type="text" class="txt enterAsSearch" />
		</div>
	</div>

    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>

<div class="main_content" style="width: 60%;  float: left; margin:3px; overflow-y: scroll;">
	<div id="gridBox"></div>
</div>
<div id="main_content3" style="width: 38%;  margin:3px;  overflow-y: scroll;">
	<div id="gridBox3"></div>
</div>

<script type="text/javascript">
var grid,manage,firstLoad = true;
var itemsStr;
$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = manage= $("#gridBox").ligerGrid({
		columns: [
			{display:'id',name:'id',width:50},
			{display:'虚拟币',name:'currency',width:60},
			{display:'会员账号',name:'m_name',width:120},
			{display:'会员UID',name:'member_id',width:120},
			{display:'锁仓数量',name:'lock_num',width:100},
			{display:'待释放',name:'release_volume',width:100,render: function (row) {
				if (row.release_volume === undefined || row.release_volume === null) {
				    return "0";
				}
                return row.release_volume;
            }},
			{display:'创建时间',name:'create_time',width:120},
			{display:'更新时间',name:'update_time',width:120},
            {display:'操作',name:'member_id',width:60,render:
                function(row){

                    return "<a href='javascript:void(0);' onclick='loadBuyReleaseDetail(\""+row.member_id+","+row.currency+"\")'><div>明细</div></a>"
                }
            },
		],
		sortName: 'id',
//	  	checkbox: true,
	   	sortOrder: 'desc',
		url: "${rootPath}/member/buy_release_list_data",
//		onSelectRow: loadBuyReleaseDetail,s
		method: "get",
		rownumbers: true,
		onContextmenu : function (parm,e)
        {
            menu.show({ top: e.pageY, left: e.pageX });
            return false;
        },

		delayLoad: true
	});
      
	$('#btnOK').trigger('click');
	
	$("#userDetailDiv").height($(window).height() - 100);
    if(firstLoad){
        firstLoad = false;
        loadBuyReleaseDetail();
    }
});

// 加载明细
function loadBuyReleaseDetail(params) {
    if(params === undefined || params === null) {
        params = ","
	}
    var arr = params.split(",");
    grid = $("#gridBox3").ligerGrid({
        columns: [
            {display:'UID',name:'member_id',width:80},
            {display:'虚拟币',name:'currency',width:80},
            {display:'操作类型',name:'m_type',width:60,render: function (row) {
                if (row.m_type === 0) {
                    return "释放";
				}
                if (row.m_type === 1) {
                    return "充值";
                }
                return "";

            }},
            {display:'操前数量',name:'oper_num_before',width:100},
            {display:'操作数量',name:'oper_num',width:100},
            {display:'释放时间',name:'create_time',width:150}
        ],
        delayLoad: true,
        rownumbers:true,
        url: "${rootPath}/member/buy_release_list_data/detail?member_id="+arr[0]+"&currency="+arr[1],
        method: "get",
        sortName: 'create_time',
        sortOrder: 'asc',
        isSort: false
    });
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
