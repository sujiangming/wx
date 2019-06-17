var imagePath1 = "";
var imagePath2 = "";
var imagePath = "";
/**
 * 上传图片
 * 
 * @returns
 */
function uploadImage(el) {
	var index = showLoading("图片上传中……");
	var that = el;
	var elId = that.id;
	lrz(that.files[0], {
		width : 300
	}).then(function(rst) {
		var submitData = {
			image : rst.base64,
			name : rst.origin.name,
			fileLength : rst.base64.length
		};

		$.ajax({
			type : "POST",
			url : getRootPath() + "upimg",
			data : submitData,
			dataType : "json",
			success : function(data) {
				// 关闭loading
				closeLoading(index);
				if ("success" == data.mse) {
					layerTip("上传成功");
					if (elId == "input1") {
						imagePath1 = data.path;
						$("#blah1").attr("src", imagePath1);
						$("#blah1").show();
						$("#input1").attr("disabled", true);
					} else {
						imagePath2 = data.path;
						$("#blah2").attr("src", imagePath2);
						$("#blah2").show();
						$("#input2").attr("disabled", true);
					}
					return;
				}
				layerTip("上传失败");
			}
		});
		return rst;
	});
}
/**
 * 获取检车站列表
 * 
 * @returns
 */
function getStation() {
	$.ajax({
		url : getRootPath() + "checkStation/list",
		type : "post",
		dataType : "json",
		success : function(result) {
			if ($("#checkStationName").length > 0) {
				$("#checkStationName").empty();
			}
			if (result.status == 1) {
				var list = eval(result.data);
				if (null == list || list.length == 0) {
					return;
				}
				$("#checkStationName")
				.append(
						'<option value="1" selected="selected">--请选择--</option>');
				for (var i = 0; i < list.length; i++) {
					$("#checkStationName").append(
							"<option value='" + list[i].checkStationName + "'>"
									+ list[i].checkStationName + "</option>");

				}
			}

		}

	});
}
/**
 * 根据所选检车站获取车型列表和获取对应的时间段和人数列表
 * 
 * @param stationName
 * @returns
 */
function checkStationOnChangeEvent(stationName) {
	chexin(stationName);
	getTimeCountList(stationName);
}

/**
 * 获取车型列表
 * 
 * @param stationName
 * @returns
 */
function chexin(stationName) {
	var data = {
		"checkStationName" : stationName
	}

	$
			.ajax({
				url : getRootPath() + "carTypePrice/list",
				type : "post",
				data : data,
				dataType : "json",
				success : function(result) {
					if ($("#carType").length > 0) {
						$("#carType").empty();
					}
					if (result.status == 1) {
						var list = eval(result.data);
						if (null == list || list.length == 0) {
							return;
						}
						$("#carType")
								.append(
										'<option value="1" selected="selected">--请选择--</option>');
						for (var i = 0; i < list.length; i++) {
							$("#carType").append(
									"<option value='" + list[i].carType + "'>"
											+ list[i].carType + "("
											+ list[i].carStandard + ")"
											+ "</option>");
						}
					}
				}

			});
}

/**
 * 根据检车站名称来查询所有的时间段和人数
 * 
 * @returns
 */
function getTimeCountList(stationName) {

	if (null == stationName || "" == stationName) {
		layerTip("请先选择检车站~");
		return;
	}
	var json = {
		"checkStationName" : stationName
	};
	$.ajax({
		url : getRootPath() + "timeCount/selectListByName",
		data : json,
		dataType : "json",
		type : "post",
		success : function(result) {
			if ($("#orderTime").length > 0) {
				$("#orderTime").empty();
			}

			$("#orderTime").append(
					'<option value="1" selected="selected">--请选择--</option>');

			if (null == result || result == "") {
				layerTip("所选检车站暂没预设时间段~");
				return;
			}
			var list = result.data;
			if (null == list || list == "" || list.length == 0) {
				layerTip("所选检车站暂没预设时间段~");
				return;
			}
			for (var i = 0; i < list.length; i++) {
				$("#orderTime").append(
						"<option flag='" + list[i].orderCount + "' value='"
								+ list[i].orderTime + "'>" + list[i].orderTime
								+ "</option>");
			}
		}

	});
}

/**
 * 车辆类型对应的价格列表
 * 
 * @param carType
 * @returns
 */
function getCarTypePrice(carType) {
	var checkStationName = $("#checkStationName").val();
	if (checkStationName != "") {
		var data = {
			"carType" : carType,
			"checkStationName" : checkStationName
		}
		$.ajax({
			url : getRootPath() + "carTypePrice/money",
			type : "post",
			data : data,
			dataType : "json",
			success : function(result) {
				if (result.status == 1) {
					var meg = eval(result.data);
					$("#sumPrice").text("总价：￥" + meg.carPrice);
					$("#djPrice").text(meg.deposit);

					$("#sumPrice").show();
					$("#dingjin").show();
				}
			}
		});
	}

};

/**
 * 获取时间段及剩余次数
 * 
 * @param el
 *            dom元素对象
 * @returns
 */
function getTimeOrderCount(el) {

	// 选择检车站
	var stationName = $("#checkStationName").val();
	if (null == stationName || "" == stationName) {
		layerTip("请先选择检车站~");
		return;
	}
	// 预约日期
	var orderDate = $("#orderDate").val();// el.value;
	if (null == orderDate || orderDate == "") {
		layerTip('请选择预约日期~');
		return;
	}
	// 预约时间段
	var orderTime = el.value;// $("#orderTime").val();
	if (null == orderTime || "" == orderTime) {
		layerTip("请先选择预约时间段~");
		return;
	}

	var data = {
		"checkStationName" : stationName,
		"orderDate" : orderDate,
		"orderTime" : orderTime
	}
	$.ajax({
		url : getRootPath() + "timeOrderCount/select",
		data : data,
		dataType : "json",
		type : "post",
		success : function(result) {
			if (result.status == 0) {
				// 获取时间段对应的预约人数
				var leftCount = setOrderCount(el);
				console.log("=leftCount=" + leftCount);
				$("#orderCount").text(leftCount);
				return;
			}
			var timeOrderCount = result.data;
			$("#orderCount").text(timeOrderCount.orderCount);
		}
	});
}

/**
 * 获取所选时间段对应的可以预约的剩余次数
 * 
 * @param el
 * @returns
 */
function setOrderCount(el) {
	$el = el;
	var els = $el.options[$el.options.selectedIndex];
	var flag = $(els).attr("flag");
	// $("#orderCount").text(flag);
	return flag;
}

/**
 * 微信预支付
 * 
 * @param openId
 * @returns
 */
var newOpenId = "";
function doPrePayId(openId) {
	newOpenId = openId;
	if (!verifyCar(openId)) {
		return;
	}
	var index = showLoading("预约中……");
	// 获取预支付id
	$.ajax({
		type : 'POST',
		url : getRootPath() + "unifiedOrder",
		data : {
			"openId" : openId,
			"refund" : $("#djPrice").text()
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
	WeixinJSBridge.invoke('getBrandWCPayRequest', payParam, function(res) {
		// 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
		if (res.err_msg == "get_brand_wcpay_request:ok") {
			openidValue();
			WeixinJSBridge.call('closeWindow');
		}
	});
}

// 判断是否是工作日
function isWorkDay(el) {
	var chooseDate = el.value;
	var tt = isCurrentDate(chooseDate);
	if (!tt) {
		layerTip('所选日期不能为当前日期与之前日期！');
		el.value = "";
		return;
	}
	if (isHoliday(chooseDate)) {
		layerTip('所选日期不是工作日！');
		el.value = "";
		return;
	}
}

function isCurrentDate(chooseDay) {
	var d = new Date;
	var today = new Date(d.getFullYear(), d.getMonth(), d.getDate());
	var reg = /\d+/g;
	var temp = chooseDay.match(reg);
	var foday = new Date(temp[0], parseInt(temp[1]) - 1, temp[2]);
	if (foday > today) {
		return true;
	}
	return false;
}

function verifyCar(openId) {
	var mobile = $("#carOwnerMobile").val();
	var name = $("#carOwnerName").val();
	// 车主姓名：
	if (name == "") {
		layerTip('请输入车主姓名~');
		return false;
	}
	if (!(/^([\u4e00-\u9fa5]){2,7}$/.test(name))) {
		layerTip('只能输入中文姓名，请重填~');
		return false;
	}
	if (mobile == "") {
		layerTip('请输入车主电话~');
		return false;
	}
	if (!(/^1(3|4|5|7|8)\d{9}$/.test(mobile))) {
		layerTip('手机号码有误，请重填~');
		return false;
	}
	// 车牌号码：
	if ($("#CarNO").val() == "") {
		layerTip('请输入车牌号码~');
		return false;
	}
	// 车辆类型：
	if ($("#carType").val() == "") {
		layerTip('请选择车辆类型~');
		return false;
	}

	// 保险日期：
	if ($("#Date").val() == "") {
		layerTip('请选择保险日期~');
		return false;
	}

	// 选择检车站：
	if ($("#checkStationName").val() == "") {
		layerTip('请选择检车站~');
		return false;
	}

	// 预约日期
	if ($("#orderDate").val() == "") {
		layerTip('请选择预约日期~');
		return false;
	}

	// 预约时间
	if ($("#orderTime").val() == "") {
		layerTip('请选择预约时间段~');
		return false;
	}

	// 上传行车证
	if (imagePath1 == "" || typeof (imagePath1) == undefined
			|| null == imagePath1) {
		layerTip('请上传行车证主页~');
		return false;
	}

	if (imagePath2 == "" || typeof (imagePath2) == undefined
			|| null == imagePath2) {
		layerTip('请上传行车证副页~');
		return false;
	}

	if ($("#orderCount").text() == "" || $("#orderCount").text() == 0) {
		layerTip('当日预约次数已完，请明天再来~');
		return false;
	}

	// 判断openId是否为空
	if (null == openId || "" == openId) {
		layerTip("微信授权登录失败，请重试！");
		return false;
	}

	var refund = $("#djPrice").text();
	if (null == refund || "" == refund) {
		layerTip("未获取到价格，请重试！");
		return false;
	}

	return true;
}

// 保存审车信息
function openidValue() {
	var carOwnerName = $("#carOwnerName").val();
	var carOwnerMobile = $("#carOwnerMobile").val();
	var carCode = $("#CarNOSF").val() + $("#CarNOXS").val() + $("#CarNO").val();
	var carType = $("#carType").val();
	var carInsurance = $("#Date").val();
	var checkStationName = $("#checkStationName").val();
	var orderDate = $("#orderDate").val();
	var orderTime = $("#orderTime").val();

	var data = {
		"carType" : encodeURIComponent(carType),
		"carOwnerName" : encodeURIComponent(carOwnerName),
		"carOwnerMobile" : encodeURIComponent(carOwnerMobile),
		"carCode" : encodeURIComponent(carCode),
		"carInsurance" : encodeURIComponent(carInsurance),
		"checkStationName" : encodeURIComponent(checkStationName),
		"orderDate" : orderDate,
		"orderTime" : encodeURIComponent(orderTime),
		"imagePath" : imagePath1 + ";" + imagePath2,
		"openid" : newOpenId
	}

	$.ajax({
		url : getRootPath() + "saveOrder",
		type : "post",
		data : data,
		dataType : "json",
		success : function(data) {
			if (data.msg == "success") {
				layerTip("您的申请已提交成功，工作人员会尽快给您去电，请保持手机畅通，请稍等");
				WeixinJSBridge.call('closeWindow');
			}
		}
	});
}

// 封装的loading方法
function showLoading(text) {
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

function getRootPath() {
	// 获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
	var curWwwPath = window.document.location.href;
	// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	// 获取主机地址，如： http://localhost:8083
	var localhostPaht = curWwwPath.substring(0, pos);
	// 获取带"/"的项目名，如：/uimcardprj
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 2);
	return (localhostPaht + projectName);
};