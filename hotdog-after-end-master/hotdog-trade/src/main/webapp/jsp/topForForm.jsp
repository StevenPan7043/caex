<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/buttonPermission" prefix="bp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="rootPath" value="/backstage" />
<%
	String curIDCardDomain = "";
	pageContext.setAttribute("curIDCardDomain", curIDCardDomain);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
  	<title>${MGRCONFIG.comp_name }</title>
  	<link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
  	<link href="/resources/js/liger/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
  		
  	
	
	<link href="/resources/js/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
	
	<!-- ligerUI -->
	<script src="/resources/js/jquery-1.7.1.min.js" type="text/javascript"></script>
	<!-- yuwei 添加 -->
	<script type="text/javascript" src="/resources/js/jquery-validation/jquery.validate.js"></script> 
	<script type="text/javascript" src="/resources/js/jquery-validation/jquery.metadata.js"></script>
	<script type="text/javascript" src="/resources/js/jquery-validation/messages_cn.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.json-2.3.min.js"></script>
	<script type="text/javascript" src="/resources/js/soTree.js"></script>
	 
	<script src="/resources/js/liger/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	<!-- 日历 -->
	<script src="/resources/js/datetimepicker/jquery.datetimepicker.js?v=2" type="text/javascript"></script>

	<!-- 公用函数 -->
	<script src="/resources/js/commcomponent.js?v=6" type="text/javascript"></script>
	<script src="/resources/js/layer/layer.min.js" type="text/javascript"></script>
	<script src="/resources/js/layer/extend/layer.ext.js" type="text/javascript"></script>
	
	<link type="text/css" rel="stylesheet" media="all" href="/resources/css/wanwustyle.css" />
	<link type="text/css" rel="stylesheet" media="all" href="/resources/css/style.css" />
	<link type="text/css" rel="stylesheet" media="all" href="/resources/css/index.css" />
	<link rel="stylesheet" type="text/css" href="/resources/js/liger/ligerUI/skins/Gray/css/dialog.css?v=1.2" />
	
	
	<link rel="stylesheet" type="text/css" href="/resources/js/liger/ligerUI/skins/ligerui-icons.css?v=1.2"/>
	<style type="text/css">
		label{cursor:pointer;}
		.div_block .txt, .div_block .number {
		    width: 160px;
		}
		select {
		    width: 168px;
			height:24px;
		    line-height:24px;
		}
		.div_block .sel {
			height:24px;
		    line-height:24px;
		}
		
		.l-text-wrapper {
		    float: left;
		    position: relative;
		}
	</style>
  </head>
  <body  style="overflow:auto;">