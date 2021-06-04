<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/buttonPermission" prefix="bp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  	<title>${MGRCONFIG.comp_name }</title>
  	<link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
  	<link href="/resources/js/liger/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" />
	<link href="/resources/js/datetimepicker/jquery.datetimepicker.css" rel="stylesheet" type="text/css" />
	<!-- ligerUI -->
	<script src="https://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
	<!-- yuwei 添加 -->
	<script type="text/javascript" src="/resources/js/jquery-validation/jquery.validate.js"></script> 
	<script type="text/javascript" src="/resources/js/jquery-validation/jquery.metadata.js"></script>
	<script type="text/javascript" src="/resources/js/jquery-validation/messages_cn.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.json-2.3.min.js"></script>
	<script type="text/javascript" src="/resources/js/soTree.js"></script>
	 
	<script src="/resources/js/liger/ligerUI/js/ligerui.all.js" type="text/javascript"></script>
	<!-- 日历 -->
	<script src="/resources/js/datetimepicker/jquery.datetimepicker.js" type="text/javascript"></script>
	<!-- 报表 -->
	
	<!-- 公用函数 -->
	<script src="/resources/js/layer/layer.min.js" type="text/javascript"></script>
	<link type="text/css" rel="stylesheet" media="all" href="/resources/css/wanwustyle.css" />
	<link type="text/css" rel="stylesheet" media="all" href="/resources/css/style.css" />
	<link type="text/css" rel="stylesheet" media="all" href="/resources/css/index.css" />
	<link rel="stylesheet" type="text/css" href="/resources/js/liger/ligerUI/skins/Gray/css/dialog.css?v=1.2" />
	<link rel="stylesheet" type="text/css" href="/resources/js/webuploader/webuploader.css">
	
	<link rel="stylesheet" type="text/css" href="/resources/js/liger/ligerUI/skins/ligerui-icons.css?v=1.2"/>
	<style type="text/css">
		label{cursor:pointer;}
	</style>
  </head>
  <body  style="overflow:auto;">
	

<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form" method="post" id="dwanwuForm">
					<div class="itembox">
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">图片预览：</label>
								<img src='${image}' style="width: 240px; height: 240px;" id="fileList1" class="uploader-list">
								
								<div style="padding-top: 5px; font-size: 20px; left: 200px; z-index: 9999;" id="_a_img_file" >
									+
									<div style="font-size: 14px; width: 100%;"></div>
								</div>
							</p>
						</div>
						<div class="div_block" style="display: none;">
							<p class="p_block" style="width:100%"><label class="label_block">图片URL：</label>
							<input type="text" style="width: 300px; height: 25px; line-height: 25px;" id="a_img_file" name="a_img_file" value="${image}" >
						</div>
						
					</div>
					<p class="p_block p_btn">
						<input type="button" class="btn_a btn_a1" onclick="setImage();" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="放 弃" />
					</p>
				</form>
				<hr class="hrStyle">
				<div class="div_block">
					<p class="p_block_all" style="margin-left:15px;">
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">注：本界面选择并确定后，需要在广告主编辑界面再次确定才能保存。</label>
					</p>
				</div>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script src="/resources/js/webuploader/webuploader.js"></script>
<script>
function createUploader(filePicker, fileNameId) {
    return WebUploader.create({
        auto: true,
        swf: '/resources/js/webuploader/Uploader.swf',
        method:'post',
        server: "/otc/addImg",
        pick: '#' + filePicker,
        duplicate: true,
        //withCredentials: true,
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/jpg,image/jpeg,image/png'
        }
    })
};
var uploader1;
$(document).ready(function() {
    var loadingLayer = null;
    uploader1 = createUploader("_a_img_file", "");
    uploader1.on('uploadAccept',
	    function(file, res) {
	        layer.closeAll();
	        if (res.state === 1) {
	            $("#fileList1").attr('src', res.data);
	            $("#a_img_file").val(res.data);
	            loadingLayer = null;
	            return true
	        } else {
	            layer.alert(res.msg);
	            return false
	        }
	    }
   	);
    uploader1.on('uploadProgress',
    function(file, percentage) {
        if (null == loadingLayer) {
            loadingLayer = layer.load()
        }
    });
});

function setImage() {
	parent.backQrCode("${sType}", $("#a_img_file").val());
	parent.subDialog.close()
}

</script>