<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.lang.*"%>
<%
	String path = request.getContextPath();
	request.setAttribute("ContextPath", path);
	//获取用户信息,从session 里面
	HttpSession httpSession = request.getSession();
	if (null != httpSession.getAttribute("oppind")) {
		String oppind = (String) httpSession.getAttribute("oppind");
		request.setAttribute("oppind", oppind);
	} else {
		request.setAttribute("oppind", "");
	}
	if (null != httpSession.getAttribute("resultCode")) {
		String resultCode = (String) httpSession.getAttribute("resultCode");
		request.setAttribute("resultCode", resultCode);
	}
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
<link rel="stylesheet"
	href="${ContextPath}/weixinweb/css/demo-center.css?${kjscb_time}" />
<style type="text/css">
html, body, #container {
	width: 100%;
	height: 100%;
}

#panel {
	padding: .75rem 1.25rem;
	margin-bottom: 1rem;
	border-radius: .25rem;
	position: fixed;
	top: 1rem;
	background-color: white;
	width: auto;
	min-width: 10rem;
	border-width: 0;
	/*right: 1rem;*/
	left: 42rem;
	box-shadow: 0 2px 6px 0 rgba(114, 124, 245, .5);
	text-align: center;
	color: #FF0000;
}

.custom-content-marker {
	position: relative;
	width: 25px;
	height: 34px;
}

.custom-content-marker img {
	width: 100%;
	height: 100%;
}

.custom-content-marker .close-btn {
	position: absolute;
	top: -15px;
	right: -148px;
	width: 160px;
	height: 20px;
	font-size: 12px;
	background: #ccc;
	border-radius: 15px;
	color: #fff;
	text-align: center;
	line-height: 20px;
	box-shadow: -1px 1px 1px rgba(10, 10, 10, .2);
}

.custom-content-marker .close-btn:hover {
	background: #666;
}

.wrap_img {
	position: absolute;
	top: 0;
	left: 0;
	padding: 0px;
	margin: 0px;
	height: 100%;
	width: 100%;
	background: rgba(0, 0, 0, 0.6);
	z-index: 100000;
	display: none;
	background: rgba(0, 0, 0, 0.6);
}

#image_big {
	height: auto;
	width: 80%;
	position: relative;
	margin-top: 45%;
	margin-left: 10%;
	background: #FFFCF5;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-box-align: center;
	-webkit-box-pack: center;
	border-radius: 5px;
	padding: 25px;
}
</style>
</head>

<body>
	<div id="container"></div>
	<div class="info">
		<font id="text">请选择求援线路</font> <font id="price" style="color: red;"></font>
		<button
			style="padding-left: 12px; padding-right: 12px; margin-left: 12px; padding-top: 5px; padding-bottom: 5px; color: red; border: none; outline: none;"
			onclick="showBigImage()">立即预约</button>
	</div>
	<div class="wrap_img">
		<img src="${ContextPath}/weixinweb/img/close.png"
			onclick="hideBigImage()"
			style="position: absolute; right: 15px; top: 15px; width: 40px; height: 40px;" />
		<div id="image_big">
			<font style="color: #ED8E46; font-size: 14px;">*请填写车主姓名</font><input
				id="userName" name="userName" type="text" placeholder="请输入姓名">
			<font style="color: #ED8E46; font-size: 14px;">*请填写车主手机</font><input
				id="userMobile" name="userMobile" type="text" placeholder="请输入手机">
			<font style="color: #ED8E46; font-size: 14px;">*请填写车牌号码</font><input
				id="carCode" name="carCode" type="text" placeholder="请输入车牌号码">
			<button
				style="background: #ED8E46; margin-top: 12px; border-raduis: 5px; width: 100%; height: 40px; color: white; border: none;"
				onclick="doPrePayId()">提交</button>
		</div>
	</div>
	<input id="openId" value="${oppind}" style="display: none;">
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/jquery-1.8.2.min.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/layer_mobile/layer.js?${kjscb_time}"></script>
	<script
		src="https://a.amap.com/jsapi_demos/static/demo-center/js/demoutils.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="https://webapi.amap.com/maps?v=1.4.11&key=6ece49b46b1726d90003a810c41e69e0"></script>
	<script type="text/javascript"
		src="https://cache.amap.com/lbs/static/addToolbar.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/jweixin-1.4.0.js?${kjscb_time}"></script>
	<script type="text/javascript">
		var myPosition = []; //我的位置
		var checkStationObj = {}; //用于存放被选择检车站的信息
		var sumDistance = 0; //检车站与我之间的距离
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
				policy : AMap.DrivingPolicy.LEAST_FEE
			//AMap.DrivingPolicy.LEAST_DISTANCE //,
			//panel: "panel"
			});
			var geolocation = new AMap.Geolocation({
				enableHighAccuracy : true, //是否使用高精度定位，默认:true
				timeout : 10000, //超过10秒后停止定位，默认：5s
				buttonPosition : 'RB', //定位按钮的停靠位置
				buttonOffset : new AMap.Pixel(10, 20), //定位按钮与设置的停靠位置的偏移量，默认：Pixel(10, 20)
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
			log.success('定位成功' + +data.position);
			myPosition = getFormatPosition(data.position); //我的位置
			//获取检车站数据
			getStations();
		}
		//解析定位错误信息
		function onError(data) {
			log.error('定位失败');
			log.error('失败原因排查信息:' + data.message);
		}

		function getFormatPosition(pos) {
			var newPos = [];
			var cx = pos.toString().split(",");
			var xx = cx[0];
			var yy = cx[1];
			newPos[0] = xx;
			newPos[1] = yy;
			return newPos;
		}

		function initGuihua(endPos, desc) {
			sumDistance = 0;
			// 根据起终点经纬度规划驾车导航路线
			driving.search(new AMap.LngLat(myPosition[0], myPosition[1]),
					new AMap.LngLat(endPos[0], endPos[1]), function(status,
							result) {
						// result 即是对应的驾车导航信息，相关数据结构文档请参考  https://lbs.amap.com/api/javascript-api/reference/route-search#m_DrivingResult
						if (status === 'complete') {
							log.success('绘制驾车路线完成');
							var routes = result.routes;
							for (var i = 0; i < routes.length; i++) {
								var dis = routes[i].distance;
								sumDistance += dis;
							}
							sumDistance = Math.round(sumDistance / 1000);
							//获取价格
							getPriceAndPay(desc);
						} else {
							log.error('获取驾车数据失败：' + result);
						}

					});
		}

		//获取价格并进行支付
		function getPriceAndPay(desc) {
			$.ajax({
				type : "POST",
				url : getRootPath() + "emergency/getPrice",
				dataType : "json",
				success : function(result) {
					if (result.status == 0) {
						layerTip(result.message);
						return;
					}
					var sumPrice = sumDistance * result.data.unitPrice; //单价
					var deposit = result.data.deposit;//定金
					var text = '您选择了 [ ' + desc.checkStationName + ' ] ,距您['
							+ sumDistance + ']km,定金为：';
					var priceText = '￥' + deposit + "，总价为：" + sumPrice;
					setTextInfo(text, priceText);
					checkStationObj = desc;
					checkStationObj.kilometer = sumDistance;
					checkStationObj.deposit = deposit;
					checkStationObj.sumPrice = sumPrice;
				}
			});
		}
		
		//车牌的正则校验
		function isVehicleNumber(vehicleNumber) {
		      var result = false;
		      if (vehicleNumber.length == 7){
		        var express = /^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$/;
		        result = express.test(vehicleNumber);
		      }
		      return result;
		  }
		
		/**
		 * 微信预支付
		 * 
		 * @param openId
		 * @returns
		 */
		function doPrePayId() {
			var userName = $("#userName").val();
			var userMobile = $("#userMobile").val();
			var carCode = $("#carCode").val();

			if (null == userName || "" == userName) {
				layerTip("请输入姓名")
				return;
			}

			if (!(/^([\u4e00-\u9fa5]){2,7}$/.test(userName))) {
				layerTip('只能输入中文姓名，请重填~');
				return false;
			}

			if (null == userMobile || "" == userMobile) {
				layerTip("请输入手机")
				return;
			}
			if (!(/^1(3|4|5|7|8)\d{9}$/.test(userMobile))) {
				layerTip('手机号码有误，请重填~');
				return;
			}

			if (null == carCode || "" == carCode) {
				layerTip("请输入车牌号码")
				return;
			}
			
			if (!isVehicleNumber(carCode)) {
				layerTip("请输入正确的车牌号码")
				return;
			}

			checkStationObj.userName = userName;
			checkStationObj.userMobile = userMobile;
			checkStationObj.carCode = carCode;
			
			var openId = $("#openId").val();
			if (openId == null || "" == openId) {
				layerTip("微信未授权~");
				return;
			}

			checkStationObj.openId = openId;

			if (sumDistance == null || "" == sumDistance || sumDistance == 0) {
				layerTip("路线规划错误~");
				return;
			}

			hideBigImage();

			var index = loading("预约中……");

			// 获取预支付id
			$.ajax({
				type : 'POST',
				url : getRootPath() + "unifiedOrder",
				data : {
					"openId" : checkStationObj.openId,
					"refund" : checkStationObj.deposit
				},
				dataType : "json",
				success : function(res) {
					if (res.status == 1) {
						closeLoading(index); // 关闭loading
						onBridgeReady(res.param); // 调起控件
					} else {
						layerTip(res.msg);
					}
				},
				error : function() {
					layerTip("获取预支付id错误");
				}
			});
		}

		// 微信提供代码
		function onBridgeReady(payParam) {
			WeixinJSBridge.invoke('getBrandWCPayRequest', payParam, function(
					res) {
				// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
				if (res.err_msg == "get_brand_wcpay_request:ok") {
					submitOrder();
					WeixinJSBridge.call('closeWindow');
				}
			});
		}

		//获取检车站
		function getStations() {
			$.ajax({
						type : "GET",
						url : getRootPath() + "emergency/getStationList",
						dataType : "json",
						success : function(result) {
							if (result.status == 1) {
								var list = eval(result.data);
								if (null == list || list.length == 0) {
									layerTip(result.message);
									return;
								}

								var locationArray = new Array();
								var distanceArray = new Array();

								for (var i = 0; i < list.length; i++) {
									// 点标记显示内容，HTML要素字符串
									var markerContent = '<div class="custom-content-marker">'
											+ '   <img src="https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png">'
											+ '   <div class="close-btn">'
											+ list[i].checkStationName
											+ '</div>' + '</div>';
									var pos = [ list[i].checkStationX,
											list[i].checkStationY ];
									var marker = new AMap.Marker({
										map : map,
										//icon: "https://webapi.amap.com/theme/v1.3/markers/n/mark_b.png",
										position : pos,
										content : markerContent,
										offset : new AMap.Pixel(-13, -30), // 以 icon 的 [center bottom] 为原点
										extData : list[i]
									//.checkStationName
									});

									marker.on('click', showInfoM);

									locationArray.push(pos);
									// 返回 p1 到 p2 间的地面距离，单位：米
									var dis = AMap.GeometryUtil.distance(
											myPosition, pos);
									distanceArray.push(dis);
								}

								var minIndex = getMinIndex(distanceArray);
								var targetPos = locationArray[minIndex];

								//线路规划
								initGuihua(targetPos, list[minIndex]);
							}
						}
					});
		}

		function showInfoM(e) {
			//获取数据
			var extData = e.target.getExtData();
			var pos = [];
			pos[0] = e.lnglat.getLng();
			pos[1] = e.lnglat.getLat();
			initGuihua(pos, extData);
		}

		function setTextInfo(text, price) {
			document.querySelector("#text").innerText = text;
			document.querySelector("#price").innerText = price;
		}

		function getMinIndex(arr) {
			var max = arr[0];
			var min = arr[0];
			for (var i = 0; i < arr.length; i++) {
				if (arr[i] > max) {
					max = arr[i];
				} else if (arr[i] < min) {
					min = arr[i];
				}
			}
			var minIndex = 0;
			for (var j = 0; j < arr.length; j++) {
				if (arr[j] == min) {
					minIndex = j;
				}
			}

			return minIndex;
		}

		//保存订单数据对应t_emergency_order_info
		function submitOrder() {
			var loc = myPosition[0] + "," + myPosition[1];
			var addJson = {
				openid : checkStationObj.openId,//需要授权拿到
				carOwnerName : encodeURIComponent(checkStationObj.userName),
				carOwnerMobile : checkStationObj.userMobile,
				checkStationName : encodeURIComponent(checkStationObj.checkStationName),
				checkStationId : checkStationObj.id,
				orderLocation : encodeURIComponent(loc),
				deposit : checkStationObj.deposit,
				kilometer : checkStationObj.kilometer,
				sumPrice : checkStationObj.sumPrice,
				carCode : checkStationObj.carCode
			};
			$.ajax({
				type : "POST",
				url : getRootPath() + "emergencyOrderInfo/saveOrderInfo",
				data : addJson,
				dataType : "json",
				success : function(result) {
					if (result.status == 1) {
						layerTip(result.message);
						hideBigImage();
					}
				}
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

		function showBigImage() {
			$(".wrap_img").show();
		}

		function hideBigImage() {
			$(".wrap_img").hide();
			$("#userName").val("");
			$("#userMobile").val("");
		}

		/**通过检查车名称来获取客服人员或救援人员**/
		function getSerStaff(checkName) {
			var json = {
				checkName : checkName
			};
			
			$.ajax({
				type : "POST",
				url : getRootPath() + "emergencyOrderInfo/saveOrderInfo",
				data : json,
				dataType : "json",
				success : function(result) {
					if (result.status == 1) {
						layerTip(result.message);
						hideBigImage();
					}
				}
			});
		}

		//初始化select元素，
		function addOpToSelect(list) {
			//先清空
			$("#serId").empty();
			//再添加
			$("#serId").append('<option value="">请选择客服人员</option>');
			for (var i = 0; i < list.length; i++) {
				$("#serId").append(
						"<option value='" + '客服' + i + "'>" + '毕节瀚元机动车检查站-客服'
								+ i + "</option>");

			}
		}
	</script>
</body>

</html>