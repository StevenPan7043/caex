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
<title>会员管理</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 会员管理 <span class="c-gray en">&gt;</span> 会员列表 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/customer/showCustomerList" id="seachFrom">
		<div class="page-container">
			<div class="col-12"> 
				<div class="col-4 cl f-l">
					<label>日期范围：</label> 
					<input value="${customer.starttime }" type="text" onfocus="WdatePicker({ maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}' })" id="datemin" class="input-text Wdate" style="width:120px;" name="starttime">
					-
					<input value="${customer.endtime }" type="text" onfocus="WdatePicker({ minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d' })" id="datemax" class="input-text Wdate" style="width:120px;" name="endtime">
				</div>
				<div class="col-4 cl f-l">
					<label>联系电话：</label>
					<input name="phone"  value="${customer.phone }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>邀请编码：</label>
					<input name="invitationcode"  value="${customer.invitationcode }" class="input-text "style="width:120px;">
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>子公司：</label>
					<select name="userid" class="input-text "style="width:120px;">
						<c:if test="${roleid==1 }"><option value="" >全部</option></c:if>
						<c:forEach items="${users }" var="u">
							<option value="${u.id }" <c:if test="${customer.userid==u.id}">selected="selected"</c:if>>${u.login}</option>
						</c:forEach>
					</select>
				</div>
				<div class="col-4 cl f-l">
					<label>业务员：</label>
					<input name="salemanname"  value="${customer.salemanname }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>会员身份：</label>
					<select name="identity" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${customer.identity==1}">selected="selected"</c:if>>会员</option>
							<option value="2" <c:if test="${customer.identity==2}">selected="selected"</c:if>>业务员</option>
							<option value="3" <c:if test="${customer.identity==3}">selected="selected"</c:if>>模拟账号</option>
					</select>
				</div>
				<div class="pd-20"></div>
				<div class="col-4 cl f-l">
					<label>邀请人：</label>
					<input name="parentname"  value="${customer.parentname }" class="input-text "style="width:120px;">
				</div>
				<div class="col-4 cl f-l">
					<label>实名状态：</label>
					<select name="authflag" class="input-text "style="width:120px;">
							<option value="">请选择</option>
							<option value="1" <c:if test="${customer.authflag==1}">selected="selected"</c:if>>未认证</option>
							<option value="2" <c:if test="${customer.authflag==2}">selected="selected"</c:if>>待审核</option>
							<option value="3" <c:if test="${customer.authflag==3}">selected="selected"</c:if>>审核通过</option>
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
						<th>邀请码</th>
						<th>实名</th>
						<th>实名审核</th>
						<th>钱包</th>
						<th>直推邀请人</th>
						<th>业务员</th>
						<th>子公司</th>
						<th>身份</th>
						<th>提现权限</th>
						<th>是/否有效</th>
						<th>时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${pageInfo.list }" var="l">
						<tr >
							<td class="tc" style="width: 10%">${l.phone }</td>
							<td class="tc" style="width: 10%">${l.invitationcode }</td>
							<td class="tc" style="width: 10%">${l.realname }</td>
							<td class="tc" style="width: 5%">
								<c:if test="${l.authflag==2 }">
										<a class="btn btn-link radius size-S"  onclick="showLayer('实名审核','/customer/showAuth?cid=${l.id}')" href="javaScript:;">审核</a>
								</c:if>
								<c:if test="${l.authflag==1 }">未认证</c:if>
								<c:if test="${l.authflag==3 }">已认证</c:if>
							</td>
							<td class="tc" style="width: 10%">
								<p>USDT:<a class="size-S" style="color: blue;" href="/customer/showMoneyList?cid=${l.id}&type=USDT&flag=1"><fmt:formatNumber value="${l.balance }" pattern="#0.000000"/> </a></p>
								<p>逐仓:<a class="size-S" style="color: blue;" href="/customer/showMoneyList?cid=${l.id}&type=USDT&flag=2"><fmt:formatNumber value="${l.zcbalance }" pattern="#0.000000"/></a></p>
								<p>周期资产:<a class="size-S" style="color: blue;" href="/customer/showMoneyList?cid=${l.id}&type=USDT&flag=2"><fmt:formatNumber value="${l.gdbalance }" pattern="#0.000000"/></a></p>
							</td>
                            <td class="tc" style="width: 10%">${l.parentname }</td>
							<td class="tc" style="width: 10%">${l.salemanname }</td>
							<td class="tc" style="width: 10%">${l.username }</td>
							<td class="tc" style="width: 10%">
								<c:if test="${l.identity==1 }">会员</c:if>
								<c:if test="${l.identity==2 }">业务员</c:if>
								<c:if test="${l.identity==3 }">模拟账号</c:if>
							</td>
							<td class="tc" style="width: 5%">
								<div class="cancash switch size-S" data-id="${l.id }" data-on-label="<i class='icon-ok icon-white'>允许</i>" data-off-label="<i class='icon-remove'>禁止</i>">
									<input type="checkbox" <c:if test="${l.cancash==0 }">checked="checked"</c:if> />
								</div>
							</td>
							<td class="tc" style="width: 5%">
								<div class="statusflag switch size-S" data-id="${l.id }" data-on-label="<i class='icon-ok icon-white'>是</i>" data-off-label="<i class='icon-remove'>否</i>">
									<input type="checkbox" <c:if test="${l.status==0 }">checked="checked"</c:if> />
								</div>
							</td>
							<td class="tc" style="width: 10%"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm" />
							</td>
							<td  style="width: 8%">
								<span class="dropDown dropDown_hover"><a class="dropDown_A btn  radius size-S" href="#"><i class="Hui-iconfont">&#xe6d5;</i>更多操作</a>
									<ul class="dropDown-menu menu radius box-shadow">
										<li><a class="btn btn-link radius size-S" href="/customer/showWallet?cid=${l.id }">钱包信息</a></li>
										<li><a class="btn btn-link radius size-S" onclick="showLayer('编辑资料','/customer/showEditcus?cid=${l.id }')" href="javaScript:;">编辑资料</a></li>
										<c:if test="${roleid==1 }">
                                            <li><a class="btn btn-link radius size-S"
                                                   onclick="showEditCustomer('邀请人设置','/customer/showEditCustomer?id=${l.id }')"
                                                   href="javaScript:;">邀请人设置</a></li>
                                        </c:if>
										<li><a class="btn btn-link radius size-S" onclick="resetPwd(${l.id},1)" href="javaScript:;">重置密码</a></li>
										<li><a class="btn btn-link radius size-S" onclick="resetPwd(${l.id},2)" href="javaScript:;">重置交易密码</a></li>
									</ul>
								</span>
							</td>
						</tr>
					</c:forEach>
				</tbody>
				<tfoot>
					<tr>
						<th colspan="13">总余额:${totalmoney }</th>
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
$('.authflag').on('switch-change', function (e, data) {
    var $el = $(data.el), value = data.value;
    var id=$(this).attr("data-id");
    var authflag=1;
    if(!value){
    	  authflag=3;
    }
    $.ajax({
   		url:"/customer/updateCusAuthflag",
   		dataType:"json",
   		data:{"id":id,"authflag":authflag},
   		type:"post",
   		success:function(data){
   			$.Huimodalalert(data.desc, 3000);
				if(data.status) {
					window.setTimeout(function() {
					location.reload();
				}, 1000);
			}
   		}
   	});
});
function showEditCustomer(title, url) {
    layer_show(title, url, 500, 300);
}
function authCard(id,authflag){
	 $.ajax({
	   		url:"/customer/updateCusAuthflag",
	   		dataType:"json",
	   		data:{"id":id,"authflag":authflag},
	   		type:"post",
	   		success:function(data){
	   			$.Huimodalalert(data.desc, 3000);
					if(data.status) {
						window.setTimeout(function() {
						location.reload();
					}, 1000);
				}
	   		}
	   	});
}
$('.cancash').on('switch-change', function (e, data) {
    var $el = $(data.el), value = data.value;
    var id=$(this).attr("data-id");
    var status=0;
    if(!value){
    		status=-1;
    }
    $.ajax({
   		url:"/customer/updateCusStatus",
   		dataType:"json",
   		data:{"id":id,"cancash":status},
   		type:"post",
   		success:function(data){
   			$.Huimodalalert(data.desc, 1000);
   		}
   	});
});
$('.statusflag').on('switch-change', function (e, data) {
    var $el = $(data.el), value = data.value;
    var id=$(this).attr("data-id");
    var status=0;
    if(!value){
    		status=-1;
    }
    $.ajax({
   		url:"/customer/updateCusStatus",
   		dataType:"json",
   		data:{"id":id,"status":status},
   		type:"post",
   		success:function(data){
   			$.Huimodalalert(data.desc, 1000);
   		}
   	});
});
function resetPwd(id,type){
	$.ajax({
   		url:"/customer/resetPwd",
   		dataType:"json",
   		data:{"id":id,"type":type},
   		type:"post",
   		success:function(data){
   			if(data!=null){
   				$.Huimodalalert(data.desc, 3000);
   			}
   		}
   	});
}
function editSys(id){
	$.ajax({
   		url:"/customer/editSys",
   		dataType:"json",
   		data:{"id":id},
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
   		}
   	});
}
function showLayer(title, url) {
	layer_show(title, url, 950, 500);
}
</script>
</body>
</html>