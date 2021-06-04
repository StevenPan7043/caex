<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
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
<script type="text/javascript" src="lib/html5shiv.js"></script>
<script type="text/javascript" src="lib/respond.min.js"></script>
<![endif]-->
<link rel="stylesheet" type="text/css" href="/static/h-ui/css/H-ui.min.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/H-ui.admin.css" />
<link rel="stylesheet" type="text/css" href="/lib/Hui-iconfont/1.0.8/iconfont.css" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/skin/default/skin.css" id="skin" />
<link rel="stylesheet" type="text/css" href="/static/h-ui.admin/css/style.css" />
<script type="text/javascript" src="/lib/jquery/1.9.1/jquery.min.js"></script> 
<script type="text/javascript" src="/js/jquery.form.js"></script> 
<link rel="stylesheet" type="text/css" href="/css/loading.css" />
<script src="/js/preview.js"></script>
<!--[if IE 6]>
<script type="text/javascript" src="lib/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<!--/meta 作为公共模版分离出去-->

<title>新增/编辑Banner </title>
<meta name="keywords" content="">
<meta name="description" content="">
</head>
<body>
<article class="page-container">
<!-- 	<form class="form form-horizontal" id="form-article-add"> -->
	<form method="post" class="form form-horizontal" id="addNewsForm" enctype="multipart/form-data">
		<input type="hidden" name="id" value="${banner.id }" />
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-2">banner：</label>
			<div class="col-sm-2">
			<label for="img" class="btn btn-white">上传图片</label>
			 <input accept=".jpg,.png,.gif,.jpeg" type="file" style="display: none" name="file" id="img"
			onchange="showImg(this)" /> <img width="375px;" height="175px" id="url" src="${banner.imgurl }" />
			 </div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>类型：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<select name="type" class="input-text" >
					<option value="1" <c:if test="${banner.type==1}">selected="selected"</c:if>>APP</option>
					<option value="2" <c:if test="${banner.type==2}">selected="selected"</c:if>>PC</option>
				</select>
			</div>
		</div>
		<div class="row cl">
			<label class="form-label col-xs-4 col-sm-3"><span class="c-red">*</span>跳转公告：</label>
			<div class="formControls col-xs-8 col-sm-9">
				<select name="noticeid" class="input-text" >
					<option value="">请选择</option>
					<c:forEach items="${list }" var="l">
						<option value="${l.id }" <c:if test="${banner.noticeid==l.id}">selected="selected"</c:if>>${l.title }</option>
					</c:forEach>
				</select>
			</div>
		</div>
		<div class="row cl">
			<div class="col-xs-8 col-sm-9 col-xs-offset-4 col-sm-offset-2">
				<a class="btn btn-primary radius" onclick="addNewsNext()"><i class="Hui-iconfont">&#xe632;</i>保存</a>
				<a class="btn btn-default radius" href="javascript:history.back(-1);">&nbsp;&nbsp;取消&nbsp;&nbsp;</a>
			</div>
		</div>
	</form>
</article>

<!--_footer 作为公共模版分离出去-->

<script type="text/javascript" src="/lib/layer/2.4/layer.js"></script>
<script type="text/javascript" src="/static/h-ui/js/H-ui.min.js"></script> 
<script type="text/javascript" src="/static/h-ui.admin/js/H-ui.admin.js"></script> <!--/_footer /作为公共模版分离出去-->

<!--请在下方写此页面业务相关的脚本-->
<script type="text/javascript" src="/lib/My97DatePicker/4.8/WdatePicker.js"></script>
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/jquery.validate.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/validate-methods.js"></script> 
<script type="text/javascript" src="/lib/jquery.validation/1.14.0/messages_zh.js"></script>
<script type="text/javascript" src="/lib/webuploader/0.1.5/webuploader.min.js"></script> 
<script type="text/javascript" src="/lib/ueditor/1.4.3/ueditor.config.js"></script> 
<script type="text/javascript" src="/lib/ueditor/1.4.3/ueditor.all.min.js"> </script> 
<script type="text/javascript" src="/lib/ueditor/1.4.3/lang/zh-cn/zh-cn.js"></script>
<script type="text/javascript" src="/js/loading.js"></script>

<script type="text/javascript">
function addNewsNext() {
	var load = new Loading();
    load.init();
    load.start();
	$("#addNewsForm").ajaxSubmit({
		url : "/banner/editBanner",
		data : $("#addNewsForm").serialize(),
		dataType : "json",
		type : "post",
		success : function(data) {
			load.stop();
			if(data!=null){
   				$.Huimodalalert(data.desc, 1000);
   				if(data.status) {
	   				window.setTimeout(function() {
	   					parent.location.reload();
	   				}, 1000);
   				}
   			}
		}
	})
}

function showImg(v) {
    for (var i = 0; i < v.files.length; i++) {
        var files = v.files[i];
        var name = files.name;
        var index=name.split(".").length;
        var ext = name.split(".")[index-1].toLowerCase();
        if ("png" != ext && "jpg" != ext && "jpeg" != ext && "gif" != ext) {
            alert("文件类型不相符,只允许上传PNG,JPG,GIF,JPEG文件类型");
            return;
        }
        var objUrl1 = getObjectURL(v.files[i]);
        if (objUrl1) {
            // 在这里修改图片的地址属性
            $(v).next().attr('src', objUrl1)
            $(v).next().removeClass("none");
        }
    }
};
//建立一個可存取到該file的url
function getObjectURL(file) {
    var url = null;
    // 下面函数执行的效果是一样的，只是需要针对不同的浏览器执行不同的 js 函数而已
    if (window.createObjectURL != undefined) { // basic
        url = window.createObjectURL(file);
    } else if (window.URL != undefined) { // mozilla(firefox)
        url = window.URL.createObjectURL(file);
    } else if (window.webkitURL != undefined) { // webkit or chrome
        url = window.webkitURL.createObjectURL(file);
    }
    return url;
}

$(function(){
	$('.skin-minimal input').iCheck({
		checkboxClass: 'icheckbox-blue',
		radioClass: 'iradio-blue',
		increaseArea: '20%'
	});
	
});
</script>
<!--/请在上方写此页面业务相关的脚本-->

</body>
</html>