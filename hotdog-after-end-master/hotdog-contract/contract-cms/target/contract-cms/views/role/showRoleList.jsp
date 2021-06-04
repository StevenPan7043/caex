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
<title>权限管理</title>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 权限管理 <span class="c-gray en">&gt;</span> 角色权限 <a
			class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<form action="/role/showRoleList" id="seachFrom">
		<div class="page-container">
			<div class="cl pd-5 bg-1 bk-gray">
				<span class="l">
					<a class="btn btn-primary radius" href="javascript:;" onclick="showEditRole('新增角色','/role/showEditRole')">
						<i class="Hui-iconfont">&#xe600;</i> 新增角色
					</a>
				</span>
			</div>
			<table
				class="table table-border table-bordered table-bg table-hover table-sort">
				<thead>
					<tr class="text-c">
						<th>名称</th>
						<th>状态</th>
						<th>覆盖公司</th>
						<th>创建时间</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list }" var="l">
						<tr>
							<td class="text-c">${l.name }</td>
							<td class="text-c">
								<div class="teamflag switch size-S" data-id="${l.id }" data-on-label="<i class='icon-ok icon-white'>有效</i>" data-off-label="<i class='icon-remove'>无效</i>">
									<input type="checkbox" <c:if test="${l.status==0 }">checked="checked"</c:if> 
									<c:if test="${l.id==1 }">disabled="disabled"</c:if> 
									 />
								</div>
							</td>
							<td class="text-c">
								${l.userList }
							</td>
							<td class="text-c"><fmt:formatDate value="${l.createtime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
							<td class="text-c">
								<%-- <c:if test="${l.id!=1 }"> --%>
									<a class="btn btn-primary radius size-S" href="javaScript:;" onclick="showEditRole('编辑角色','/role/showEditRole?id=${l.id}')"><i class="Hui-iconfont">&#xe6df;</i></a>
								<%-- </c:if> --%>
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
</form>
<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/datatables/1.10.0/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="/lib/laypage/1.2/laypage.js"></script>
<script type="text/javascript">
function showEditRole(title, url) {
	layer_show(title, url, 950, 500);
}
$('.teamflag').on('switch-change', function (e, data) {
    var $el = $(data.el), value = data.value;
    var id=$(this).attr("data-id");
    var status=0;
    if(!value){
    	status=-1;
    }
    $.ajax({
   		url:"/role/updateRolestatus",
   		dataType:"json",
   		data:{"id":id,"status":status},
   		type:"post",
   		success:function(data){
   			$.Huimodalalert(data.desc, 1000);
   		}
   	});
});

</script>
</body>
</html>