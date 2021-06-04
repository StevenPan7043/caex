<%@ page language="java"  pageEncoding="utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@page import="java.util.Random"%>
<%
	if (null != HelpUtils.getFrmUser() && null != HelpUtils.getFrmUser().getId()) {
		// 已经登录，直接进入
		response.sendRedirect("jsp/home.jsp");
	}

	String _RndData = "";
	int b = 0;
	int a = 0;
	Random r = new Random();
	for (int i = 0; i < 32; i++) {
		a = r.nextInt(26);
		b = (char) (a + 65);
		_RndData += new Character((char) b).toString();
	}

	session.setAttribute("Random", _RndData);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html style="overflow:hidden;overflow-y: hidden;">
  <head>
    <base target="_self" />
	<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="resources/images/favicon.ico" mce_href="resources/images/favicon.ico" rel="icon" type="image/x-icon" />
	<link href="resources/images/favicon.ico" mce_href="resources/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
	<link href="resources/css/login.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="resources/js/liger/ligerUI/skins/Gray/css/dialog.css?v=1.2" />
	
	<script type="text/javascript" src="https://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="resources/js/liger/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	<script src="resources/js/layer/layer.min.js" type="text/javascript"></script>
	<script src="resources/js/liger/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
	<meta charset="utf-8" />
	<title>${MGRCONFIG.comp_name }</title>
	 
	<script language="javascript">
		$(function(){
			 $("#user_name").focus();
			 $("#loginFrm").submit(function(){
			 	if (!$.browser.mozilla && navigator.userAgent.toLowerCase().match(/chrome/) == null) {
					alert("本系统最佳使用效果是在火狐/谷歌浏览器下运行，请下载并安装 火狐/谷歌 浏览器，链接在本页面已经提供！");
				}
				var manager = layer.load('正在登录,请稍候...');
				var user_name = $("#user_name").val();	
				var user_pwd = $("#user_pwd").val();
				var validate_code = $("#validate_code").val();
				var google_auth_key = $("#google_auth_key").val();
				if(!user_name){
					layer.alert('请输入账号!');
					$("#user_name").focus();
					return false;
				}
				if(!user_pwd){
					layer.alert('请输入密码!');
					$("#user_pwd").focus();
					return false;
				}
				<% if(HelpUtils.getMgrConfig().getIs_use_validate() == 1) {%>
				if(!validate_code){
					layer.alert('请输入验证码!');
					$("#validate_code").focus();
					return false;
				}
				<% } %>
				if(!google_auth_key){
					layer.alert('请输入谷歌验证码 !');
					$("#google_auth_key").focus();
					return false;
				}
				$.post("noau_login",{
						user_name:user_name,
						user_pwd:user_pwd,
						validate_code:validate_code,
						google_auth_key: google_auth_key
					},function(rst){
						if(rst.state == 1){
							window.location.href="jsp/home.jsp";
						}else{
							layer.closeAll();
							//清空验证码，重新加载验证码图片
							$("#validate_code").val("");
							changeImg();
							layer.alert(rst.msg);
						}
					},"json");
			 });
		});
		 
		function changeImg() { 
			var imgSrc = $("#imgObj");
			imgSrc.attr("src", chgUrl("noau_validateCode")); 
		} 
		function chgUrl(url) { 
			var timestamp = (new Date()).valueOf(); 
			if ((url.indexOf("&") >= 0)) { 
				url = url + "×tamp=" + timestamp; 
			} else { 
				url = url + "?timestamp=" + timestamp; 
			} 
			return url; 
		}
	</script>
</head>

<body>
<div class="big_box">
<div class="logo_box">
<div class="logo_1"></div><div class="gsmc_txt"></div>
</div>


<div class="login_bg" style="<% if(HelpUtils.getMgrConfig().getIs_use_validate() == 0) {%>height: 316px<%}%>">
<div style="margin:0 auto; width:648px;padding-top:60px;">
<form action="noau_login" method="post" id="loginFrm" name="loginFrm" class="wanwu_form" onsubmit="return false;">
  <table width="648" height="259" border="0" cellpadding="0" cellspacing="0" >
    <tr>
		<td valign="top" style="height:<% if(HelpUtils.getMgrConfig().getIs_use_validate() == 1) {%>180<%}else{%>120<%}%>px">
		  	<div class="geren_box">
				<ul>
					<li><span style="width:80px; float:left; text-align: right; color:#555; font-size:18px; line-height:30px;vertical-align:middle;">账&nbsp;&nbsp;&nbsp;&nbsp;号：</span>
					<dd2><input type="text" name="user_name" id="user_name" tabindex="1" style="color: black;font-size: 18px;font-weight: bold;line-height:30px;height: 30px; width:220px; text-indent: 4px;outline:0px;border:0px;"  maxlength="22" value=""/></dd2></li>
					
					<li><span style="width:80px; float:left; text-align: right; color:#555; font-size:18px; line-height:30px;vertical-align:middle;">密&nbsp;&nbsp;&nbsp;&nbsp;码：</span>
					<dd2><input type="password" name="user_pwd" id="user_pwd" tabindex="2" style="color: black;font-size: 18px;font-weight: bold;line-height:30px;height: 30px; width:220px; text-indent: 4px;outline:0px;border:0px;" maxlength="22" value=""/></dd2></li>
					<% if(HelpUtils.getMgrConfig().getIs_use_validate() == 1) {%>
						<li><span style="width:80px; float:left; text-align: right; color:#555; font-size:18px; line-height:30px;vertical-align:middle;">验证码：</span>
						<dd2><input type="text" name="validate_code" id="validate_code" tabindex="3" style="text-transform:uppercase;ime-mode:disabled; color: black;font-size: 18px;font-weight: bold;line-height:30px;height: 30px; width:100px; text-indent: 4px;outline:0px;border:0px;float:left;" maxlength="4" value=""/><img id="imgObj" onclick="javascript:changeImg()" src="noau_validateCode" style="cursor: pointer;" title="看不清？点击更换验证码。" alt="看不清？点击更换验证码。"/></dd2>
						</li>
					<% }%>
						<li><span style="width:80px; float:left; text-align: right; color:blue; font-weight: bold; font-size:13px; line-height:30px;vertical-align:middle;">谷歌验证码：</span>
						<dd2><input type="text" name="google_auth_key" id="google_auth_key" tabindex="4" style="text-transform:uppercase;ime-mode:disabled; color: black;font-size: 18px;font-weight: bold;line-height:30px;height: 30px; width:100px; text-indent: 4px;outline:0px;border:0px;float:left;" maxlength="6" value=""/><button id="smsBtn" type="button" onclick="getSMSCode()" style="display: none;">获取</button></dd2>
						</li>
				</ul>
			</div>	  
		</td>
    </tr>
    <tr>
		<td height="70" valign="top" align="center">
	  		<div ><input class="an_1_1" id="an_1_1" tabindex="4" name="btnSubmit" value="登录" type="submit" /></div>
		</td>
    </tr>
</table>
</form>
</div>  
</div>
</div>

</body>
</html>
