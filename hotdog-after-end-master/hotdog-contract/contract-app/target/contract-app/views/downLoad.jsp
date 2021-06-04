<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0">
    <title>GBCOIN下载</title>
    <link rel="stylesheet" href="/css/main.css?v=1" type="text/css">

    <script src="/js/calc.js"></script>
    <script src="/js/jquery.min.js"></script>
</head>
<body class="download">
    <div class="main" style="padding-top: 1.06rem;color: white">
        <img src="/images/logo.png?v=1" alt="" class="logo-img">
        <p class="big-title"> </p>
        <a href="${android_url }" class="con-link" id="andriod-link">
            <img src="/images/andriod_icon.png" alt="" class="link-icon">
            <span>Android下载</span>
        </a>
        <a href="${ios_url }" class="con-link" id="ios-link">
            <img src="/images/ios_icon.png" alt="" class="link-icon">
            <span>IOS下载</span>
        </a>
    </div>

    <!--提示-->
    <div class="wxtip" id="JweixinTip">
        <span class="wxtip-icon"></span>
        <p class="wxtip-txt">点击右上角<br>选择在浏览器中打开</p >
    </div>
    <script>
        $(function () {
            $('.con-link').on('click',function () {
                var u = navigator.userAgent, app = navigator.appVersion;
                var isWeixin = !!/MicroMessenger/i.test(u);
                //Android
                var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Adr') > -1;
                //IOS
                var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
                if (isWeixin) {
                    $('#JweixinTip').show();
                    $(this).click(function(e){
                        window.event ? window.event.returnValue = false : e.preventDefault();

                    });
                    $('#JweixinTip').on("click", function(){
                        $(this).hide();
                    })
                }else{
                    if(isAndroid){
                        //  跳转安卓下载链接
                    	 document.getElementById('andriod-link').setAttribute("href","${android_url }");
                    }
                    if(isiOS){
                        //  跳转ios下载链接
                    	 document.getElementById('ios-link').setAttribute("href","${ios_url }");
                    }
                }
            })
        });
    </script>
</body>
</html>