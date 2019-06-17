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

function init() {
	// 初始化Table
	oTable = new TableInit();
	oTable.Init();
};

var TableInit = function() {
	var oTableInit = new Object();
	oTableInit.Init = function() {
		$('#materialsTableInfo').bootstrapTable({
			url : getRootPath() + "emergencyOrderInfo/oneAllList", // 请求后台的URL（*）
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
				title : '手机号'
			}, {
				field : 'kilometer',
				title : '公里数'
			}, {
				field : 'payFee',
				title : '定金'
			}, {
				field : 'sumPrice',
				title : '总价'
			}, {
				field : 'staffName',
				title : '客服人员'
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
		var html = '<a id="a_check">查看 </a>';
		return html;
	}
	// 操作列的事件
	window.operateEvents = {
		'click #a_check' : function(e, value, row, index) {
			editOrCheck(row, 1);
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

// 查看和编辑
function editOrCheck(obj, type) {
	$('#myForm1').validationEngine('hide');
	$('#btEmpAdd').hide();
	$("#myModalTitle").html("查看");
	$("#carOwnerName").attr("disabled", true);
	$("#carOwnerMobile").attr("disabled", true);
	$("#kilometer").attr("disabled", true);
	$("#payFee").attr("disabled", true);
	$("#orderProject").attr("disabled", true);
	$("#sumPrice").attr("disabled", true);
	$("#orderUserName").attr("disabled", true);
	$("#orderStatus").attr("disabled", true);

	$("#id").val(obj.id);
	$("#carOwnerName").val(obj.carOwnerName);
	$("#carOwnerMobile").val(obj.carOwnerMobile);
	$("#kilometer").val(obj.kilometer);
	$("#payFee").val(obj.payFee);
	$("#orderProject").val(obj.finishTime);
	$("#sumPrice").val(obj.sumPrice);
	$("#orderUserName").val(obj.staffName);
	$("#orderStatus").val(obj.typeState);

	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
}
