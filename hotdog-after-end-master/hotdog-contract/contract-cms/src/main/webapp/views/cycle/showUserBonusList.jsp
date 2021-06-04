<%@page import="com.contract.service.cms.PlatSession"%>
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
<title>周期交易</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 周期交易 <span class="c-gray en">&gt;</span> 用户收益 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/cycle/showUserBonusList" id="seachFrom">
		<div class="page-container">
			<div class="col-12">
				<div class="col-4 cl f-l">
					<label>会员id：</label>
					<input name="memberId" id="memberId" value="${memberId}" class="input-text "style="width:120px;">
					<button type="submit" class="btn btn-success radius" id="searchBtn"><i class="Hui-iconfont">&#xe665;</i> 查询</button>
				</div>
<%--				<div class="col-4 cl f-l">--%>
<%--					<button type="submit" class="btn btn-success radius" id="searchBtn"><i class="Hui-iconfont">&#xe665;</i> 查询</button>--%>
<%--					<button type="button" onclick="method1()" class="btn btn-success radius" id="export"><i class="Hui-iconfont">&#xe665;</i>导出报表</button>--%>
<%--				</div>--%>
			</div>
			<div class="cl pd-5 bg-1 bk-gray mt-40">
				<span class="r">共有数据：<strong>${pageInfo.total}</strong> 条
				</span>
			</div>
			<table class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
						<th>订单id</th>
						<th>跟单id</th>
						<th>会员id</th>
						<th>产出币种</th>
						<th>分红数量</th>
						<th>分红日期</th>
						<th>创建时间</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="l">
						<tr >
							<td class="tc">${l.recordId}</td>
							<td class="tc">${l.projectId}</td>
							<td class="tc">${l.memberId}</td>
							<td class="tc">${l.outputCurrency}</td>
							<td class="tc">${l.bonusNum}</td>
							<td class="tc">${l.bonusDate}</td>
							<td class="tc"><fmt:formatDate value="${l.createTime}" pattern="yyyy-MM-dd HH:mm" /></td>
						</tr>
					</c:forEach>
				</tbody>
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
function showLayer(title, url) {
	layer_show(title, url, 950, 500);
}
	function method1(){
		var starttime = $('input[name="starttime"]').val();
		var endtime = $('input[name="endtime"]').val();
		var login = $('input[name="login"]').val();
		var ordercode = $('input[name="ordercode"]').val();
		var userid = $('select[name="userid"]').val();
		var status = $('select[name="status"]').val();
		var coin = $('select[name="coin"]').val();
		var identity = $('select[name="identity"]').val();
		var parentname = $('input[name="parentname"]').val();
		var username = $('input[name="username"]').val();
		var type = $('select[name="type"]').val();
		var params = "starttime="+starttime
				+ "&endtime="+endtime
				+ "&login="+login
				+ "&ordercode="+ordercode
				+ "&userid="+userid
				+ "&status="+status
				+ "&coin="+coin
				+ "&identity="+identity
				+ "&parentname="+parentname
				+ "&username="+username
				+ "&type="+type;
		window.location.href = "/contract/export?"+params;
	}

function handleCloseOrder(ordercode){
	$.ajax({
		url:"/order/handleCloseOrder",
		dataType:"json",
		data:{"ordercode":ordercode},
		type:"post",
		success:function(data){
			if(data!=null){
				$.Huimodalalert(data.desc, 3000);
				if(data.status) {
					window.setTimeout(function() {
						location.reload();
					}, 1000);
				}
			}
		},
		error:function(data){
			if(data!=null){
				$.Huimodalalert(data.desc, 3000);
				window.setTimeout(function() {
					location.reload();
				}, 1000);
			}
		}
	});
}
</script>
</body>
</html>