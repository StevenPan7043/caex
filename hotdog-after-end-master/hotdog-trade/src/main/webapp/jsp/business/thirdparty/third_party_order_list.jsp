<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>

	<div id="searchDiv" class="searchDiv">

		<div class="searchField">
			<div class="searchFieldLbl">项目方</div>
			<div class="searchFieldCtr">
				<select id="c_name" name="c_name" class="sel">
					<option value="">--请选择--</option>
					<option value="jsc">JSC</option>
					<option value="gstt">GSTTProject</option>
					<option value="lastwinner">lastWinner</option>
					<option value="gmc">gmc</option>
				</select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">下单时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${transferTime}"/>
				--
				<input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${transferTime}"/>
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
	var itemsStr = "";
	$(function () {
		menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
		grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'会员账号',name:'m_name',width:150},
				{display:'币种',name:'currency',width:80},
				{display:'交易ID',name:'tradeID',width:280},
				{display:'划转时间',name:'transferTime',width:140},
				{display:'划转前数量',name:'currentNum',width:140},
				{display:'划转数量',name:'transferNum',width:140},
				{display:'划转类型',name:'transferType',width:80,render : function(r, n, v){
                    return v == 'deposit' && '转入'
                        || v == 'withdraw' && '转出';
                }},
				{display:'划转状态',name:'transferStatus',width:140},
			],
			url: "${rootPath}/api/order/list_data",
			method: "get",
			rownumbers: true,
			toolbar:{  items: eval(itemsStr)  },
			sortName: 'id',
		   	sortOrder: 'asc',
		   	isSort: false,
		   	autoRefresh:false
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
<%@ include file="../../bottom.jsp"%>
