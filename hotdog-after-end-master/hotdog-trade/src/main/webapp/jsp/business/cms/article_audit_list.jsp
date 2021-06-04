<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
		<input type="hidden" name="a_status" id="a_status" value="21" />
		<%
			//管理员可以选择站点，非管理员默认管理自己的站点。
		%>
		<c:choose>
			<c:when test="${user_site_id == '' || user_site_id == null}">
				<div class="searchField">
					<div class="searchFieldLbl">所属站点</div>
					<div class="searchFieldCtr">
						<select name="site_id" id="site_id" class="sel ww_select" data="{code:'site|id'}"><option value="">请选择...</option></select>
					</div>
				</div>
			</c:when>
			<c:otherwise>
				<input type="hidden" name="site_id" id="site_id" value="${user_site_id}" />
			</c:otherwise>
		</c:choose>
		<div class="searchField">
			<div class="searchFieldLbl">标题关键词</div>
			<div class="searchFieldCtr">
				<input type="text" name="a_title" id="a_title" class="txt enterAsSearch" />
			</div>
		</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
    <input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
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
	+ "{ text: '预览', click: dicOper, icon: 'search'},"
	+ "{ text: '审核通过', click: dicOper, icon: 'ok'},"
	+ "{ text: '审核不通过', click: dicOper, icon: 'busy'}]";
	$(function () {
		  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
	      grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'所属栏目',name:'column_name',width:150},	
				{display:'类型',name:'a_content_type',width:60,render:function(r,n,v){return v=='1'&&'链接'||v=='0'&&'内容';}},
				{display:'文章标题',name:'a_title',width:300},
				{display:'发布时间',name:'a_time',width:100},
				{display:'排序',name:'a_order',width:60},
				{display:'缩略图',name:'a_img_file',width:110, render:
					function(row){
						var str = row.a_img_file;
						if (str == "" || str==undefined) {
							return "<div style='height:80px'>&nbsp;</div>";
						}
						return "<img src='" + str + "' height='80' width = '110'/>";  
					}
				},
				{display:'创建人',name:'creator_name',width:100},
				{display:'创建时间',name:'create_time',width:130},
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
				var item = {text: '审核通过'};
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
	        case "预览":
	        	if (row.a_content_type == 0) {
	        		window.open("${rootPath}/noau_cms/viewArticle?id=" + row.id);
	        	} else {
	        		window.open(row.a_content);
	        	}
	            break; 
		
	        case "审核通过":
	        	f_common_del($("#gridBox"), "${rootPath}/cms/auditOkArticle?id={id}", false, "审核通过");
	            break;  
	            
	        case "审核不通过":
		         if(row==null || row.length==0){
		             layer.alert("请选择您要操作的记录!");
		             break;
		         }
		         $.ligerDialog.prompt('请输入审核不通过原因',true, function (yes,value) { 
		             if(yes) {
		                 var params = {};
		                 params["audit_comments"] = value;
		                 params["id"] = row.id;
		                 $.reqUrl("${rootPath}/cms/auditNoArticle", params, function(rst){
		                     if(rst && rst.state) {
		                         $grid.load("gridBox");
		                     }
		                 });                 
		             }
		         });
		         
		         break;
	    }
	}
	
	function showAuditMemo() {
		DVSubDialog = $.ligerDialog.open({ title:'审核意见', target: $("#AuditMemoDiv"), width:800, height:500 });
		return false;
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
