<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/topForList.jsp" %>

<div id="searchDiv" class="searchDiv">
    <div class="searchField">
        <div class="searchFieldLbl">项目Id</div>
        <div class="searchFieldCtr">
            <input id="projectId" name="projectId" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">算力Id</div>
        <div class="searchFieldCtr">
            <input id="hashrateId" name="hashrateId" type="text" class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">会员Id</div>
        <div class="searchFieldCtr">
            <input id="memberId" name="memberId" type="text"
                   class="txt enterAsSearch"/>
        </div>
    </div>
    <div class="searchField">
        <div class="searchFieldLbl">分红日期</div>
        <div class="searchFieldCtr">
            <input type="text" name="startTime" id="startTime" maxlength="16" readonly="readonly"
                   class="txt txt_date required"/>
            --
            <input type="text" name="endTime" id="endTime" maxlength="16" readonly="readonly"
                   class="txt txt_date required"/>
        </div>
    </div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询"/>
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置"/>
</div>
<div class="searchDiv">
    <div class="searchField">
        <div class="searchFieldLbl">总合计</div>
        <div class="searchFieldCtr">
            <input type="text" id="allbonus" class="txt required" value="0" maxlength="16" readonly="true"/>
        </div>
    </div>
</div>
<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
    var grid;
    var itemsStr = "[{ text: '导出历史余额'}]";
    $(function () {
        // menu = $.ligerMenu({width: 120, items: eval(itemsStr)});
        grid = $("#gridBox").ligerGrid({
            columns: [
                {display: '项目id', name: 'projectId', width: 100},
                {display: '项目编码', name: 'projectCode', width: 150},
                {display: '算力id', name: 'hashrateId', width: 100},
                {display: '用户Id', name: 'memberId', width: 90},
                {display: '产出币种', name: 'outputCurrency', width: 90},
                {display: '分红数量', name: 'bonusNum', width: 100},
                {display: '手续费', name: 'fee', width: 100},
                {display: '分红日期', name: 'bonusDate', width: 100},
                {
                    display: '分红时间', name: 'createTime', width: 250,
                    render: function (r, n, v) {
                        return datetimeFormat(v);
                    }
                }
            ],
            sortName: 'id',
            sortOrder: 'desc',
            url: "${rootPath}/ipfs/userBonus",
            method: "get",
            rownumbers: true,
            delayLoad: false,
            enabledSort:false,
            onSuccess: function (data) {
                getData(data);
            },
        });
    });

    function getData(data) {
        $('input[id="allbonus"]').val(data["Allbonus"]);
        // $("#total").html(data["Allbonus"]);
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

    function datetimeFormat(longTypeDate) {
        var datetimeType = "";
        var date = new Date();
        date.setTime(longTypeDate);
        datetimeType += date.getFullYear();  //年
        datetimeType += "-" + getMonth(date); //月
        datetimeType += "-" + getDay(date);  //日
        datetimeType += "&nbsp;&nbsp;" + getHours(date);  //时
        datetimeType += ":" + getMinutes(date);   //分
        datetimeType += ":" + getSeconds(date);   //分
        return datetimeType;
    }

    function getMonth(date) {
        var month = "";
        month = date.getMonth() + 1; //getMonth()得到的月份是0-11
        if (month < 10) {
            month = "0" + month;
        }
        return month;
    }

    //返回01-30的日期
    function getDay(date) {
        var day = "";
        day = date.getDate();
        if (day < 10) {
            day = "0" + day;
        }
        return day;
    }

    //返回小时
    function getHours(date) {
        var hours = "";
        hours = date.getHours();
        if (hours < 10) {
            hours = "0" + hours;
        }
        return hours;
    }

    //返回分
    function getMinutes(date) {
        var minute = "";
        minute = date.getMinutes();
        if (minute < 10) {
            minute = "0" + minute;
        }
        return minute;
    }

    //返回秒
    function getSeconds(date) {
        var second = "";
        second = date.getSeconds();
        if (second < 10) {
            second = "0" + second;
        }
        return second;
    }
</script>
