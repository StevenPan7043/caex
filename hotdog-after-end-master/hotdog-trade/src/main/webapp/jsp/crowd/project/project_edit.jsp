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
			<form class="wanwu_form" method="post" action="/crowd/mgr/edit" id="wanwuForm">
				<%--表单提交时用--%>
				<input type="hidden" name="id" id="id" value="${info.id}" />
				<input type="hidden" name="project_introduce_txt" id="project_introduce_txt" value="" />
				<input type="hidden" name="project_introduce_en_txt" id="project_introduce_en_txt" value="" />

                <input type="hidden" name="base_info_txt" id="base_info_txt" value="" />
                <input type="hidden" name="base_info_en_txt" id="base_info_en_txt" value="" />

				<input type="hidden" name="sale_plan_txt" id="sale_plan_txt" value="" />
				<input type="hidden" name="sale_plan_en_txt" id="sale_plan_en_txt" value="" />

				<input type="hidden" name="currency_allocate_txt" id="currency_allocate_txt" value="" />
				<input type="hidden" name="currency_allocate_en_txt" id="currency_allocate_en_txt" value="" />

				<input type="hidden" name="project_overview_txt" id="project_overview_txt" value="" />
				<input type="hidden" name="project_overview_en_txt" id="project_overview_en_txt" value="" />

				<input type="hidden" name="team_member_txt" id="team_member_txt" value="" />
				<input type="hidden" name="team_member_en_txt" id="team_member_en_txt" value="" />

				<input type="hidden" name="dev_line_txt" id="dev_line_txt" value="" />
				<input type="hidden" name="dev_line_en_txt" id="dev_line_en_txt" value="" />

				<%-- 编辑时赋值用--%>
				<textarea id="project_introduce" name="project_introduce" style="display: none;">${info.project_introduce}</textarea>
				<textarea id="project_introduce_en" name="project_introduce_en" style="display: none;">${info.project_introduce_en}</textarea>

                <textarea id="base_info" name="base_info" style="display: none;">${info.base_info}</textarea>
                <textarea id="base_info_en" name="base_info_en" style="display: none;">${info.base_info_en}</textarea>

				<textarea id="sale_plan" name="sale_plan" style="display: none;">${info.sale_plan}</textarea>
				<textarea id="sale_plan_en" name="sale_plan_en" style="display: none;">${info.sale_plan_en}</textarea>

				<textarea id="currency_allocate" name="currency_allocate" style="display: none;">${info.currency_allocate}</textarea>
				<textarea id="currency_allocate_en" name="currency_allocate_en" style="display: none;">${info.currency_allocate_en}</textarea>

				<textarea id="project_overview" name="project_overview" style="display: none;">${info.project_overview}</textarea>
				<textarea id="project_overview_en" name="project_overview_en" style="display: none;">${project_overview_en}</textarea>

				<textarea id="team_member" name="team_member" style="display: none;">${info.team_member}</textarea>
				<textarea id="team_member_en" name="team_member_en" style="display: none;">${info.team_member_en}</textarea>

				<textarea id="dev_line" name="dev_line" style="display: none;">${info.dev_line}</textarea>
				<textarea id="dev_line_en" name="dev_line_en" style="display: none;">${info.dev_line_en}</textarea>

				<div class="itembox">

					<div class="div_block">
						<p class="p_block"><label class="label_block">项目名称：</label>
							<input id="name" type="text" name="name" maxlength="40" class="txt required " value="${info.name}"  />
						</p>
						<p class="p_block"><label class="label_block">货币名称：</label>
							<input id="currency" type="text" name="currency" maxlength="20" placeholder="字母大写" class="txt required" value="${info.currency}" />
						</p>
					</div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">计价货币名称：</label>
							<input id="quote_currency" type="text" name="quote_currency" maxlength="20" placeholder="字母大写"  class="txt required " value="${info.quote_currency}"  />
                        </p>
                        <p class="p_block"><label class="label_block">发行数量：</label>
                            <input id="release_amount" type="number" name="release_amount" maxlength="20" class="txt required " value="${info.release_amount}"  />
                        </p>
                    </div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">购买下限：</label>
							<input id="buy_lower_limit" type="number" name="buy_lower_limit" maxlength="20" class="txt required" value="${info.buy_lower_limit}" />
						</p>
						<p class="p_block"><label class="label_block">购买上限：</label>
							<input id="buy_upper_limit" type="number" name="buy_upper_limit" maxlength="20" class="txt required" value="${info.buy_upper_limit}" />
						</p>
					</div>
					<div class="div_block">
                        <p class="p_block"><label class="label_block">开始抢购时间：</label>
                            <input type="text" id="rush_begin_time" name="rush_begin_time" class="txt txt_datetime required" style="width: 160px" value="${info.rush_begin_time}" readonly="readonly"/>
                        </p>
                        <p class="p_block"><label class="label_block">结束抢购时间：</label>
                            <input type="text" id="rush_end_time" name="rush_end_time" class="txt txt_datetime required" style="width: 160px" value="${info.rush_end_time}" readonly="readonly"/>
                        </p>
					</div>
                    <div class="div_block">
                        <p class="p_block"><label class="label_block">排序：</label>
                            <input id="project_order" type="number" name="project_order" maxlength="20" class="txt required" placeholder="数字越小越靠前" value="${info.project_order}" />
                        </p>
						<p class="p_block"><label class="label_block">抢购价格：</label>
							<input id="rush_price" type="number" name="rush_price" maxlength="20" class="txt required" value="${info.rush_price}" />
						</p>
                    </div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">是否展示：</label>
							<select name="is_show" id="is_show" class="sel">
								<option value="1" ${info.is_show == true ? "selected" : "" }>是</option>
								<option value="0" ${info.is_show == false ? "selected" : "" }>否</option>
							</select>
						</p>
						<p class="p_block"><label class="label_block">是否锁仓：</label>
							<select name="is_lock" id="is_lock" class="sel">
								<option value="1" ${info.is_lock == true ? "selected" : "" }>是</option>
								<option value="0" ${info.is_lock == false ? "selected" : "" }>否</option>
							</select>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block"><label class="label_block">价格精度：</label>
							<input id="p_precision" type="number" name="p_precision" maxlength="20" class="txt required" value="${info.p_precision}" />
						</p>
						<p class="p_block"><label class="label_block">数量精度：</label>
							<input id="c_precision" type="number" name="c_precision" maxlength="20" class="txt required" value="${info.c_precision}" />
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width:100%"><label class="label_block">白皮书url：</label>
							<input type="text" class="txt required" style="width: 800px; height: 25px; line-height: 25px;" id="white_book"
								   name="white_book" value="${info.white_book}">
					</div>


					<%-------------------------------------图片上传------------------------------------------------------------------------%>
					<div class="div_block">
						<p class="p_block" style="width:100%"><label class="label_block">图片预览：</label>
							<img src='${info.img_url}' style="width: 800px; height: 100px;" id="fileList1"
								 class="uploader-list">
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width:100%"><label class="label_block">图片URL：</label>
							<input type="text" style="width: 800px; height: 25px; line-height: 25px;" id="a_img_file"
								   name="img_url" value="${info.img_url}">
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
						<p class="p_block" style="width:100%"><label class="label_block">项目摘要中文：</label>
							<textarea class="txta required" id="project_abstract" name="project_abstract"
									  style="width: 800px; height: 100px;">${info.project_abstract}</textarea>
						</p>
					</div>
					<div class="div_block">
						<p class="p_block" style="width:100%"><label class="label_block">项目摘要英文：</label>
							<textarea class="txta required" id="project_abstract_en" name="project_abstract_en"
									  style="width: 800px; height: 100px;">${info.project_abstract_en }</textarea>
						</p>
					</div>


					<%-------------------------------------- 活动中文介绍 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						活动中文介绍
					</div>
					<div class="div_block" style="width:100%; display:none;" id="proIntroContentDiv">
						<script id="proIntroEditor" type="text/plain" style="width:900px;height:300px;"></script>
					</div>

					<div class="div_block" style="width:100%;">
						活动英文介绍
					</div>
					<div class="div_block" style="width:100%; display:none;" id="proIntroContentEnDiv">
						<script id="proIntroEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                    </div>
					<%--------------------------------------基本信息 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						基本信息中文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="baseInfoContentDiv">
						<script id="baseInfoEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<div class="div_block" style="width:100%;">
						基本信息英文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="baseInfoContentEnDiv">
						<script id="baseInfoEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<%--------------------------------------销售计划 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						销售计划中文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="salePlanContentDiv">
						<script id="salePlanEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<div class="div_block" style="width:100%;">
						销售计划英文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="salePlanContentEnDiv">
						<script id="salePlanEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<%--------------------------------------币种分配 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						币种分配中文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="currencyAllocateContentDiv">
						<script id="currencyAllocateEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<div class="div_block" style="width:100%;">
						币种分配英文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="currencyAllocateContentEnDiv">
						<script id="currencyAllocateEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<%--------------------------------------项目概述 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						项目概述中文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="projectOverviewContentDiv">
						<script id="projectOverviewEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<div class="div_block" style="width:100%;">
						项目概述英文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="projectOverviewContentEnDiv">
						<script id="projectOverviewEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<%--------------------------------------团队成员 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						团队成员中文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="teamMemberContentDiv">
						<script id="teamMemberEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<div class="div_block" style="width:100%;">
						团队成员英文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="teamMemberContentEnDiv">
						<script id="teamMemberEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<%--------------------------------------发展路线 富文本-----------------------------------------------------------------------%>
					<div class="div_block" style="width:100%;">
						发展路线中文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="devLineContentDiv">
						<script id="devLineEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
					</div>

					<div class="div_block" style="width:100%;">
						发展路线英文
					</div>
					<div class="div_block" style="width:100%; display:none;" id="devLineContentEnDiv">
						<script id="devLineEnEditor" type="text/plain" style="width:900px;height:300px;"></script>
                        <script id="upload_ue" type="text/plain"></script>
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
    var uploader1, uploader2;
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


        var logoFileName = genUUID() + "-" +  "${info.id}";
        var logoImgUrl = "${host}/${dir}activity/"+logoFileName;
        uploader2 = createUploader("logo", logoFileName);
        uploader2.on('uploadAccept',
            function (file, res) {

                layer.closeAll();
                if (res._raw == "") {
                    // 先清空
                    $("#fileList2").attr('src', "");
                    $("#logoUrl").val("");
                    // 后填充
                    $("#fileList2").attr('src', logoImgUrl);
                    $("#logoUrl").val(logoImgUrl);
                    loadingLayer = null;
                    return true
                } else {
                    layer.alert(res._raw);
                    return false
                }
            });
        uploader2.on('uploadProgress', function (file, percentage) {
            if (null == loadingLayer) {
                loadingLayer = layer.load()
            }
        });


//        ------------------------------富文本------------------------------------------------------
        var proIntroEditor, proIntroEnEditor;
        var baseInfoEditor, baseInfoEnEditor;
        var salePlanEditor, salePlanEnEditor;
        var currencyAllocateEditor, currencyAllocateEnEditor;
        var projectOverviewEditor, projectOverviewEnEditor;
        var teamMemberEditor, teamMemberEnEditor;
        var devLineEditor, devLineEnEditor;
        //实例化编辑器
        //建议使用工厂方法getEditor创建和引用编辑器实例，如果在某个闭包下引用该编辑器，直接调用UE.getEditor('proIntroEditor')就能拿到相关的实例
        proIntroEditor = UE.getEditor('proIntroEditor');
        proIntroEnEditor = UE.getEditor('proIntroEnEditor');

        baseInfoEditor = UE.getEditor('baseInfoEditor');
        baseInfoEnEditor = UE.getEditor('baseInfoEnEditor');

        salePlanEditor = UE.getEditor('salePlanEditor');
        salePlanEnEditor = UE.getEditor('salePlanEnEditor');

        currencyAllocateEditor = UE.getEditor('currencyAllocateEditor');
        currencyAllocateEnEditor = UE.getEditor('currencyAllocateEnEditor');

        teamMemberEditor = UE.getEditor('teamMemberEditor');
        teamMemberEnEditor = UE.getEditor('teamMemberEnEditor');

        devLineEditor = UE.getEditor('devLineEditor');
        devLineEnEditor = UE.getEditor('devLineEnEditor');

        projectOverviewEditor = UE.getEditor('projectOverviewEditor');
        projectOverviewEnEditor = UE.getEditor('projectOverviewEnEditor');


//		加载准备
        proIntroEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            proIntroEditor.setContent($("#project_introduce").val());
        });
        proIntroEnEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            proIntroEnEditor.setContent($("#project_introduce_en").val());
        });

        baseInfoEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            baseInfoEditor.setContent($("#base_info").val());
        });
        baseInfoEnEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            baseInfoEnEditor.setContent($("#base_info_en").val());
        });
        salePlanEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            salePlanEditor.setContent($("#sale_plan").val());
        });
        salePlanEnEditor.addListener("ready", function () {
            // editor准备好之后才可以使用

            salePlanEnEditor.setContent($("#sale_plan_en").val());
        });
        currencyAllocateEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            currencyAllocateEditor.setContent($("#currency_allocate").val());
        });
        currencyAllocateEnEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            currencyAllocateEnEditor.setContent($("#currency_allocate_en").val());
        });
        teamMemberEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            teamMemberEditor.setContent($("#team_member").val());
        });
        teamMemberEnEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            teamMemberEnEditor.setContent($("#team_member_en").val());
        });
        devLineEnEditor.addListener("ready", function () {
            devLineEnEditor.setContent($("#dev_line_en").val());
        });
        devLineEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            devLineEditor.setContent($("#dev_line").val());
        });
        projectOverviewEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            projectOverviewEditor.setContent($("#project_overview").val());
        });
        projectOverviewEnEditor.addListener("ready", function () {
            // editor准备好之后才可以使用
            projectOverviewEnEditor.setContent($("#project_overview_en").val());
        });

        // 显示富文本框
        toogleContentLinkDiv();
    });

    function checkData() {
        if ($("#a_img_file").val() == "" && $("#a_img_file_en").val() == "") {
            layer.alert("请至少选择一个要上传的文件（中文或英文）！");
            return false;
        }
    }


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


    //        显示富文本框
    function toogleContentLinkDiv() {
        $('#proIntroContentDiv').show();
        $('#proIntroContentEnDiv').show();
        $('#baseInfoContentDiv').show();
        $('#baseInfoContentEnDiv').show();
        $('#salePlanContentDiv').show();
        $('#salePlanContentEnDiv').show();
        $('#currencyAllocateContentDiv').show();
        $('#currencyAllocateContentEnDiv').show();
        $('#teamMemberContentDiv').show();
        $('#teamMemberContentEnDiv').show();
        $('#devLineContentDiv').show();
        $('#devLineContentEnDiv').show();
        $('#projectOverviewContentDiv').show();
        $('#projectOverviewContentEnDiv').show();
    }

    //用于生成uuid
    function S4() {
        return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    function genUUID() {
        return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    }


    // 提交表单时操作
    function pCheck(){
        var logo = $("#logoUrl").val();
        if (logo == "") {
            layer.alert("请上传图片");
            return false;
        }

//      赋值
        $("#project_introduce").val(UE.getEditor('proIntroEditor').getContent());
        $("#project_introduce_txt").val(UE.getEditor('proIntroEditor').getContentTxt());

        $("#project_introduce_en").val(UE.getEditor('proIntroEnEditor').getContent());
        $("#project_introduce_en_txt").val(UE.getEditor('proIntroEnEditor').getContentTxt());

        $("#base_info").val(UE.getEditor('baseInfoEditor').getContent());
        $("#base_info_txt").val(UE.getEditor('baseInfoEditor').getContentTxt());

        $("#base_info_en").val(UE.getEditor('baseInfoEnEditor').getContent());
        $("#base_info_en_txt").val(UE.getEditor('baseInfoEnEditor').getContentTxt());

        $("#sale_plan").val(UE.getEditor('salePlanEditor').getContent());
        $("#sale_plan_txt").val(UE.getEditor('salePlanEditor').getContentTxt());

        $("#sale_plan_en").val(UE.getEditor('salePlanEnEditor').getContent());
        $("#sale_plan_en_txt").val(UE.getEditor('salePlanEnEditor').getContentTxt());

        $("#currency_allocate").val(UE.getEditor('currencyAllocateEditor').getContent());
        $("#currency_allocate_txt").val(UE.getEditor('currencyAllocateEditor').getContentTxt());

        $("#currency_allocate_en").val(UE.getEditor('currencyAllocateEnEditor').getContent());
        $("#currency_allocate_en_txt").val(UE.getEditor('currencyAllocateEnEditor').getContentTxt());

        $("#team_member").val(UE.getEditor('teamMemberEditor').getContent());
        $("#team_member_txt").val(UE.getEditor('teamMemberEditor').getContentTxt());

        $("#team_member_en").val(UE.getEditor('teamMemberEnEditor').getContent());
        $("#team_member_en_txt").val(UE.getEditor('teamMemberEnEditor').getContentTxt());

        $("#dev_line").val(UE.getEditor('devLineEditor').getContent());
        $("#dev_line_txt").val(UE.getEditor('devLineEditor').getContentTxt());

        $("#dev_line_en").val(UE.getEditor('devLineEnEditor').getContent());
        $("#dev_line_en_txt").val(UE.getEditor('devLineEnEditor').getContentTxt());

        $("#project_overview").val(UE.getEditor('projectOverviewEditor').getContent());
        $("#project_overview_txt").val(UE.getEditor('projectOverviewEditor').getContentTxt());

        $("#project_overview_en").val(UE.getEditor('projectOverviewEnEditor').getContent());
        $("#project_overview_en_txt").val(UE.getEditor('projectOverviewEnEditor').getContentTxt());




        $("#wanwuForm").validate();
        $("#wanwuForm").submit();
    }


</script>

