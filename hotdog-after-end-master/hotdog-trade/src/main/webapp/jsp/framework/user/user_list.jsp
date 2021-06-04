<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="../../topForList.jsp"%>

	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">用户名</div>
			<div class="searchFieldCtr">
				<input  id="user_name" name="user_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
		<div class="searchField">
			<div class="searchFieldLbl">姓名</div>
			<div class="searchFieldCtr">
				<input  id="user_real_name" name="user_real_name" type="text" class="txt enterAsSearch" />
			</div>
		</div>
	    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'gridBox',scope:'searchDiv'}" value="查询" />
		<input type="button" class="btn_a" onclick="btnClearInput()" value="重置" />
	</div>
	<div class="main_content">
		<div id="gridBox" style="margin:0; padding:0"></div>
	</div>					
						

<script type="text/javascript">
	var grid;
	var itemsStr = "[ "
		+ "{ text: '增加', click: dicOper, icon: 'add'},"
		+ "{ text: '修改', click: dicOper, icon: 'modify'},"
		+ "{ text: '删除', click: dicOper, icon: 'delete'}]";
	$(function () {
		menu = $.ligerMenu({ width: 120, items: eval(itemsStr)}); 
		grid = $("#gridBox").ligerGrid({
			columns: [
				{display:'用户名',name:'user_name',width:120},
				{display:'姓名',name:'user_real_name',width:200},	
				{display:'启用状态',name:'is_can_login',width:100,render:function(r,n,v){return v=='1'&&'已启用'||v=='0'&&'未启用';}},	
				{display:'OTC广告主',name:'o_name',width:140},
				{display:'初始化密码',width:90,render:
					function(row){
						var html = '<a href=\'javascript:void(0);\' onclick="reUserPassword(\''+row.id+'\');">初始化密码</a>';
						return html;
					}
				<bp:buttonAndColumnPermission functionId="XTGL-YHGL-CSHMM"/>},
				{display:'权限设置',width:90,render:
					function(row){
						var html = '<a href="javascript:void(0);" onclick="setUserRight(\''+row.id+'\');return false;">权限设置</a>';
						return html;
					}
				<bp:buttonAndColumnPermission functionId="XTGL-YHGL-QXSZ"/>}
			],
			url: "${rootPath}/frm/listUser",
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
				openAddWindow("增加", "${rootPath}/frm/toAddUser", 750, 320);
				break;
	        
			case "修改":
		         f_common_edit($("#gridBox"), "${rootPath}/frm/toEditUser?id={id}", false, 750, 320);
		        break;
		        
	        case "删除":
	        	f_common_del($("#gridBox"), "${rootPath}/frm/delUser?id={id}", false);
		        
	            break;
	       
	    }
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
