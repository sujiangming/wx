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
	// initBtnEvent();
	// initUpload();
});

function paidan() {
	checkStationName = $("#csname").val();
	data = {
		"checkStation" : checkStationName
	};
	// 派单客服
	$
			.ajax({
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
									"<option value='" + data.list[i].id + "'>"
											+ data.list[i].innerUserName
											+ "</option>");

						}
					}
				}
			});

}

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
			url : getRootPath() + "order/list", // 请求后台的URL（*）
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
				field : 'carCode',
				align : 'left',
				title : '车牌号',
			}, {
				field : 'carLicence',
				title : '行车证',
				formatter : impopen
			}, {
				field : 'carOwnerMobile',
				title : '手机号',
			}, {
				field : 'carInsurance',
				title : '车辆保险时间',
			}, {
				field : 'orderTime',
				title : '预约时间段',
			}, {
				field : 'orderProject',
				title : '预约车型',
			}, {
				field : 'checkStationName',
				visible : false,
				title : '检车站'
			}, {
				field : 'insertTime',
				visible : false,
				title : '插入时间'
			}, {
				field : 'typeState',
				title : '状态'
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
	function impopen(value, row, index){
		var imgs=value.split(";");
		var html = "<img width=30  src='"+imgs[0]+"' /><img width=30  src='"+imgs[1]+"' />";
		return html;
	}
	function operateFormatter(value, row, index) {

		var html = "<a id='a_edit' onclick='paidan()'>派单</a>";
		return html;
	}
	// 操作列的事件
	window.operateEvents = {
		'click #a_check' : function(e, value, row, index) {
			editOrCheck(row, 1);
		},
		'click #a_edit' : function(e, value, row, index) {
			editOrCheck(row, 2);
		}
	};

	oTableInit.queryParams = function(params) {
		var temp = { // 这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
			limit : params.limit, // 页面大小
			offset : params.offset, // 页码
			search : params.search
		// 表格搜索框的值
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

/**
 * 保存操作
 */
function openSaveButton() {
	var flag = $('#myForm1').validationEngine('validate');
	if(flag){
		var addJson = getDataForm();
		$.ajax({
			type : "post",
			url : getRootPath() + "orderinfo/saveOrder",
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
	}else{
		layer.msg('表单验证未通过！',{
			time: 3000
		});
	}
}

/**
 * 获取表单数据
 */
function getDataForm() {
	var addJson = {
		carOwnerName : $("#carOwnerName").val(),
		carCode : $("#carCode").val(),
		carLicence : $("#carLicence").val(),
		carOwnerMobile : $("#carOwnerMobile").val(),
		carInsurance : $("#carInsurance").val(),
		orderTime : $("#orderTime").val(),
		orderProject : $("#orderProject").val(),
		checkStationName : $("#checkStationName").val(),
		kefuid : $("#isInnerPeople").val(),
		insertTime : $("#insertTime").val(),
	};
	return addJson;
}

// 清空表单
function clearForm() {
	$("#carOwnerName").val("");
	$("#carCode").val("");
	$("#carLicence").val("");
	$("#carOwnerMobile").val("");
	$("#carInsurance").val("");
	$("#orderTime").val("");
	$("#orderProject").val("");

}

// 查看和编辑
function editOrCheck(obj, type) {
	if (obj.typeState == "派单中") {
		layer.msg("已派单，请勿重复派单", {
			time : 2000
		});
		return;
	}
	$('#myForm1').validationEngine('hide');
	if (type == 1) {
		$('#btEmpAdd').hide();
		$("#myModalTitle").html("查看");
		$("#carOwnerName").attr("disabled", true);
		$("#carCode").attr("disabled", true);
		$("#carLicence").attr("disabled", true);
		$("#carOwnerMobile").attr("disabled", true);
		$("#carInsurance").attr("disabled", true);
		$("#orderTime").attr("disabled", true);
		$("#orderProject").attr("disabled", true);
	} else {
		$('#btEmpAdd').show();
		$("#myModalTitle").html("派单");
		$("#carOwnerName").attr("disabled", true);
		$("#carCode").attr("disabled", true);
		$("#carLicence").attr("disabled", true);
		$("#carOwnerMobile").attr("disabled", true);
		$("#carInsurance").attr("disabled", true);
		$("#orderTime").attr("disabled", true);
		$("#orderProject").attr("disabled", true);
	}
	$("#id").val(obj.id);
	$("#carOwnerName").val(obj.carOwnerName);
	$("#carCode").val(obj.carCode);
	$("#carLicence").val(obj.carLicence);
	$("#carOwnerMobile").val(obj.carOwnerMobile);
	$("#carInsurance").val(obj.carInsurance);
	$("#orderTime").val(obj.orderTime);
	$("#orderProject").val(obj.orderProject);
	$("#checkStationName").val(obj.checkStationName);
	$("#insertTime").val(obj.insertTime);
	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
}
