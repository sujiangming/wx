$(function() {
	var checksn = window.location.search;
	var index = checksn.lastIndexOf("\?");
	var checkName = checksn.substring(index + 1, checksn.length).split(";");
	selectOrders(checkName[0],checkName[1]);
});

function showBigImage(url) {
	$(".wrap_img").show();
	$("#image_big").attr("src", url);
}

function hideBigImage() {
	$(".wrap_img").hide();
	$("#image_big").attr("src", "");
}

function selectOrders(checkStationName,kfid) {
	$.ajax({
		url : getRootPath() + "orderinfo/onelist",
		type : "post",
		data : {
			"checkStationName" : checkStationName,
			"kfid" : kfid
		},
		dataType : "json",
		success : function(data) {
			var result = eval(data);
			if (result.status == 1) {
				var list = result.list;
				var listObj = eval(list);
				var html = addOrderse(listObj);
				$("#orderContainer").html(html);
			}
		}
	});
}

function addOrderse(objs){
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
		html +='					<a href="tel://'+ objs[i].carOwnerMobile +'"><div style="margin-top: 6px;margin-bottom: 6px;">电话：' + objs[i].carOwnerMobile + '</div></a>';
		html +='					<div>车牌：' + objs[i].carCode + '</div>';
		html +='				</div>';
		html +='			</div>';
		html +='			<div class="bottom_div">';
		html +='				<div onclick="sendOrder('+JSON.stringify(objs[i]).replace(/\"/g,"'")+')">服务完成</div>';
		html +='			</div>';
		html +='		</div>';
	}
	return html;
}


function sendOrder(jsonObj){
	var index=showLoading("服务完成中……");
	$.ajax({
		url : getRootPath() + "orderinfo/save",
		type : "post",
		data : {
			"id":jsonObj.id,
			"openid":jsonObj.orderUserId,
			"carOwnerName":jsonObj.carOwnerName,
			"checkStationName":jsonObj.checkStationName,
			"carOwnerMobile":jsonObj.carOwnerMobile,
			"insertTime" : jsonObj.insertTime
		},
		dataType : "json",
		success : function(data) {
			if (data.status == 1) {
				closeLoading(index);
				layerTip("服务完成");
				window.location.reload();
			}
		}
	});
}

//封装的loading方法
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

function tanchu(obj){
	layer.open({
		type:1,
		content:'<img src="'+obj+'" />'
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