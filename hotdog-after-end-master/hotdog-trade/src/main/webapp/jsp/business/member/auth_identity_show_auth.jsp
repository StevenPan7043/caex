<%@ page contentType="text/html; charset=utf-8"%>
身份证正面：<br/>
<img src='' id="front"><br/>
"身份证背面：<br/>
<img src='' id="back"><br/>
人脸比对：<br/>
<img src='' id="handheld"><br/>
<script src="https://apps.bdimg.com/libs/jquery/2.1.1/jquery.min.js" type="text/javascript"></script>
<script>
	$(function(){
		$("#front").attr("src",getUrlParam("front"));
		$("#back").attr("src",getUrlParam("back"));
		$("#handheld").attr("src",getUrlParam("handheld"));
	});
	//获取url中的参数
	function getUrlParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
		var r = window.location.search.substr(1).match(reg);  //匹配目标参数
		if (r != null) return unescape(r[2]); return null; //返回参数值
	}
</script>