<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0">
<title>GBCOIN注册账号</title>
<link rel="stylesheet" href="/css/main.css?v=1.0" type="text/css">

<script src="/js/calc.js"></script>
<script src="/js/jquery.min.js"></script>
<script src="/js/layer/layer.js"></script>
</head>
<body class="register">
	<div class="main" style="padding-top: 1.2rem">
		<div class="container">
			<img src="/images/logo.png?v=1.1" alt="" class="logo-img">
			<p class="big-title">注册账号</p>
			<div class="con-form">
				<div class="con-li">
					<div class="li-text">
						<span>手机号</span>
					</div>
					<input type="number" class="li-input"
						onblur="getValidcode()" name="phone" id="phone" maxlength="11"
						placeholder="请输入手机号">
				</div>
				<div class="con-li">
					<div class="li-text">
						<span>登录密码</span>
					</div>
					<input type="password" name="password" maxlength="14" id="password"
						placeholder="请设置密码 6～14位英文及数字" class="li-input">
				</div>
				<div class="con-li">
					<div class="li-text">
						<span>支付密码</span>
					</div>
					<input type="password" name="payword" id="payword" maxlength="6"
						placeholder="设置交易密码,6位数字" class="li-input">
				</div>
				<div class="con-li">
					<div class="li-text">
						<span>图形验证码</span>
					</div>
					<input type="text" placeholder="请输入图形验证码" class="li-input"
						name="validimg" id="validimg"> <img src="/utils/validCode" onclick="getValidcode()" name="codeimage" id="codeimage" alt="">
				</div>
				<div class="con-li">
					<div class="li-text">
						<span>验证码</span>
					</div>
					<input type="tel" placeholder="请输入验证码" class="li-input"
						name="validcode" maxlength="6" id="validcode"> <label
						class="get-code take-code">获取验证码</label> <label
						class="get-code got-code">已获取(<label id="time">60</label>s)
					</label>
				</div>
				<input type="hidden" readonly="readonly" id="parentcode" name="parentcode" value="${invitationcode}">
			</div>
			
			<div class="reg-btn" onclick="handle_regist();">注册</div>
		</div>
	</div>
	<script>
	function getValidcode(){
		var phone=$("#phone").val();
		if(phone!=null && phone.length>0 && phone!='undefined' && phone!=undefined){
			document.getElementById('codeimage').src='/utils/validCode?login='+phone+'&t=' + Math.random();
		}
	}
		// 60秒倒计时
		var setTime;
		$(function() {
			var time = parseInt($("#time").text());
			$(".take-code").on(
					"click",
					function() {
						var phone = $("#phone").val();
						if (phone == null || phone.length < 1
								|| phone == 'undefined' || phone == undefined) {
							layer.open({
								content :"请输入手机号/邮箱",
								skin : 'msg',
								time : 1
							});
							return;
						}
						var validcode = $("#validimg").val();
						if (validcode == null || validcode.length < 1
								|| validcode == 'undefined'
								|| validcode == undefined) {
							layer.open({
								content :"请输入图片验证码",
								skin : 'msg',
								time : 1
							});
							return;
						}
						var url = "/util/sendPhonecode";
						var pamars = {
							"phone" : phone,
							"validcode" : validcode
						};
						var type = $("#type").val();
						if (type == 2) {
							url = "/util/sendMailcode";
							pamars = {
								"email" : phone,
								"validcode" : validcode
							};
						}
						$.ajax({
							url : url,
							type : "post",
							data : pamars,
							dataType : "json",
							success : function(data) {
								if (data.status) {
									layer.open({
										content : '验证码发送成功',
										skin : 'msg',
										time : 1
									});
									$(".got-code").show();
									$(".take-code").hide();
									setTime = setInterval(function() {
										if (time <= 0) {
											clearInterval(setTime);
											$(".got-code").hide();
											$(".take-code").show();
											time = 60;
											return;
										}
										time--;
										$("#time").text(time);
									}, 1000);
								} else {
									layer.open({
										content : data.desc,
										skin : 'msg',
										time : 1
									});
								}
							}
						});
					})
		});
		function handle_regist() {
			var type = $("#type").val();
			var params = {
				"password" : $("#password").val(),
				"confirmpwd" : $("#confirmpwd").val(),
				"payword" : $("#payword").val(),
				"validcode" : $("#validcode").val(),
				"parentcode" : $("#parentcode").val(),
				"phone" : $("#phone").val()
			};
			$.ajax({
				url : "/customer/handleRegister",
				type : "post",
				data : params,
				dataType : "json",
				success : function(data) {
					layer.open({
						content : data.desc,
						skin : 'msg',
						time : 1
					});
					console.log(data)
					if (data.status) {
						location.href="/downLoad";
					}
				}
			});
		}
	</script>
</body>
</html>