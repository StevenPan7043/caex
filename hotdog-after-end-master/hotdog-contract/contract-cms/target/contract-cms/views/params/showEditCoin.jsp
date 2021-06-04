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
<title>编辑配置</title>
</head>
<body>
<article class="page-container">
	<form class="form form-horizontal" id="form-edit">
		<input type="hidden" name="coin" value="${coins.coin }">
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>点差/开始：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${coins.beginspread }" name="beginspread">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>点差/结束：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${coins.stopspread }" name="stopspread">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>是否逐仓：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<select class="input-text" name="type">
						<option value="0" <c:if test="${coins.type==0 }">selected="selected" </c:if>>参与逐仓</option>
						<option value="-1" <c:if test="${coins.type==-1 }">selected="selected" </c:if>>不参与逐仓</option>
				</select>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>平仓系数(USDT)：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${coins.zcstopscale }" name="zcstopscale">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>逐仓奖励倍数：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${coins.zcscale }" name="zcscale">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>逐仓手续费：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<input type="text" class="input-text" value="${coins.zctax }" name="zctax">
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" type="button" onclick="editSysPara()" value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
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
<script type="text/javascript">
	function editSysPara(){
	 	$.ajax({
	   		url:"/params/editCoin",
	   		data:$('#form-edit').serialize(),
	   		dataType:"json",
	   		type:"post",
	   		success:function(data){
	   			$.Huimodalalert(data.desc, 1000);
   				if(data.status) {
	   				window.setTimeout(function() {
	   					window.parent.location.reload();
	   				}, 1000);
		   		}
	   		}
	   	});
	}
</script>
</body>
</html>