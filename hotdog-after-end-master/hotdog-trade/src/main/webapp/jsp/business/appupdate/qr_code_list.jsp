<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
<div id="searchDiv" class="searchDiv">
	<input id="btnOK" type="button" value="查询" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" />
	<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
</div>

<div class="main_content" style="width: 100%; ">
	<div id="gridBox"></div>
</div>


<script type="text/javascript">
	var itemsStr = "[ "
			+ "{ text: '添加', click: dicOper, icon: 'add'},"
			+ "{ text: '编辑', click: dicOper, icon: 'modify'},"
			+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	var grid,selectDialog,firstLoad = true;
	$(function(){
		menu = $.ligerMenu({ width: 120, items: eval(itemsStr)});
		grid  = $("#gridBox").ligerGrid({
			columns: [
				{display:'客服信息',name:'service_info',width:120},
				{display:'图片地址',name:'img_url',width:700},
				{display:'是否显示',name:'is_show',width:80,render: function (r,n,v) {
						return v == false ? '否' : '是';
					}},
				{display:'缩略图',name:'img_url',width:180, render:
							function(row){
								var str = row.img_url;
								if (str == "" || str==undefined) {
									return "<div style='height:80px'>&nbsp;</div>";
								}
								return "<img src='" + str + "' height='140' width = '140'/>";
							}
				},
			],
			url: "${rootPath}/aub/qr/list",
			method: "get",
			sortName: 'id',
			sortOrder: 'desc',
			rownumbers:true,
			delayLoad: true,
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
			toolbar:{  items: eval(itemsStr)  }
		});
		$(".wanwu_search").click();


	});
	function dicOper(item){
		var gm = $("#gridBox").ligerGetGridManager();
		var row = gm.getSelected();

		switch (item.text) {
			case "添加":
				openAddWindow("添加", "${rootPath}/aub/qr/edit_page", 800, 500);
				break;

			case "编辑":
				f_common_edit($("#gridBox"),"${rootPath}/aub/qr/edit_page?id={id}", false,800, 500);
				break;

			case "删除":
				f_common_del($("#gridBox"), "${rootPath}/aub/qr/del?id={id}", false, "删除");
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

<%@ include file="../../bottom.jsp"%>