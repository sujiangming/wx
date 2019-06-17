<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%
	String path = request.getContextPath();
	request.setAttribute("ContextPath", path);
	long kjscb_time = new Date().getTime();
	request.setAttribute("kjscb_time", kjscb_time);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>正在登录微信……</title>
<script type="text/javascript"
	src="${ContextPath}/weixinweb/app/js/jquery-1.8.2.min.js?${kjscb_time}"></script>
<script type="text/javascript">
	function getRootPath() {
		//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
		var curWwwPath = window.document.location.href;
		//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
		var pathName = window.document.location.pathname;
		var pos = curWwwPath.indexOf(pathName);
		//获取主机地址，如： http://localhost:8083
		var localhostPaht = curWwwPath.substring(0, pos);
		//获取带"/"的项目名，如：/uimcardprj
		var projectName = pathName.substring(0,
				pathName.substr(1).indexOf('/') + 2);
		return (localhostPaht + projectName);
	}
	function shouquan() {
		$.ajax({
			url : getRootPath() + "yuYueShouQuan",
			type : "get",
			dataType : "json",
			success : function(data) {
				if (data.msg == "success") {
					var url = data.url;
					location.href=url;
					//window.open(url);
				}
			}
		});
	}
</script>

</head>
<body onload="shouquan()">
	<div id="tt"></div>
</body>
</html>