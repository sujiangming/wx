<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%
	String path = request.getContextPath();
	request.setAttribute("ContextPath", path);

	long kjscb_time = new Date().getTime();
	request.setAttribute("kjscb_time", kjscb_time);
%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="initial-scale=1.0, user-scalable=no, width=device-width">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<title>检查站位置</title>
<style type="text/css">
html, body, #container {
	width: 100%;
	height: 100%;
}
</style>
<style type="text/css">
#panel {
	position: fixed;
	background-color: white;
	max-height: 90%;
	overflow-y: auto;
	top: 10px;
	right: 10px;
	width: 280px;
}

#panel .amap-call {
	background-color: #009cf9;
	border-top-left-radius: 4px;
	border-top-right-radius: 4px;
}

#panel .amap-lib-driving {
	border-bottom-left-radius: 4px;
	border-bottom-right-radius: 4px;
	overflow: hidden;
}
</style>
<link rel="stylesheet"
	href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css?${kjscb_time}" />
<script
	src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js?${kjscb_time}"></script>
<script type="text/javascript"
	src="https://webapi.amap.com/maps?v=1.4.11&key=6ece49b46b1726d90003a810c41e69e0"></script>
<script type="text/javascript"
	src="https://cache.amap.com/lbs/static/addToolbar.js?${kjscb_time}"></script>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.4.0.js?${kjscb_time}"></script>
</head>
<body>
	<div id="container"></div>
	<div id="panel"></div>
	<script type="text/javascript">
		var xy = getURLKey();
		xy = decodeURIComponent(xy);

		function getURLKey() {
			var url = window.location.search;
			var index = url.lastIndexOf("\?");
			var key = url.substring(index + 1, url.length).split(";");
			return key[0];
		}
		//基本地图加载
		var map = new AMap.Map("container", {
			resizeEnable : true,
			zoom : 13
		});
		//定义路线规划插件
		var driving;
		//优先定位
		AMap.plugin([ 'AMap.Driving', 'AMap.Geolocation' ], function() {
			//构造路线导航类
			driving = new AMap.Driving({
				map : map,
				panel : "panel"
			});
			var geolocation = new AMap.Geolocation({
				enableHighAccuracy : true,//是否使用高精度定位，默认:true
				timeout : 10000, //超过10秒后停止定位，默认：5s
				buttonPosition : 'RB', //定位按钮的停靠位置
				buttonOffset : new AMap.Pixel(10, 20),//定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
				zoomToAccuracy : true, //定位成功后是否自动调整地图视野到定位点
			});
			map.addControl(geolocation);
			geolocation.getCurrentPosition(function(status, result) {
				if (status == 'complete') {
					onComplete(result)
				} else {
					onError(result)
				}
			});
		});
		//解析定位结果
		function onComplete(data) {
			log.success('定位成功' + + data.position);
			initGuihua(data.position);
		}
		//解析定位错误信息
		function onError(data) {
			log.error('定位失败');
			log.error('失败原因排查信息:' + data.message);
		}

		function initGuihua(centerXY) {
			var center = centerXY;
			var cx = center.toString().split(",");
			var xx = cx[0];
			var yy = cx[1];
			var stst = xy;
			var ss = stst.split(",");
			var x = ss[0];
			var y = ss[1];
			// 根据起终点经纬度规划驾车导航路线
			driving.search(new AMap.LngLat(xx, yy), new AMap.LngLat(x, y),
					function(status, result) {
						// result 即是对应的驾车导航信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_DrivingResult
						if (status === 'complete') {
							log.success('绘制驾车路线完成')
						} else {
							log.error('获取驾车数据失败：' + result)
						}
					});
		}
	</script>
</body>
</html>