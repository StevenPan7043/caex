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
<title>USDT提币管理</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 提币管理 <span class="c-gray en">&gt;</span> USDT提币列表 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/cash/showCashUsdtlist" id="seachFrom">
		<div class="page-container">
		<div class="Huialert Huialert-danger"><i class="Hui-iconfont">&#xe6a6;</i>注意：当提币为重审状态时，需要到官网核查转账记录确认后在审核，以免发生重复审核核查地址：https://etherscan.io/tx/HASH单号,如果账单没有问题则标记正常即可，账单核对为每60分钟一次</div>
			<div class="col-12"> 
				<div class="col-4 cl f-l">
					<label>日期范围：</label> 
					<input value="${logs.starttime }" type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin" class="input-text Wdate" style="width:120px;" name="starttime">
					-
					<input value="${logs.endtime }" type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax" class="input-text Wdate" style="width:120px;" name="endtime">
				</div>
				<div class="col-4 cl f-l">
					<label>会员账号：</label>
					<input name="login"  value="${logs.login }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>HASH单号：</label>
					<input name="hashcode"  value="${logs.hashcode }" class="input-text "style="width:120px;">
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>子公司：</label>
					<select name="userid" class="input-text "style="width:120px;">
						<c:if test="${roleid==1 }"><option value="" >全部</option></c:if>
						<c:forEach items="${users }" var="u">
							<option value="${u.id }" <c:if test="${logs.userid==u.id}">selected="selected"</c:if>>${u.login}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-4 cl f-l">
					<label>接收地址：</label>
					<input name="receiveaddr"  value="${logs.toaddr }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>提币状态：</label>
					<select name="status"  class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${logs.status==1 }">selected="selected"</c:if>>待审核</option>
							<option value="2" <c:if test="${logs.status==2 }">selected="selected"</c:if>>已审核</option>
							<option value="3" <c:if test="${logs.status==3 }">selected="selected"</c:if>>已驳回</option>
					</select>
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>业务员：</label>
					<input name="salemanname"  value="${logs.salemanname }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>对账状态：</label>
					<select name="dzstatus"  class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="-1" <c:if test="${logs.dzstatus==-1 }">selected="selected"</c:if>>未对账</option>
							<option value="0" <c:if test="${logs.dzstatus==0 }">selected="selected"</c:if>>对账成功</option>
							<option value="1" <c:if test="${logs.dzstatus==1 }">selected="selected"</c:if>>重审中</option>
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
						<th>提供方</th>
						<th>提币数量</th>
						<th>手续费</th>
						<th>实际到账</th>
						<th>接收者</th>
						<th>Hash单号</th>
						<th>状态</th>
						<th>对账状态</th>
						<th>子公司</th>
						<th>申请时间</th>
						<th>审核信息</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="l">
						<tr>
							<td class="tc" style="width: 5%">${l.login }/姓名:${l.realname }</td>
							<td class="tc" style="width: 5%">${l.parentphone }/姓名:${l.parentname }</td>
							<td class="tc" style="width: 5%">${l.salemanname }</td>
							<td class="tc" style="width: 5%">${l.fromaddr }</td>
							<td class="tc" style="width: 5%">${l.money }</td>
							<td class="tc" style="width: 5%">${l.tax }</td>
							<td class="tc" style="width: 5%">${l.realmoney }</td>
							<td class="tc" style="width: 5%"><a target="_blank" class=" btn-link" href="https://tokenview.com/cn/search/${l.toaddr }">${l.toaddr }</a></td>
							<td class="tc" style="width: 5%"><a target="_blank" class=" btn-link" href="https://usdt.tokenview.com/cn/pending/${l.hashcode }">${l.hashcode }</a></td>
							<td class="tc" style="width: 5%">
								<c:if test="${l.status==1 }">待审核</c:if>
								<c:if test="${l.status==2 }">已通过</c:if>
								<c:if test="${l.status==3 }">已驳回</c:if>
							</td>
							<td class="tc" style="width: 5%">
								<c:if test="${l.dzstatus==-1 }">未对账</c:if>
								<c:if test="${l.dzstatus==0 }">对账成功</c:if>
								<c:if test="${l.dzstatus==1 }">重审中</c:if>
							</td>
							<td class="tc" style="width: 5%">${l.username }</td>
							<td class="tc" style="width: 5%"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm" />
							</td>
							<td class="tc" style="width: 5%">
								<p><fmt:formatDate value="${l.checktime }" pattern="yyyy-MM-dd HH:mm" />
								</p>
								<p>${l.checker }</p>
							</td>
							<td  style="width: 5%">
								<c:if test="${l.status==1 }">
									<a class="btn btn-success radius size-S" onclick="showlayer('USDT提币审核','/cash/showUsdtCheck?id=${l.id }')" href="javaScript:;">通过</a>
									<a class="btn btn-warning radius size-S" onclick="refundEthCash(${l.id})" href="javaScript:;">驳回</a>
								</c:if>
								<c:if test="${l.status==2 && l.dzstatus==1 }">
									<a class="btn btn-danger radius size-S" onclick="showlayer('USDT提币重审','/cash/showUsdtCheck?id=${l.id }')" href="javaScript:;">重审</a>
									<a class="btn btn-success radius size-S" onclick="dzEthSuccess(${l.id})" href="javaScript:;">标记正常</a>
								</c:if>
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="15">
							统计：提现总笔数:【${map.num }】 / 提现总额:【${map.money }】 / 提现总手续费:【${map.tax }】 / 提现实际到账总额:【${map.realmoney }】
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
function refundEthCash(id){
	$.ajax({
   		url:"/cash/handlerefundUsdtCash",
   		dataType:"json",
   		data:{"id":id},
   		type:"post",
   		success:function(data){
   			$.Huimodalalert(data.desc, 3000);
   			if(data!=null){
   				window.setTimeout(function() {
   					location.reload();
   				}, 1000);
   			}
   		}
   	});
}
function dzEthSuccess(id){
	$.ajax({
   		url:"/cash/dzUsdtSuccess",
   		dataType:"json",
   		data:{"id":id},
   		type:"post",
   		success:function(data){
   			$.Huimodalalert(data.desc, 3000);
   			if(data!=null){
   				window.setTimeout(function() {
   					location.reload();
   				}, 1000);
   			}
   		}
   	});
}
function showlayer(title, url) {
	layer_show(title, url, 950, 500);
}
</script>
</body>
</html>