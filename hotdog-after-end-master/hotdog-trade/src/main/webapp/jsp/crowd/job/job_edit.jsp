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
			<form class="wanwu_form" method="post" action="${rootPath}/crowd/job/edit" id="wanwuForm">
				<%--表单提交时用--%>
				<input type="hidden" name="id" id="id" value="${info.id}" />
					<input type="hidden" id="tempProjectId" value="${info.project_id}" />
					<input type="hidden" id="job_class_name" value="${info.job_class_name == null ? 'com.pmzhongguo.crowd.config.scheduler.CrowdOrderJobScheduler' : info.job_class_name}" />
					<div class="itembox">

						<div class="div_block">
						<p class="p_block"><label class="label_block">任务名称：</label>
							<input id="job_name" type="text" name="job_name" maxlength="50" class="txt required" value="${info.job_name}" />
						</p>
							<p class="p_block"><label class="label_block">任务执行间隔（秒）：</label>
								<input id="cron_expression" type="number" name="cron_expression" maxlength="20"
									   class="txt required" value="${info.cron_expression}" />多少秒执行一次
							</p>
					</div>


					<div class="div_block">
						<p class="p_block"><label class="label_block">单笔购买币种数量：</label>
							<input id="order_volume" type="number" name="order_volume" maxlength="50" class="txt required" value="${info.order_volume}" />
						</p>
						<p class="p_block"><label class="label_block">下单总数：</label>
							<input id="order_num" type="number" name="order_num" maxlength="50"
								   class="txt required" value="${info.order_num}"/>
						</p>
					</div>

					<div class="div_block">
						<p class="p_block"><label class="label_block">每秒下单数：</label>
							<input id="sec_num" type="number" name="sec_num" maxlength="50" class="txt required" value="${info.sec_num}" />每n秒执行下单的次数
						</p>
						<p class="p_block">
							<label class="label_block">项目名称：</label>
							<select name="project_id" id="project_id" class="sel required"></select>
						</p>

					</div>


					</div>

                <p class="p_block p_btn" style="display: ${info.id == null ? 'block' : 'none'}">
                    <input type="button" class="btn_a btn_a1"  onclick="pCheck()"  name="btn_submit" id="ok" value="确 定"/>
                    <input type="button" class="btn_a btn_a1" onclick="parent.subDialog.close();" name="btn_reset" value="关 闭"/>
                </p>
            </form>
        </div>
    </div>
</div>
<%@ include file="/jsp/bottom.jsp" %>

<script src="/resources/js/webuploader/webuploader.js"></script>
<script>
    $.get("${rootPath}/crowd/mgr/project/ids", function(data) {
        var optionstring = "<option>--请选择项目--</option>";
		var projectId = $("#tempProjectId").val();
        $.each(data.data, function(k, v) { //循环遍历后台传过来的json数据
            var selected = "";
            if (projectId == v.id) {
                selected = 'selected="selected"';
            }
            optionstring += '<option value="' + v.id + '" '+ selected +'>'
                + v.name + '</option>';
        });
        $("#project_id").html(optionstring);
    });
    // 提交表单时操作
    function pCheck() {

        $("#wanwuForm").validate();
        $("#wanwuForm").submit();
    }
</script>

