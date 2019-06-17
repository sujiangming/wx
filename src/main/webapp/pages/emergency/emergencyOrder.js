$(function() {
	init();
	// 定位表格查询框
	$(".search").css({
		'position' : 'fixed',
		'right' : '15px',
		'top' : '0',
		'z-index' : '1000',
		'width' : '240px'
	});
	$(".search input").attr("placeholder", "只能输入姓名");
	$(".search input").css({
		'padding-right' : '23px'
	});
	$('#myForm1').validationEngine();
});

function init() {
	// 初始化Table
	oTable = new TableInit();
	oTable.Init();
	var i = 0;
	$("#moreSearch").bind('click', function() {
		$("#more_search").slideToggle("slow", function() {
			if (i == 0) {
				i = 1;
				$("#moreSearch").html('更多查询&nbsp;<span class="caret"></span>');
				$("#moreSearch").addClass("dropup");
			} else {
				i = 0;
				$("#moreSearch").html('更多查询 <span class="caret"></span>');
				$("#moreSearch").removeClass("dropup");
			}
		});
	});
};

var TableInit = function() {
	var oTableInit = new Object();
	oTableInit.Init = function() {
		$('#materialsTableInfo').bootstrapTable({
			url : getRootPath() + "emergencyOrderInfo/listAll", // 请求后台的URL（*）
			toolbar : '#toolbar', // 工具按钮用哪个容器
			striped : true,
			search : true,
			searchOnEnterKey : true,
			showColumns : true, // 是否显示所有的列
			showRefresh : true, // 是否显示刷新按钮
			showToggle : true, // 是否显示详细视图和列表视图的切换按钮
			sortable : false,
			sortOrder : "asc",
			sidePagination : "server", // 分页方式：client客户端分页，server服务端分页（*）
			cache : false,
			clickToSelect : false,
			queryParams : oTableInit.queryParams,
			showExport : "true",
			minimumCountColumns : 2, // 最少允许的列数
			buttonsAlign : "left",
			buttonsClass : 'white',
			showPaginationSwitch : false,
			pagination : true,
			pageNumber : 1, // 初始化加载第一页，默认第一页
			pageSize : 10, // 每页的记录行数（*）
			pageList : '[10, 25, 50, 100,ALL]', // 可供选择的每页的行数（*）
			columns : [ {
				field : 'state',
				checkbox : true,
			}, {
				field : 'id',
				visible : false,
				title : 'id'
			}, {
				field : 'carOwnerName',
				title : '姓名'
			}, {
				field : 'carOwnerMobile',
				title : '手机号',
			}, {
				field : 'orderUserId',
				title : '微信号'
			}, {
				field : 'kilometer',
				title : '公里数',
			}, {
				field : 'payFee',
				title : '定金',
			}, {
				field : 'sumPrice',
				title : '总价',
			}, {
				field : 'typeState',
				title : '订单状态',
			}, {
				field : 'checkStationName',
				visible : false,
				title : '预约站点',
			}, {
				field : 'insertTime',
				visible : false,
				title : '插入时间',
			}, {
				field : 'id',
				title : '操作',
				align : 'center',
				events : operateEvents,
				formatter : operateFormatter
			} ],
			onCheck : function(row, e) {
				// tableCheckEvents();
			},
			onUncheck : function(row, e) {
				// tableCheckEvents();
			},
			onCheckAll : function(rows) {
				// $("#btn_del").attr("disabled",false);
			},
			onUncheckAll : function(rows) {
				// $("#btn_del").attr("disabled",true);
			},
			onLoadSuccess : function(rows) {
				// $("#btn_del").attr("disabled",true);
			}
		});
	};
	function operateFormatter(value, row, index) {
		var html = '<a id="a_check">查看 <span style="color:#CCC">|</span> </a>'
				+ '<a id="a_edit">派单<span style="color:#CCC">|</span></a><a id="a_finish">服务完成</a>';
		return html;
	}
	// 操作列的事件
	window.operateEvents = {
		'click #a_check' : function(e, value, row, index) {
			check(row);
		},

		'click #a_edit' : function(e, value, row, index) {
			if ($("#isManager").val() == 0) { // 区域管理员
				editOrCheck(row, 2);
			} else {
				layer.msg("没有权限", {
					time : 2000
				});
			}
		},

		'click #a_finish' : function(e, value, row, index) {
			if ($("#isManager").val() == 1) { // 客服人员
				finishOrder(row);
			} else {
				layer.msg("没有权限", {
					time : 2000
				});
			}
		}
	};

	oTableInit.queryParams = function(params) {
		var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			limit : params.limit, // 页面大小
			offset : params.offset, // 页码
			search : params.search
		};
		return temp;
	};
	return oTableInit;

}
// 表格选择事件
function tableCheckEvents() {
	var r = $('#materialsTableInfo').bootstrapTable('getSelections');
	if (r.length == 0) {
		$("#btn_del").attr("disabled", true);
	}
	if (r.length == 1) {
		$("#btn_del").attr("disabled", false);
	}
}

/**
 * 选择框
 * 
 * @returns
 */
function getIdSelections() {
	return $.map($('#materialsTableInfo').bootstrapTable('getSelections'),
			function(row) {
				return row.id;
			});
}

// 查看
function check(obj) {
	$('#myForm1').validationEngine('hide');

	$('#btEmpAdd').hide();
	$("#myModalTitle").html("查看");
	$("#carOwnerName").attr("disabled", true);
	$("#carOwnerMobile").attr("disabled", true);
	$("#kilometer").attr("disabled", true);
	$("#payFee").attr("disabled", true);
	$("#sumPrice").attr("disabled", true);
	$("#orderProject").attr("disabled", true);
	$("#isInnerPeople").attr("disabled", true);
	
	$("#id").val(obj.id);
	$("#carOwnerName").val(obj.carOwnerName);
	$("#carOwnerMobile").val(obj.carOwnerMobile);
	$("#kilometer").val(obj.kilometer);
	$("#payFee").val(obj.payFee);
	$("#sumPrice").val(obj.sumPrice);
	$("#orderProject").val(obj.typeState);
	$("#checkStationName").val(obj.checkStationName);
	$("#insertTime").val(obj.insertTime);

	if ($("#isInnerPeople").length > 0) {
		// 如果元素存在就先清空下面的元素
		$("#isInnerPeople").empty();
	}

	if(typeof(obj.staffName) != "undefined"){
		$("#isInnerPeople").append(
				"<option value='" + obj.staffName + "'>"
						+ obj.staffName + "</option>");
	}
	
	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
}

var orderUserWxId = null;

// 派工
function editOrCheck(obj, type) {
	if (obj.typeState == "派单中") {
		layer.msg("已派单，请勿重复派单", {
			time : 2000
		});
		orderUserWxId = null;
		return;
	}
	// 获取下单人的微信号
	orderUserWxId = obj.orderUserId;

	// 请求后台接口获取客户人员或救援人员
	getSerList();

	$('#myForm1').validationEngine('hide');

	$('#btEmpAdd').show();
	$("#myModalTitle").html("派单");
	$("#carOwnerName").attr("disabled", true);
	$("#carOwnerMobile").attr("disabled", true);
	$("#kilometer").attr("disabled", true);
	$("#payFee").attr("disabled", true);
	$("#sumPrice").attr("disabled", true);
	$("#orderProject").attr("disabled", true);
	$("#isInnerPeople").attr("disabled", false);

	$("#id").val(obj.id);
	$("#carOwnerName").val(obj.carOwnerName);
	$("#carOwnerMobile").val(obj.carOwnerMobile);
	$("#kilometer").val(obj.kilometer);
	$("#payFee").val(obj.payFee);
	$("#sumPrice").val(obj.sumPrice);
	$("#orderProject").val(obj.typeState);
	$("#checkStationName").val(obj.checkStationName);
	$("#insertTime").val(obj.insertTime);
	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
}

/**
 * 派单操作
 */
function sendOrder() {
	var addJson = getDataForm();
	$.ajax({
		type : "post",
		url : getRootPath() + "emergencyOrderInfo/sendOrder",
		data : addJson,
		dataType : "json",
		success : function(data) {
			if (data.status == "1") {
				layer.msg(data.message, {
					time : 2000
				}, function() {
					$('#materialsTableInfo').bootstrapTable('refresh');
					$('#myModal1').modal('hide');
				});
			} else {
				layer.msg(data.message, {
					time : 2000
				});
			}
		}
	});
}

// 服务完成
function finishOrder(jsonObj) {

	layer.confirm("您的服务确定完成了吗？", {
		skin : 'layui-layer-molv',
		btn : [ '确定', '取消' ]
	}, function() {
		var url = getRootPath() + "emergencyOrderInfo/save";
		$.post(url, {
			"id" : jsonObj.id
		}, function(data) {
			if (data.status == "1") {
				layer.msg(data.message, {
					time : 2000
				}, function() {
					$('#materialsTableInfo').bootstrapTable('refresh');
					$('#myModal1').modal('hide');
				});
			} else {
				layer.msg(data.message, {
					time : 2000
				});
			}
		}, "json");
	}, function() {
		return;
	});
}

/**
 * 获取表单数据
 */
function getDataForm() {
	var addJson = {
		carOwnerName : $("#carOwnerName").val(),
		carOwnerMobile : $("#carOwnerMobile").val(),
		kilometer : $("#kilometer").val(),
		payFee : $("#payFee").val(),
		sumPrice : $("#sumPrice").val(),
		orderProject : $("#orderProject").val(),
		checkStationName : $("#checkStationName").val(),
		kefuObj : $("#isInnerPeople").val(),
		insertTime : $("#insertTime").val(),
		orderId : $("#id").val(),
		orderUserId : orderUserWxId
	};
	return addJson;
}

/**
 * 获取派工人员信息
 * 
 * @returns
 */
function getSerList() {
	checkStationName = $("#csname").val();
	data = {
		"checkStation" : checkStationName
	};
	// 派单客服
	$.ajax({
		url : getRootPath() + "innerUser/selectcs",
		type : "post",
		data : data,
		dataType : "json",
		success : function(data) {
			if (data.messige == "success") {
				// 判断元素存在否
				if ($("#isInnerPeople").length > 0) {
					// 如果元素存在就先清空下面的元素
					$("#isInnerPeople").empty();
				}
				for (var i = 0; i < data.list.length; i++) {
					$("#isInnerPeople").append(
							"<option value='" + JSON.stringify(data.list[i])
									+ "'>" + data.list[i].innerUserName
									+ "</option>");
				}
			}
		}
	});
}
