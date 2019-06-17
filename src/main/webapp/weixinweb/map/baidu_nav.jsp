<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
body, html, #allmap {
	width: 100%;
	height: 100%;
	overflow: hidden;
	margin: 0;
	font-family: "微软雅黑";
	z-index: 777;
}

#l-map {
	height: 100%;
	width: 100%;
}

#r-result {
	position: absolute;
	top: 0px;
	right: 0px;
	width: 50%;
	height: 100%;
	overflow-y: scroll;
	background: white;
	z-index: 888;
}

#r-result table {
	margin: 12px;
}

#r-result table>tr, td {
	width: 100%;
}

table thead>tr>td:first-child {
	padding: 5px;
}

table tbody>tr:nth-child(odd) {
	background: #F5F3F0;
}

table tbody>tr>td {
	padding: 5px;
}

#shadow_layer {
	position: absolute;
	height: 100%;
	width: 100%;
	background: rgba(0, 0, 0, 0.5);
	top: 0;
	left: 0;
	z-index: 9999;
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
<script type="text/javascript"
	src="http://api.map.baidu.com/api?v=2.0&ak=j0gnvGDrOFXQTAMfyf23Gpfhh6SGMvlB"></script>
<title>根据起终点经纬度驾车导航</title>
</head>

<body>
	<div id="l-map"></div>
	<div id="r-result"></div>
	<div id="shadow_layer" style="display: none;">
		<div id="shadow_layer_div">
			<div id="a_map" class="div_s">
				<a
					href="https://uri.amap.com/marker?position=27.2642,105.379487&name=毕节市">高德地图</a>
			</div>
			<div id="b_map" class="div_s" onclick="openBaiDuApp();">百度地图</div>
			<div id="t_map" class="div_s">腾讯地图</div>
			<div id="cancel_btn" class="div_s" onclick="hideDiv()">取消</div>
		</div>
	</div>
</body>

</html>
<script type="text/javascript">
	// 百度地图API功能
	var posResult = null;
	var scheme = "";
	var map = new BMap.Map("l-map");
	map.centerAndZoom(new BMap.Point(106.64433, 26.390623), 12);

	var geolocation = new BMap.Geolocation();
	geolocation.getCurrentPosition(function(r) {
		if (this.getStatus() == BMAP_STATUS_SUCCESS) {
			posResult = r;
			//alert('定位成功后的返回结果：' + JSON.stringify(r));
			//定位成功后，获取当前地址，并开始进行路径规划
			guihuaPath(r);
			//显示导航遮罩层
			showDiv();
		} else {
			alert('failed' + this.getStatus());
		}
	}, {
		enableHighAccuracy : true
	});

	function showDiv() {
		document.getElementById("shadow_layer").style = "display: block;";
	}

	function hideDiv() {
		document.getElementById("shadow_layer").style = "display: none;";
	}

	function guihuaPath(r) {
		var p1 = new BMap.Point(r.point.lng, r.point.lat);
		var p2 = new BMap.Point(105.379487, 27.2642);

		var options = {
			onSearchComplete : function(results) {
				if (driving.getStatus() == BMAP_STATUS_SUCCESS) {
					// 获取第一条方案   
					var plan = results.getPlan(0);
					// 获取方案的驾车线路   
					var route = plan.getRoute(0);
					// 获取每个关键步骤，并输出到页面   					
					var htmlstr = '<table>'
							+ '		<thead>'
							+ '			<tr>'
							+ '				<td style="color: blue;width:50%;">推荐</td>'
							+ '				<td style="text-align: right;">'
							+ '					<a onclick="directNav()" href="##" style="width:50%;text-align: right;">APP导航</a>'
							+ '				</td>'
							+ '			</tr>'
							+ '			<tr>'
							+ '				<td style="color: green;" colspan="2">起点</td>'
							+ '			</tr>' + '		</thead>' + '	<tbody>';

					for (var i = 0; i < route.getNumSteps(); i++) {
						var step = route.getStep(i);
						//s.push((i + 1) + ". " + step.getDescription());
						htmlstr += '<tr><td colspan="2">'
								+ step.getDescription() + '</td></tr>';
					}
					htmlstr += '</tbody><tfoot><tr><td colspan="2" style="color: green;">终点</td></tr></tfoot></table>';
					document.getElementById("r-result").innerHTML = htmlstr; //s.join("<br>");
				}
			}
		};
		var driving = new BMap.DrivingRoute(map, options);
		driving.search(p1, p2);
	}

	function openBaiDuApp() {
		if (posResult == null || posResult.point.lat == ""
				|| posResult.point.lng == "") {
			alert("未获取到当前的位置信息，无法进行导航~");
			return;
		}

		scheme = "://map/direction?origin=latlng:"
				+ posResult.point.lat
				+ ","
				+ posResult.point.lng
				+ "|name:我的位置&destination=latlng:27.2642,105.379487|name:毕节市&mode=driving";

		var I = navigator.userAgent;
		var isiPad = (I.match(/(iPad).*OS\s([\d_]+)/)) ? true : false;
		var isAndroid = I.match(/android/i) ? true : false;
		//var isMac = (I.match(/(Mac\sOS)\sX\s([\d_]+)/)) ? true : false;
		var isiPhone = (!isiPad && I.match(/(iPhone\sOS)\s([\d_]+)/)) ? true
				: false;
		//var isSafari = (isiPhone || isiPad) && I.match(/Safari/);
		//var isMQQBrowser = I.match(/MQQBrowser\/([\d\.]+)/) ? true : false;
		//var isUCBrowser = I.match(/UCBrowser\/([\d\.]+)/) ? true : false;
		//var isQQ = (!isMQQBrowser) ? (I.match(/QQ\/([\d\.]+)/) ? true : false) : false;
		//var safariVersion = 0;
		//isSafari && (safariVersion = I.match(/Version\/([\d\.]+)/));
		//try {
		//	safariVersion = parseFloat(safariVersion[1], 10)
		//} catch(R) {}
		//var isSAMSUNG = I.toUpperCase().indexOf("SAMSUNG-SM-N7508V") != -1;
		// 尝试打开手机APP  
		if (isiPhone) {
			tryIOpen(scheme);
			return;
		}
		if (isAndroid) {
			tryAOpen(scheme);
			return;
		}

		directNav();
	}

	function tryAOpen(scheme) {
		var protocol = "bdapp";
		var iframe = document.createElement("iframe");
		iframe.Id = "i" + Math.random().toString().replace(".", "");
		iframe.src = protocol + scheme;
		iframe.style.display = "none";
		document.body.appendChild(iframe);
		setTimeout(function() {
			document.body.removeChild(iframe);
		}, 500);

	}

	function tryIOpen(scheme) {
		var protocol = "baidumap";
		var a = document.createElement("a");
		a.id = "a" + Math.random().toString().replace(".", "");
		a.href = protocol + scheme;
		document.body.appendChild(a);
		var T = document.createEvent("HTMLEvents");
		T.initEvent("click", !1, !1), a.dispatchEvent(T)
	}

	function directNav() { //直接导航
		var latCurrent = posResult.point.lat;
		var lngCurrent = posResult.point.lng;
		var region = posResult.address.city + posResult.address.district;
		location.href = "http://api.map.baidu.com/direction?origin="
				+ latCurrent
				+ ","
				+ lngCurrent
				+ "|name:我的位置&destination=27.2642,105.379487|name:毕节市瀚元机动车检测站&mode=driving&region="
				+ region + "&output=html";
	}
</script>