<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils" %>
<%@ include file="/jsp/topForList.jsp" %>

<div id="searchDiv" class="searchDiv">
    <input id="OTC" name="OTC" type="hidden" value="${OTC }" class="txt enterAsSearch"/>
    <input id="otc_owner_id" name="otc_owner_id" type="hidden" value="${otc_owner_id }" class="txt enterAsSearch"/>
    <div class="searchField">
        <div class="searchFieldLbl">币种</div>
        <div class="searchFieldCtr">
            <input id="currency" name="currency" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">账号</div>
        <div class="searchFieldCtr">
            <input id="m_name" name="m_name" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">UID</div>
        <div class="searchFieldCtr">
            <input id="id" name="id" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">地址</div>
        <div class="searchFieldCtr">
            <input id="_r_address" name="_r_address" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldCtr">
            <select id="r_status" name="r_status" class="sel">
                <option value="">--状态--</option>
                <option value="-1">待付款[OTC]</option>
                <option value="0">未确认</option>
                <option value="1">已确认</option>
                <option value="2">已取消</option>
                <option value="3">回写待确认</option>
            </select>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldCtr">
            <select id="r_address" name="r_address" class="sel">
                <option value="">--来源--</option>
                <option value="INVITE_REWARD">推荐奖励</option>
                <option value="SYS_RECHARGE">正常充值(钱包漏扫等情况使用)</option>
                <option value="MAN_RECHARGE">平台人工充值</option>
                <option value="REG_REWARD">注册奖励</option>
                <option value="SYS_REWARD">系统奖励</option>
                <option value="TRADE_RANKING_REWARD">交易排名奖励</option>
                <option value="TRADE_RETURN_FEE_REWARD">交易返还手续费</option>
                <option value="wxpay">wxpay</option>
                <option value="alipay">alipay</option>
                <option value="bank">bank</option>
                <option value="RETURN_COMMISSION">返佣</option>
                <option value="IEO众筹">IEO众筹</option>
                <option value="TRANSFER">划转</option>
                <option value="DATALAB">数据实验室</option>

            </select>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldCtr">
            <select id="r_guiji" name="r_guiji" class="sel">
                <option value="">--是否归集--</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldCtr">
            <select id="r_gas" name="r_gas" class="sel">
                <option value="">--转手续费--</option>
                <option value="1">是</option>
                <option value="0">否</option>
            </select>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">充值时间</div>
        <div class="searchFieldCtr">
            <input type="text" name="create_time_start" id="create_time_start" maxlength="16" readonly="readonly"
                   class="txt txt_datetime required"/>
            --
            <input type="text" name="create_time_end" id="create_time_end" maxlength="16" readonly="readonly"
                   class="txt txt_datetime required"/>
        </div>
    </div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询"/>
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
    <input onclick="deleteRecharge()" type="button" value="删除" data="{grid:'gridBox',scope:'searchDiv'}"
           class="btn_a ope_refresh"/>
</div>

<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
    var grid, manage;
    var itemsStr = "[ ";

    if ("${OTC }" == 0 && "${user_name}" == 'admin') {
        itemsStr += "{ text: '手动充值', click: dicOper, icon: 'modify' <bp:buttonAndColumnPermission functionId='CZTX-XNBCZ-SDCZ'/>},";
    }
    if ("${OTC }" == "1") {
        itemsStr += "{ text: '确认入账', click: dicOper, icon: 'ok' <bp:buttonAndColumnPermission functionId='JYOTC-XNBCZ-SDCZ'/>},"
    } else {
        itemsStr += "{ text: '确认入账', click: dicOper, icon: 'ok' <bp:buttonAndColumnPermission functionId='CZTX-XNBCZ-SDCZ'/>},"
    }
    itemsStr += "{ text: '报表导出', click: dicOper, icon: 'logout' <bp:buttonAndColumnPermission functionId='CZTX-XNBCZ-SDCZ'/>}]";

    $(function () {
        menu = $.ligerMenu({width: 120, items: eval(itemsStr)});
        grid = manage = $("#gridBox").ligerGrid({
            columns: [
                {display: '虚拟币', name: 'currency', width: 80},
                {
                    display: 'UID', name: 'member_id', width: 80, render: function (r, n, v) {
                    return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;
                }
                },
                {display: '会员账号', name: 'm_name', width: 150},
                {display: '充值数量', name: 'r_amount', width: 80, align: 'right'},
                {display: '充值时间', name: 'r_create_time', width: 125},
                // r: 所有字段，n：第几条，v：该字段的值
                {
                    display: '我方地址', name: 'r_address', width: 330, align: 'left', render: function (r, n, v) {
                    var tmp = v.split('→');
                    var addr = '';
                    for (var i = 0; i < tmp.length; i++) {
                        if (tmp[i]) {
                            var info = tmp[i].split('▲');
                            if (info[0] == 'bank') {
                                addr = '支付类型：' + info[0] + '<br>卡号：' + info[2] + '<br>姓名：' + info[1] + '<br>银行：' + info[3];
                            } else if (info[0] == 'alipay' || info[0] == 'wxpay') {
                                addr = '支付类型：' + info[0] + '<br>卡号：' + info[2] + '<br>姓名：' + info[1] + '<br>图片链接' + info[3];
                            }
                        }
                    }
                    if (addr == '') {
                        if (r.r_address_ != 'null') {
                            v = '<a target="_blank" href="' + r.r_address_ + r.r_txid + '">' + v + '</a>'
                        }
                        return v;
                    } else {
                        return addr;
                    }
                }
                },
                /*
                {display:'Hash地址',name:'r_txid',width:380,align: 'left',render:function (r,n,v) {
                    if(v !='' && v != null && v.includes('0x')) {
                        return '<a target="_blank" href="https://etherscan.io/tx/'+v+'">'+v+'</a>'
                    }
                    return v;
                }},
                */
                //{display:'确认数',name:'r_confirmations',width:70},
                {
                    display: '来源', name: 'r_address', width: 80, render: function (r, n, v) {
                        if (r.currency == "USDT" && r.r_address == "TRANSFER") {
                            return v == 'INVITE_REWARD' && '推荐奖励' || v == 'MAN_RECHARGE' && '平台人工充值' || v == 'REG_REWARD' && '注册奖励' || v == 'SYS_REWARD' && '系统奖励' || v == 'COMMUNITY_RECHARGE' && '社区划转' || v == 'TRANSFER' && "<span style='color: red; font-weight: bold;'>" + '通道划转' + "</span>" || '正常充值';
                        } else {
                            return v == 'INVITE_REWARD' && '推荐奖励' || v == 'MAN_RECHARGE' && '平台人工充值' || v == 'REG_REWARD' && '注册奖励' || v == 'SYS_REWARD' && '系统奖励' || v == 'COMMUNITY_RECHARGE' && '社区划转' || v == 'TRANSFER' && '通道划转' || v == 'DATALAB' && '数据实验室' || '正常充值';
                        }
                    }
                },
                {
                    display: '状态', name: 'r_status', width: 70, render: function (r, n, v) {
                    return (v == '3' && '回写待确认' || v == '-1' && '待付款' || v == '1' && "<span style='color: red; font-weight: bold;'>" + '已确认' + "</span>" || v == '0' && "<span style='color:green; font-weight: bold;'>" + '未确认' + "</span>" || v == '2' && '已取消') + ("<br><span style='color: blue;' title='" + (v == '2' ? r.reject_reason : '') + "'>" + (v == '2' ? r.reject_reason : '') + "</span>");
                }
                },
                {
                    display: '是否归集', name: 'r_guiji', width: 70, render: function (r, n, v) {
                    return v > 0 ? '是' : '否'
                }
                },
                {
                    display: '是否转手续费', name: 'r_gas', width: 100, render: function (r, n, v) {
                    return v > 0 ? '是' : '否'
                }
                }
            ],
            sortName: 'id',
            checkbox: true,
            sortOrder: 'desc',
            url: "${rootPath}/member/coin_recharge",
            method: "get",
            rownumbers: true,
            onContextmenu: function (parm, e) {
                menu.show({top: e.pageY, left: e.pageX});
                return false;
            },
            onDblClickRow: function (data, rowindex, rowobj) {
                var item = {text: '确认入账'};
                <bp:displayPermission functionId='CZTX-XNBCZ-SDCZ'>dicOper(item);
                </bp:displayPermission>
            },
            delayLoad: true,
            toolbar: {items: eval(itemsStr)}
        });

        $('#btnOK').trigger('click');
    });

    function dicOper(item) {
        var gm = $("#gridBox").ligerGetGridManager();
        var row = gm.getSelected();

        switch (item.text) {
            case "手动充值":
                openAddWindow("手动充值", "${rootPath}/member/coin_recharge_add", 750, 500);
                break;
            case "确认入账":
                if (row.r_status != "0") {
                    //layer.alert("只有未确认的订单才可以确认");
                    //return;
                }
                f_common_edit($("#gridBox"), "${rootPath}/member/coin_recharge_add?id={id}", false, 750, 620, "确认入账");
                break;
            case "报表导出":
                var start_time = $("#create_time_start").val() === undefined ? '' : $("#create_time_start").val();
                var end_time = $("#create_time_end").val() === undefined ? '' : $("#create_time_end").val();
                var m_name = $("#m_name").val() === undefined ? '' : $("#m_name").val();
                var uid = $("#id").val() === undefined ? '' : $("#id").val();
                var r_status = $("#r_status").val() === undefined ? '' : $("#r_status").val();
                var r_address = $("#r_address").val() === undefined ? '' : $("#r_address").val();
                var r_guiji = $("#r_guiji").val() === undefined ? '' : $("#r_guiji").val();
                var currency = $("#currency").val() === undefined ? '' : $("#currency").val();
                var OTC = "${OTC}";
                var params = "create_time_start="+start_time
                    + "&create_time_end="+end_time
                    + "&m_name="+m_name
                    + "&member_id="+uid
                    + "&r_status="+r_status
                    + "&r_address="+r_address
                    + "&r_guiji="+r_guiji
                    + "&OTC="+OTC
                    + "&currency="+currency;
                window.location.href = "${rootPath}/member/coinRecharge/export?"+params;

                break;
        }
    }


    // 删除
    function deleteRecharge() {
        var obj = manage.selected;
        if (obj.length <= 0) {
            layer.alert('至少选择一个！');
            return;
        }

        $util.sure("您确定删除【" + obj.length + "】笔充值记录吗？", function () {
            var url = '${rootPath}/member/coin_recharge_delete';
            $.ajax({
                method: 'post',
                url: url,
                dataType: "json",
                contentType: "application/json",
                data: JSON.stringify(obj),
                success: function (res) {
                    $.submitShowResult(res);
                    try {
                        $hook.opeRefresh("ope_refresh", "gridBox", "searchDiv");
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
