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

<title>会员管理</title>
</head>
<body>
<nav class="breadcrumb">
	<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span> 会员管理 <span class="c-gray en">&gt;</span> 添加业务员 <a
		class="btn btn-success radius r" style="line-height: 1.6em; margin-top: 3px" href="javascript:location.replace(location.href);" title="刷新"><i
		class="Hui-iconfont">&#xe68f;</i></a>
</nav>
<article class="page-container">
	<form method="post"  class="form form-horizontal" id="form-change-password">
		<div class="row cl">
			<label class="form-label col-xs-3"><span class="c-red">*</span>会员账号：</label>
			<div class="formControls col-xs-3">
				<input type="text" class="input-text radius size-M" autocomplete="off" placeholder="请输入会员账号进行操作" id="login" name="login">
			</div>
			<div class="formControls col-xs-3">
				<input class="btn btn-primary radius" onclick="seachMem()" type="button" value="&nbsp;&nbsp;查询&nbsp;&nbsp;">
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-3"><span class="c-red">*</span>会员信息：</label>
			<div class="formControls col-xs-6" id="showMsg">
				<p>会员账号:<span id="span0" class="spanclear"></span></p>
				<p>姓名:<span id="span1" class="spanclear"></span></p>
				<p>证件号:<span id="span2" class="spanclear"></span></p>
			</div>
			<c:if test="${roleid==1 }">
			<div class="formControls col-xs-12 " id="inputMsg" >
				<div class="col-xs-3"></div>
				<div class="col-xs-6">
					<div class="cl row" style="display: none;"><span style="color:red;">账号不存在，输入关键信息标记业务员/模拟账户</span></div>
					<div class="cl row">
						<label><span class="c-red">*</span>选择身份：</label>
						<select class="input-text "style="width:120px;" name="identity">
							<option value="2">业务员</option>
							<option value="3">模拟账号</option>
						</select>
					</div>
					<div class="cl row" style="display: none;">
						<label><span class="c-red">*</span>登录密码：</label>
						<input name="passWord"  value="" class="input-text "style="width:120px;">
					</div>
					<div class="cl row" style="display: none;">
						<label><span class="c-red">*</span>支付密码：</label>
						<input name="payWord"  value="" class="input-text "style="width:120px;">
					</div>
					<div class="cl row" style="display: none;">
						<label>推荐码：</label>
						<input name="invitationcode"  value="" class="input-text "style="width:120px;">
					</div>
				</div>
			</div>
			</c:if>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-3">
				<input class="btn btn-primary radius" onclick="handleMoney()" type="button" value="&nbsp;&nbsp;新增会员&nbsp;&nbsp;">
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
function seachMem() {
	var login=$("#login").val();
	if(login==null || login==''){
		$.Huimodalalert("请填写会员编号", 1000);
		return;
	}
	$.ajax({
   		url:"/customer/getCustomer",
   		dataType:"json",
   		data:{"login":login},
   		type:"post",
        success: function (data) {
            if (!data.status) {
                $.Huimodalalert(data.desc, 2000);
            } else {
                if (data.status) {
                    $("#showMsg").show()
                    $("#span0").text(data.data.login);
                    $("#span1").text(data.data.realname);
                    $("#span2").text(data.data.idcard);
                }
            }
        }
   	});
}
function handleMoney(){
	$.ajax({
   		url:"/customer/handleSaleman",
   		dataType:"json",
   		data:$("#form-change-password").serialize(),
   		type:"post",
   		success:function(data){
   			if(data!=null){
   				$.Huimodalalert(data.desc, 2000);
   			}
   		}
   	});
}
</script>
<!--/请在上方写此页面业务相关的脚本-->
</body>
</html>