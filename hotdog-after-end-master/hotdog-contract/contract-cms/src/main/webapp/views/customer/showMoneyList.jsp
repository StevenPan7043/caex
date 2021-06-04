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
		 <span class="c-gray en">&gt;</span>${details.type }账单<a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
			
			<a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" onclick="javascript:history.back(-1);" href="javascript:;" title="返回会员列表"><i
			class="Hui-iconfont">&#xe67d;</i></a>
	</nav>
	<form action="/customer/showMoneyList" id="seachFrom">
		<div class="page-container">
			<div class="col-12"> 
				<input type="hidden" name="cid" value="${details.cid }"/>
				<input type="hidden" name="type" value="${details.type }"/>
				<input type="hidden" name="flag" value="${flag }"/>
				<div class="col-4 cl f-l">
					<label>日期范围：</label> 
					<input value="${details.starttime }" type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin" class="input-text Wdate" style="width:120px;" name="starttime">
					-
					<input value="${details.endtime }" type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax" class="input-text Wdate" style="width:120px;" name="endtime">
				</div>
				<div class="col-4 cl f-l">
					<label>流水号：</label>
					<input name="paycode"  value="${details.paycode }" class="input-text "style="width:120px;">
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>收入|支出：</label>
					<select name="isout" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${details.isout==1 }">selected="selected"</c:if>>收入</option>
							<option value="0" <c:if test="${details.isout==0 }">selected="selected"</c:if>>支出</option>
						</select>
					<button type="submit" class="btn btn-success radius" id="searchBtn"><i class="Hui-iconfont">&#xe665;</i> 查询</button>
				</div>
			</div>
			<div class="cl pd-5 bg-1 bk-gray mt-40">
				<span class="r">共有数据：<strong>${pageInfo.total}</strong> 条
				</span>
			</div>
			<table
				class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
						<th>流水号</th>
						<th>操作类型</th>
						<th>收/支</th>
						<th>上一次金额</th>
						<th>本次操作金额</th>
						<th>最后金额</th>
						<th>备注</th>
						<th>时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="l">
						<tr>
							<td class="tc">${l.paycode }</td>
							<td class="tc">${l.typename }</td>
							<td class="tc">
								<c:if test="${l.isout==1 }"><span style="color: green">收入</span></c:if>
								<c:if test="${l.isout==0 }"><span style="color: red">支出</span></c:if>
							</td>
							<td class="tc">${l.original }</td>
							<td class="tc">
								<c:if test="${l.isout==1 }"><span style="color: green">+${l.cost }</span></c:if>
								<c:if test="${l.isout==0 }"><span style="color: red">-${l.cost }</span></c:if>
							</td>
							<td class="tc">${l.last }</td>
							<td class="tc">${l.remark }</td>
							<td class="tc"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
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