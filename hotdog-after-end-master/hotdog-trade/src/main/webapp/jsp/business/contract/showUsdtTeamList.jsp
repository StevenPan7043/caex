<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
    <%--<div class="searchField">--%>
        <%--<div class="searchFieldLbl">会员账号</div>--%>
        <%--<div class="searchFieldCtr">--%>
            <%--<input  id="pName" name="pName" type="text" class="txt enterAsSearch" />--%>
        <%--</div>--%>
    <%--</div>--%>
    <%--<div class="searchField">--%>
        <%--<div class="searchFieldLbl">业务员</div>--%>
        <%--<div class="searchFieldCtr">--%>
            <%--<input  id="salesmanName" name="salesmanName" type="text" class="txt enterAsSearch" />--%>
        <%--</div>--%>
    <%--</div>--%>
    <div class="searchField" style="margin-top: 10px" >
        <div class="searchFieldCtr" >
            <span>子公司：</span>
            <select id="userid" name="userid"  class="sel" >
                <option class="userid" value="" selected>--子公司--</option>
                <c:forEach items="${contractSysUsers}" var="m">
                    <option class="userid" value="${m.id}">${m.login}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="searchField">
        <span>划转类型：</span>
        <div class="searchFieldCtr">
            <select id="isout" name="isout" class="sel">
                <option value="" selected>--划转类型--</option>
                <option value="0">转出</option>
                <option value="1" >转入</option>
            </select>
        </div>
    </div>
    <div class="searchField">日期:</div>
    <div class="searchField">
        <input type="text" name="startDate" id="startDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${startDate}"/>
        --
        <input type="text" name="endDate" id="endDate"  maxlength="16" readonly="readonly" class="txt txt_datetime required" value="${endDate}"/>
    </div>
    <input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>
<div class="main_content">
    <div id="gridBox"></div>
</div>
<script type="text/javascript">
    var grid,selectDialog;
    $(function(){

        grid = $("#gridBox").ligerGrid({
            columns: [
                <%--{display:'会员ID',name:'id',width:80,render:function(r,n,v){return "<%=HelpUtils.PRE_INTRODUCE_ID%>" + v;}},--%>
                {display:'划转类型',name:'isoutCode',width:70},
                {display:'子公司',name:'contrSysLogin',width:100},
                {display:'次数',name:'queryCount',width:100},
                {display:'金额',name:'cost',width:100},
            ],
            delayLoad: true,
            rownumbers:true,
            url: "${rootPath}/contract/showUsdtTransferTeam",
            method: "get",
            sortName: 'createtime',
            sortOrder: 'desc',
            isSort: false
        });
        $('#btnOK').trigger('click');
    });

    function commonOpenDialog1(operDesc){
        layer.alert(operDesc);
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