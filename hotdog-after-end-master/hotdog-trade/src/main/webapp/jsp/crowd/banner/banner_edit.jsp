<%@ page contentType="text/html; charset=utf-8" %>
<%@ include file="/jsp/topForForm.jsp" %>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.all.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/lang/zh-cn/zh-cn.js"></script>
<link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css"/>
<link href="/resources/js/liger/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css"/>
<link href="/resources/js/liger/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css"/>
<link href="/resources/js/liger/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css"/>
<%--<!-- 公用函数 -->--%>
<script src="/resources/js/layer/layer.min.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" media="all" href="/resources/css/wanwustyle.css"/>
<link type="text/css" rel="stylesheet" media="all" href="/resources/css/style.css"/>
<link type="text/css" rel="stylesheet" media="all" href="/resources/css/index.css"/>
<link rel="stylesheet" type="text/css" href="/resources/js/liger/ligerUI/skins/Gray/css/dialog.css?v=1.2"/>
<link rel="stylesheet" type="text/css" href="/resources/js/webuploader/webuploader.css">

<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
			<form class="wanwu_form" method="post" action="/img/mgr/banner/edit" id="wanwuForm">
				<%--表单提交时用--%>
				<input type="hidden" name="id" id="id" value="${info.id}" />

					<div class="itembox">

						<div class="div_block">
							<p class="p_block"><label class="label_block">场景：</label>
								<input id="scene" type="text" name="scene" maxlength="20" class="txt required" value="${info.scene}" />
							</p>
							<p class="p_block"><label class="label_block">排序：</label>
								<input id="banner_order" type="text" name="banner_order" maxlength="20"
									   class="txt required" value="${info.banner_order}" /> &nbsp;排序数字，越小越靠前
							</p>
						</div>



						<%-------------------------------------图片上传------------------------------------------------------------------------%>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">图片预览：</label>
								<img src='${info.banner_url}' style="width: 800px; height: 400px;" id="fileList1"
									 class="uploader-list">
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">图片URL：</label>
								<input type="text" style="width: 800px; height: 25px; line-height: 25px;" id="a_img_file"
									   name="banner_url" value="${info.banner_url}">
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%"><label class="label_block">或上传图片：</label>
							<div style="padding-top: 5px; font-size: 20px; left: 120px; z-index: 9999;" id="poster">
								+
								<div style="font-size: 14px; width: 100%;"></div>
							</div>
							</p>
						</div>

						<%--------------------------------------图片上传-----------------------------------------------------------------------%>

						<div class="div_block">
							<p class="p_block" style="width:100%;"><label class="label_block">图片名称：</label>
								<input id="name" type="text" name="name" maxlength="50" class="txt " style="width:672px;" value="${info.name}" />
							</p>
						</div>



					</div>

                <p class="p_block p_btn">
                    <input type="button" class="btn_a btn_a1" onclick="pCheck()" name="btn_submit" id="ok" value="确 定"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset" value="关 闭"/>
                </p>
            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp" %>

<script src="/resources/js/webuploader/webuploader.js"></script>
<script>

	// 图片上传工具
    function createUploader(filePicker, fileNameId) {
        return WebUploader.create({
            auto: true,
            swf: '/resources/js/webuploader/Uploader.swf',
            method: 'post',
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
                'key': "${dir}" + "activity/" + fileNameId,
                'policy': "${policy}",
                'OSSAccessKeyId': "${accessid}",
                'success_action_status': '200', //让服务端返回200,不然，默认会返回204
                'signature': "${signature}"
            }
        })
    };

    // 图片上传
    var uploader1;
    $(document).ready(function () {
        var loadingLayer = null;
        var fileName = genUUID() + "-" + "${info.id}";
        var imgUrl = "${host}/${dir}activity/"+fileName;
        console.log(imgUrl);
        uploader1 = createUploader("poster",  fileName );
        uploader1.on('uploadAccept',
            function (file, res) {
                layer.closeAll();
                if (res._raw == "") {
                    // 先清空
                    $("#fileList1").attr('src', "");
                    $("#a_img_file").val("");
                    // 后填充
                    $("#fileList1").attr('src', imgUrl);
                    $("#a_img_file").val(imgUrl);
                    loadingLayer = null;
                    return true
                } else {
                    layer.alert(res._raw);
                    return false
                }
            }
        );
        uploader1.on('uploadProgress',
            function (file, percentage) {
                if (null == loadingLayer) {
                    loadingLayer = layer.load()
                }
            });
	});



	//用于生成uuid
	function S4() {
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	}
	function genUUID() {
		return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
	}


	// 提交表单时操作
	function pCheck() {
		var logo = $("#logoUrl").val();
		if (logo == "") {
			layer.alert("请上传图片");
			return false;
		}
		$("#wanwuForm").validate();
		$("#wanwuForm").submit();
	}

</script>

