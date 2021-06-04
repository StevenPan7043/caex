<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForForm.jsp"%>

<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.config.js"></script>
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/ueditor.all.js"> </script>
<!--建议手动加在语言，避免在ie下有时因为加载语言失败导致编辑器加载失败-->
<!--这里加载的语言文件会覆盖你在配置项目里添加的语言类型，比如你在配置项目里配置的是英文，这里加载的中文，那最后就是中文-->
<script type="text/javascript" charset="utf-8" src="/resources/js/ueditor/lang/zh-cn/zh-cn.js"></script>

<div class="main_content">
	<div class="contbox_a1">
		<div class="box_a1">
				<form class="wanwu_form"  method="post" action="${rootPath}${info.id == null ? '/cms/addArticle' : '/cms/editArticle'}" id="wanwuForm">
					<input type="hidden" name="id" id="id" value="${info.id}" />
					<textarea id="a_content" name="a_content" style="display: none;">${info.a_content}</textarea>
					<textarea id="a_content_en" name="a_content_en" style="display: none;">${info.a_content_en}</textarea>
					<textarea id="a_content_ko" name="a_content_ko" style="display: none;">${info.a_content_ko}</textarea>
					<textarea id="a_content_jp" name="a_content_jp" style="display: none;">${info.a_content_jp}</textarea>
					<input type="hidden" name="a_content_txt" id="a_content_txt" value="" />
					<input type="hidden" name="a_content_en_txt" id="a_content_en_txt" value="" />
					<input type="hidden" name="a_content_ko_txt" id="a_content_ko_txt" value="" />
					<input type="hidden" name="a_content_jp_txt" id="a_content_jp_txt" value="" />
					<div class="itembox">
						<%
							//管理员可以选择站点，非管理员默认管理自己的站点。
						%>
						<c:choose>
							<c:when test="${user_site_id == '' || user_site_id == null}">
								<div class="div_block">
									<p class="p_block"><label class="label_block">站点ID：</label>
										<select name="site_id" id="site_id" class="sel ww_select required" data="{code:'site|id',initValue:'${info.site_id }'}"><option value="">请选择...</option></select>
									</p>
								</div>
							</c:when>
							<c:otherwise>
								<input type="hidden" name="site_id" id="site_id" value="${info.site_id == null || info.site_id == '' ? user_site_id : info.site_id}" />
							</c:otherwise>
						</c:choose>
						
						<div class="div_block">
							<p class="p_block" style="width:100%;">
								<label class="label_block">所属栏目：</label>
								<select name="column_id" id="column_id" class="sel required"><option value="">请选择...</option></select>
								
								<label style="text-align: right; margin: 2px 0 0 20px; font-size: 13px;">时间：</label><input style="width: 120px;" type="text" name="a_time" class="txt txt_datetime" value="${info.a_time == null ? curDateAndCurTime :  info.a_time}" readonly="readonly"/>
								<label style="text-align: right; margin: 2px 0 0 20px; font-size: 13px;">来源：</label><input style="width: 100px;" type="text" id="a_source" name="a_source" maxlength="20" class="txt" value="${info.a_source == null ? '' :  info.a_source}" />
								<label style="text-align: right; margin: 2px 0 0 22px; font-size: 13px;">作者：</label><input style="width: 80px;" type="text" id="a_author" name="a_author" maxlength="20" class="txt" value="${info.a_author == null ? '' :  info.a_author}" />
								<label style="text-align: right; margin: 2px 0 0 25px; font-size: 13px;">排序[越小越靠前]：</label><input style="width: 60px;" type="text" id="a_order" name="a_order" maxlength="8" class="txt digits required" value="${info.a_order == null ? '1' :  info.a_order}" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block"><label class="label_block">是否跳转：</label>
								<select name="is_jump" id="is_jump" class="sel">
									<option value="0" <c:if test="${info.is_jump == '0' }">selected</c:if> >不跳转</option>
									<option value="1" <c:if test="${info.is_jump == '1' }">selected</c:if> >跳转</option>
								</select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block jump_url_p" style="width:100%; display: none;"><label class="label_block">跳转链接：</label>
								<input id="jump_url" type="text" name="jump_url" maxlength="255" class="txt" style="width:672px;" value="${info.jump_url}" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%;"><label class="label_block">文章标题：</label>
								<input id="a_title" type="text" name="a_title" maxlength="50" class="txt required" style="width:672px;" value="${info.a_title}" />
								<label style="text-align: right; margin: 2px 0 0 58px; font-size: 13px; color:#0000FF; font-weight: bold;">是否横幅：</label><select name="a_is_banner" id="a_is_banner" class="sel ww_select" style="width:69px;" data="{code:'bool|id',initValue:'${info.a_is_banner == null ? 0 : info.a_is_banner}'}"></select>
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%;"><label class="label_block">英文标题：</label>
								<input id="a_title_en" type="text" name="a_title_en" maxlength="150" class="txt required" style="width:672px;" value="${info.a_title_en}" />
							</p>
							<div style="display: none;"><label style="text-align: right; margin: 2px 0 0 58px; font-size: 13px;">内容为链接：</label><select name="a_content_type" id="a_content_type" class="sel ww_select" style="width:69px;" data="{code:'bool|id',initValue:'${info.a_content_type == null ? 0 : info.a_content_type}'}"></select></div>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%;"><label class="label_block">韩文标题：</label>
								<input id="a_title_ko" type="text" name="a_title_ko" maxlength="150" class="txt required" style="width:672px;" value="${info.a_title_ko}" />
							</p>
						</div>
						<div class="div_block">
							<p class="p_block" style="width:100%;"><label class="label_block">日文标题：</label>
								<input id="a_title_jp" type="text" name="a_title_jp" maxlength="150" class="txt required" style="width:672px;" value="${info.a_title_jp}" />
							</p>
						</div>

						<div class="div_block" style="width:100%;">
							<label class="label_block">文章摘要[中文]：</label>
							<textarea class="txta" id="a_abstract" name="a_abstract" style="width:880px; height:50px;">${info.a_abstract }</textarea>
						</div>
						<div class="div_block" style="width:100%;">
							<label class="label_block">文章摘要[英文]：</label>
							<textarea class="txta" id="a_abstract_en" name="a_abstract_en" style="width:880px; height:50px;">${info.a_abstract_en }</textarea>
						</div>
						<div class="div_block" style="width:100%;">
							<label class="label_block">文章摘要[韩文]：</label>
							<textarea class="txta" id="a_abstract_ko" name="a_abstract_ko" style="width:880px; height:50px;">${info.a_abstract_ko }</textarea>
						</div>
						<div class="div_block" style="width:100%;">
							<label class="label_block">文章摘要[日文]：</label>
							<textarea class="txta" id="a_abstract_jp" name="a_abstract_jp" style="width:880px; height:50px;">${info.a_abstract_jp }</textarea>
						</div>
						
						<div class="div_block" style="width:100%; display: none;">
							<p class="p_block" style="width:100%;"><label class="label_block">视频链接：</label>
								<input id="a_viedo_url" type="text" name="a_viedo_url" maxlength="100" class="txt" style="width:506px;" value="${info.a_viedo_url }" />
								<a href="javascript:void(0);" onclick="upFiles();" style="color:blue;text-decoration:underline;">上传视频</a>
								<label style="text-align: right; margin: 2px 0 0 25px; font-size: 13px;">视频宽度：</label><input style="width: 60px;" type="text" id="a_viedo_width" name="a_viedo_width" maxlength="4" class="txt digits required" value="${info.a_viedo_width == null ? '700' :  info.a_viedo_width}" />
								<label style="text-align: right; margin: 2px 0 0 25px; font-size: 13px;">视频高度：</label><input style="width: 60px;" type="text" id="a_viedo_height" name="a_viedo_height" maxlength="4" class="txt digits required" value="${info.a_viedo_height == null ? '525' :  info.a_viedo_height}" />
							</p>
						</div>
						
						<div class="div_block" style="width:100%;">
							中文内容
						</div>	
						<div class="div_block" style="width:100%; display:none;" id="ContentDiv">
							<script id="editor" type="text/plain" style="width:1000px;height:300px;"></script>
							<script id="upload_ue" type="text/plain"></script>
						</div>
						
						<div class="div_block" style="width:100%;">
							英文内容
						</div>
						<div class="div_block" style="width:100%; display:none;" id="ContentEnDiv">
							<script id="editor1" type="text/plain" style="width:1000px;height:300px;"></script>
						</div>

                        <div class="div_block" style="width:100%;">
                            韩文内容
                            </div>
                            <div class="div_block" style="width:100%; display:none;" id="ContentKoDiv">
                            <script id="editor2Ko" type="text/plain" style="width:1000px;height:300px;"></script>
						</div>

						<div class="div_block" style="width:100%;">
							日文内容
						</div>
						<div class="div_block" style="width:100%; display:none;" id="ContentJpDiv">
							<script id="editor3Jp" type="text/plain" style="width:1000px;height:300px;"></script>
						</div>





						
						<div class="div_block" style="width:100%; display:none;" id="LinkDiv">
							<p class="p_block" style="width:100%;"><label class="label_block">文章链接：</label>
								<input id="a_content_link" type="text" name="a_content_link" maxlength="1000" class="txt required" style="width:880px;" value="" />
							</p>
						</div>
					</div>
					<p class="p_block p_btn">
						<input type="button" onclick="return pCheck();" class="btn_a btn_a1" name="btn_submit" id="ok" value="确 定"/>
						<input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset"  value="关 闭" />
					</p>
				</form>
		</div>
	</div>
</div>
<%@ include file="/jsp/bottom.jsp"%>

<script>
var ue, ue1,ue2Ko,ue3Jp;

$(function(){
	// 跳转下拉框判断
    if('${info.is_jump}' == '1') {
        $(".jump_url_p").show();
	}

	if ('${user_site_id}' != '') {
		funcGetArticleColumnBySiteId("${user_site_id}", 'column_id', "${info.column_id}");
	} else {
		funcGetArticleColumnBySiteId("${info.site_id}", 'column_id', "${info.column_id}");
	}
	
	//实例化编辑器
	//建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('editor')就能拿到相关的实例
	ue = UE.getEditor('editor');
	ue1 = UE.getEditor('editor1');
	ue2Ko = UE.getEditor('editor2Ko');
	ue3Jp = UE.getEditor('editor3Jp');

	ue.addListener("ready", function () {
        // editor准备好之后才可以使用
        if ('${info.a_content_type}' == '0') {
			ue.setContent($("#a_content").val());
			ue1.setContent($("#a_content_en").val());
            ue2Ko.setContent($("#a_content_ko").val());
            ue3Jp.setContent($("#a_content_jp").val());
		} else {
			$("#a_content_link").val($("#a_content").val())
		}
    });
    
    
    if ('${info.a_content_type}' == '') {
    	toogleContentLinkDiv(0);
    } else {
    	toogleContentLinkDiv(${info.a_content_type});
    }
    
});

//--------------------------
//上传独立使用  开始
//--------------------------
// 这一段表示自定义上传链接
UE.Editor.prototype._bkGetActionUrl=UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl=function(action){
	if (action == 'uploadimage' ||action== 'uploadscrawl' || action == 'uploadimage') {
		return '/m/upload/ueditor';
	}   else if(action == 'listimage'){
		return this._bkGetActionUrl.call(this, action);
	} else{
		return this._bkGetActionUrl.call(this, action);
	}
}

var _editor = UE.getEditor('upload_ue');
_editor.ready(function () {
    _editor.setDisabled();
    _editor.hide();
    _editor.addListener('afterUpfile', function (t, arg) {
        $("#a_viedo_url").attr("value", arg[0].url);
    })
});
function upFiles() {
    var myFiles = _editor.getDialog("attachment");
    myFiles.open();
}
//--------------------------
//上传独立使用  结束
//--------------------------

$('#site_id').live("change", (function() {
	//联动栏目
	funcGetArticleColumnBySiteId(this.value, 'column_id', "${info.column_id}");
}));

$('#a_content_type').live("change", (function() {
	toogleContentLinkDiv($('#a_content_type').val());
}));

function toogleContentLinkDiv(aContentType) {
	if (aContentType == 0) {
		$('#LinkDiv').hide();
		$('#ContentDiv').show();
		$('#ContentEnDiv').show();
		$('#ContentKoDiv').show();
		$('#ContentJpDiv').show();
	} else {
		$('#ContentDiv').hide();
		$('#ContentEnDiv').hide();
		$('#ContentKoDiv').hide();
		$('#ContentJpDiv').hide();
		$('#LinkDiv').show();
	}
}

function pCheck(){
	var bool = true;
	
	
	// 表示内容为正文，否则内容为链接
	if ($('#a_content_type').val() == 0) {
		$("#a_content").val(UE.getEditor('editor').getContent());
		$("#a_content_txt").val(UE.getEditor('editor').getContentTxt());
		
		$("#a_content_en").val(UE.getEditor('editor1').getContent());
		$("#a_content_en_txt").val(UE.getEditor('editor1').getContentTxt());

        $("#a_content_ko").val(UE.getEditor('editor2Ko').getContent());
        $("#a_content_ko_txt").val(UE.getEditor('editor2Ko').getContentTxt());

        $("#a_content_jp").val(UE.getEditor('editor3Jp').getContent());
        $("#a_content_jp_txt").val(UE.getEditor('editor3Jp').getContentTxt());
	} else {
		$("#a_content").val($("#a_content_link").val());
	}
	
	if ($("#a_content").val() == '' && $('#a_content_type').val() == 0) {
		layer.alert("请输入正文内容。");
		bool = false;
	} else if ($('#a_content_type').val() == 1){
		if (!checkURL($("#a_content").val())) {
			layer.alert("文章链接的正确格式是： http://www.xxxx.xx/xxx.html 。");
			bool = false;
		}
	}
	
	if (bool) {
		$("#wanwuForm").validate();
		$("#wanwuForm").submit();
	}
}

function checkURL(URL){
	var str=URL;
	//判断URL地址的正则表达式为:http(s)?://([\w-]+\.)+[\w-]+(/[\w- ./?%&=]*)?
	//下面的代码中应用了转义字符"\"输出一个字符"/"
	var Expression=/http(s)?:\/\/([\w-]+\.)+[\w-]+(\/[\w- .\/?%&=]*)?/;
	var objExp=new RegExp(Expression);
	if(objExp.test(str)==true){
		return true;
	}else{
		return false;
	}
}


$("#is_jump").change(function () {
	var is_jump = $(this).children('option:selected').val();
	if (is_jump == "0") {
		$(".jump_url_p").hide();
		$("#jump_url").val("");
	} else if (is_jump == "1") {
		$(".jump_url_p").show();
	}
});
</script>