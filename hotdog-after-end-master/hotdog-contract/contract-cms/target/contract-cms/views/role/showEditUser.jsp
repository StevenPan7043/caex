<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<title>${empty id ? "新增" : "编辑" }操作员</title>
</head>
<body>
	<article class="page-container">
		<form class="form form-horizontal" id="form-edit">
			<input type="hidden" name="id" value="${cliqueUser.id }">
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>角色：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<span class="select-box">
						<select class="select" name="roleid" id="roleid">
							<c:forEach items="${list }" var="l">
								<option <c:if test="${l.id == cliqueUser.roleid }">selected</c:if> value="${l.id }">${l.name }</option>
							</c:forEach>
						</select>
					</span>
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>账号：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${cliqueUser.login }" placeholder="请输入操作员账号" name="login">
				</div>
			</div>
			<c:if test="${empty id }">
				<div class="row cl">
					<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>密码：</label>
					<div class="formControls col-xs-8 col-sm-9">
						<input type="text" class="input-text" value="${cliqueUser.password }" placeholder="请输入操作员密码" name="password">
					</div>
				</div>
			</c:if>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3">名称：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${cliqueUser.name }" placeholder="请输入操作员名称" name="name">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>联系方式：</label>
				<div class="formControls col-xs-8 col-sm-9">
					<input type="text" class="input-text" value="${cliqueUser.phone }" placeholder="请输入操作员联系方式" name="phone">
				</div>
			</div>
			<div class="row cl">
				<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3 text-c">
					<input class="btn btn-primary radius" type="button" onclick="editRole()" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</article>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/static/h-ui/js/H-ui.min.js"></script>
<script type="text/javascript" src="/static/h-ui.admin/js/H-ui.admin.js"></script>
<!--/_footer 作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script>
<%@include file="../base.jsp" %>
<script type="text/javascript">
function editRole(){
 	$.ajax({
   		url:"/role/editUser",
   		data:$('#form-edit').serialize(),
   		dataType:"json",
   		type:"post",
   		traditional:true,
   		success:function(data){
   			if(data!=null){
   				$.Huimodalalert(data.desc, 1000);
	  				if(data.status) {
	   				window.setTimeout(function() {
	   					window.parent.location.reload();
	   				}, 1000);
		   		}
   			}
   		}
   	});
}
</script>
</body>
</html>