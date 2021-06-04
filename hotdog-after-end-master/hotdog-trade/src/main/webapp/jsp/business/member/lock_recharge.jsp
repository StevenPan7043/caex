<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="l_status" name="l_status" class="sel">
				<option value="">--状态--</option>
				<option value="0">未释放</option>
				<option value="1">已释放</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldCtr">
			<select id="l_type" name="l_type" class="sel">
				<option value="">--锁仓周期--</option>
				<option value="1">1个月</option>
				<option value="3">3个月</option>
				<option value="6">6个月</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">下单时间</div>
		<div class="searchFieldCtr">
			<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" />
			--
			<input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" />
		</div>
	</div>

	<input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div id="gridBox" style="margin:0; padding:0;"></div>
<script type="text/javascript">
	var grid;
	var itemsStr = "[ ]";
//	+ "{ text: '增加', click: dicOper, icon: 'add'},"
//	+ "{ text: '修改', click: dicOper, icon: 'modify'}]"
//	+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	$(function () {
		  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
	      grid = $("#gridBox").ligerGrid({
			columns: [
//				{display:'数据ID',name:'id',width:150},
				{display:'会员账号',name:'m_name',width:150},
				{display:'锁仓币种',name:'currency',width:70},
				{display:'锁仓数量',name:'amount',width:150},
				{display:'锁仓时间',name:'locktime',width:150,render:function (r,n,v) {
                    return $util.timestampToString(v * 1000);
                }},
				{display:'释放时间',name:'undata',width:150,render:function (r,n,v) {
                    return $util.timestampToString(v * 1000);
                }},
				{display:'锁仓周期(单位：月)',name:'l_type',width:150},
				{display:'状态',name:'l_status',width:50,render:function (r,n,v) {
					if(v == 0) return "未释放";
					if(v == 1) return "已释放";
                }},
			],
			sortName: 'id',
		   	sortOrder: 'asc',
			method: 'get',
			url: "${rootPath}/member/lock_recharge_list",
			rownumbers: true,
			onContextmenu : function (parm,e)
	        {
	            menu.show({ top: e.pageY, left: e.pageX });
	            return false;
	        },
			toolbar:{  items: eval(itemsStr)  }
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
