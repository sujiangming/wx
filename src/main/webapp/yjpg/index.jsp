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
<html lang="en">
<head>
<meta charset="UTF-8">
<title>应急救援派单</title>
<meta
	content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0"
	name="viewport" />
<meta content="yes" name="apple-mobile-web-app-capable" />
<meta content="black" name="apple-mobile-web-app-status-bar-style" />
<meta content="telephone=no" name="format-detection" />
 <link href="${ContextPath}/yjpg/css/style.css?${kjscb_time}"
	rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${ContextPath}/yjpg/js/jquery.min.js?${kjscb_time}"></script>
<script type="text/javascript"
	src="${ContextPath}/weixinweb/app/layer_mobile/layer.js?${kjscb_time}"></script>
<script type="text/javascript"
	src="${ContextPath}/yjpg/js/popUP.js?${kjscb_time}"></script>
<script type="text/javascript"
	src="${ContextPath}/yjpg/js/area.js?${kjscb_time}"></script>
<style type="text/css">
.wrap_img {
	position: absolute;
	top: 0;
	left: 0;
	padding: 0px;
	margin: 0px;
	height: 100%;
	width: 100%; background : rgba( 0, 0, 0, 0.6);
	z-index: 100000;
	display: none;
	background: rgba(0, 0, 0, 0.6);
}

#image_big {
	height: 80%;
	width: 80%;
	position: relative;
	margin-top: 10%;
	margin-left: 10%;
}
#orderContainer{
	display: -webkit-box;
	-webkit-box-orient: vertical;
	width: 100%;
}
</style>
</head>
<body>
	<div class="content">
		<span>全部</span>
	</div>
	<div id="orderContainer"></div>
	<div class="wrap_img">
		<img id="image_big" onclick="hideBigImage()"
			src="${ContextPath}/yjpg/images/pd-002.png">
	</div>
</body>
</html>