<%@page import="com.contract.service.cms.PlatSession"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css"
	href="/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css"
	href="/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css"
	href="/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css"
	href="/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css"
	href="/static/h-ui.admin/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>会员管理</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
		会员管理 <span class="c-gray en">&gt;</span> 会员列表 <a
			class="btn btn-success radius r"
			style="line-height: 1.6em; margin-top: 3px"
			href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/customer/showCustomerList" id="seachFrom">
		<div class="page-container">
		<c:if test="${roleid==1 }">
			<table >
				<thead>
					<tr class="text-c">
						<th colspan="6"><div class="Huialert Huialert-danger"><i class="Hui-iconfont">&#xe6a6;</i>平台钱包信息：待归集USDT数量(节点总和)：<span style="color: black">【<fmt:formatNumber value="${waitusd}" pattern="0.000000"/>】</span>
						总充值(USDT)：<span style="color: black">【<fmt:formatNumber value="${totalrecharge}" pattern="0.00"/>】</span>
						</div></th>
					</tr>
				</thead>
			</table>
			<table class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr>
						<th colspan="6">BTC节点/手续费地址信息</th>
					</tr>
					<tr class="text-c">
						<th>节点</th>
						<th>主钱包</th>
						<th>总额(USDT)</th>
						<th>待归集USDT</th>
						<th>手续费地址</th>
						<th>剩余BTC</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${btcList }" var="l">
						<tr>
							<td rowspan="${fn:length(l.list)+1 }" class="tc" style="width: 5%">${l.name }</td>
							<td rowspan="${fn:length(l.list)+1 }" class="tc" style="width: 5%"><a target="_blank" class=" btn-link"  href="https://tokenview.com/cn/search/${l.mainAddr }"> ${l.mainAddr }</a></td>
							<td rowspan="${fn:length(l.list)+1 }" class="tc" style="width: 5%">${l.totalmoney }</td>
							<td rowspan="${fn:length(l.list)+1 }" class="tc" style="width: 5%">${l.waitmoney }</td>
						</tr>
						<c:forEach items="${l.list }" var="d">
							<tr>
								<td class="tc" style="width: 5%"><a target="_blank" class=" btn-link"  href="https://btc.com/${d.addr }"> ${d.addr }</a></td>
								<td class="tc" style="width: 5%"><a target="_blank" class=" btn-link"  href="https://btc.com/${d.addr }"> ${d.money }</a></td>
							</tr>
						</c:forEach>
					</c:forEach>
				</tbody>
			</table>
			
			<table class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr>
						<th colspan="5">ETH地址信息</th>
					</tr>
					<tr class="text-c">
						<th>钱包地址</th>
						<th>ETH数量</th>
						<th>USDT数量</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${ethDList }" var="e">
						<tr>
							<td class="tc" style="width: 5%"><a target="_blank" class=" btn-link"  href="https://etherscan.io/address/${e.addr }"> ${e.addr }</a></td>
							<td class="tc" style="width: 5%">${e.money }</td>
							<td class="tc" style="width: 5%">${e.token }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
			</c:if>
		</div>

	

		<!--/_footer 作为公共模版分离出去-->
	</form>
	<!--_footer 作为公共模版分离出去-->
	<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
	<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
	<script type="text/javascript" src="/static/h-ui/js/H-ui.min.js"></script>
	<script type="text/javascript"
		src="/static/h-ui.admin/js/H-ui.admin.js"></script>
	<!--请在下方写此页面业务相关的脚本-->
	<script type="text/javascript"
		src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
	<script type="text/javascript"
		src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="/lib/laypage/1.2/laypage.js"></script>

	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/echarts.min.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts-gl/echarts-gl.min.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts-stat/ecStat.min.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/extension/dataTool.min.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/china.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/map/js/world.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/echarts/extension/bmap.min.js"></script>
	<script type="text/javascript"
		src="http://echarts.baidu.com/gallery/vendors/simplex.js"></script>
	<script type="text/javascript">
		
	</script>
</body>
</html>