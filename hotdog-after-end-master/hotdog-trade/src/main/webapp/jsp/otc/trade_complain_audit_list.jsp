<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">会员Id</div>
		<div class="searchFieldCtr">
			<input id="memberId" name="memberId" type="text"
				class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text"
					   class="txt enterAsSearch" />
			</div>
		</div>
	</div>
	<div class="searchField">
			<div class="searchFieldLbl">订单号</div>
			<div class="searchFieldCtr">
				<input id="tNumber" name="tNumber" type="text"
					class="txt enterAsSearch" />
			</div>
		</div>
	<div class="searchField">
		<div class="searchFieldLbl">交易ID </div>
		<div class="searchFieldCtr">
			<input id="depositCurrency" name="id" type="text"
				class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<select id="complainType" name="complainType" class="sel">
			<option value="">--申诉状态--</option>
			<option value="NP">对方未付款</option>
			<option value="UNCONFIRMED">对方未放行</option>
			<option value="CANCELED">恶意取消订单</option>
			<option value="OTHER">其他</option>
		</select>
	</div>
	<div class="searchField">
		<select id="status" name="status" class="sel">
			<option value="">--交易状态--</option>
			<option value="NP" >待付款</option>
			<option value="UNCONFIRMED">待确认</option>
			<option value="DONE">已完成</option>
			<option value="CANCELED">已取消</option>
			<option value="COMPLAINING" selected>申诉中</option>
		</select>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">基础币种 </div>
		<div class="searchFieldCtr">
			<input id="baseCurrency" name="baseCurrency" type="text"
				class="txt enterAsSearch" />
		</div>
	</div>
	<input id="btnOK" type="button" class="btn_a wanwu_search"
		data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>

<div id="gridBox" style="margin: 0; padding: 0;"></div>

<script type="text/javascript">
	var grid;
	var itemsStr = "[ " + "{ text: '审核', click: dicOper, icon: 'modify'}]";
	$(function() {
		menu = $.ligerMenu({
			width : 120,
			items : eval(itemsStr)
		});
		grid = $("#gridBox").ligerGrid(
				{
					columns : [
							{
								display : '交易ID',
								name : 'id',
								width : 60
							},
							{
								display : '会员ID',
								name : 'memberId',
								width : 60
							},
							{
								display : '会员账号',
								name : 'mName',
								width : 120
							},
							{
								display : '订单号',
								name : 'tNumber',
								width : 120
							},
							{
								display : '基础货币',
								name : 'baseCurrency',
								width : 80
							},
							{
								display : '计价货币',
								name : 'quoteCurrency',
								width : 80
							},
							{
								display : '订单类型',
								name : 'tType',
								width : 80
							},
							{
								display : '单价',
								name : 'price',
								width : 50
							},
							{
								display : '数量',
								name : 'volume',
								width : 50
							},
                        // {
                        //     display : '交易状态',
                        //     name : 'status',
                        //     width : 150,
                        //     render : function(r, n, v) {
                        //         return v == 'NP' && '对方未付款'
                        //             || v == 'UNCONFIRMED' && '对方未放行'
                        //             || v == 'CANCELED' && '恶意取消订单'
                        //             || v == 'OTHER' && '其他';
                        //     }
                        // },
							{
								display : '申诉类型',
								name : 'complainType',
								width : 100,
								render : function(r, n, v) {
									return v == 'NP' && '对方未付款'
											|| v == 'UNCONFIRMED' && '对方未放行'
											|| v == 'CANCELED' && '恶意取消订单'
											|| v == 'OTHER' && '其他';
								}
							},
                        {
                            display : '交易状态',
                            name : 'status',
                            width : 100,
                            render : function(r, n, v) {
                                return v == 'NP' && '待付款'
                                    || v == 'UNCONFIRMED' && '待确认'
                                    || v == 'DONE' && '已完成'
                                    || v == 'CANCELED' && '已取消'
                                    || v == 'COMPLAINING' && '申诉中';
                            }
                        },
							{
								display : '最后操作时间',
								name : 'modifyTime',
								width : 140
							}, {
								display : '申诉原因',
								name : 'memo',
								width : 300
							} ],
					sortName : 'modify_time',
					sortOrder : 'desc',
					url : "${rootPath}/member/trade_complain_audit",
					method : "get",
					rownumbers : true,
					onContextmenu : function(parm, e) {
						menu.show({
							top : e.pageY,
							left : e.pageX
						});
						return false;
					},
					onDblClickRow : function(data, rowindex, rowobj) {
						var item = {
							text : '审核'
						};
						dicOper(item);
					},
					delayLoad : true,
					toolbar : {
						items : eval(itemsStr)
					},
				});

		$('#btnOK').trigger('click');
	});

	function dicOper(item) {
		var gm = $("#gridBox").ligerGetGridManager();
		var row = gm.getSelected();

		switch (item.text) {
		case "审核":
			f_common_edit($("#gridBox"), "${rootPath}/member/trade_complain_audit_edit?id={id}",
					false, 900, 700, "审核");
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
