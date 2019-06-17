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
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="initial-scale=1.0, user-scalable=no, width=device-width">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<style type="text/css">
body, html, #allmap {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
	font-family: "微软雅黑";
	z-index: 777;
	background:white;
	/* background: url(../img/yingji2.png) no-repeat center;
	background-size: cover; */
}

#shadow_layer {
	position: absolute;
	height: 100%;
	width: 100%;
	background: rgba(0, 0, 0, 0.5);
	top: 0;
	left: 0;
	z-index: 9999;
	display: none;
}

#shadow_layer_div {
	height: 100%;
	width: 100%;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-box-align: center;
	-webkit-box-pack: end;
}

#shadow_layer_div .div_s {
	width: 100%;
	text-align: center;
	background: #F5F3F0;
	opacity: 0.9;
	padding-top: 10px;
	padding-bottom: 10px;
	margin-bottom: 1px;
}
</style>
<title>导航</title>
</head>
<body>
	<div id="shadow_layer">
		<div id="shadow_layer_div">
			<div id="a_map" class="div_s" onclick="goAMap()">高德地图</div>
			<div id="b_map" class="div_s" onclick="goBMap()">百度地图</div>
			<div id="t_map" class="div_s" onclick="goTencentMap()">腾讯地图</div>
		</div>
	</div>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/jquery-1.8.2.min.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/layer_mobile/layer.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/jweixin-1.4.0.js?${kjscb_time}"></script>
	<script type="text/javascript">
		
		//获取模板消息传递过来的参数
		var url = window.location.search;
		var index = url.lastIndexOf("\?");
		var key = url.substring(index + 1, url.length).split(";");
		//获取坐标
		var xy = decodeURIComponent(key[0]);
		var posArray = xy.split(",");
		//获取检车站名称
		var checkName = decodeURIComponent(key[1]);

		//alert(JSON.stringify(posArray)+ "-----" + checkName);
		
		//调用签名
		getSignature();

		//获取检车站
		function getSignature() {
			var dataJson = {
				url : location.href.split('#')[0]
			}
			
			var index = loading("正在打开……");
			
			$.ajax({
				type : "GET",
				url : getRootPath() + "nav/signature",
				data : dataJson,
				dataType : "json",
				success : function(result) {
					
					closeLoading(index); // 关闭loading
					
					if (result == null) {
						layerTip(result.message);
						return;
					}
					initJSSDK(result);
				}
			});
		}

		function initJSSDK(json) {
			json = eval(json);
			wx.config({
				debug : false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				appId : json.appId, // 必填，公众号的唯一标识
				timestamp : json.timestamp, // 必填，生成签名的时间戳
				nonceStr : json.noncestr, // 必填，生成签名的随机串
				signature : json.signature,// 必填，签名
				jsApiList : [ // 必填，需要使用的JS接口列表
				"openLocation", "getLocation" ]
			});

			/**
				config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，
				config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，
				则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，
				则可以直接调用，不需要放在ready函数中。
			 **/
			wx.ready(function() {
				
				//打开地图	
				wx.openLocation({
					latitude : posArray[1], // 纬度，浮点数，范围为90 ~ -90
					longitude : posArray[0], // 经度，浮点数，范围为180 ~ -180。
					name : checkName, // 位置名
					address : checkName, // 地址详情说明
					scale : 28, // 地图缩放级别,整形值,范围从1~28。默认为最大
					infoUrl : '' // 在查看位置界面底部显示的超链接,可点击跳转
				});
				
			});
			/**
				config信息验证失败会执行error函数，如签名过期导致验证失败，
				具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，
				对于SPA可以在这里更新签名。
			 **/
			wx.error(function(res) {
				alert(JSON.stringify(res));
			});
		}

		function getRootPath() {
			// 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
			var curWwwPath = window.document.location.href;
			// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
			var pathName = window.document.location.pathname;
			var pos = curWwwPath.indexOf(pathName);
			// 获取主机地址，如： http://localhost:8083
			var localhostPaht = curWwwPath.substring(0, pos);
			// 获取带"/"的项目名，如：/uimcardprj
			var projectName = pathName.substring(0, pathName.substr(1).indexOf(
					'/') + 2);
			return (localhostPaht + projectName);
		}

		// 封装的loading方法
		function loading(text) {
			// loading带文字
			var index = layer.open({
				type : 2,
				content : text,
				shadeClose : false
			});
			return index;
		}

		// 关闭loading
		function closeLoading(index) {
			layer.close(index);
		}

		// 封装的提示方法
		function layerTip(text) {
			// 2秒后自动关闭
			layer.open({
				content : text,
				skin : 'msg',
				time : 2
			});
		}

		//高德地图
		function goAMap() {
			//location.href = "nav_amap.jsp?" + xy + ";1";
			location.href = "https://uri.amap.com/marker?position=" + xy
					+ "&name=" + checkName;
		}

		//百度地图
		function goBMap() {
			//location.href = "nav_bmap.jsp?" + xy + ";1";
			location.href = "http://api.map.baidu.com/marker?location="
					+ posArray[1] + "," + posArray[0] + "&title=" + checkName
					+ "&output=html";
		}

		//腾讯地图
		function goTencentMap() {
			//location.href= 'http://apis.map.qq.com/uri/v1/marker?marker=coord:30.595810,103.912830;title:渔虾跳主题餐吧;addr: 城南优品道广场星光广场一楼';
			location.href = "http://apis.map.qq.com/uri/v1/marker?marker=coord:"
					+ posArray[1] + "," + posArray[0] + ";title:" + checkName;//;title:渔虾跳主题餐吧;addr: 城南优品道广场星光广场一楼
		}
	</script>
</body>

</html>