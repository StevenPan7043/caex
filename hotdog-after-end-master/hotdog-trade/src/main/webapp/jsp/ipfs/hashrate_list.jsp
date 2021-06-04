<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">项目Id</div>
		<div class="searchFieldCtr">
			<input id="projectId" name="projectId" type="text" class="txt enterAsSearch"/>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">会员Id</div>
		<div class="searchFieldCtr">
			<input id="memberId" name="memberId" type="text"
				   class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">状态</div>
		<div class="searchFieldCtr">
			<select name="status"  class="txt enterAsSearch">
				<option value="">请选择</option>
				<option value="1">有效</option>
				<option value="2">无效</option>
			</select>
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">类型</div>
		<div class="searchFieldCtr">
				<select name="type"  class="txt enterAsSearch">
					<option value="">请选择</option>
					<option value="1">算力</option>
					<option value="2">矿机</option>
				</select>
		</div>
	</div>
	<input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>


<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
	var grid;
	var itemsStr = "[ "
			+ "{ text: '批量修改', click: PLDicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='JYOTC-XNBTX-SH'/>},"
			+ "{ text: '修改', click: dicOper, icon: 'modify'}]";

	$(function () {
		menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
		grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'id',name:'id',width:100},
				{display:'项目id',name:'projectId',width:100},
				{display:'项目编码',name:'projectCode',width:150},
				{display:'项目名称',name:'projectName',width:100},
				{display:'用户Id',name:'memberId',width:90},
				{display:'状态',name:'status',width:90,
					render : function(r, n, v) {
						return v == '1' && '有效'
								|| v == '2' && '无效';
					}},
				{display:'类型',name:'type',width:100,
					render : function(r, n, v) {
						return v == '1' && '算力'
								|| v == '2' && '矿机';
					}},
				{display:'产出币种',name:'outputCurrency',width:90},
				{display:'单价',name:'price',width:90},
				{display:'购买币种',name:'quoteCurrency',width:90},
				{display:'购买数量',name:'num',width:100},
				{display:'购买总价',name:'total',width:60},
				{display:'购买时间',name:'createTime',width:250,
					render : function(r, n, v) {
						return datetimeFormat(v) ;
					}}

			],
			sortName: 'id',
			checkbox: true,
			sortOrder: 'desc',
			url: "${rootPath}/ipfs/hashrate",
			method: "get",
			rownumbers: true,
			onContextmenu : function (parm,e)
			{
				menu.show({ top: e.pageY, left: e.pageX });
				return false;
			},
			onDblClickRow : function (data, rowindex, rowobj)
			{
				var item = {text: '修改'};
				dicOper(item);
			},
			onTransferlClickRow : function (data, rowindex, rowobj)
			{
				var item = {text: '批量修改'};
				<bp:displayPermission functionId='CZTX-XNBTX-SH'>PLDicOper(item);</bp:displayPermission>
			},
			delayLoad: false,
			toolbar:{  items: eval(itemsStr)  }
		});
	});


	function dicOper(item){
		var gm = $("#gridBox").ligerGetGridManager();
		var row = gm.getSelected();

		switch (item.text) {
			case "修改":
				f_common_edit($("#gridBox"), "${rootPath}/ipfs/hashrate_edit?id={id}", false, 1000, 400, "修改");
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

	function datetimeFormat(longTypeDate){
		var datetimeType = "";
		var date = new Date();
		date.setTime(longTypeDate);
		datetimeType+= date.getFullYear();  //年
		datetimeType+= "-" + getMonth(date); //月
		datetimeType += "-" + getDay(date);  //日
		datetimeType+= "&nbsp;&nbsp;" + getHours(date);  //时
		datetimeType+= ":" + getMinutes(date);   //分
		datetimeType+= ":" + getSeconds(date);   //分
		return datetimeType;
	}
	function getMonth(date){
		var month = "";
		month = date.getMonth() + 1; //getMonth()得到的月份是0-11
		if(month<10){
			month = "0" + month;
		}
		return month;
	}
	//返回01-30的日期
	function getDay(date){
		var day = "";
		day = date.getDate();
		if(day<10){
			day = "0" + day;
		}
		return day;
	}
	//返回小时
	function getHours(date){
		var hours = "";
		hours = date.getHours();
		if(hours<10){
			hours = "0" + hours;
		}
		return hours;
	}
	//返回分
	function getMinutes(date){
		var minute = "";
		minute = date.getMinutes();
		if(minute<10){
			minute = "0" + minute;
		}
		return minute;
	}
	//返回秒
	function getSeconds(date){
		var second = "";
		second = date.getSeconds();
		if(second<10){
			second = "0" + second;
		}
		return second;
	}

	function PLDicOper(item){
		var obj = grid.selected;
		if(obj.length <= 0) {
			layer.alert('至少选择一个！');
			return;
		}
		var ids = "";
		var statuss = "";
		for (var i = 0; i < obj.length; i++) {
			ids += obj[i].id + ",";
			statuss += obj[i].status + ",";
		}

		$util.sure("您确定对这【"+obj.length+"】条进行批量修改吗？", function() {
			var url = '${rootPath}/ipfs/PLHashrate_edit';
			$.ajax({
				method: 'post',
				url: url,
				data: {
					"ids":ids,
					"statuss":statuss
				},
				success: function (res) {
					$.submitShowResult(res);
					try {
						// $hook.opeRefresh("ope_refresh","gridBox","searchDiv");
						location.reload();
					} catch (e) {
						layer.alert(e);
					}
				},
				error: function (res) {
					$.submitShowResult(res);
				}
			})
		});
	}
</script>
