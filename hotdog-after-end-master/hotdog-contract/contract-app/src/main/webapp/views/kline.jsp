<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0">
<title>${coin }</title>
<link rel="stylesheet" href="/css/kline.css?v=1" type="text/css">

<script src="/js/calc.js"></script>
<script src="/js/jquery.min.js"></script>
</head>
<body class="register">
	<div class="chart-head">
		<div class="chart-head-left">
			<span id="usdtPrice"
				class="lhclass <c:if test="${dto.isout==0}">red</c:if><c:if test="${dto.isout==1}">green</c:if>">${dto.usdtPrice }</span>
			<!--<span>≈{{topObj.cny || '0.00'}}CNY <span class="topObj.isout == 0?'red':'green'">{{topObj.isout == 0?'-':'+'}}{{(topObj.scale?topObj.scale * 100:0).toFixed(2) || '0.00'}}%</span></span>-->
			<span
				class="lhclass <c:if test="${dto.isout==0}">red</c:if><c:if test="${dto.isout==1}">green</c:if>">≈<span
				id="cny">${dto.cny }</span>CNY <span id="scale"
				class="lhclass <c:if test="${dto.isout==0}">red</c:if><c:if test="${dto.isout==1}">green</c:if>">
					<c:if test="${dto.isout==0}">-</c:if> <c:if test="${dto.isout==1}">+</c:if>
					<fmt:formatNumber value="${dto.scale*100 }" pattern="#0.00" /> %
			</span>
			</span>
		</div>
		<div class="chart-head-right">
			<div class="chart-head-right-item">
				<span>高</span> <span id="high">${dto.high }</span>
			</div>
			<div class="chart-head-right-item">
				<span>低</span> <span id="low">${dto.low }</span>
			</div>
			<div class="chart-head-right-item">
				<span>24H</span> <span id="vol"><fmt:formatNumber
						value="${dto.vol }" pattern="#0.00" /> </span>
			</div>
		</div>
	</div>
	<!-- TradingView Widget BEGIN -->
	<div class="tradingview-widget-container">
		<div id="tradingview_7945f" style="height: 8rem"></div>
		<script type="text/javascript" src="/js/tv.js"></script>
		<script type="text/javascript">
			new TradingView.widget({
				"autosize" : true,
				"symbol" : "HUOBI:${klinecoin}",
				"interval" : "1",
				"timezone" : "Asia/Shanghai",
				"theme" : "Light",
				"style" : "1",
				"locale" : "zh_CN",
				"toolbar_bg" : "#f1f3f6",
				"enable_publishing" : false,
				"hide_legend" : true,
				"save_image" : false,
				"studies" : [ "MACD@tv-basicstudies", "BB@tv-basicstudies",
						"Volume@tv-basicstudies" ],
				"no_referral_id" : true,
				"container_id" : "tradingview_7945f"
			});
		</script>
	</div>
	<!-- TradingView Widget END -->
	<div style="height: .40rem;"></div>
	<div class="chart-deep">
		<div class="chart-deep-head">
			<span class="flex1">买入</span> <span class="flex2">数量(${name })</span>
			<span style="text-align: center;">金额(USTD)</span> <span class="flex2"
				style="text-align: right;">数量(${name })</span> <span class="flex1"
				style="text-align: right;">卖出</span>
		</div>
		<div class="chart-deep-box">
			<div class="chart-deep-list" id="ask">
				<c:forEach items="${depth.asks }" var="a" varStatus="i">
					<div class="chart-deep-item">
						<span>${i.index+1}</span> 
						<span><fmt:formatNumber value="${a[1] }" pattern="#0.0000" /></span>
						<span><fmt:formatNumber value="${a[0] }" pattern="#0.00" /></span> 
					</div>
				</c:forEach>
			</div>
			<div class="chart-deep-list" id="bids">
				<c:forEach items="${depth.bids }" var="b" varStatus="bi">
					<div class="chart-deep-item2">
						<span><fmt:formatNumber value="${b[0] }" pattern="#0.00" /></span>
						<span><fmt:formatNumber value="${b[1] }" pattern="#0.0000" /></span>
						<span>${bi.index+1}</span>
					</div>
				</c:forEach>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">
	var websocket = null;
	var coin = "${coin}";
	var token = "${token}";
	var key = token + "_kline_" + coin;
	//判断当前浏览器是否支持WebSocket
	if ('WebSocket' in window) {
		/*   websocket = new WebSocket("ws://blockcoin.sanxininfo.com/websocket/31/31"); */
		websocket = new WebSocket("ws://localhost/websocket/"+ token);
	} else {
		alert('Not support websocket')
	}
	//连接发生错误的回调方法
	websocket.onerror = function() {
		websocket = new WebSocket("ws://app.r0297.cn/websocket/"+ token);
	};

	//连接成功建立的回调方法
	websocket.onopen = function(event) {
		websocket.send(key);
	}
	//接收到消息的回调方法
	websocket.onmessage = function(event) {
		var obj = JSON.parse(event.data);
		if ("1001" != obj) {
			var depth = obj.depth;
			if (depth != null) {
				$("#ask").html("");
				var asks = depth.asks;
				for (var i = 0; i < asks.length; i++) {
					var str = "";
					var index = i + 1;
					var num = asks[i][0].toFixed(2);
					var money = asks[i][1].toFixed(4);
					str += '<div class="chart-deep-item">';
					str += '<span>' + index + '</span>';
					str += '<span>' + money + '</span>';
					str += '<span>' + num + '</span>';
					str += '</div>';
					$("#ask").append(str);
				}
				$("#bids").html("");
				var bids = depth.bids;
				for (var i = 0; i < bids.length; i++) {
					var str = "";
					var index = i + 1;
					var num = bids[i][0].toFixed(2);
					var money = bids[i][1].toFixed(4);
					str += '<div class="chart-deep-item2">';
					str += '<span>' + num + '</span>';
					str += '<span>' + money + '</span>';
					str += '<span>' + index + '</span>';
					str += '</div>';
					$("#bids").append(str);
				}
			}
			var dto = obj.dto;
			if (dto != null) {
				var a = (dto.scale * 100).toFixed(2);
				if (dto.isout == 0) {
					$(".lhclass").removeClass("green");
					$(".lhclass").addClass("red");
					$("#scale").text("-" + a + "%");
				} else {
					$(".lhclass").removeClass("red");
					$(".lhclass").addClass("green");
					$("#scale").text("+" + a + "%");
				}
				$("#usdtPrice").text(dto.usdtPrice);
				$("#cny").text(dto.cny);
				$("#high").text(dto.high.toFixed(2));
				$("#low").text(dto.low.toFixed(2));
				$("#vol").text(dto.vol.toFixed(2));
			}
			setTimeout(function() {
				websocket.send(key);
			}, 2000);
			/* 	 */
		}
	}
	//连接关闭的回调方法
	websocket.onclose = function() {
	}

	//监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
	window.onbeforeunload = function() {
		websocket.close();
	}
	//将消息显示在网页上
	function setMessageInnerHTML(innerHTML) {
		document.getElementById('message').innerHTML += innerHTML + '<br/>';

	}
	//关闭连接
	function closeWebSocket() {
		websocket.close();
	}
	$(window).bind('beforeunload', function() {
		websocket.close();
	});
</script>
</html>