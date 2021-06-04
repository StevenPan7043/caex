<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">



		<div class="searchField">
			<div class="searchFieldLbl">会员账号</div>
			<div class="searchFieldCtr">
				<input id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" style="width: 200px" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">订单号</div>
			<div class="searchFieldCtr">
				<input id="order_no" name="order_no" type="text" class="txt enterAsSearch" style="width: 200px" />
			</div>
		</div>
        <div class="searchField">
            <div class="searchFieldLbl">划转</div>
            <div class="searchFieldCtr">
                <div class="searchFieldCtr">
                    <select id="is_transfer" name="is_transfer" class="sel">
                        <option value="">--请选择--</option>
                        <option value="0">未划转</option>
                        <option value="1">已划转</option>
                    </select>
                </div>
            </div>
        </div>
		<%--<div class="searchField">--%>
			<%--<div class="searchFieldLbl">接收地址</div>--%>
			<%--<div class="searchFieldCtr">--%>
				<%--<input id="addr" name="addr" type="text" class="txt enterAsSearch" style="width: 200px" />--%>
			<%--</div>--%>
		<%--</div>--%>
		<div class="searchField">
			<div class="searchFieldLbl">订单来源</div>
			<div class="searchFieldCtr">
				<select id="order_source" name="order_source" class="sel">
					<option value="">--请选择--</option>
					<%--<option value="ios">ios</option>--%>
					<%--<option value="android">android</option>--%>
					<option value="web">web</option>
					<option value="api">api</option>
				</select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">成交时间</div>
			<div class="searchFieldCtr">
				<input type="text" name="start_time" id="start_time"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value=""/>
			    --
			    <input type="text" name="end_time" id="end_time"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value=""/>
			</div>
		</div>
		<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
		<input id="transfer" type="button" value="划转" onclick="transferCurrency()" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />

	</div>
	<div class="main_content" style="width: 100%; ">
		<div id="gridBox"></div>
	</div>


<script type="text/javascript">
var grid,selectDialog,manage,firstLoad = true;
$(function(){
	 
	grid = manage = $("#gridBox").ligerGrid({
		columns: [
			{display:'币种',name:'currency',width:120},
			{display:'计价货币',name:'quote_currency',width:120},
			{display:'会员账号',name:'m_name',width:120},
			{display:'数量',name:'volume',width:80},
			{display:'价格',name:'price',width:100},
			{display:'总价',name:'total_price',width:100},
			{display:'订单号',name:'order_no',width:220},
			{display:'接收地址',name:'addr',width:220},
			{display:'成交时间',name:'done_time',width:125},
			{display:'划转时间',name:'update_time',width:125},
			{display:'划转',name:'is_transfer',width:50,render: function (r,n,v) {
				return v == 0? '否':'是';
            }},
			{display:'订单源',name:'order_source',width:100,render: function (r,n,v) {
                console.log(v);
                switch (v) {
					case 0: return 'web';
					case 1: return 'ios';
					case 2: return 'android';
					case 3: return 'api';
					default: return '';
				}


            }},
			{display:'是否锁仓',name:'is_lock',width:80,render: function (r,n,v) {
				return v == false ? '否' : '是';
            }}
		],
        checkbox: true,
        delayLoad: true,
		rownumbers:true,
		url: "${rootPath}/crowd/order/mgr/list",
		method: "get",
		sortName: 'id',
	   	sortOrder: 'desc'
	});

	$('#btnOK').trigger('click');


});
function transferCurrency() {

    var obj = manage.selected;
    if(obj.length <= 0) {
        layer.alert('至少选择一条记录！');
        return;
    }



    $util.sure("您确定要划转【"+obj.length+"】条订单记录吗？", function() {
        var url = '${rootPath}/crowd/transfer';
        $.ajax({
            method: 'post',
            url: url,
            dataType: "json",
            contentType:"application/json",
            data: JSON.stringify(obj),
            success: function (res) {
                $.submitShowResult(res);
                try {
                    $("#gridBox").ligerGetGridManager().loadData(true);
//                    $hook.opeRefresh("ope_refresh","gridBox","searchDiv");
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

<%@ include file="../../bottom.jsp"%>