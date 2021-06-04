<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
		<%
			//管理员可以选择站点，非管理员默认管理自己的站点。
		%>
		<input type="hidden" name="site_id" id="site_id" value="1" />
		<div class="searchField">
			<div class="searchFieldLbl">所属栏目</div>
			<div class="searchFieldCtr">
				<select name="column_id" id="column_id" class="sel required"><option value="">请选择...</option></select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">标题关键词</div>
			<div class="searchFieldCtr">
				<input type="text" name="a_title" id="a_title" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">状态</div>
			<div class="searchFieldCtr">
				<select name="a_status" id="a_status" class="sel ww_select" data="{code:'article_status|id'}"><option value="">请选择...</option></select>
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">是否横幅</div>
			<div class="searchFieldCtr">
				<select name="a_is_banner" id="a_is_banner" class="sel ww_select" data="{code:'bool|id'}"><option value="">请选择...</option></select>
			</div>
		</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
    <input  type="button" class="btn_a" onclick="btnClearInput()"  value="重置" />
</div>

<div class="main_content">
	<div id="gridBox" style="margin:0; padding:0"></div>
</div>

	<!-- 隐藏功能区 开始 -->
	<%
		//审核意见
	%>
	<div id="AuditMemoDiv" class="hide" style="-moz-user-select:none;">
		<div class="div_block" id="AuditMemoDivInner">
		</div>
	</div>
	<!-- 隐藏功能区 结束 -->

<script type="text/javascript">
	var grid;
	var itemsStr = "[ "
	+ "{ text: '增加', click: dicOper, icon: 'add'},"
	+ "{ text: '修改', click: dicOper, icon: 'modify'},"
	+ "{ text: '删除', click: dicOper, icon: 'delete'},"
	+ "{ text: '排序+1', click: dicOper, icon: 'down'},"
	+ "{ text: '送审', click: dicOper, icon: 'right'},"
	+ "{ text: '数据同步', click: dicOper, icon: 'refresh'}]";
	$(function () {
		  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
	      grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'所属栏目',name:'column_name',width:150},	
				{display:'类型',name:'a_content_type',width:60,render:function(r,n,v){return v=='1'&&'链接'||v=='0'&&'内容';}},
				{display:'文章标题',name:'a_title',width:220},
				{display:'发布时间',name:'a_time',width:100},
				{display:'排序',name:'a_order',width:60},
				{display:'缩略图',name:'a_img_file',width:110, render:
					function(row){
						var str = row.a_img_file;
						if (str == "" || str==undefined) {
							return "<a href='javascript:void(0);' onclick='javascript:setThumbFile(" + row.id + ")'><div style='height:80px; line-height:80px;'>点击设置</div></a>";
						}
						return "<a href='javascript:void(0);' onclick='javascript:setThumbFile(" + row.id + ")' style='height:80px'><img src='" + str + "' height='80' width = '110'/></a>";  
					}
				},
				{display:'创建人',name:'creator_name',width:100},
				{display:'创建时间',name:'create_time',width:130},
				{display:'阅读次数',name:'a_count',width:80},
				{display:'是否跳转',name:'is_jump',width:80,render: function (r,n,v) {
					return v == '1' ? '是' : '否';
                }},
				{display:'跳转链接',name:'jump_url',width:180,render: function (r,n,v) {
                    if (v == null || v == "" || v==undefined) {
                        return;
                    }else {
                        return '<a href="'+v+'" target="_blank" style="height:80px">'+v+'</a>';
					}

                }},
				{display:'状态',name:'status_name',width:100, render:
					function(row){
						var html = row.status_name;
						if (row.audit_comments != '' && row.audit_comments != null) {
							$("#AuditMemoDivInner").html(row.audit_comments);
							html = '<a href="javascript:void(0);" onclick="javascript:showAuditMemo()">' + row.status_name + '</a> ';
						}
						return html;
					}
				}
			],
			sortName: 'create_time',
		   	sortOrder: 'desc',
			url: "${rootPath}/cms/listArticle",
			method: "get",
			rownumbers: true,
			onContextmenu : function (parm,e)
	        {
	            menu.show({ top: e.pageY, left: e.pageX });
	            return false;
	        },
	        onDblClickRow : function (data, rowindex, rowobj)
			{
				var item = {text: '修改'};
			    dicOper(item);
			},
			delayLoad: true,
			toolbar:{  items: eval(itemsStr)  }
		});
		
		$(".wanwu_search").click();
	});
 
	function dicOper(item){
		var gm = $("#gridBox").ligerGetGridManager();
	    var row = gm.getSelected();
	        
		switch (item.text) {
			case "增加":
				openAddWindow("增加", "${rootPath}/cms/toAddArticle", 1100, 700);
				break;
	        
			case "修改":
		        f_common_edit($("#gridBox"), "${rootPath}/cms/toEditArticle?id=" + row.id, false, 1100, 700);
		        break;
		        
	        case "删除":
	        	f_common_del($("#gridBox"), "${rootPath}/cms/delArticle?id={id}", false);
	            break;
	            
	       	case "送审":
	        	f_common_del($("#gridBox"), "${rootPath}/cms/sendAuditArticle?id={id}", false, "送审");
	            break;

            case "数据同步":
                syncData();
                break;
	            
			case "排序+1":
				var gm = $("#gridBox").ligerGetGridManager();
				var url="${rootPath}/cms/upArticleOrder";
				$.reqUrlEx(url, null, function(rst) {
					if (rst && rst.state)gm.loadData(true);
				}, '您确定要将所有记录的排序都加1吗？<br>执行此操作的目的是把原文章的排序都加1，使最新的记录排在最前面。');
	            break;
	            
	    }
	}
	// 同步文章数据
	function syncData() {
        var gm = $("#gridBox").ligerGetGridManager();
        var url="${rootPath}/cms/sync/article";
        $.reqUrlEx(url, null, function(rst) {
            if (rst && rst.state)gm.loadData(true);
        }, '确定同步数据？');

    }
	function showAuditMemo() {
		DVSubDialog = $.ligerDialog.open({ title:'审核意见', target: $("#AuditMemoDiv"), width:800, height:500 });
		return false;
	}
	
	function setThumbFile(id) {
		editRow("设置缩略图", "${rootPath}/cms/toSetArticleThumb?id=" + id, 1000, 700);
	}
	
	$(function(){
		if ('${user_site_id}' != '') {
			funcGetArticleColumnBySiteId("${user_site_id}", 'column_id', "${info.column_id}");
		} else {
			funcGetArticleColumnBySiteId("${info.site_id}", 'column_id', "${info.column_id}");
		}
	});

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
