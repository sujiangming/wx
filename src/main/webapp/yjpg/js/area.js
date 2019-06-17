$(function() {
	var checksn = window.location.search;
	var index = checksn.lastIndexOf("\?");
	var checkStationName = checksn.substring(index + 1, checksn.length).split(";");
	selectOrders(checkStationName[0]);
});

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

function showBigImage(url) {
	$(".wrap_img").show();
	$("#image_big").attr("src", url);
}
function hideBigImage() {
	$(".wrap_img").hide();
	$("#image_big").attr("src", "");
}

function tanchu(obj) {
	layer.open({
		type : 1,
		content : '<img src="' + obj + '" />'
	});
}

function kefu(checkStationName) {
	$.ajax({
		url : getRootPath() + "innerUser/selectcs",
		type : "post",
		data : {
			"checkStation" : checkStationName
		},
		dataType : "json",
		success : function(data) {
			if (data.messige == "success") {
				if (null == data.list || data.list.length == 0) {
					return;
				}
				addOptions(data.list);
			}
		}
	});
}

function addOptions(obj) {
	// 先清空
	if ($(".s_province").length > 0) {
		$(".s_province").empty();
	}
	// 再添加
	$(".s_province").append("<option selected value=''>---请选择---</option>")
	for (var i = 0; i < obj.length; ++i) {
		var itemValue = obj[i].id + ";" + obj[i].innerUserId + ";" + obj[i].innerUserMobile; 
		$(".s_province").append(
				'<option value="' + itemValue + '">' + obj[i].innerUserName
						+ '</option>');
	}
}

function selectOrders(checkStationName) {
	$.ajax({
		url : getRootPath() + "emergencyOrderInfo/queryYJOrderByName",
		type : "post",
		data : {
			"checkStation" : checkStationName
		},
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				var html = addOrders(data.m);
				$("#orderContainer").html(html);
				// 获取工作人员
				kefu(checkStationName);
			}
		}
	});
}

function addOrders(objs){
	var html="";
	for (var i=0; i<objs.length; i++){
		html += '	<div class="dispatch_wrap">';
		html +='			<div class="top_div">';
		html +='				<div>预约信息</div>';
		html +='				<div>'+ objs[i].insertTime +'</div>';
		html +='			</div>';
		html +='			<div class="center_div">';
		html +='				<div class="center_div_left">';
		html +='					<img src="https://www.kjscb.com/wx/weixinweb/img/logo.png"/>';
		//html +='					<img src="http://localhost:8080/wx/weixinweb/img/logo.png"/>';
		html +='				</div>';
		html +='				<div class="center_div_center"></div>';
		html +='				<div class="center_div_right">';
		html +='					<div>姓名：' + objs[i].carOwnerName + '</div>';
		html +='					<div style="margin-top: 6px;margin-bottom: 6px;">电话：' + objs[i].carOwnerMobile + '</div>';
		html +='					<div>车牌：' + objs[i].carCode + '</div>';
		html +='				</div>';
		html +='			</div>';
		html +='			<div class="bottom_div">';
		html +='				<div>';
		html +='					<select class="s_province" onchange="selectedOption(this.options[this.options.selectedIndex])">';
		html +='					</select>';
		html +='				</div>';
		html +='				<div onclick="sendOrder('+ JSON.stringify(objs[i]).replace(/\"/g, "'")+ ')">立即派单</div>';
		html +='			</div>';
		html +='		</div>';
	}
	return html;
}

var optionStaffIdValue = null;
var optionName = null;
var optionStaffWxIdValue = null;
var optionUserMobile = null;
function selectedOption(obj) {
	var values = obj.value.split(";");
	optionStaffIdValue = values[0];
	optionStaffWxIdValue = values[1];
	optionUserMobile = values[2];
	optionName = obj.innerHTML;
}

function sendOrder(jsonObj) {
	var innerPeopleId = optionStaffIdValue;
	if (innerPeopleId == null) {
		layerTip("请选择客服人员");
		return;
	}

	var index = showLoading("正在派单中……");

	$.ajax({
		url : getRootPath() + "emergencyOrderInfo/updateOrderInfo",
		type : "post",
		data : {
			"id" : jsonObj.id,
			"staffId" : innerPeopleId,
			"staffName" : optionName,
			"staffWxIdValue" : optionStaffWxIdValue,//客服的微信号
		    "staffMobile":optionUserMobile,
			"carOwnerName" : jsonObj.carOwnerName, //预约人的名称
			"checkStationName" : jsonObj.checkStationName,
			"orderWxId" : jsonObj.orderUserId,//预约人的微信号
			"typeState" : "派单中"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				closeLoading(index);
				layerTip(data.message);
				window.location.reload();
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
	var projectName = pathName
			.substring(0, pathName.substr(1).indexOf('/') + 2);
	return (localhostPaht + projectName);
};