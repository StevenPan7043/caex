<%@ page contentType="text/html; charset=utf-8"%>
<%@page import="com.pmzhongguo.ex.core.utils.HelpUtils"%>
<%@page import="com.pmzhongguo.ex.core.web.Constants"%>
<%@page import="com.pmzhongguo.ex.framework.entity.FrmUser"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/buttonPermission" prefix="bp"%>
<%
	String basePathShort = request.getScheme() + (request.getScheme().indexOf("www.") > 0 ? "s" : "") + "://" + request.getServerName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>${MGRCONFIG.comp_name }后台管理系统</title>
	<link href="/resources/images/favicon.ico" mce_href="resources/images/favicon.ico" rel="icon" type="image/x-icon" />
	<link href="/resources/images/favicon.ico" mce_href="resources/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
	<link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-dialog.css" rel="stylesheet" type="text/css" />
	<link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	<!-- ligerUI -->
	<script src="https://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
	<script src="/resources/js/liger/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	<script src="/resources/js/liger/ligerUI/js/plugins/ligerTab.js"></script>
	<script src="/resources/js/commcomponent.js" type="text/javascript"></script>
    <script type="text/javascript">
		var tab = null;
		var accordion = null;
		var tree = null;
		var messageDialog = null;
		$(function ()
		{
			//布局
			$("#layout1").ligerLayout({ leftWidth: 140, space:3, onHeightChanged: f_heightChanged });
			
			var height = $(".l-layout-center").height();
			
			//Tab
			$("#framecenter").ligerTab({ height: height });
			//面板
			$("#accordion1").ligerAccordion({ height: height - 24, speed: null});
			
			$(".l-link").hover(function ()
			{
				$(this).addClass("l-link-over");
			}, function ()
			{
				$(this).removeClass("l-link-over");
			});
			//树
			$("#tree1").ligerTree({
			});
			
			tab = $("#framecenter").ligerGetTabManager();
			          
			accordion = $("#accordion1").ligerGetAccordionManager();
			tree = $("#tree1").ligerGetTreeManager();			          
			          
			$("#pageloading").hide();
		});
		function f_heightChanged(options)
		{
			if (tab)
            	tab.addHeight(options.diff);
			if (accordion && options.middleHeight - 24 > 0)
				accordion.setHeight(options.middleHeight - 24);
		}
		function f_addTab(tabid,text, url)
		{ 
             tab.addTabItem({ tabid : tabid,text: text, url: url });
		} 
		function f_addTabNoClose(tabid,text, url)
		{ 
             tab.addTabItem({ tabid : tabid,text: text, url: url, showClose: false });
		} 
         
		function closeLeft() {
			$(".l-layout-header-toggle").click();
		}
         
		var subDialog;
		function modPwd() {
			subDialog = $.ligerDialog.open({title:"修改密码", url:'/noau_toupdatePassword' ,width : 435,height: 255,isResize: false,isHidden : false});
		}
		function setAddToFolder(func_id){
			subDialog = $.ligerDialog.open({title:"添加快捷方式", url:'/noau_toAddToFolder?func_id='+func_id ,width : 435,height: 245,isResize: false,isHidden : false});
		}
		

         
	</script> 
	<style type="text/css"> 
	    body,html{height:100%;font-family:Verdana, Tahoma, Arial, Helvetica, sans-serif;}
	    body{ padding:0px; margin:0; overflow:hidden;}  
	    .l-link{ display:block; height:26px; line-height:26px; padding-left:10px; text-decoration:underline; color:#333;}
	    .l-link2{text-decoration:underline; margin-left:2px;margin-right:2px;}
	    .l-layout-top{background:#102A49; color:White;height:42px;}
	    .l-layout-bottom{ background:#E5EDEF; text-align:center;}
	    #pageloading{position:absolute; left:0px; top:0px; background:white url('/resources/images/loading.gif') no-repeat center; width:100%; height:100%;z-index:99999;}
	    .l-link{ display:block; line-height:22px; height:22px; padding-left:16px;border:1px solid white; margin:4px;}
	    .l-link-over{ background:#FFEEAC; border:1px solid #DB9F00;} 
	    .l-winbar{ background:#2B5A76; height:30px; position:absolute; left:0px; bottom:0px; width:100%; z-index:99999;}
	    /* 顶部  url('resources/images/logo_1.png') no-repeat left*/ 
	    .l-topmenu{ margin:0; padding:0; height:42px; line-height:42px; background:#3367D6 ;position:relative; border-top:1px solid #1D438B;  }
	    .l-topmenu-logo{ color:#E7E7E7; font-family: "Times New Roman", Times, serif; padding-left:10px; font-size:26px; font-weight:bold; line-height:42px; top:4px;}
	    .l-topmenu-welcome{  position:absolute; height:42px; line-height:42px; font-size:14px;  right:0px; top:6px;color:#070A0C;}
	    .l-topmenu-welcome a{ color:#E7E7E7; text-decoration:underline}  
		
		.msg-content{ height : 105px;}
		.msg-button{
			margin-top : 5px;
			margin-bottom : 5px;
			position:relative; 
			text-align:center;
		}
		.msg-button a{ color:blue; text-decoration:underline}
		.right-span{
			position:absolute;
			right:7px;
		}
		.left-span{
			position:absolute;
			left:7px;
		}
		.center-span{
			position:absolute;
		    left:90px;
		
		}
		.contentClass{
			height:110px;
			overflow-y:auto;
		}
		.panel-goal-tips{width:900px;border:2px solid #6cbc28;background-color:#fff;box-shadow:0 0 5px rgba(102,162,50,0.5);position:fixed;_position:absolute;left:50%;margin-left:-450px;top:0;z-index:10000}.panel-goal-tips .table td{border:0;text-align:center;padding:1px 0}.panel-goal-tips .table .team{float:left;width:100px;padding-top:1px}.panel-goal-tips .table td.a-l{text-align:left}.panel-goal-tips .table td.a-r{text-align:right}.panel-goal-tips .table .score{float:left;width:80px;font-size:24px;line-height:24px}.panel-goal-tips .link{text-align:right;padding:0 2px}
		.space{color:#FFFFFF;}
	</style>
</head>
<body style="padding:0px;margin:0px;background:#3367D6;overflow-x:hidden;overflow-y:hidden;" scroll=no>  
	<div id="pageloading"></div>  
	<div id="topmenu" class="l-topmenu">
	    <div class="l-topmenu-logo">${MGRCONFIG.comp_name }后台管理系统</div>
	    <div class="l-topmenu-welcome">
	    	<span style="margin-right:20px;color:#E7E7E7;">
					欢迎您：${sessionScope.SYS_SESSION_USER.user_real_name}   今天是：<span id="today"></span>
			</span>  
			<a href="#" onclick='f_addTab("home", "桌面", "desktop");return false;' class="l-link2">桌面</a> 
	        <span class="space">|</span>
			<a id="/updataPass" class="l-link2" href="#" onclick="modPwd();return false;">修改密码</a> 
	        <span class="space">|</span>
			<a href="/noau_logout" class="l-link2">退出系统</a>
			&nbsp;&nbsp;
	    </div> 
	</div>
	<div id="layout1" style="width:99.2%; margin:0 auto; margin-top:4px; "> 
        <div position="left" title="系统功能" style="height:100%;" id="accordion1"> 
		<c:forEach var="map" items="${userMune}" varStatus="status">
			<c:if test="${map.fuc_parent_id == '0' and map.is_display_in_menu ==1}">
				<div title="${map.fuc_name }">
					<c:forEach var="subMap" items="${userMune}">
						<c:if test="${subMap.fuc_parent_id == map.id}">
							<a class="l-link" id="func${subMap.id }" onclick="f_addTab('${subMap.id }', '${subMap.fuc_name }', '${subMap.fuc_url }');return false;" href="">${subMap.fuc_name }</a>
						</c:if>
					</c:forEach>
				</div>
			</c:if>
		</c:forEach>
		</div>
     	<div position="center" id="framecenter"> 
			<div tabid="home" title="桌面" style="height:300px;" >
				<iframe frameborder="0" name="desktop" id="desktop" src="/noau_desktop"></iframe>
			</div> 
		</div> 
	</div>
    <div style="display:none"></div>
</body>
<script type="text/javascript">
	var tip;
	var vtalkId = "";
	function showSysTip(vcontent, height, msgId,sender) {
		vtalkId = msgId;
		var content = '';
		content += '<div class="contentClass">'+vcontent+'</div><div class="msg-button"><div class="left-span">'
				+  '<font color="blue"><a href="javascript:void(0);" onclick="closeTalk(\'1\');">不再提醒</a></div>';
		if(sender != '0'){
			content += '<div class="center-span"><a href="javascript:void(0);" onclick="closeTalk(\'0\');">稍后提醒</a></div>';
		}	
			content += "<div class='right-span'><a href='javascript:void(0);' onclick='f_addTab(\"GRZX-GTJL\", \"沟通记录\", \"/listMessage?msg_type=1\");return false;'>查看全部</a>"
			 		+	"</font></div></div>";
		if (!tip) {
		    tip = $.ligerDialog.tip({ title: '系统消息', content:content, height: height});
		}else {
		    var visabled = tip.get('visible');
		    if (!visabled) tip.show();
		    tip.set('content', content);
		}
	}

	function closeTalk(type){
		if("1" == type){
			var params ={"msgId":vtalkId,"is_readed":"1"}
			$.reqUrl("updateMsgFlag",params,function(rst){
				tip.hide();
			});
		}else{
			tip.hide();
		}
	} 
	$(function(){
			setDate();
		 	setInterval("setDate()",1* 60 * 1000);	
	});
	function setDate(){
		var now = new Date();
	    $("#today").html(now.getFullYear() +"年"+ (now.getMonth()+1)+"月"+ now.getDate()+"日"); 
	}

	//防止快捷键前进后退
	$(document).keydown(function (e) {
	    var doPrevent;
	    if (e.keyCode == 8) {
	        var d = e.srcElement || e.target;
	        if (d.tagName.toUpperCase() == 'INPUT' || d.tagName.toUpperCase() == 'TEXTAREA') {
	            doPrevent = d.readOnly || d.disabled;
	        }
	        else
	            doPrevent = true;
	    }
	    else
	        doPrevent = false;
	
	    if (doPrevent)
	        e.preventDefault();
	}); 
</script>
</html>