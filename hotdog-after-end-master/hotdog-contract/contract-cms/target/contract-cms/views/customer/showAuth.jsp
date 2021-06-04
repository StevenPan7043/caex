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
<link rel="Bookmark" href="/favicon.ico" >
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
<!--[if IE 6]>
<script type="text/javascript" src="/lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<!--/meta 作为公共模版分离出去-->

<title>充值/扣除</title>
</head>
<body>
<article class="page-container">
	<form method="post"  class="form form-horizontal" id="form-change-password">
		<div class="row cl">
			<label class="form-label col-xs-3"><span class="c-red">*</span>真实名称：</label>
			<div class="formControls col-xs-3">
				<input type="text" class="input-text radius size-M" readonly="readonly" value="${cCustomer.realname }">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-3"><span class="c-red">*</span>证件号：</label>
			<div class="formControls col-xs-3">
				<input type="text" class="input-text radius size-M" readonly="readonly" value="${cCustomer.idcard }">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-3"><span class="c-red">*</span>正面照：</label>
			<div class="formControls col-xs-3">
				<img alt="" src="${zmurl }" width="90px">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-3"><span class="c-red">*</span>反面照：</label>
			<div class="formControls col-xs-3">
				<img alt="" src="${fmurl }" width="90px">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-success radius" onclick="authCard(${cCustomer.id},3)" type="button" value="&nbsp;&nbsp;通过&nbsp;&nbsp;">
				
				<input class="btn btn-danger radius" onclick="authCard(${cCustomer.id},1)" type="button" value="&nbsp;&nbsp;驳回&nbsp;&nbsp;">
			</div>
		</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去-->
<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>  
<script type="text/javascript" src="/static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="/static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script> 
<%@include file="../base.jsp" %>
<script type="text/javascript">
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
						parent.location.reload();
					}, 1000);
				}
	   		}
	   	});
}
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>