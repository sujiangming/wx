<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.bj.scb.pojo.SNSUserInfo,java.lang.*"%>
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
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,initial-scale=1.0" />
<title>审车预约</title>
<style type="text/css">
html, body, select, input {
	padding: 0;
	margin: 0;
}

input {
	background: none;
	outline: none;
	border: 0px;
	color: #173177;
}

input::-webkit-input-placeholder {
	color: #aab2bd;
	font-size: 12px;
}

select {
	display: block;
	font-size: 12px;
	color: #173177;
	background: none;
	outline: none;
	border: 0px;
}

select option {
	color: #173177;
}

.order_item {
	height: 44px;
	background: white;
	line-height: 44px;
	margin-bottom: 1px;
	font-size: 14px;
	color: #686868;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: center;
}

.order_item .order_img_left {
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-flex: 1.0;
}

.order_item .order_img {
	height: 100%;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-flex: 2.0;
	-webkit-box-align: center;
	-webkit-box-pack: end;
}

.order_item .order_item_first_child {
	width: 30%;
	text-align: right;
}

.order_item .order_item_second_child {
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: center;
	width: 65%;
	padding-left: 12px;
	height: 100%;
}

.order_car_wrap {
	height: 100px;
	background: white;
	line-height: 44px;
	margin-bottom: 1px;
	font-size: 14px;
	color: #686868;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: center;
}

.order_car_wrap .order_car_wrap_first_div {
	width: 25%;
	text-align: right;
}

.order_car_wrap_licence {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	width: 70%;
	padding-left: 12px;
	height: 100%;
}

.order_car_wrap_licence .car_select_div_1 {
	display: -webkit-box;
	-webkit-box-flex: 1.0;
	height: 33%;
	border-bottom: 1px solid #F4F6FC;
}

.car_select_div_1 select {
	display: block;
	width: 100%;
	height: 100%;
}

.order_car_wrap_licence .car_select_div_3 {
	height: 33%;
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-box-flex: 1.0;
	-webkit-box-pack: center;
}

.car_select_div_3 input {
	display: block;
}

.order_item_second_child .select_style {
	display: block;
	width: 100%;
}

.order_item_second_child .input_style {
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: center;
	height: 44px;
	line-height: 44px;
	width: 100%;
}

.order_item_second_child .car_pro_select_div {
	display: -webkit-box;
	-webkit-box-flex: 1.0;
}

.btn_right_now {
	height: 44px;
	background: darkred;
	margin-top: 40px;
	margin-left: 10px;
	margin-right: 10px;
	border-radius: 5px;
	text-align: center;
	line-height: 44px;
	color: white;
}

#orderCount {
	color: red;
}

.img_upload_container {
	height: 100px;
	background: white;
	line-height: 44px;
	margin-bottom: 1px;
	font-size: 14px;
	color: #686868;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: center;
}

.img_upload_container .img_upload_container_first_div {
	width: 30%;
	text-align: right;
}

.img_upload_container>.img_upload_container_second_div {
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	width: 68%;
	padding-left: 5px;
	height: 100%;
}

.img_upload_container_second_div>div {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-box-align: center;
	-webkit-box-pack: center;
	width: 45%;
	margin-right: 5px;
	vertical-align: middle;
}

.img_upload_container_second_div>div>img {
	display: block;
	width: 100%;
	height: 100%;
}
</style>
</head>

<body style="background: #F5F5F5;">
	<div class="order_item" style="background: #FBFDF3;">
		<div class="order_img_left">
			<span style="margin-left: 12px;">请填写预约信息</span>
		</div>
		<div class="order_img">
			<img src="${ContextPath}/weixinweb/img/logo.png"
				style="width: 40px; height: 40px; margin-right: 15px; display: block;" />
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">车主名称：</div>
		<div class="order_item_second_child">
			<input id="carOwnerName" class="input_style" type="text"
				placeholder="请输入车主名称" />
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">车主电话：</div>
		<div class="order_item_second_child">
			<input id="carOwnerMobile" class="input_style" type="number"
				placeholder="请输入车主电话" />
		</div>
	</div>
	<div class="order_car_wrap">
		<div class="order_car_wrap_first_div">车牌号码：</div>
		<div class="order_car_wrap_licence">
			<div class="car_select_div_1">
				<select id="CarNOSF" name="CarNOSF">
					<option value="贵">贵</option>
					<option value="晋">晋</option>
					<option value="京">京</option>
					<option value="津">津</option>
					<option value="冀">冀</option>
					<option value="内">内</option>
					<option value="辽">辽</option>
					<option value="吉">吉</option>
					<option value="黑">黑</option>
					<option value="沪">沪</option>
					<option value="苏">苏</option>
					<option value="浙">浙</option>
					<option value="皖">皖</option>
					<option value="闽">闽</option>
					<option value="赣">赣</option>
					<option value="鲁">鲁</option>
					<option value="豫">豫</option>
					<option value="鄂">鄂</option>
					<option value="湘">湘</option>
					<option value="粤">粤</option>
					<option value="桂">桂</option>
					<option value="琼">琼</option>
					<option value="川">川</option>
					<option value="云">云</option>
					<option value="渝">渝</option>
					<option value="陕">陕</option>
					<option value="甘">甘</option>
					<option value="青">青</option>
					<option value="宁">宁</option>
					<option value="藏">藏</option>
					<option value="新">新</option>
				</select>
			</div>
			<div class="car_select_div_1">
				<select id="CarNOXS" name="CarNOXS">
					<option value="A">A</option>
					<option value="B">B</option>
					<option value="C">C</option>
					<option value="D">D</option>
					<option value="E">E</option>
					<option value="F" selected="selected">F</option>
					<option value="G">G</option>
					<option value="H">H</option>
					<option value="J">J</option>
					<option value="K">K</option>
					<option value="L">L</option>
					<option value="M">M</option>
					<option value="N">N</option>
					<option value="P">P</option>
					<option value="Q">Q</option>
					<option value="R">R</option>
					<option value="S">S</option>
					<option value="V">V</option>
				</select>
			</div>
			<div class="car_select_div_3">
				<input id="CarNO" name="CarNO" type="text" placeholder="请输入车牌号码" />
			</div>
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">选检车站：</div>
		<div class="order_item_second_child">
			<select id="checkStationName" class="select_style" style="height:100%"
				onchange="checkStationOnChangeEvent(this.value)">
				<!-- <option value="">请选择</option> -->
			</select>
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">车辆类型：</div>
		<div class="order_item_second_child">
			<select id="carType" class="select_style" name="carType"
				onchange="getCarTypePrice(this.value)">
				<option value="">请选择</option>
			</select>
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">保险日期：</div>
		<div class="order_item_second_child">
			<input id="Date" name="carInsurance" type="date" class="input_style"
				placeholder="请选择保险日期" />
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">预约日期：</div>
		<div class="order_item_second_child">
			<input id="orderDate" onchange="isWorkDay(this)" class="input_style"
				name="orderDate" type="date" placeholder="请选择预约日期" />
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">预约时间：</div>
		<div class="order_item_second_child">
			<select id="orderTime" class="select_style" name="orderTime"
				onchange="getTimeOrderCount(this)">
				<option value="1" selected="selected">请选择</option>
			</select>
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">剩余次数：</div>
		<div class="order_item_second_child">
			<font id="orderCount"></font>
		</div>
	</div>
	
	<div class="img_upload_container">
		<div class="img_upload_container_first_div">上传行车证：</div>
		<div class="img_upload_container_second_div">
			<div id="up_image_1"
				style="position: relative; border: 1px dashed #999999;">
				<font id="font_1"
					style="z-index: 150; position: absolute; width: 100%; height: 98px; line-height: 98px; font-size: 12px; text-align: center;">点击上传主页</font>
				<input type='file' accept="image/*" id="input1" name="carLicence"
					style="float: left; z-index: 150; position: absolute; width: 100%; height: 98px; opacity: 0;" onchange="uploadImage(this)" />
				<img id="blah1"
					style="z-index: 200; position: absolute; width: 80%; height: 80%; left: 10%; top: 10%; display: none;"
					src="img/photo.png" />
			</div>
			<div id="up_image_2"
				style="position: relative; border: 1px dashed #999999;">
				<font id="font_1"
					style="z-index: 150; position: absolute; width: 100%; height: 98px; line-height: 98px; font-size: 12px; text-align: center;">点击上传副页</font>
				<input type='file' accept="image/*" id="input2" name="carLicence"
					style="float: left; z-index: 350; position: absolute; width: 100%; height: 98px; opacity: 0;" onchange="uploadImage(this)"/>
				<img id="blah2"
					style="z-index: 200; position: absolute; width: 80%; height: 80%; left: 10%; top: 10%; display: none;"
					src="img/photo.png" />
			</div>
		</div>
	</div>
	<div class="order_item">
		<div class="order_item_first_child">价格信息：</div>
		<div class="order_item_second_child">
			<font id="sumPrice" style="color: brown; display: none;">总价：￥450</font>
			<font id="dingjin"
				style="color: red; margin-left: 15px; display: none;">定金：￥<span
				id="djPrice">40</span></font>
		</div>
	</div>
	<div class="btn_right_now" onclick="doPrePayId('${oppind}');">立即预约</div>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/jquery-1.8.2.min.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/lrz.bundle.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/layer_mobile/layer.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/workday.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/jweixin-1.4.0.js?${kjscb_time}"></script>
	<script type="text/javascript"
		src="${ContextPath}/weixinweb/app/js/changeCard.js?${kjscb_time}"></script>
	<script type="text/javascript">
		//var imagePath = "";
		getStation(); //获取检车站列表
		//initLrz();
	</script>
</body>
</html>