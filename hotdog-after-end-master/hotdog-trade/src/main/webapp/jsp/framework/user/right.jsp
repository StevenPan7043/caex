<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="/jsp/topForForm.jsp"%>
<style>

.l-tab-links li a {
	margin-right:2px;
}
.dl_area_a1 dd {
    padding: 0 0 0 18px;
    width: 105%;
    line-height: 26px;
}

.dl_area_a1 dd span {
	float:none;
    height: 22px;
    line-height: 22px;
    margin-right: 10px;
   	width: 150px;
    display:inline-block;
}

.dl_area_a1 .chk {
    margin: 0 4px 0 0;
    vertical-align: middle;
}
input, textarea, select {
    background: none repeat scroll 0 0 #fff;
    font-family: inherit;
    font-size: 1em;
    font-weight: inherit;
    margin: 0;
    padding: 0;
}


.grid_opbox {
    background: none repeat scroll 0 0 #eee;
    border-bottom: 1px solid #ddd;
    height: 24px;
    line-height: 24px;
    padding: 0 5px;
}


.grid_opbox a.a_gridOp {
    background: url("/resources/images/opbg.png") no-repeat scroll 0 -100px rgba(0, 0, 0, 0);
    cursor: pointer;
    float: left;
    height: 20px;
    line-height: 20px;
    margin: 2px 2px 0;
    padding: 0 0 0 5px;
}
.grid_opbox a {
    color: #1761b3;
    margin: 0 10px 0 0;
    padding: 2px 6px 2px 20px;
}

.grid_opbox a.a_gridOp span {
    background: url("/resources/images/opbg.png") no-repeat scroll 100% -100px rgba(0, 0, 0, 0);
    display: inline-block;
    height: 20px;
    padding: 0 5px 0 0;
}

.grid_opbox a.a_gridSave em {
    background-position: 0 -1049px;
}
.grid_opbox a.a_gridOp em {
    background: url("/resources/images/icon_op.gif") no-repeat scroll 0 1px rgba(0, 0, 0, 0);
    padding-left: 15px;
}
</style>
<script type="text/javascript">

$(function (){
	$("li").click(function(){
		$("li").attr("class","");
		$(".l-tab-content-item").hide();
		$(this).attr("class","l-selected");
		var divId = $(this).attr("id");
		$("#div"+divId).css("display","block");
		
	});
	$(".fn_chk_p").click(function(){
		var ischecked = $(this).prop("checked");
		if(ischecked){
			$(this).parents("dl").find(".fn_chk_c").attr("checked","checked");
			$(this).parents("dz").find(".fn_chk_gp").attr("checked","checked");
		}else{
			$(this).parents("dl").find(".fn_chk_c").removeAttr("checked");
			
			var p_hasCheck = false;
			$(this).parents("dz").find(".fn_chk_p").each(function(){
				if($(this).attr("checked") == "checked"){
					p_hasCheck = true;
					return false;
				}
			});
			if(!p_hasCheck){
				$(this).parents("dz").find(".fn_chk_gp").removeAttr("checked");
			}
		}
		
	
	});
	$(".fn_chk_c").click(function(){
		var ischecked = $(this).prop("checked");
		if(ischecked){
			$(this).parents("dl").find(".fn_chk_p").attr("checked","checked");
			$(this).parents("dz").find(".fn_chk_gp").attr("checked","checked");
		}
	});
	$(".a_gridOther_7").click(function(){
		$(".l-tab-content-item").each(function(){
			if($(this).css("display") == 'block'){
				$(this).find("input[type='checkbox']").attr("checked","checked");
				$(this).parents("dz").find(".fn_chk_gp").attr("checked","checked");
			}
		});
	});
	$(".a_gridOther_20").click(function(){
		$(".l-tab-content-item").each(function(){
			if($(this).css("display") == 'block'){
				$(this).find("input[type='checkbox']").removeAttr("checked");
				$(this).parents("dz").find(".fn_chk_gp").removeAttr("checked");
			}
		});
	});
	
	$("#sure").click(function(){
		var data='';
		$("input:checkbox[name=^'chk_']:checked").each(function(){
			data += $(this).val() +",";
		});
		var objectId = $("#objectId").val();
		var operatorType =$("#operatorType").val();
		if("" != data){
			data = data.substring(0,data.length-1);
		}
		$.reqUrlEx("${rootPath}/frm/saveRight",{objectId:objectId,operatorType:operatorType,funcIds:data});
	});
});
var selectDialog;
$(function(){
	$("#showRoleUser").click(function(){
		var roleId = $(this).attr("data_");
		selectDialog = $.ligerDialog.open({ title: "用户明细", name:'showUserList',width: "650", height: "430", url: '${rootPath}/getUserOfRole?roleId='+roleId});
		
	});
});
function showVal(){
	selectDialog.close();
}
</script>
 

	<div class="grid_opbox">
		<a id="sure" class="a_gridOp a_gridSave" href="javascript:void(0);"><span><em>保存</em></span></a>
		<a id="all" class="a_gridOp a_gridOther_7" href="javascript:void(0);"><span><em>全选</em></span></a>
		<a id="notall" class="a_gridOp a_gridOther_20" href="javascript:void(0);"><span><em>取消全选</em></span></a>
		&nbsp;&nbsp;&nbsp;
		<c:choose>
			<c:when test="${operatorType == 'role'}">
				<label>当前角色：<span id="showRole" style="font-weight:bold">${info.role_name }</span></label>
				&nbsp;&nbsp;&nbsp;
				<input type="button" class="showTipssss" id="showRoleUser" data_="${info.id }" value="查看角色下的用户">
			</c:when>
			<c:otherwise>
			
			</c:otherwise>
		</c:choose>
	 
	</div>
	<input type="hidden" id="operatorType" name="operatorType" value="${operatorType }">	
	<input type="hidden" id="objectId" name="objectId" value="${objectId }">		
	<div id="tab1" style="width: 99%;overflow:hidden; border:1px solid #A3C0E8; " class="l-tab" ligeruiid="tab1">
		<div style="width:980px ;overflow:scroll;-webkit-overflow-scrolling : touch;height: 44px"class="l-tab-links">
			<ul style="left: 0px; width: 1400px">
				<c:forEach items="${functions}" var="func" varStatus="i">
					<c:if test="${func.fuc_parent_id == '0'}">
						<li style="width:70px;" tabid="tabitem${i.index }" id="tabitem${i.index }" <c:if test="${func.id=='XTGL' }" >class="l-selected"</c:if> >
							<a>${func.fuc_name }</a>
							<div class="l-tab-links-item-left"></div>
							<div class="l-tab-links-item-right"></div>
						</li>
					</c:if>
				</c:forEach>
			</ul>
				<div class="l-tab-switch">
				</div>
		</div>
					
		<div class="l-tab-content">
			<c:forEach items="${functions}" var="func" varStatus="i">
				<c:if test="${func.fuc_parent_id == '0'}">
					<dz class="dd_area_a1"> 
					<input type="checkbox" class="fn_chk_gp"  <c:if test="${func.haveright == 1}">checked</c:if>  style="display:none;" value="${func.id }" />
					<div title="" id="divtabitem${i.index }"  <c:if test="${func.id !='XTGL' }" >style="display:none;"</c:if> tabid="tabitem${i.index }" class="l-tab-content-item">
						<c:forEach items="${functions}" var="subfunc">
							<c:if test="${func.id == subfunc.fuc_parent_id}">
							 
								<dl class="dl_area_a1">
									<dt>
										<span>
											<input type="checkbox" id="${subfunc.id}" <c:choose> <c:when test="${subfunc.is_need_auth==0 }">checked disabled</c:when> <c:otherwise> <c:if test="${subfunc.haveright == 1}">checked</c:if> </c:otherwise> </c:choose> class="chk fn_chk fn_chk_p" value="${subfunc.id}" name="chk_${subfunc.id}"/>
											<label for="${subfunc.id}" class="showTips" _data="${subfunc.fuc_desc}">${subfunc.fuc_name}</label> 
										</span>
									</dt>
									<dd>
										<c:forEach items="${functions}" var="ssubv">
											<c:if test="${subfunc.id == ssubv.fuc_parent_id}">
												<span>
													<input type="checkbox" id="${ssubv.id }" <c:if test="${ssubv.haveright == 1}">checked</c:if> class="chk fn_chk fn_chk_c" value="${ssubv.id}" name="chk_${ssubv.id}"/>
													<label for="${ssubv.id}" class="showTips" _data="${ssubv.fuc_desc}">${ssubv.fuc_name}</label>
												</span>
											</c:if>
										</c:forEach>
									</dd>
								</dl>
									 
						</c:if>
						</c:forEach>	
					</div>
					</dz>
				</c:if>
			</c:forEach>
		</div>
	</div>		

<br><br><br><br><br><br>