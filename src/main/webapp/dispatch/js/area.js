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
		$(".s_province").append(
				'<option value="' + obj[i].id + '">' + obj[i].innerUserName
						+ '</option>');
	}
}

var optionValue = null;
function selectedOption(select) {
	optionValue = select;
}

function selectOrders(checkStationName) {
	$.ajax({
		url : getRootPath() + "order/selectOrders",
		type : "post",
		data : {
			"checkStation" : checkStationName//checkStationName
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
		var images = objs[i].carLicence.split(";");
		html += '	<div class="dispatch_wrap">';
		html +='			<div class="top_div">';
		html +='				<div>预约信息</div>';
		html +='				<div>'+ objs[i].orderTime +'</div>';
		html +='			</div>';
		html +='			<div class="center_div">';
		html +='				<div class="center_div_left">';
		html +='					<img src="' + images[0] + '" onclick="showBigImage('+ "'" + images[0]+"'"+')"/>';
		html +='					<img src="' + images[1] + '" onclick="showBigImage('+ "'" + images[1]+"'"+')"/>';
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
		html +='					<select class="s_province" onchange="selectedOption(this.options[this.options.selectedIndex].value)">';
		html +='					</select>';
		html +='				</div>';
		html +='				<div onclick="sendOrder('
			+ JSON.stringify(objs[i]).replace(/\"/g, "'")
			+ ')">立即派单</div>';
		html +='			</div>';
		html +='		</div>';
	}
	return html;
}



function sendOrder(jsonObj) {
	var isInnerPeople = optionValue;
	if (isInnerPeople == null) {
		layerTip("请选择客服人员");
		return;
	}

	var index = showLoading("正在派单中……");

	$.ajax({
		url : getRootPath() + "orderinfo/saveOrder",
		type : "post",
		data : {
			"carOwnerName" : jsonObj.carOwnerName,
			"carCode" : jsonObj.carCode,
			"carLicence" : jsonObj.carLicence,
			"carOwnerMobile" : jsonObj.carOwnerMobile,
			"carInsurance" : jsonObj.carInsurance,
			"orderTime" : jsonObj.orderTime,
			"orderProject" : jsonObj.orderProject,
			"checkStationName" : jsonObj.checkStationName,
			"kefuid" : isInnerPeople,
			"orderUserId" : jsonObj.orderUserId,//insertTime
			"insertTime" : jsonObj.insertTime
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