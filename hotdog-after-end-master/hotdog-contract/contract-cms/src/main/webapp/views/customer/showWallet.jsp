<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>会员管理</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 会员管理 <span class="c-gray en">&gt;</span> 会员列表
		 <span class="c-gray en">&gt;</span>会员钱包<a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
			
			<a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" onclick="javascript:history.back(-1);" href="javascript:;" title="返回会员列表"><i
			class="Hui-iconfont">&#xe67d;</i></a>
	</nav>
	<form action="/customer/showWallet" id="seachFrom">
		<div class="page-container">
			<div class="col-12"> 
				<input type="hidden" name="cid" value="${cid }"/>
			</div>
			<table
				class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
						<th>OMNI地址</th>
						<th>ECR20地址</th>
						<th>USDT余额</th>
						<th>逐仓余额</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${wallets}" var="l">
						<tr>
							<td class="tc">${l.addr }</td>
							<td class="tc">${l.label }</td>
							<td class="tc">${l.balance }</td>
							<td class="tc">${l.zcbalance }</td>
							<td class="tc">
								<a class="btn btn-secondary radius size-S" href="/customer/showMoneyList?cid=${cid}&type=${l.type }&flag=1">USDT账单</a>
								<a class="btn btn-secondary radius size-S" href="/customer/showMoneyList?cid=${cid}&type=${l.type }&flag=2">逐仓账单</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="/static/h-ui.admin/js/H-ui.admin.js"></script>
<!--/_footer 作为公共模版分离出去-->
<%@include file="../paging.jsp"%>
</form>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/lib/laypage/1.2/laypage.js"></script>
</body>
</html>