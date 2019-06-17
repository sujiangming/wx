<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	request.setAttribute("ContextPath", path);
	//获取用户信息,从session 里面
	HttpSession httpSession = request.getSession();
	String oppind = (String) httpSession.getAttribute("oppind");
	request.setAttribute("oppind", oppind);
	long kjscb_time = new Date().getTime();
	request.setAttribute("kjscb_time", kjscb_time);
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="ie=edge" />
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<script
	src="${ContextPath}/UI-lib/plugins/jQuery/jquery-2.2.3.min.js?${kjscb_time}"></script>
<title>我的订单</title>
<style type="text/css">
html {
	font-size: 100px;
}

body, a, ul, li {
	padding: 0;
	margin: 0;
	list-style: none;
	text-decoration: none;
}

body {
	/*width: 100%;*/
	background: rgb(240, 240, 240);
	font-family: .PingFangSC-Regular;
}

div {
	box-sizing: border-box;
}

.head {
	font-size: 0.16rem;
	height: 0.44rem;
	background: #26A69A;
	color: white;
	text-align: center;
	line-height: 0.44rem;
}

.cell1 {
	height: 1.80rem;
	background: white;
	font-size: 0.12rem;
	margin-top: 0.20rem;
}

.title {
	height: 0.30rem;
	border-bottom: 1px solid #D8D8D8;
	padding-left: 0.12rem;
	padding-right: 0.12rem;
	line-height: 0.30rem;
}

.title span {
	color: #5A6F95; /* #A4A3A3*/
	font-size: 0.17rem;
	margin-top: 0.56rem;
}

.cell {
	height: 1.59rem;
	background: white;
	border-bottom: 1px solid #D8D8D8;
	padding: 0.12rem 0;
}

.cell>.text {
	/*width: 2.60rem;*/
	margin-right: 0.12rem;
	padding-left: 0.12rem;
}

.cell>.text>span {
	display: block;
	font-size: 0.16rem;
}

.cell .spay {
	font-size: 0.10rem;
	color: #3D4245; /* #A4A3A3 #3D4245*/
	margin: 0.05rem 0;
}
</style>

<script type="text/javascript">
	var opind = "";

	$(function() {
		init();
	});

	function init() {
		opind = $("#input1").val();//"o0lMawYqLRSVXkbK60dNhIj8KXx8";
		list(opind);//请求获取预约管理订单
		getYJOrderByOpenId(opind);//请求获取应急救援管理订单
		autoSize();
		window.onresize = function() {
			autoSize();
		}
	}

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

	//获取预约审车订单
	function list(data) {
		$.ajax({
			url : getRootPath() + "order/listAll",
			type : "post",
			dataType : "json",
			data : {
				"opind" : data
			},
			success : function(me) {
				for (var i = 0; i < me.m.length; i++) {
					$(".content").append(
							"<div class='cell1'>" + "<div class='title'>"
									+ "<span>订单时间："
									+ formatDateTime(me.m[i].insertTime)
									+ "</span><label style='color:#0082E4;margin-left:15px;font-size:0.17rem;'>预约审车</label></div>"
									+ "<div class='cell'><div class='text'>"
									+ "<span class='spay'>车牌号："
									+ me.m[i].carCode + "</span>"
									+ "<span class='spay'>车主姓名："
									+ me.m[i].carOwnerName + "</span>"
									+ "<span class='spay'>车主电话："
									+ me.m[i].carOwnerMobile + "</span>"
									+ "<span class='spay'>车辆类型："
									+ me.m[i].orderProject + "</span>"
									+ "<span class='spay'>检测站点："
									+ me.m[i].checkStationName + "</span>"
									+ "</div>" + "</div>" + "</div>");
				}

			}

		});
	}
	
	//获取应急救援订单
	function getYJOrderByOpenId(openId) {
		$.ajax({
			url : getRootPath() + "emergencyOrderInfo/queryYJOrderByOpenId",
			type : "post",
			dataType : "json",
			data : {
				"openId" : openId
			},
			success : function(me) {
				for (var i = 0; i < me.m.length; i++) {
					$(".content").append(
							"<div class='cell1'>" + "<div class='title'>"
									+ "<span>订单时间："
									+ me.m[i].insertTime
									+ "</span><label style='color:red;margin-left:15px;font-size:0.17rem;'>应急救援</label></div>"
									+ "<div class='cell'><div class='text'>"
									+ "<span class='spay'>车牌号："
									+ me.m[i].carCode + "</span>"
									+ "<span class='spay'>车主姓名："
									+ me.m[i].carOwnerName + "</span>"
									+ "<span class='spay'>车主电话："
									+ me.m[i].carOwnerMobile + "</span>"
									+ "<span class='spay'>订单状态："
									+ me.m[i].typeState + "</span>"
									+ "<span class='spay'>救援点名称："
									+ me.m[i].checkStationName + "</span>"
									+ "</div>" + "</div>" + "</div>");
				}

			}

		});
	}

	function autoSize() {
		// 获取当前浏览器的视窗宽度，放在w中
		var w = document.documentElement.clientWidth;
		// 计算实际html font-size大小
		var size = w * 100 / 375;
		// 获取当前页面中的html标签
		var html = document.querySelector('html');
		// 设置字体大小样式
		html.style.fontSize = size + 'px';
	}

	function formatDateTime(inputTime) {
		var date = new Date(inputTime);
		var y = date.getFullYear();
		var m = date.getMonth() + 1;
		m = m < 10 ? ('0' + m) : m;
		var d = date.getDate();
		d = d < 10 ? ('0' + d) : d;
		var h = date.getHours();
		h = h < 10 ? ('0' + h) : h;
		var minute = date.getMinutes();
		var second = date.getSeconds();
		minute = minute < 10 ? ('0' + minute) : minute;
		second = second < 10 ? ('0' + second) : second;
		return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second;
	};
</script>
</head>

<body>

	<div class="wrap">
		<div class="header">
			<div class="head">我的订单</div>
		</div>
		<div class="content"></div>
		<input id="input1" type="text" value="${oppind}"
			style="display: none;" />
	</div>

</body>

</html>