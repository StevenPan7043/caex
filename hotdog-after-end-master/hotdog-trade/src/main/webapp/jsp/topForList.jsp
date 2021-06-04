<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/buttonPermission" prefix="bp"%>
<c:set var="rootPath" value="/backstage" />
<%
	pageContext.setAttribute("curIDCardDomain", "");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  	<title>${MGRCONFIG.comp_name }</title>
	<!-- ligerUI -->
	<script src="https://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
	<!-- yuwei 添加 -->
    <script src="/resources/js/liger/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerGrid.js?v=5" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerToolBar.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerCheckBox.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerLayout.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerSpinner.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script> 
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerFilter.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
    <script src="/resources/js/liger/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
	
	<script src="/resources/js/jquery-validation/jquery.validate.js" type="text/javascript"></script> 
	<script src="/resources/js/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
	<script src="/resources/js/jquery-validation/messages_cn.js" type="text/javascript"></script>
	<script src="/resources/js/jquery.json-2.3.min.js" type="text/javascript"></script>
	<script src="/resources/js/soTree.js" type="text/javascript"></script>
	 
	
	<script src="/resources/js/commcomponent.js?v=6" type="text/javascript"></script>
	<script src="/resources/js/layer/layer.min.js" type="text/javascript"></script>
	<script src="/resources/js/layer/extend/layer.ext.js" type="text/javascript"></script>
	
	<!-- 日历 -->
	<script src="/resources/js/datetimepicker/jquery.datetimepicker.js" type="text/javascript"></script>
    <link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <link href="/resources/js/liger/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
    <link href="/resources/js/liger/ligerUI/skins/ligerui-icons.css" rel="stylesheet" type="text/css" />
    <link href="/resources/css/style_for_list.css?v=1" rel="stylesheet" type="text/css" />
    <link href="/resources/js/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
    
	<style>
		body{font-size:13px; font-family:Arial,宋体,Verdana,Tahoma,Helvetica,sans-serif;}
		label{cursor:pointer;}
		.searchDiv .sel {
			height:24px;
		    line-height:24px;
		}
		.main_content {
		    margin: 2px;
		}
	</style>
  </head>
  <body>
 