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
<title>${empty id ? "新增" : "编辑" }角色</title>
</head>
<body>
	<article class="page-container">
		<form class="form form-horizontal" id="form-edit">
			<input type="hidden" name="id" value="${cliqueRoles.id }">
			<%-- <div class="row cl">
				<label class="form-label col-xs-2"><span class="c-red">*</span>角色类型：</label>
				<div class="formControls col-xs-10">
					<span class="select-box">
						<select class="select" name="type" id="type" onchange="changeLogin()">
							<c:forEach items="${enums }" var="l">
								<option <c:if test="${logintype!=l.id }">style="display:none " </c:if> <c:if test="${l.id == cliqueRoles.type || l.id==logintype }">selected</c:if> value="${l.id }">${l.name }</option>
							</c:forEach>
						</select>
					</span>
				</div>
			</div> --%>
			<div class="row cl">
				<label class="form-label col-xs-2"><span class="c-red">*</span>角色名称：</label>
				<div class="formControls col-xs-10">
					<input type="text" class="input-text" value="${cliqueRoles.name }" placeholder="角色名称" name="name">
				</div>
			</div>
			<div class="row cl">
				<label class="form-label col-xs-2"><span class="c-red">*</span>菜单：</label>
				<div class="formControls col-xs-10 skin-minimal">
					<c:forEach items="${list }" var="l">
						<c:if test="${l.parentid==0 }">
							<div class="formControls col-xs-12">
								<div class="formControls col-xs-12">
									<div class="check-box main" data-id="${l.id }">
									    <input  class="main${l.id }" data-id="${l.id }" type="checkbox" ${l.checked } id="checkbox-${l.id }">
									    <label for="checkbox-${l.id }" class="label label-primary radius ">${l.name }</label>
									    <c:if test="${l.id==4 || l.id==10 }">
									    		<span class="label label-danger radius">此功能仅对集团开放</span>
									    </c:if>
									    <c:if test="${l.id==30 }">
									    		<span class="label label-danger radius">此功能仅集团可分配使用权</span>
									    </c:if>
									</div>
								</div>
								<c:forEach items="${list }" var="p">
									<c:if test="${p.parentid==l.id }">
										<div class="formControls col-xs-4">
											<div class="check-box child">
											    <input  data-parentid="${l.id }" data-id="${p.id }" name="child" class="child${l.id }" type="checkbox" ${p.checked } id="checkbox-${p.id }">
											    <label for="checkbox-${p.id }">${p.name }
												    	<c:if test="${p.type==2 }"><span class="label label-danger radius">按钮</span></c:if>
												    	<c:if test="${p.type==1 }"><span class="label label-success radius">页面</span></c:if>
											    </label>
											</div>
										</div>
									</c:if>
								</c:forEach>
							</div>
							<div class="mt-10">&nbsp;</div>
						</c:if>
					</c:forEach>
				</div>
			</div>
			<input type="hidden" id="menuids" name="menuids">
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
changeLogin();
function changeLogin(){
	var type=$("#type").val();
	var logintype="${logintype}";
	if(logintype!=1){
		$(".main30").attr("disabled","disabled");
		$(".child30").attr("disabled","disabled");
	}else{
		$(".main30").removeAttr("disabled","disabled");
		$(".child30").removeAttr("disabled","disabled");
	}
	if(type!=1){
		$(".main4").attr("disabled","disabled");
		$(".child4").attr("disabled","disabled");
		
		$(".main10").attr("disabled","disabled");
		$(".child10").attr("disabled","disabled");
	}else{
		$(".main4").removeAttr("disabled","disabled");
		$(".child4").removeAttr("disabled","disabled");
		
		$(".main10").removeAttr("disabled","disabled");
		$(".child10").removeAttr("disabled","disabled");
	}
}
$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	})});
function editRole(){
	var checkBoxArr = [];
	$('input[name="child"]:checked').each(function() {
		var id=$(this).attr("data-id");
		var parentid=$(this).attr("data-parentid");
		checkBoxArr.push(id);
		checkBoxArr.push(parentid);
	});
	$("#menuids").val(checkBoxArr);
 	$.ajax({
   		url:"/role/editRole",
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
$(".main").on('ifChecked', function(event){
	var id=$(this).attr("data-id");
	$(".child"+id).iCheck('check');
})
$(".main").on('ifUnchecked', function(event){
	var id=$(this).attr("data-id");
	$(".child"+id).iCheck('uncheck');
});
</script>
</body>
</html>