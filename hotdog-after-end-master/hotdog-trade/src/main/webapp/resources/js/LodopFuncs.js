var CreatedOKLodop7766=null;

function getLodop(oOBJECT,oEMBED){
/**************************
  本函数根据浏览器类型决定采用哪个页面元素作为Lodop对象：
  IE系列、IE内核系列的浏览器采用oOBJECT，
  其它浏览器(Firefox系列、Chrome系列、Opera系列、Safari系列等)采用oEMBED,
  如果页面没有相关对象元素，则新建一个或使用上次那个,避免重复生成。
  64位浏览器指向64位的安装程序install_lodop64.exe。
**************************/
        var strHtmInstall="<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='/jsp/download/install_lodop32.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
        var strHtmUpdate="<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='/jsp/download/install_lodop32.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
        var strHtm64_Install="<br><font color='#FF00FF'>打印控件未安装!点击这里<a href='/jsp/download/install_lodop64.exe' target='_self'>执行安装</a>,安装后请刷新页面或重新进入。</font>";
        var strHtm64_Update="<br><font color='#FF00FF'>打印控件需要升级!点击这里<a href='/jsp/download/install_lodop64.exe' target='_self'>执行升级</a>,升级后请重新进入。</font>";
        var strHtmFireFox="<br><br><font color='#FF00FF'>（注意：如曾安装过Lodop旧版附件npActiveXPLugin,请在【工具】->【附加组件】->【扩展】中先卸它）</font>";
        var strHtmChrome="<br><br><font color='#FF00FF'>(如果此前正常，仅因浏览器升级或重安装而出问题，需重新执行以上安装）</font>";
        var LODOP;		
	try{	
	     //=====判断浏览器类型:===============
	     var isIE	 = (navigator.userAgent.indexOf('MSIE')>=0) || (navigator.userAgent.indexOf('Trident')>=0);
	     var is64IE  = isIE && (navigator.userAgent.indexOf('x64')>=0);
	     //=====如果页面有Lodop就直接使用，没有则新建:==========
	     if (oOBJECT!=undefined || oEMBED!=undefined) { 
               	 if (isIE) 
	             LODOP=oOBJECT; 
	         else 
	             LODOP=oEMBED;
	     } else { 
		 if (CreatedOKLodop7766==null){
          	     LODOP=document.createElement("object"); 
		     LODOP.setAttribute("width",0); 
                     LODOP.setAttribute("height",0); 
		     LODOP.setAttribute("style","position:absolute;left:0px;top:-100px;width:0px;height:0px;");  		     
                     if (isIE) LODOP.setAttribute("classid","clsid:2105C259-1E0C-4534-8141-A753534CB4CA"); 
		     else LODOP.setAttribute("type","application/x-print-lodop");
		     document.documentElement.appendChild(LODOP); 
	             CreatedOKLodop7766=LODOP;		     
 	         } else 
                     LODOP=CreatedOKLodop7766;
	     };
	     //=====判断Lodop插件是否安装过，没有安装或版本过低就提示下载安装:==========
	     if ((LODOP==null)||(typeof(LODOP.VERSION)=="undefined")) {
	             if (navigator.userAgent.indexOf('Chrome')>=0)
	                 document.documentElement.innerHTML=strHtmChrome+document.documentElement.innerHTML;
	             if (navigator.userAgent.indexOf('Firefox')>=0)
	                 document.documentElement.innerHTML=strHtmFireFox+document.documentElement.innerHTML;
	             if (is64IE) document.write(strHtm64_Install); else
	             if (isIE)   document.write(strHtmInstall);    else
	                 document.documentElement.innerHTML=strHtmInstall+document.documentElement.innerHTML;
	             return LODOP;
	     } else 
	     if (LODOP.VERSION<"6.1.9.3") {
	             if (is64IE) document.write(strHtm64_Update); else
	             if (isIE) document.write(strHtmUpdate); else
	             document.documentElement.innerHTML=strHtmUpdate+document.documentElement.innerHTML;
	    	     return LODOP;
	     };
	     //=====如下空白位置适合调用统一功能(如注册码、语言选择等):====	     
		 LODOP.SET_LICENSES("万物工业软件有限公司", "662587775718688748719056235623","", "");
	     //============================================================	     
	     return LODOP; 
	} catch(err) {
	     if (is64IE)	
            document.documentElement.innerHTML="Error:"+strHtm64_Install+document.documentElement.innerHTML;else
            document.documentElement.innerHTML="Error:"+strHtmInstall+document.documentElement.innerHTML;
	     return LODOP; 
	};
}


var $printUtil = {
	// designName 表示frm_lodop_design表的design_name。
	// data 表示打印的数据表的id，或者打印的数据（如自定义打印）
	// isPrint 表示是打印还是预览，true表示打印，false表示预览
	print : function(designName,source, data, isPrint,callback) {
		if($.isFunction(isPrint)){
			callback=isPrint;
			isPrint=false;
		}
		var params={designName: designName};
		if(!$.isPlainObject(data)){
			params.id=data;
		}
		$.reqUrl('/getPrintInfo',params, function(rst) {
			if (!rst.state) {
				layer.alert(rst.msg);
				return;
			}
			var strArr = rst.data.designInfo.lodop_code.split(';');
			var printData = rst.data.printData != null ? rst.data.printData : data;
			var newStr = $.map(strArr, function(str) {
				return str.replace(/(.*)\{(.*)\}(.*)".*"(.*)/, function(v0, v1,	v2, v3, v4) {
					return (v1 + v3 + '"' + (printData[v2]||'') + '"' + v4);
				});
			});
			var LODOP = getLodop();
			eval(newStr.join(';'));
			

			if (rst.data.designInfo.lodop_width) {
				LODOP.SET_PRINT_PAGESIZE(0, rst.data.designInfo.lodop_width, rst.data.designInfo.lodop_height,"A4");
			}
			var num=1;
			if (isPrint){
				num=LODOP.PRINT()?1:0;
			}else{
				num=LODOP.PREVIEW();
			}
			if(callback)callback(num,source, printData.id);
		});
	}


};

function printEvent(designName,source, id, isPrint, callback) {
	$printUtil.print(designName,source, id, isPrint, callback);
}

// 更新打印次数，注意被刷新的表格名字
function updateInvoicePrintNum(num,source, id){
	if(num>0){
		$.reqUrl("/addInvoicePrintNum",{printNum: num, id: id,station_id:source},function(){
			$grid.load("gridBox");
		});
	}
}

// 自动打印发货单
function autoPrintInvoice(source){
	if(!$("#chkAutoPrintInvoice").prop("checked")) return false;
	
	var data = $grid.getData("gridBox");
	for(var i=0; i<data.length; i++){
		if((data[i].i_status == '2' || data[i].i_status == '3') && data[i].print_num == '0'){
			printEvent("invoice", source,data[i].id, true, updateInvoicePrintNum);
			break;
		}
	}
}