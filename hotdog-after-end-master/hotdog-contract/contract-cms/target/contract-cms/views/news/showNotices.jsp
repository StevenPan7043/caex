<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<link rel="Bookmark" href="/favicon.ico">
<link rel="Shortcut Icon" href="/favicon.ico" />
<!--[if lt IE 9]>
<script type="text/javascript" src="/lib/html5shiv.js"></script>
<script type="text/javascript" src="/lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/style.css" />
<style type="text/css">
	td, th { text-align: left!important }
</style>
<!--[if IE 6]>
<script type="text/javascript" src="/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>资讯管理</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 资讯管理 <span class="c-gray en">&gt;</span> 资讯列表 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<div class="page-container">
		<div class="cl pd-5 bg-1 bk-gray">
			<span class="l">
				<a class="btn btn-primary radius" href="/news/showNewEdit">
					<i class="Hui-iconfont">&#xe600;</i> 发布资讯
				</a>
			</span>
			<span class="r">共有数据：<strong>${fn:length(list) }</strong> 条</span>
		</div>
		<table class="table table-border table-bordered table-bg table-hover table-sort">
			<thead>
				<tr class="text-c">
					<th>封面图</th>
					<th>标题</th>
					<th>描述</th>
					<th>类型</th>
					<th>创建时间</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="l">
					<tr>
						<td class="text-c" width="20%"><img alt="" src="${l.imgurl }" width="100px"> </td>
						<td class="tc" width="25%">${l.title }</td>
						<td class="tc" width="25%">${l.descval }</td>
						<td class="tc" width="25%"><c:if test="${l.type==1 }">公告</c:if>
						<c:if test="${l.type==2 }">关于我们</c:if>
						 </td>
						<td class="text-c" width="20%"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td class="text-c" width="10%">
							<a class="btn btn-success radius size-S" href="/news/showNewEdit?id=${l.id }">编辑</a>
							<a class="btn btn-danger radius size-S" href="javaScript:;" onclick="deleteNew(${l.id},this)">删除</a>
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

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/lib/laypage/1.2/laypage.js"></script>
<%@include file="../base.jsp" %>
<script type="text/javascript">
	$('.table-sort').dataTable({
		"pageLength": 10, // 每页显示的数量
		"lengthChange": true, // 不允许改变每页长度
		"ordering": false, // 不允许排序
		"info": true, // 不允许显示左下角的信息
		"searching": true // 不允许显示搜索框
	});

function deleteNew(id, t) {
	$.ajax({
   		url:"/news/deleteNew",
   		data:{"id":id,"status":status},
   		dataType:"json",
   		type:"post",
   		success:function(data){
   			if(data!=null){
   				$.Huimodalalert(data.desc, 1000);
   				if(data.status) {
   					$(t).parent().parent().remove();
	   				/* window.setTimeout(function() {
	   					location.reload();
	   				}, 1000); */
   				}
   			}
   		}
   	});
}
function show_layer(title, url) {
	layer_show(title, url, 800, 500);
}
</script>
</body>
</html>