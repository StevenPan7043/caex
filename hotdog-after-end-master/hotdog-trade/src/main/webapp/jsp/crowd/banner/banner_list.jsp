<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>

<div id="searchDiv" class="searchDiv">
		<input type="hidden" name="a_status" id="a_status" value="21" />

		<div class="searchField">
			<div class="searchFieldLbl">场景</div>
			<div class="searchFieldCtr">
				<input type="text" name="scene" id="scene" class="txt enterAsSearch" />
			</div>
		</div>
    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>

<div class="main_content">
	<div id="gridBox" style="margin:0; padding:0"></div>
</div>


	<div id="AuditMemoDiv" class="hide" style="-moz-user-select:none;">
		<div class="div_block" id="AuditMemoDivInner">
		</div>
	</div>
	<!-- 隐藏功能区 结束 -->

<script type="text/javascript">
	var grid;
	var itemsStr = "[ "
	+ "{ text: '添加', click: dicOper, icon: 'add'},"
	+ "{ text: '编辑', click: dicOper, icon: 'modify'},"
	+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	$(function () {
		  menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
	      grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'场景',name:'scene',width:120},
				{display:'名称',name:'name',width:120},
				{display:'排序',name:'banner_order',width:50},
				{display:'更新时间',name:'update_time',width:120},
				{display:'缩略图',name:'banner_url',width:180, render:
					function(row){
						var str = row.banner_url;
						if (str == "" || str==undefined) {
							return "<div style='height:80px'>&nbsp;</div>";
						}
						return "<img src='" + str + "' height='100' width = '280'/>";
					}
				}
			],
			sortName: 'id',
		   	sortOrder: 'desc',
			url: "/img/mgr/banners/list",
			method: "get",
			rownumbers: true,
			onContextmenu : function (parm,e)
	        {
	            menu.show({ top: e.pageY, left: e.pageX });
	            return false;
	        },
	        onDblClickRow : function (data, rowindex, rowobj)
			{
				var item = {text: '编辑'};
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
	        case "添加":
            openAddWindow("添加", "/img/mgr/banner/edit_page", 1000, 700);
            break;

            case "编辑":
                f_common_edit($("#gridBox"),"/img/mgr/banner/edit_page?id={id}", false,1000, 700);
                break;

            case "删除":
                f_common_del($("#gridBox"), "/img/mgr/banner/del?id={id}", false, "删除");
		         break;
	    }
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
