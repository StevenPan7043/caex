<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
	<div class="searchField">
		<div class="searchFieldLbl">会员账号</div>
		<div class="searchFieldCtr">
			<input  id="m_name" name="m_name" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">推荐人ID</div>
		<div class="searchFieldCtr">
			<input  id="introduce_m_id" name="introduce_m_id" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">最后IP</div>
		<div class="searchFieldCtr">
			<input  id="last_login_ip" name="last_login_ip" type="text" class="txt enterAsSearch" />
		</div>
	</div>
	<div class="searchField">
		<div class="searchFieldLbl">状态</div>
		<div class="searchFieldCtr">
			<select id="id_status" name="id_status" class="sel">
				<option value="">--请选择--</option>
				<option value="0">审核中</option>
				<option value="1">审核通过</option>
				<option value="2">审核不通过</option>
				<option value="3">人脸识别中</option>
			</select>
		</div>
	</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	<input onclick="batchNotPass()" type="button" style="width: 100px;" value="批量不通过" data="{grid:'gridBox',scope:'searchDiv'}" class="btn_a ope_refresh" />

</div>
	
<div id="gridBox" style="margin:0; padding:0;"></div>

<script type="text/javascript">
var grid,manage;
var itemsStr = "[ "
	+ "{ text: '审核', click: dicOper, icon: 'modify'}]";
$(function () {
	  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
      grid = manage = $("#gridBox").ligerGrid({
		columns: [
			{display:'会员账号',name:'m_name',width:180},
			{display:'姓名',name:'real_name',width:120,render:function(r,n,v){return r.given_name + "|" + r.middle_name + "|" + r.family_name;}},
			{display:'提交时间',name:'last_submit_time',width:125},
			{display:'证件信息',name:'id_number',width:210},
			{display:'省市',name:'province',width:250,render:function(r,n,v){return r.province + "|" + r.city;}},
			{display:'国家',name:'nationality',width:120},
			{display:'介绍人ID',name:'introduce_m_id',width:120,render:function(r,n,v){return (v == "0" || !v) ? "无" : v;}},
			{display:'状态',name:'id_status',width:80,render:function(r,n,v){return v=='1'&&'审核通过'||v=='0'&&'审核中'||v=='2'&&'审核不通过'||v=='3'&&'人脸识别中';}},
			{display:'拒绝原因',name:'reject_reason',width:260},
			{display:'审核方式',name:'auditor',width:80,render:function(r,n,v){
				return r.auditor;
				}
			},
			{
				display: '操作', isSort: false, width: 120, render: function (rowdata, rowindex, value) {
					var show = "<a href='javascript:showImages(\"" + rowdata.id_front_img+ "\",\""+rowdata.id_back_img+"\",\""+rowdata.id_handheld_img+"\")'>查看实名信息</a> ";
					return show;
				}
			}
		],
		sortName: 'last_submit_time',
	   	sortOrder: 'desc',
		url: "${rootPath}/member/auth_identity",
		method: "get",
        checkbox: true,
		rownumbers: true,
		onContextmenu : function (parm,e)
        {
            menu.show({ top: e.pageY, left: e.pageX });
            return false;
        },
        onDblClickRow : function (data, rowindex, rowobj)
		{
			var item = {text: '审核'};
			dicOper(item);
		},
		delayLoad: true,
		toolbar:{  items: eval(itemsStr)  },
	});
      
	$('#btnOK').trigger('click');
});

function showImages(front,back,handheld){
	$.ligerDialog.open({ height: 800,width:1300,url: '/jsp/business/member/auth_identity_show_auth.jsp?front='+front+'&back='+back+'&handheld='+handheld+'' });
}

function dicOper(item){
	var gm = $("#gridBox").ligerGetGridManager();
    var row = gm.getSelected();
        
	switch (item.text) {
		case "审核":
	        f_common_edit($("#gridBox"), "${rootPath}/member/auth_identity_edit?id={id}", false, 900, 700, "审核");
	        break; 
    }
}
// 批量审核不通过
function batchNotPass() {
    var obj = manage.selected;
    if(obj.length <= 0) {
        layer.alert('至少选择一个！');
        return;
    }

    $util.sure("您确定【"+obj.length+"】条记录不通过吗？", function() {
        var url = '${rootPath}/member/auth_identity_not_pass';
        $.ajax({
            method: 'post',
            url: url,
            dataType: "json",
            contentType:"application/json",
            data: JSON.stringify(obj),
            success: function (res) {
                $.submitShowResult(res);
                try {
                    $hook.opeRefresh("ope_refresh","gridBox","searchDiv");
                } catch (e) {
                    layer.alert(e);
                }
            },
            error: function (res) {
                $.submitShowResult(res);
            }
        })
    });
}

/**
 * 清空条件查询选项
 */
function btnClearInput() {
    $("#searchDiv").find("option:selected").each(function () {
        this.selected = false;
    })
    $("#searchDiv input[type=text]").each(function () {
        this.value = null;
    })

}
</script>
