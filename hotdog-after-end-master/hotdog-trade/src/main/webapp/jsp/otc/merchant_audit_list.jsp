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
		<div class="searchFieldLbl">押金币种 </div>
		<div class="searchFieldCtr">
			<input id="depositCurrency" name="depositCurrency" type="text"
				class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">状态</div>
		<div class="searchFieldCtr">
			<select id="status" name="status" class="sel">
				<option value="">--请选择--</option>
				<option value="APPLY_AUDITING">申请审核</option>
				<option value="APPLY_PASSED">申请通过</option>
				<option value="APPLY_REJECT">申请驳回</option>
				<option value="SECEDE_AUDITING">退商申请</option>
			</select>
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
								display : '会员ID',
								name : 'memberId',
								width : 180
							},
							{
								display : '会员账户',
								name : 'mName',
								width : 180
							},
							{
								display : '押金币种',
								name : 'depositCurrency',
								width : 120
							},
							{
								display : '币种数量',
								name : 'depositVolume',
								width : 250
							},
							{
								display : '最后操作时间',
								name : 'modifyTime',
								width : 250
							},
							{
								display : '状态',
								name : 'status',
								width : 80,
								render : function(r, n, v) {
									return v == 'APPLY_AUDITING' && '申请审核'
											|| v == 'APPLY_PASSED' && '申请通过'
											|| v == 'APPLY_REJECT' && '申请驳回'
											|| v == 'SECEDE_AUDITING' && '退商申请';
								}
							}, {
								display : '备注',
								name : 'memo',
								width : 100
							}
							,{
                            display : '操作',
                            name : '',
                            width: 100,
                            render:
                                function (row) {
                                    if (row.status != 'APPLY_PASSED') {
                                        return "";
                                    }
                                    return "<a href='javascript:void(0);' onclick='charRecord("
                                        + row.id + "," + row.memberId+"," + row.mName+")' ><div>注销</div></a>";
                                }
                        }
                        ],
					sortName : 'modify_time',
					sortOrder : 'desc',
					url : "${rootPath}/member/merchant_audit",
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
			f_common_edit($("#gridBox"), "${rootPath}/member/merchant_audit_edit?id={id}",
					false, 900, 700, "审核");
			break;
		}
	}

    //取消商家资格
    function charRecord(id, memberId,mName) {
        $util.sure("您确定要取消会员:["+mName+"]的商家资格么？", function() {
            var url = '${rootPath}/otc/merchant/cancelBusAuth';
            $.ajax({
                method: 'get',
                url: url,
                dataType: "json",
                contentType:"application/json",
                data: {
                    "id":id,"memberId":memberId
                },
                success: function (res) {
                    $.submitShowResult(res);
                    try {
                        $hook.opeRefresh("ope_refresh","gridBox","searchDiv");
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
