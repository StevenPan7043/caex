<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>
	<div id="searchDiv" class="searchDiv">

		<div class="searchField">
			<div class="searchFieldLbl">项目币种</div>
			<div class="searchFieldCtr">
				<input id="currency" name="currency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">项目状态</div>
			<div class="searchFieldCtr">
				<select id="o_type" name="status" class="sel">
					<option value="">--请选择--</option>
					<option value="0">预热中</option>
					<option value="1">进行中</option>
					<option value="2">已结束</option>
				</select>
			</div>
		</div>

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
			{display:'项目名称',name:'name',width:120},
			{display:'项目币种',name:'currency',width:80},
			{display:'计价货币',name:'quote_currency',width:80},
			{display:'抢购价格',name:'rush_price',width:120},
			{display:'库存',name:'remain_amount',width:80},
			{display:'购买上限',name:'buy_upper_limit',width:100},
			{display:'购买下限',name:'buy_lower_limit',width:100},
			{display:'购买人数',name:'buy_person_count',width:80},
			{display:'发行数量',name:'release_amount',width:220},
			{display:'开始抢购',name:'rush_begin_time',width:125},
            {display:'结束抢购',name:'rush_end_time',width:125},
            {display:'是否锁仓',name:'is_lock',width:80,render: function (r,n,v) {
                return v == false ? '否' : '是';
            }},
			{display:'是否显示',name:'is_show',width:80,render: function (r,n,v) {
				return v == false ? '否' : '是';
            }},
            {display:'缩略图',name:'img_url',width:180, render:
                function(row){
                    var str = row.img_url;
                    if (str == "" || str==undefined) {
                        return "<div style='height:80px'>&nbsp;</div>";
                    }
                    return "<img src='" + str + "' height='100' width = '280'/>";
                }
            },
            {display:'白皮书',name:'white_book',width:180, render:
                function(row){
                    var str = row.white_book;
                    if (str == "" || str==undefined) {
                        return "<div style='height:80px'>&nbsp;</div>";
                    }
                    return "<a href='" + str + "' target='_blank' width = '280'>查看</a>";
                }
            }
		],
		url: "/crowd/mgr/list",
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
                openAddWindow("添加", "/crowd/mgr/edit_page", 1000, 700);
                break;

            case "编辑":
                f_common_edit($("#gridBox"),"/crowd/mgr/edit_page?id={id}", false,1000, 700);
                break;

            case "删除":
                f_common_del($("#gridBox"), "/crowd/mgr/del?id={id}", false, "删除");
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