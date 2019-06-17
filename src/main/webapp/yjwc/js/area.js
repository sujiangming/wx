$(function() {
	var checksn = window.location.search;
	var index = checksn.lastIndexOf("\?");
	var checkName = checksn.substring(index + 1, checksn.length).split(";");
	selectOrders(checkName[0]);
});

function selectOrders(staffId) {
	$.ajax({
		url : getRootPath() + "emergencyOrderInfo/queryYJOrderByStaffId",
		type : "POST",
		data : {
			"staffId" : staffId
		},
		dataType : "json",
		success : function(data) {
			var result = eval(data);
			if (result.status == 1) {
				var list = result.data;
				var listObj = eval(list);
				var html = addOrderse(listObj);
				$("#orderContainer").html(html);
			}
		}
	});
}

function addOrderse(objs) {
	var html = "";
	for (var i = 0; i < objs.length; i++) {
		html += '	<div class="dispatch_wrap">';
		html += '			<div class="top_div">';
		html += '				<div>预约信息</div>';
		html += '				<div>' + objs[i].insertTime + '</div>';
		html += '			</div>';
		html += '			<div class="center_div">';
		html += '				<div class="center_div_left">';
		html += '					<img src="https://www.kjscb.com/wx/weixinweb/img/logo.png"/>';
		//html += '					<img src="http://localhost:8080/wx/weixinweb/img/logo.png"/>';
		html += '				</div>';
		html += '				<div class="center_div_center"></div>';
		html += '				<div class="center_div_right">';
		html += '					<div>姓名：' + objs[i].carOwnerName + '</div>';
		html += '					<a href="tel://' + objs[i].carOwnerMobile
				+ '"><div style="margin-top: 6px;margin-bottom: 6px;">电话：'
				+ objs[i].carOwnerMobile + '</div></a>';
		html += '					<div>车牌：' + objs[i].carCode + '</div>';
		html += '				</div>';
		html += '			</div>';
		html += '			<div class="bottom_div">';
		html += '				<div onclick="goNavPage('+ '\'' + objs[i].orderLocation + '\'' + ',' + '\'' + objs[i].checkStationName+'\'' +')">查看导航</div>';		
		html += '				<div onclick="sendOrder('
				+ JSON.stringify(objs[i]).replace(/\"/g, "'") + ')">服务完成</div>';
		html += '			</div>';
		html += '		</div>';
	}
	return html;
}

function goNavPage(position,stationName){
	var loc = encodeURIComponent(position);
	var name = encodeURIComponent("施救位置");
	location.href = "https://www.kjscb.com/wx/weixinweb/map/nav_guide.jsp?" + loc + ";" + name;
}

function sendOrder(jsonObj) {
	var index = showLoading("服务完成中……");
	$.ajax({
		url : getRootPath() + "emergencyOrderInfo/save",
		type : "POST",
		data : {
			"id" : jsonObj.id
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

function tanchu(obj) {
	layer.open({
		type : 1,
		content : '<img src="' + obj + '" />'
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