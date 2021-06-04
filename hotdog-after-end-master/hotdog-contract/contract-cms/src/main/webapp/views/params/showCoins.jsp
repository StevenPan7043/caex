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
<script type="text/javascript" src="/lib/html5shiv.js"></script>
<script type="text/javascript" src="/lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/style.css" />
<!--[if IE 6]>
<script type="text/javascript" src="/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<title>参数配置</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 参数管理 <span class="c-gray en">&gt;</span> 货币管理 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<div class="page-container">
		<table class="table table-border table-bordered table-hover table-bg table-sort">
			<thead>
				<tr>
					<th scope="col" colspan="12">配置参数</th>
				</tr>
				<tr class="text-c">
					<th>货币名称</th>
					<th>市场名称</th>
					<th>交易对</th>
					<th>中币合并深度</th>
					<th>日开盘价</th>
					<th>最后更新时间</th>
					<th>点差范围</th>
					<th>是否逐仓</th>
					<th>平仓系数(USDT)</th>
					<th>逐仓奖励倍数</th>
					<th>逐仓手续费</th>
					<th>操作</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${list }" var="l">
					<tr class="text-c">
						<td>${l.coin }</td>
						<td>${l.name }</td>
						<td>${l.symbol }</td>
						<td>${l.zbdepth }</td>
						<td>${l.openval }</td>
						<td><fmt:formatDate value="${l.updatetime }" pattern="yyyy-MM-dd HH:mm"/> </td>
						<td>${l.beginspread }~${l.stopspread }</td>
						<td><c:if test="${l.type==0 }">是</c:if><c:if test="${l.type==-1 }">否</c:if> </td>
						<td>${l.zcstopscale }</td>
						<td>${l.zcscale }</td>
						<td>${l.zctax }</td> 
						<td class="f-14">
							<a title="编辑" href="javascript:;" onclick="showEditSysPara('编辑配置','/params/showEditCoin?coin=${l.coin }')" style="text-decoration: none">
								<i class="Hui-iconfont">&#xe6df;</i>
							</a>
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
<script type="text/javascript">
function showEditSysPara(title, url) {
	layer_show(title, url, 800, 500);
}
</script>
</body>
</html>