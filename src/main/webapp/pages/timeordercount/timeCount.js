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
	$(".search input").attr("placeholder", "只能输入检车站名称");
	$(".search input").css({
		'padding-right' : '23px'
	});
	$('#myForm1').validationEngine();
	initBtnEvent();
});

function initBtnEvent() {
	var urlmethod = "";
	$("#btn_add").bind('click', function() {
		clearForm();
		$('#btEmpAdd').show();
		$("#myModalTitle").html("新增");
		$("#orderTime").attr("disabled", false);
		$("#orderCount").attr("disabled", false);
		$('#myModal1').modal({
			backdrop : 'static',
			keyboard : false
		});
	});

	$("#btn_del").bind('click', function() {
		var selections = getIdSelections();
		layer.confirm("您确定要删除所选信息吗?", {
			skin : 'layui-layer-molv',
			btn : [ '确定', '取消' ]
		}, function() {
			console.log(selections);
			var url = getRootPath() + "timeCount/delete";
			$.post(url, {
				id : selections + ""
			}, function(data) {
				if (data.status == "1") {
					layer.msg(data.message, {
						time : 2000
					}, function() {
						$('#materialsTableInfo').bootstrapTable('refresh');
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
	});

}

function init() {
	// 初始化Table
	oTable = new TableInit();
	oTable.Init();
};

var TableInit = function() {
	var oTableInit = new Object();
	oTableInit.Init = function() {
		$('#materialsTableInfo').bootstrapTable({
			url : getRootPath() + "timeCount/list", // 请求后台的URL（*）
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
				title : 'ID'
			}, {
				field : 'orderTime',
				align : 'center',
				title : '预约时间段'
			}, {
				field : 'orderCount',
				title : '预约人数'
			}, {
				field : 'operate',
				title : '操作',
				width : '10%',
				align : 'center',
				formatter : operateFormatter,
				events : operateEvents
			} ],
			onCheck : function(row, e) {
				tableCheckEvents();
			},
			onUncheck : function(row, e) {
				tableCheckEvents();
			},
			onCheckAll : function(rows) {
				$("#btn_del").attr("disabled", false);
			},
			onUncheckAll : function(rows) {
				$("#btn_del").attr("disabled", true);
			},
			onLoadSuccess : function(rows) {
				$("#btn_del").attr("disabled", true);
			}
		});
	};

	function operateFormatter(value, row, index) {
		var html = '<a id="a_check">查看 <span style="color:#CCC">|</span> </a>'
				+ '<a id="a_edit">修改</a>';
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

};

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

function showLayerTip(text) {
	layer.msg(text, {
		time : 3000
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
			url : getRootPath() + "timeCount/saveOrUpdate",
			data : addJson,
			dataType : "json",
			async : true,
			success : function(data) {
				if (data.status == "1") {
					layer.msg(data.message, {
						time : 2000
					}, function() {
						$('#materialsTableInfo').bootstrapTable('refresh');
	
						$('#myModal1').modal('hide');
					});
				} else {
					showLayerTip(data.message);
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
	var orderTime = $("#orderTime").val();
	if (null == orderTime || "" == orderTime) {
		layer.msg("请按照示例输入时间段", {
			time : 3000
		});
		return;
	}
	
	var index = orderTime.indexOf("-");
	if (-1 == index) {
		layer.msg("请按照示例输入正确的时间段", {
			time : 3000
		});
		return;
	}

	var orderCount = $("#orderCount").val();
	if (null == orderCount || orderCount == "") {
		layer.msg("请输入预约次数", {
			time : 3000
		});
		return;
	}
	var tid = $("#id").val();
	var addJson = {
		id : tid,
		orderTime : orderTime,
		orderCount : orderCount,
		orderStart : orderTime
	};
	return addJson;
}

// 清空表单
function clearForm() {
	$("#orderTime").val("");
	$("#orderCount").val("");
	$("#id").val("");
}

// 查看和编辑
function editOrCheck(obj, type) {
	if (type == 1) {
		$('#btEmpAdd').hide();
		$("#myModalTitle").html("查看");
		$("#orderTime").attr("disabled", true);
		$("#orderCount").attr("disabled", true);
	} else {
		$('#btEmpAdd').show();
		$("#myModalTitle").html("编辑");
		$("#orderTime").attr("disabled", false);
		$("#orderCount").attr("disabled", false);
	}
	$("#id").val(obj.id);
	$("#orderTime").val(obj.orderTime);
	$("#orderCount").val(obj.orderCount);
	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
}
