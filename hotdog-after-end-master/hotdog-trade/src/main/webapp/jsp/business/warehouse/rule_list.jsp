<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>

	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">币种</div>
			<div class="searchFieldCtr">
				<input  id="currency" name="currency" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">释放类型</div>
			<div class="searchFieldCtr">
				<input  id="ruleType" name="ruleType" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldCtr">
				<select id="enable" name="enable" class="sel">
					<%--<option value="">--是否启用--</option>--%>
					<option value="0" selected>已启用</option>
					<option value="1">未启用</option>
				</select>
			</div>
		</div>
	    <input id="btnOK" type="button" class="btn_a wanwu_search ope_refresh" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
	<div class="main_content">
		<div id="gridBox" style="margin:0; padding:0"></div>
	</div>					
						

<script type="text/javascript">
	var grid;
    var itemsStr = "[ { text: '增加', click: dicOper, icon: 'add'},"
			+ "{ text: '数据同步', click: dicOper, icon: 'refresh'}"
			+ "]";
	$(function () {
		menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
		grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'币种',name:'currency',width:120},
				{display:'入仓比例',name:'ruleReleaseScale',width:120,render:function(r,n,v){
					if (r.tradeLock == 0) return '100%';
					return v/0.01+"%";}},
                {display: '规则类型', name: 'ruleType', width: 100,render:function(r,n,v){return v=='1'&&'自动锁仓释放'||v=='2'&&'交易锁仓'||v}},
                {display: '锁仓释放间隔时长', name: 'lockReleaseTime', width: 140},
                {display: '释放单位时间', name: 'ruleTimeType', width: 100,render:function(r,n,v){return v=='MIN'&&'分钟'||v=='HOUR'&&'小时'||v=='DAY'&&'天'||v=='MONTH'&&'月'||v}},
                {display:'释放比例',name:'ruleDetail',width:140,render:function(r,n,v){return v+"%";}},
				{display:'是否启用',name:'enable',width:140,render:function(r,n,v){return v=='0'&&'已启用'||v=='1'&&'未启用';}},
				{
                    display: '操作',
                    name: '',
                    width: 100,
                    render:
                        function (r, n, v) {
                             if (r.enable == 0) {
                                return "<a href='javascript:void(0);' onclick='charRecord("
                                    + r.id + "," + 1 + "," + r.tradeLock + ")' ><div>禁用</div></a>";
                            } else {
                                return "<a href='javascript:void(0);' onclick='charRecord("
                                    + r.id + "," + 0 + "," + r.tradeLock + ")' ><div>启用</div></a>";
                            }
                        }
                }
			],
			url: "${rootPath}/a/getRuleList",
			method: "get",
			rownumbers: true,
			onContextmenu : function (parm,e)
	        {
	            menu.show({ top: e.pageY, left: e.pageX });
	            return false;
	        },
	        // onDblClickRow : function (data, rowindex, rowobj)
			// {
			// 	var item = {text: '修改'};
			// 	dicOper(item);
			// },
			toolbar:{  items: eval(itemsStr)  },
			sortName: 'id',
		   	sortOrder: 'asc',
		   	isSort: false,
		   	autoRefresh:false
		});
	});
  
	function dicOper(item){
		switch (item.text) {
			case "增加":
				openAddWindow("增加", "${rootPath}/a/toAddRule", 500, 500);
				break;
			// case "修改":
		    //      f_common_edit($("#gridBox"), "/a/toEditRule?id={id}", false, 750, 320);
		    //     break;
			case "数据同步":
				syncData();
				break;
	    }
	}


	function syncData() {
		var gm = $("#gridBox").ligerGetGridManager();
		var url="${rootPath}/currency/sync/currency-rule";
		$.reqUrlEx(url, null, function(rst) {
			if (rst && rst.state)gm.loadData(true);
		}, '确定同步数据？');

	}

	function setUserRight(userId){
		openAddWindow("设置权限", '${rootPath}/frm/loadUserFuncs?id='+userId, 980, 550);
	}
	
	function reUserPassword(userid) {
		$.ligerDialog.confirm('确定初始化密码吗？', function (yes) {
			if(yes) {
				$.reqUrl("${rootPath}/frm/resetUserPassword?id="+userid, null, function(rst){
					if (rst && rst.state){$.ligerDialog.success('初始化密码成功！');};
				});
			}
		});
	}
	
	var currDicId = '';
	var currType = "multiRole";
	var id="";
	function showRoleWindow (vid,roleIds) {
		id = vid;
		openSelectDialog("角色授权", currType, roleIds, 400, 500, true,"0");
	}
	function f_selectCommonOK(item, dialog) {
		var fn = dialog.frame.f_select || dialog.frame.window.f_select; 
	    var data = fn();
	    if (!data) {
	        alert('请选择行!');
	        return;
	    }
	   	var roleIds = "";
	   	for(var s in data) {
	   		var row = data[s];
	   		roleIds += row.id+",";
	   	}
	   	$.reqUrl("${rootPath}/frm/updateUser?id=" + id +"&roleIds="+roleIds, null, function(rst) {
			if (rst && rst.state){
				$.ligerDialog.success('授权成功');
				$grid.load("gridBox")
				dialog.close();
			}
		});
	   	
	}


    //启用/禁用
    function charRecord(id, enableType,tradeLock) {
        var enable = "";
        if (enableType == 0) {
            enable = "启用";
        } else {
            enable = "禁用";
        }
        $util.sure("您确定要「" + enable + "」该币种规则么？", function () {
            $.submitShowWaiting();
            var url = '${rootPath}/a/ruleEnable';
            $.ajax({
                method: 'get',
                url: url,
                dataType: "json",
                contentType: "application/json",
                data: {
                    "id": id, "enable": enableType,"tradeLock":tradeLock
                },
                success: function (res) {
                    $.submitShowResult(res);
                    try {
                        $hook.opeRefresh("ope_refresh", "gridBox", "searchDiv");
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
<%@ include file="../../bottom.jsp"%>
