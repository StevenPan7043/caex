<%@ page contentType="text/html; charset=utf-8"%>
<%
	response.setHeader("Pragma","no-cache");
	response.setHeader("Cache-Control","no-cache");
	response.setDateHeader("Expires", 0);
%>
<%@ include file="/jsp/topForList.jsp"%>

    <script type="text/javascript">
    	var url = "/common/grid";
        var g${param.radomcode} = null;
        var code = '${param.code}';
        var param = '${param.param}';
        var needCheckBox = ${param.needCbx};
        var isNeedPage = true;
        if('${param.isNeedPage}' == '0'){
        	isNeedPage = false;
        }
        var initWhere = '${param.initWhere}';
        var pageSize = '${param.pageSize}';
        pageSize = (pageSize && pageSize != "undefined") ? pageSize : 10;
        
        var sortName="";
        var sortOrder="";
        var column;
        switch (code) {
			case "user":
	        	column = [
	                { display: '用户ID', name: 'id', width: 60 },
	                { display: '用户名', name: 'user_name', width: 160},  
	                { display: '姓名', name: 'user_real_name', width: 160 }
	                ];
	        	break;
	        
	        case "FreqMemo":
	        	column = [
	                { display: '常用备注', name: 'freq_memo', width: 420 },
	                { display: '使用次数', name: 'times', width: 60}
	                ];
	        	break;
	        
	        case "multiUser":
	        	column = [
	                { display: '用户ID', name: 'id', width: 60 },
	                { display: '用户名', name: 'user_name', width: 160},  
	                { display: '姓名', name: 'user_real_name', width: 160 }
	                ];
	        	break;
	        
	        case "multiRole":
	        	column = [
	                { display: '角色ID', name: 'id', width: 60 },
	                { display: '角色名', name: 'role_name', width: 160}
	                ];
	        	break;
	        
	        case "material_no_name":
	        	column = [
					{ display: '物料类型',name:'m_type_name',width:100},
	                { display: '物料编号', name: 'm_no', width: 100 },
	                { display: '物料名称', name: 'm_name', width: 150 },
	                { display: '物料规格',name:'m_spec',width:150}
	                ];
	        	break;
	        
	        case "customer":
	        	column = [
	                {display:'客户编号',name:'c_no',width:60},
					{display:'客户名称',name:'c_name',width:300}
	                ];
	            sortName="id";
				sortOrder="desc";
	        	break;
	        
	        case "contract":
	        	column = [
	                { display: '合同编号', name: 'c_no', width: 60 },
	                { display: '合同名称', name: 'c_name', width: 300 }
	                ];
	        	break;
	        
	        case "pump":
	        	column = [
	                {display:'内部编号',name:'v_no',width:80},
					{display:'车辆类型',name:'v_type_name',width:120},
					{display:'车牌号',name:'plate_number',width:150}
	                ];
	        	break;
	        
	        case "project":
	        	column = [
	                {display:'工程编号',name:'p_no',width:120},
					{display:'工程名称',name:'p_name',width:120},
					{display:'客户名称',name:'c_name',width:120},
					{display:'工程运距',name:'p_distance',width:100},
					{display:'业务员',name:'salesmanName', width:120}
	                ];
	        	break;
				        
	        case "factoryCar":
	        	column = [
	                {display:'车牌号',name:'car_no',width:180},
	                {display:'司机',name:'driver_name',width:160},
	                {display:'电话',name:'driver_mobile',width:180}
	                ];
	        	break;
	        case "deliveryFleet":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	                {display:'车队名称',name:'fleet_name'}
	                ];
	        	break;
	        
	        case "slump":
	        	column = [
	                {display:'塌落度',name:'text'}
	                ];
	        	break;
			
			case "material":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	                {display:'原料名称',name:'m_name', width:220},
	                {display:'规格',name:'m_spec',width:120},
					{display:'产地',name:'made_in',width:120}
	                ];
	        	break;
	        	
			case "order":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	                {display:'订单编号',name:'o_no', width:150},
	                {display:'订单名称',name:'o_name',width:300}
	                ];
	        	break;
	        	
	        case "m_member":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	                {display:'账号',name:'m_name', width:220},
 	                {display:'UID',name:'id', width:140,render:function(r,n,v){return v}},
 	                {display:'虚拟币(查询条件是地址时)',name:'currency', width:200}
	                ];
	        	break;
	        	
	        case "storage":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	                {display:'仓库名称',name:'s_name', width:320}
	                ];
	        	break;
	        	
			case "frmDictionaryData":
	        	column = [
	        		{display:'id',name:'id',width:120,hide:true},
	                {display:'名称',name:'text', width:250}
	                ];
	        	break;
			case "recipe_b":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	                {display:'配方名称',name:'r_name', width:120},
	                {display:'站点匹配',name:'stationMatch', width:120,render:function(r,n,v){
					var s=(v||"").split("@");
					var rst=[];
					for(var i=0; i<s.length; i++){
						if(s[i]){
							var tem=s[i].split(",");
							if(tem[1].indexOf('match')>=0){
								rst.push(tem[0]+"<span class='span_pass'/>");
							}else{
								rst.push(tem[0]+"<span class='span_noPass a_tooltip' data-type='recipe'  data-id='"+tem[1]+"'>"+tem[0]+"</span>");	
							}
						}
					}
					return rst.join("&nbsp;");
				}},
	                {display:'强度等级',name:'r_grade', width:100},
	                {display:'容重',name:'rz', width:100},
	                {display:'抗渗度',name:'ksd', width:100},
	               ];
	        	break;
			case "supplier":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	        		{display:'编号',name:'s_no', width:120},
	        		{display:'名称',name:'s_name', width:220} 
	                ];
	        	break;	 
			case "factory":
	        	column = [
	        		{display:'id',name:'id', hide: true},
	        		{display:'编号',name:'factory_no', width:120},
	        		{display:'名称',name:'factory_name', width:220} 
	                ];
	        	break;	
	        case "dept":
	        	column=[
					{display:'部门名称',name:'dept_name',align:'left',width:200,isSort:false},
					{display:'部门编号',name:'dept_no',width:80,align:'left',isSort:false},
					{display:'上级部门',name:'pname',width:200,align:'left',isSort:false}
				];  
				break;  
			case "unloadway":
	        	column=[
					{display:'id',name:'id', hide: true},
					{display:'浇筑方式',name:'w_name',width:130,align:'left',isSort:false},
					{display:'浇筑类型',name:'w_type',width:130,align:'left',isSort:false}
				];  
				break;   
			case "financeAccount":
	        	column=[
					{display:'id',name:'id', hide: true},
					{display:'账户类型',name:'account_type_name',width:100,align:'left',isSort:false},
					{display:'账户名',name:'account_username',width:130,align:'left',isSort:false},
					{display:'账号',name:'account',width:150,align:'left',isSort:false},
					{display:'开户行',name:'account_bank',width:150,align:'left',isSort:false}
				];  
				break;
			case "product":
	        	column=[
					{display:'id',name:'id', hide: true},
					{display:'强度等级',name:'p_grade',width:100},
					{display:'产品名称',name:'p_name',width:250}
				];  
				sortName="p_order";
				sortOrder="asc";
				break;
			case "autoPartSupplier":
	        	column=[
					{display:'供应商',name:'supplier_name',width:240} 
				];  
				break;
			case "part_name":
	        	column=[
					{display:'id',name:'id', hide: true},
					{display:'配件名称',name:'part_name',width:120,align:'left',isSort:false},
					{display:'配件规格',name:'part_standard',width:110,align:'left',isSort:false},
					{display:'供应商',name:'supplier_name',width:120,align:'left',isSort:false},
					{display:'part_type',name:'part_type', hide: true},
				];  
				break;
				
	        default:
	        	alert("参数错误");
	     }
        
        //alert(code);
        $(function () {
        	if(code=='dept'){
        		g${param.radomcode} = $("#maingrid4${param.radomcode}").ligerGrid({
	           		parms: {code:code, param:param, initWhere:initWhere},
	           		checkbox: needCheckBox,
	                columns: column,
	                pageSize:pageSize,
	                url: url,
	                tree: { columnName: 'dept_name' },
	                width: '100%',height:'100%',
	                onDblClickRow : function (data, rowindex, rowobj)
	                {
	                	if(!needCheckBox) {
	                    	parent.f_selectCommonOK${param.SuffixCode}(data, code);
	                    }
	                     
	                },
	                usePager:isNeedPage,
	                isChecked: f_isChecked
            	});
        	}else{
        		g${param.radomcode} = $("#maingrid4${param.radomcode}").ligerGrid({
	           		parms: {code:code, param:param, initWhere:initWhere},
	           		checkbox: needCheckBox,
	                columns: column,
	                method: "get",
	                pageSize:pageSize,
	                url: url,
	                width: '100%',height:'100%',
	                onDblClickRow : function (data, rowindex, rowobj)
	                {
	                	if(!needCheckBox) {
	                    	parent.f_selectCommonOK${param.SuffixCode}(data, code);
	                    }
	                     
	                },
	                isChecked: f_isChecked,
	                isSort: true,
	                sortName: sortName,
	   				sortOrder: sortOrder,
	   				usePager:isNeedPage
	            });
        	}
           
            $("#pageloading").hide();
        });
        
        function f_isChecked(rowdata) {
        	if(rowdata.checked == true)
                return true;
            return false;
        }
        
        
        function f_select(){
        	//alert(g${param.radomcode}.getSelectedRows() + "--" + JSON.stringify(g${param.radomcode}.getSelectedRows()));
            return g${param.radomcode}.getSelectedRows();
        }
    </script>
    
<body style="overflow:hidden;">   
 <c:if test="${param.code != 'dept'}">
 	<div id="searchDiv" class="searchDiv">
		<div class="searchField">
			<div class="searchFieldLbl">关键词</div>
			<div class="searchFieldCtr">
				<input id="text" name="text" type="text" class="txt enterAsSearch" style="width: 350px;" />
		    <input id="code" name="code" type="hidden" value="${param.code}" />
		    <input id="param" name="param" type="hidden" value="${param.param}" />
			</div>
		</div>
		    <input id="btnOK" type="button" class="btn_a wanwu_search" data="{grid:'maingrid4${param.radomcode}',scope:'searchDiv'}" value="查询" />
	   
	</div>
	 </c:if>
    <div id="maingrid4${param.radomcode}" style="margin:0; padding:0;"></div> 
    
    <div style="display:none;"></div>
</body>
</html> 