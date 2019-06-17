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
<meta name="viewport"
	content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<title>订单评价</title>
<link rel="stylesheet" href="${ContextPath}/weixinweb/css/new_file.css">

<script src="${ContextPath}/weixinweb/app/js/jquery-1.8.2.min.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${ContextPath}/weixinweb/app/layer_mobile/layer.js?${kjscb_time}"></script>
</head>
<script type="text/javascript">
	var openid = null;
	var name = null;
	var checkStationName = null;
	var order = null;
	window.onload = function() {
		initEvent('mydiv1');
		var checksn = window.location.search;
		var ind = checksn.lastIndexOf("\?");
		var checkName = checksn.substring(ind + 1, checksn.length).split(";");
		openid = decodeURIComponent(checkName[0]);
		name = decodeURIComponent(checkName[1]);
		checkStationName = decodeURIComponent(checkName[2]);
		order = decodeURIComponent(checkName[3]);
	}

	//封装的提示方法
	function layerTip(text) {
		// 2秒后自动关闭
		layer.open({
			content : text,
			skin : 'msg',
			time : 5
		});
	}
	var isclick = false;
	function choseStar(mydivid, num) {
		if (!isclick) {
			var tds = $("#" + mydivid + " ul li");
			for (var i = 0; i < num; i++) {
				var td = tds[i];
				$(td).find("img").attr("src",
						"${ContextPath}/weixinweb/img/star_full.png");
			}
			var tindex = $("#" + mydivid).attr("currentIndex");
			tindex = tindex == 0 ? 0 : tindex + 1;
			for (var j = num; j < tindex; j++) {
				var td = tds[j];
				$(td).find("img").attr("src",
						"${ContextPath}/weixinweb/img/star_empty.png");
			}
			$("#" + mydivid).attr("currentIndex", num);
		}
	}
	function change(mydivid, num) {
		choseStar(mydivid, num);
	}
	function initEvent(mydivid) {
		var tds = $("#" + mydivid + " ul li");
		for (var i = 0; i < tds.length; i++) {
			var td = tds[i];
			$(td).live('click', function() {
				var num = $(this).attr("num");
				change(mydivid, num);
				var data = {
					"data" : num,
					"openid" : openid,
					"name" : name,
					"checkStationName" : checkStationName,
					"order" : order
				};
				$.ajax({
					type : "post",
					url : getRootPath() + "comment/save",
					data : data,
					dataType : "json",
					async : true,
					success : function(data) {
						if (data.status == 1) {
							alert(data.message);
							WeixinJSBridge.call('closeWindow');
						} else if (data.status == 2) {
							alert(data.message);
							WeixinJSBridge.call('closeWindow');
						}

					}

				});
			});
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
</script>
</head>
<body>
	<div class="main">
		<div class="main-wrap">
			<span class="revtit">评&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价:</span>
			<div currentIndex="0" class="mydiv">
				<ul class="star_ul">
					<li><div>
							<input type="radio" name="radio" value="radio1" /><img
								src="${ContextPath}/weixinweb/img/hua.png" />
						</div></li>
					<li><div>
							<input type="radio" name="radio" value="radio2" /><img
								src="${ContextPath}/weixinweb/img/huah.png" />
						</div></li>
					<li><div>
							<input type="radio" name="radio" value="radio3" /><img
								src="${ContextPath}/weixinweb/img/huae.png">
						</div></li>

				</ul>
			</div>
		</div>
		<div class="main-wrap">
			<span class="revtit">综合评分:</span>
			<div id="mydiv1" currentIndex="0" class="mydiv">
				<ul class="star_ul">
					<li num="1"><img
						src="${ContextPath}/weixinweb/img/star_empty.png" class="xing_hui" /></li>
					<li num="2"><img
						src="${ContextPath}/weixinweb/img/star_empty.png" class="xing_hui" /></li>
					<li num="3"><img
						src="${ContextPath}/weixinweb/img/star_empty.png" class="xing_hui" /></li>
					<li num="4"><img
						src="${ContextPath}/weixinweb/img/star_empty.png" class="xing_hui" /></li>
					<li num="5"><img
						src="${ContextPath}/weixinweb/img/star_empty.png" class="xing_hui" /></li>
				</ul>
			</div>
		</div>
	</div>
</body>

</html>