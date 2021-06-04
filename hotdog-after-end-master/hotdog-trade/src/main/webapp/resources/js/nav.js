$(function () {
    var st = 180;
    
    $('#nav_all>li').click(function () {
    	$("#nav_all").find("li").each(function(){
    		$(this).find('ul').stop(false, true).slideUp(st);
    	});
        $(this).find('ul').stop(false, true).slideDown(st);
    }) 
    
    $("#defaultMenu").click();
});