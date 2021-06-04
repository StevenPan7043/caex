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
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 会员管理 <span class="c-gray en">&gt;</span> USDT充值列表 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/customer/showUsdtRechargeList" id="seachFrom">
		<div class="page-container">
			<div class="col-12"> 
				<div class="col-4 cl f-l">
					<label>日期范围：</label> 
					<input value="${rechargeLog.starttime }" type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin" class="input-text Wdate" style="width:120px;" name="starttime">
					-
					<input value="${rechargeLog.endtime }" type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax" class="input-text Wdate" style="width:120px;" name="endtime">
				</div>
				<div class="col-4 cl f-l">
					<label>充值账号：</label>
					<input name="login"  value="${rechargeLog.login }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>发送地址：</label>
					<input name="fromaddr"  value="${rechargeLog.fromaddr }" class="input-text "style="width:120px;">
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>子公司：</label>
					<select name="userid" class="input-text "style="width:120px;">
						<c:if test="${roleid==1 }"><option value="" >全部</option></c:if>
						<c:forEach items="${users }" var="u">
							<option value="${u.id }" <c:if test="${rechargeLog.userid==u.id}">selected="selected"</c:if>>${u.login}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-4 cl f-l">
					<label>接收地址：</label>
					<input name="toaddr"  value="${rechargeLog.toaddr }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>交易Hash：</label>
					<input name="hash"  value="${rechargeLog.hash }" class="input-text "style="width:120px;">
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>业务员：</label>
					<input name="salemanname"  value="${rechargeLog.salemanname }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>状态：</label>
					<select name="status" class="input-text "style="width:120px;">
							<option value="">请选择</option>
								<option value="-1" <c:if test="${rechargeLog.status==-1 }">selected="selected"</c:if>>未归集</option>
								<option value="0" <c:if test="${rechargeLog.status==0 }">selected="selected"</c:if>>已归集</option>
						</select>
					<button type="submit" class="btn btn-success radius" id="searchBtn"><i class="Hui-iconfont">&#xe665;</i> 查询</button>
				</div>
			</div>
			<div class="cl pd-5 bg-1 bk-gray mt-40">
				<span class="r">共有数据：<strong>${pageInfo.total}</strong> 条
				</span>
			</div>
			<table class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
						<th>账号</th>
						<th>推荐人</th>
						<th>业务员</th>
						<th>交易Hash</th>
						<th>发送方</th>
						<th>接收方</th>
						<th>金额</th>
						<th>状态</th>
						<th>时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="l">
						<tr >
							<td class="tc" style="width: 10%">${l.login }/真实名称:${l.realname }</td>
							<td class="tc" style="width: 10%">${l.parentphone }/真实名称:${l.parentname }</td>
							<td class="tc" style="width: 10%">${l.salemanname }</td>
							<td class="tc" style="width: 10%">${l.hash }</td>
							<td class="tc" style="width: 10%">${l.fromaddr }</td>
							<td class="tc" style="width: 10%">${l.toaddr }</td>
							<td class="tc" style="width: 10%"><fmt:formatNumber value="${l.valuecoin }" pattern="#0.000000"/> </td>
							<td class="tc" style="width: 10%">
								<c:if test="${l.status==-1 }">未归集</c:if>
								<c:if test="${l.status==0 }">已归集</c:if>
							</td>
							<td class="tc" style="width: 10%"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm:ss" />
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="13">
							统计：充值总笔数:【${map.num }】 / 充值总额:【${map.valuecoin }】
						</th>
					</tr>
				</tfoot>
			</table>
		</div>
		
<!--/_footer 作为公共模版分离出去-->
<%@include file="../paging.jsp"%>
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

</script>
</body>
</html>