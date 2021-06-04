<%@ page language="java" pageEncoding="UTF-8"%>
<html>
	<head>
	<title>哎呀，出错啦！</title>
	<style>
		body {
		    font-family: Helvetica, Arial, sans-serif;
		    -webkit-font-smoothing: antialiased;
		    overflow: hidden;
		    text-align: center;
		}
		.content {
			width: 350px;
			margin: 200px auto;
			padding: 20px;
			border: 1px solid #ccc;
			text-align: center;
		}
	</style>
	</head>
	<body>
	    <div class="content">
			<a href="javascript:void(0);" onclick="reLogin()">${error}</a>
	    </div>
	</body>
</html>

<script>
function reLogin() {
	if ("${flag}" != "") {
		window.top.location.href = "/adminpmzhongguo";
	} else {
		window.top.location.href = "/";
	}
}
</script>