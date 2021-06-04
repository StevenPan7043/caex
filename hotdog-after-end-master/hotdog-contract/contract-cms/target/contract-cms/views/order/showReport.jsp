<%@page import="com.contract.service.cms.PlatSession" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <meta name="renderer" content="webkit|ie-comp|ie-stand">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport"
          content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no"/>
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <!--[if lt IE 9]>
    <script type="text/javascript" src="lib/html5shiv.js"></script>
    <script type="text/javascript" src="lib/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="/static/h-ui/css/H-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/H-ui.admin.css"/>
    <link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css"/>
    <link rel="stylesheet" type="text/css" href="/static/h-ui.admin/skin/default/skin.css" id="skin"/>
    <link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/style.css"/>
    <!--[if IE 6]>
    <script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js"></script>
    <script>DD_belatedPNG.fix('*');</script>
    <![endif]-->
    <title>合约交易</title>
</head>
<body>
<nav class="breadcrumb">
    <i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 合约交易 <span class="c-gray en">&gt;</span>
    报表 <a
        class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px"
        href="javascript:location.replace(location.href);" title="刷新"><i
        class="Hui-iconfont">&#xe68f;</i></a>
</nav>
<form action="/order/showReport" id="seachFrom">
    <div class="page-container">
        <div class="col-12">
            <div class="col-4 cl f-l">
                <label>日期范围：</label>
                <input value="${order.starttime }" type="text"
                       onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin"
                       class="input-text Wdate" style="width:120px;" name="starttime">
                -
                <input value="${order.endtime }" type="text"
                       onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax"
                       class="input-text Wdate" style="width:120px;" name="endtime">
            </div>
            <div class="col-4 cl f-l">
                <label>会员账号：</label>
                <input name="login" value="${order.login }" class="input-text " style="width:120px;">
            </div>
            <div class="col-4 cl f-l">
                <button type="submit" class="btn btn-success radius" id="searchBtn"><i class="Hui-iconfont">&#xe665;</i>
                    查询
                </button>
            </div>
        </div>
        <div class="cl pd-5 bg-1 bk-gray mt-40">
            <div class="row cl">
                <div class="formControls col-xs-6">
                    <p>订单数量:<span id="span0" class="spanclear">${map.num }</span></p>
                    <p>保证金:<span id="span1" class="spanclear">${map.money }</span></p>
                    <p>利息:<span id="span2" class="spanclear">${map.rates }</span></p>
                    <p>点差:<span id="span3" class="spanclear">${map.spreadmoney }</span></p>
                    <p>手续费:<span id="span5" class="spanclear">${map.tax }</span></p>
                    <p>净利息:<span id="span4" class="spanclear">${map.rates-map.spreadmoney-map.tax }</span></p>
                    <p><span id="span6" style="color:red;" class="spanclear">备注:净利息=利息-点差-手续费</span></p>
                </div>
            </div>
        </div>
    </div>

    <!--/_footer 作为公共模版分离出去-->
</form>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="/static/h-ui.admin/js/H-ui.admin.js"></script>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
    function showLayer(title, url) {
        layer_show(title, url, 950, 500);
    }
</script>
</body>
</html>