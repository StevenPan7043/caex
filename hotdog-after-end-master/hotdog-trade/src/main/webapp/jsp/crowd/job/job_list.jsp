<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/jsp/topForList.jsp"%>


<%--<div id="searchDiv" class="searchDiv">--%>
	<%--<input type="hidden" name="a_status" id="a_status" value="21" />--%>

	<%--<div class="searchField">--%>
		<%--<div class="searchFieldCtr">--%>
			<%--<select id="r_status" name="r_status" class="sel">--%>
				<%--<option value="">--状态--</option>--%>
				<%--<option value="0">未开始</option>--%>
				<%--<option value="1">运行中</option>--%>
				<%--<option value="2">暂停</option>--%>
				<%--<option value="3">已结束</option>--%>
			<%--</select>--%>
		<%--</div>--%>
	<%--</div>--%>
	<%--<input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />--%>
<%--</div>--%>

<div class="main_content">
	<div id="gridBox" style="margin:0; padding:0"></div>
</div>


<div id="AuditMemoDiv" class="hide" style="-moz-user-select:none;">
	<div class="div_block" id="AuditMemoDivInner">
	</div>
</div>
<!-- 隐藏功能区 结束 -->


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
                {display:'任务名称',name:'job_name',width:220},
                {display:'任务组',name:'job_group',width:120},
                {display:'类名',name:'job_class_name',width:350},
                {display:'单笔购买数量',name:'order_volume',width:80},
                {display:'下单次数',name:'order_num',width:80},
                {display:'每次单数',name:'sec_num',width:80},

//                {display:'触发器名称',name:'trigger_name',width:120},
//                {display:'触发器组',name:'trigger_group',width:180},
                {display:'corn表达式',name:'cron_expression',width:120},
                {display:'创建时间',name:'create_time',width:120},
                {display:'任务状态',name:'status',width:120,render:
                    function(row){
                        switch (row.status) {
                            case 0 : return "<p style='color: gray'>未开始</p>";break;
                            case 1 : return "<p style='color: green'>进行中</p>";break;
                            case 2 : return "<p style='color: blue'>暂停</p>";break;
                            case 3 : return "<p style='color: red'>已结束</p>";break;
                        }
                    }
                },
                {display:'操作',name:'id',width:120,render:
                    function(row){
                        var content = "";
                        if(row.status == 0) {
                            content += "<a href='javascript:void(0);' onclick='javascript:start(" + row.id + ")'>激活</a>&nbsp;&nbsp;"
                        }else if(row.status == 1) {
                            content += "<a href='javascript:void(0);' onclick='javascript:pause(" + row.id + ")'>暂停</a>&nbsp;&nbsp;"
                        }else if(row.status == 2) {
                            content += "<a href='javascript:void(0);' onclick='javascript:resume(" + row.id + ")'>开始</a>&nbsp;&nbsp;"
                        }
                        return content;
                    }
                },
            ],
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
            delayLoad: false,
            rownumbers:true,
            url: "${rootPath}/crowd/job/list",
            method: "get",
            sortName: 'id',
            sortOrder: 'asc',
            checkbox: true,
            isSort: false,
            toolbar:{  items: eval(itemsStr)  }
        });
		

	});


	function dicOper(item){
		var gm = $("#gridBox").ligerGetGridManager();
	    var row = gm.getSelected();
	        
		switch (item.text) {
	        case "添加":
	            openAddWindow("添加", "${rootPath}/crowd/job/edit_page", 1000, 700);
            break;

            case "编辑":
                f_common_edit($("#gridBox"),"${rootPath}/crowd/job/edit_page?id={id}", false,1000, 700);
                break;

            case "删除":
                f_common_del($("#gridBox"), "${rootPath}/crowd/job/delete?id={id}", false, "删除");
		         break;
	    }
	}
	
	function start(id) {
        // status=1 运行中
        $.get("${rootPath}/crowd/job/start?id="+id,function (res) {
			if (res.state == 1) {
                $("#gridBox").ligerGetGridManager().loadData(true);
            }else {
                layer.alert(res.msg);
			}
            layer.closeAll();
        })
    }
    function pause(id) {
        // status=2 暂停
        $.get("${rootPath}/crowd/job/pause?id="+id,function (res) {
            var manager;
            if (res.state == 1) {
                $("#gridBox").ligerGetGridManager().loadData(true);
            }else {
                layer.alert(res.msg);
            }
            layer.closeAll()
        })
    }
    function resume(id) {
        // status=2 暂停
        $.get("${rootPath}/crowd/job/resume?id="+id,function (res) {
            var manager;
            if (res.state == 1) {
                $("#gridBox").ligerGetGridManager().loadData(true);
            }else {
                layer.alert(res.msg);
            }
            layer.closeAll()
        })
    }
	
</script>
