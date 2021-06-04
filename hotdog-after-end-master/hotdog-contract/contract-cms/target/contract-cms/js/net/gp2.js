'use strict';

(function($){
  $(function() {
	var ajaxURLs = {  
	  'children': '/customer/getNetClientByParentCode/'
	};  
	
	$('#chart-container').orgchart({  
	  'data' : '/customer/getNetClientByParentCode/-1',  
	  'ajaxURL': ajaxURLs,  
	  'nodeTitle': 'name',  
	  'nodeContent': 'title',  
	  'nodeId': 'id' ,
	  'exportButton': false,  
	  'exportFilename': '会员节点图'  
	});  
  });
})(jQuery);