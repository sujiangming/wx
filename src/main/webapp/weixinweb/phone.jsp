<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>电话</title>
<script type="text/javascript" src="${ContextPath}/weixinweb/app/js/jquery-1.8.2.min.js?${kjscb_time}"></script>
<script type="text/javascript">
$(function(){
	var phone = window.location.search;
	var index = phone.lastIndexOf("\?");
	var phoneName = phone.substring(index + 1, phone.length).split(";");
	window.location.href='tel://'+phoneName[0]+'';
});
</script>
</head>
<body>

</body>
</html>