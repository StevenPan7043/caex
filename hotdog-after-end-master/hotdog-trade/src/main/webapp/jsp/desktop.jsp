<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="/buttonPermission" prefix="bp"%>
 
<head>
	<script src="https://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
	<script src="resources/js/liger/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	<script src="resources/js/commcomponent.js" type="text/javascript"></script>
	<link href="resources/js/liger/ligerUI/skins/Aqua/css/ligerui-dialog.css" rel="stylesheet" type="text/css" />
	<link href="resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
	<link href="resources/css/index_home.css" rel="stylesheet" type="text/css" />
	<style>
		img:hover{
			 cursor:pointer;
		}
		table{
			border-radius :6px !important;
		}
		.spanCls {font-family: Georgia, "宋体";font-size:28px;
			display:inline-block;color:#E20B16;font-weight:bold;
			margin-left:2px;margin-right:5px;}
		.box-shadow{
			filter: progid:DXImageTransform.Microsoft.Shadow(color=’#969696′, Direction=135, Strength=5);/*for ie6,7,8*/
			background-color: #FFFFFF;
			-moz-box-shadow:2px 2px 5px #969696;/*firefox*/
			-webkit-box-shadow:2px 2px 5px #969696;/*webkit*/
			box-shadow:2px 2px 5px #969696;/*opera或ie9*/
			text-align:left;font-size:18px;padding:5px;
		}
	</style>
</head>
<body>

<div class="div_warp" style="margin-bottom:15px;padding-top:10px;">
  <table width="990" height="153" border="0" cellpadding="0" cellspacing="0">
    <tr>
		<td width="330" valign="top">
			<table width="330" height="199" border="0" cellpadding="0" cellspacing="0" class="new_box">
		        <tr>
		          <td height="41" valign="top"><div class="new_lm_bg">
				  <div class="dhbt_zi">待办事项</div>
				  </div></td>
		        </tr>
		        <tr>
		          <td height="160" valign="top">
				  		<div class="newslist">
							<ul>
							<c:forEach var="entry" items="${dbMap}">
							<c:if test="${entry.value.dbSize > 0}">
								<li>
								<a class="l-link memuForAB" id="func${entry.value.id }" onclick="f_addTab('${entry.value.id }', '${entry.value.fuc_name }', '${entry.value.fuc_url }');return false;" href="">${entry.value.dbCount }</a>
								</li>
							</c:if>
							</c:forEach>
							</ul>
						</div>
				  </td>
		        </tr>
		      </table>
		</td>
    </tr>
  </table>
</div>


</body>
<script type="text/javascript">
function f_addTab(tabid, tabname, taburl) {
	window.parent.f_addTab(tabid, tabname, taburl);
}
</script>
</html>