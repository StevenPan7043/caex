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
				<form class="wanwu_form"  method="post" onsubmit="return checkData();" action="/backstage/cms/setArticleThumb" id="dwanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<div class="itembox">
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">文章标题：</label><input  id="user_real_name" type="text" name="user_real_name" style="width:370px;" class="txt required txt_readonly" readonly="readonly" value="${info.a_title }" /></p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">中文图片预览：</label>
								<img src='${info.a_img_file}' style="width: 800px; height: 100px;" id="fileList1" class="uploader-list">
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">中文图片URL：</label>
							<input type="text" style="width: 800px; height: 25px; line-height: 25px;" id="a_img_file" name="a_img_file" value="${info.a_img_file}" >
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">或上传中文图片：</label>
								<div style="padding-top: 5px; font-size: 20px; left: 120px; z-index: 9999;" id="_a_img_file" >
									+
									<div style="font-size: 14px; width: 100%;"></div>
								</div>
							</p>
						</div>
						
						<br><hr style="border-top: 1px solid #0000FF;"><br>
						
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">英文图片预览：</label>
								<img src='${info.a_img_file_en}' style="width: 800px; height: 100px;" id="fileList2" class="uploader-list">
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">英文图片URL：</label>
							<input type="text" style="width: 800px; height: 25px; line-height: 25px;" id="a_img_file_en" name="a_img_file_en" value="${info.a_img_file_en}" >
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">或上传英文图片：</label>
								<div style="padding-top: 5px; font-size: 20px; left: 120px; z-index: 9999;" id="_a_img_file_en" >
									+
									<div style="font-size: 14px; width: 100%;"></div>
								</div>
							</p>
						</div>

						<br><hr style="border-top: 1px solid #0000FF;"><br>

						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">韩文图片预览：</label>
								<img src='${info.a_img_file_ko}' style="width: 800px; height: 100px;" id="fileList3" class="uploader-list">
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">韩文图片URL：</label>
								<input type="text" style="width: 800px; height: 25px; line-height: 25px;" id="a_img_file_ko" name="a_img_file_ko" value="${info.a_img_file_ko}" >
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">或上传韩文图片：</label>
							<div style="padding-top: 5px; font-size: 20px; left: 120px; z-index: 9999;" id="_a_img_file_ko" >
								+
								<div style="font-size: 14px; width: 100%;"></div>
							</div>
							</p>
						</div>

						<br><hr style="border-top: 1px solid #0000FF;"><br>

						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">日文图片预览：</label>
								<img src='${info.a_img_file_jp}' style="width: 800px; height: 100px;" id="fileList4" class="uploader-list">
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">日文图片URL：</label>
								<input type="text" style="width: 800px; height: 25px; line-height: 25px;" id="a_img_file_jp" name="a_img_file_jp" value="${info.a_img_file_jp}" >
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">或上传日文图片：</label>
							<div style="padding-top: 5px; font-size: 20px; left: 120px; z-index: 9999;" id="_a_img_file_jp" >
								+
								<div style="font-size: 14px; width: 100%;"></div>
							</div>
							</p>
						</div>

					</div>
					<p class="p_block p_btn">
						<input type="submit" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
				<hr class="hrStyle">
				<div class="div_block">
					<p class="p_block_all" style="margin-left:15px;">
						<label class="label_block_info" style="display:block; color:blue; width:100%; text-align: left;">注：如果某版本不需要，不上传或清空URL即可，设置后，文章将变成新建状态。</label>
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
        server: "${host}",
        pick: '#' + filePicker,
        duplicate: true,
        //withCredentials: true,
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/jpg,image/jpeg,image/png'
        },
        formData: {
	        'key' : "${dir}" + "banner_p_" + fileNameId,
	        'policy': "${policy}",
	        'OSSAccessKeyId': "${accessid}", 
	        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
	        'signature': "${signature}"
        }
    })
};
var uploader1, uploader2,uploader3,uploader4;
$(document).ready(function() {
    var loadingLayer = null;
    uploader1 = createUploader("_a_img_file",  "${MGRCONFIG.comp_name}" + "_" +"${info.id}" + "_cn");
    uploader1.on('uploadAccept',
	    function(file, res) {
	        layer.closeAll();
	        if (res._raw == "") {
	            $("#fileList1").attr('src', "${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_cn");
	            $("#a_img_file").val("${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_cn");
	            loadingLayer = null;
	            return true
	        } else {
	            layer.alert(res._raw);
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
    
    
    uploader2 = createUploader("_a_img_file_en",  "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_en");
    uploader2.on('uploadAccept',
    function(file, res) {
        layer.closeAll();
        if (res._raw == "") {
            $("#fileList2").attr('src', "${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_en");
            $("#a_img_file_en").val("${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_en");
            loadingLayer = null;
            return true
        } else {
            layer.alert(res._raw);
            return false
        }
    });
    uploader2.on('uploadProgress',
    function(file, percentage) {
        if (null == loadingLayer) {
            loadingLayer = layer.load()
        }
    });

    uploader3 = createUploader("_a_img_file_ko",  "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_ko");
    uploader3.on('uploadAccept',
        function(file, res) {
            layer.closeAll();
            if (res._raw == "") {
                $("#fileList3").attr('src', "${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_ko");
                $("#a_img_file_ko").val("${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_ko");
                loadingLayer = null;
                return true
            } else {
                layer.alert(res._raw);
                return false
            }
        });
    uploader3.on('uploadProgress',
        function(file, percentage) {
            if (null == loadingLayer) {
                loadingLayer = layer.load()
            }
        });

    uploader4 = createUploader("_a_img_file_jp",  "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_jp");
    uploader4.on('uploadAccept',
        function(file, res) {
            layer.closeAll();
            if (res._raw == "") {
                $("#fileList4").attr('src', "${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_jp");
                $("#a_img_file_jp").val("${host}/${dir}banner_p_" + "${MGRCONFIG.comp_name}" + "_" + "${info.id}" + "_jp");
                loadingLayer = null;
                return true
            } else {
                layer.alert(res._raw);
                return false
            }
        });
    uploader4.on('uploadProgress',
        function(file, percentage) {
            if (null == loadingLayer) {
                loadingLayer = layer.load()
            }
        });
});

function checkData() {
	if ($("#a_img_file").val()=="" && $("#a_img_file_en").val()=="") {
		layer.alert("请至少选择一个要上传的文件（中文或英文）！");
		return false;
	}
}

</script>