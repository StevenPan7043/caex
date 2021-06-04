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
<title>合约交易</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 合约交易 <span class="c-gray en">&gt;</span> 合约订单列表 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/order/showOrderList" id="seachFrom">
		<div class="page-container">
			<div class="col-12">
				<div class="col-4 cl f-l">
					<label>日期范围：</label>
					<input value="${order.starttime }" type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin" class="input-text Wdate" style="width:120px;" name="starttime">
					-
					<input value="${order.endtime }" type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax" class="input-text Wdate" style="width:120px;" name="endtime">
				</div>
				<div class="col-4 cl f-l">
					<label>会员账号：</label>
					<input name="login"  value="${order.login }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>订单编号：</label>
					<input name="ordercode"  value="${order.ordercode }" class="input-text "style="width:120px;">
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>子公司：</label>
					<select name="userid" class="input-text "style="width:120px;">
						<c:if test="${roleid==1 }"><option value="" >全部</option></c:if>
						<c:forEach items="${users }" var="u">
							<option value="${u.id }" <c:if test="${order.userid==u.id}">selected="selected"</c:if>>${u.login}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-4 cl f-l">
					<label>订单状态：</label>
					<select name="status" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${order.status==1}">selected="selected"</c:if>>持仓中</option>
							<option value="2" <c:if test="${order.status==2}">selected="selected"</c:if>>已完成</option>
					</select>
				</div>
				<div class="col-4 cl f-l">
					<label>交易货币：</label>
					<select name="coin" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<c:forEach items="${coins }" var="c">
								<option value="${c.symbol }" <c:if test="${order.coin==c.symbol}">selected="selected"</c:if>>${c.symbol}</option>
							</c:forEach>
					</select>
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>会员身份：</label>
					<select name="identity" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${order.identity==1}">selected="selected"</c:if>>会员</option>
							<option value="2" <c:if test="${order.identity==2}">selected="selected"</c:if>>业务员</option>
							<option value="3" <c:if test="${order.identity==3}">selected="selected"</c:if>>模拟账号</option>
					</select>
				</div>
				<div class="col-4 cl f-l">
					<label>邀请人：</label>
					<input name="parentname"  value="${order.parentname }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>订单类型：</label>
					<select name="type" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${order.type==1}">selected="selected"</c:if>>开多</option>
							<option value="2" <c:if test="${order.type==2}">selected="selected"</c:if>>开空</option>
					</select>
					<button type="submit" class="btn btn-success radius" id="searchBtn"><i class="Hui-iconfont">&#xe665;</i> 查询</button>
					<button type="button" onclick="method1()" class="btn btn-success radius" id="export"><i class="Hui-iconfont">&#xe665;</i>导出报表</button>
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
						<th>订单编号</th>
						<th>保证金</th>
						<th>持仓量</th>
						<th>杠杆倍数</th>
						<th>币价</th>
						<th>利息</th>
						<th>点差金额</th>
						<th>手续费</th>
						<th>止盈/止损</th>
						<th>描述</th>
						<th>货币类型</th>
						<th>订单类型</th>
						<th>订单状态</th>
						<th>是否结算</th>
						<th>子公司</th>
						<th>持仓时间</th>
						<th>平仓时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="l">
						<tr >
							<td class="tc">账号:${l.login }/实名:${l.realname }</td>
							<td class="tc">${l.ordercode }</td>
							<td class="tc">${l.realmoney }</td>
							<td class="tc">${l.coinnum }</td>
							<td class="tc">${l.gearing }</td>
							<td class="tc">
								<p>持仓价:${l.buyprice }</p>
								<p>平仓价:${l.stopprice }</p>
							</td>
							<td class="tc">${l.rates }</td>
							<td class="tc">${l.spreadmoney }</td>
							<td class="tc">${l.tax }</td>
							<td class="tc">
								<p>止盈:${l.stopwin }</p>
								<p>止损:${l.stopdonat }</p>
							</td>
							<td class="tc">${l.remark }</td>
							<td class="tc">${l.coin }</td>
							<td class="tc">
								<c:if test="${l.type==1 }">开多</c:if>
								<c:if test="${l.type==2 }">开空</c:if>
							</td>
							<td class="tc">
								<c:if test="${l.status==1 }">持仓中</c:if>
								<c:if test="${l.status==2 }">已完成</c:if>
							</td>
							<td class="tc">
								<c:if test="${l.settleflag==-1 }">未结算</c:if>
								<c:if test="${l.settleflag==0 }">已结算</c:if>
							</td>
							<td class="tc">${l.username }</td>
							<td class="tc"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm" /></td>
							<td class="tc"><fmt:formatDate value="${l.stoptime }" pattern="yyyy-MM-dd HH:mm" /></td>
							<td  style="width: 8%">
								<c:if test="${l.status==1 and roleid==1}"><a class="btn btn-link radius size-S" onclick="handleCloseOrder('${l.ordercode}')" href="javaScript:;">自动平仓</a></c:if>
							</td>
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