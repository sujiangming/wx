$(function() {
	init();
	// 定位表格查询框
	$(".search").css({
		'position' : 'fixed',
		'right' : '10px',
		'top' : '0',
		'z-index' : '1000',
		'width' : '240px'
	});
	$(".search input").attr("placeholder", "搜索");
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
		$('#roleInfo').bootstrapTable({
			url : getRootPath() + "roleMember/list", // 请求后台的URL（*）
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
				field : 'innerUserName',
				title : '用户名'
			}, {
				field : 'innerUserRole',
				title : '角色'
			}, {
				field : 'checkStationName',
				title : '所属检测站'
			}, {
				field : 'operate',
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
				+ '<a id="a_edit">授权</a>';
		return html;
	}
	// 操作列的事件
	window.operateEvents = {
		'click #a_check' : function(e, value, row, index) {
			editOrCheck(row, 1);
		},
		'click #a_edit' : function(e, value, row, index) {
			$('#myForm1').validationEngine('hide');
			$('#btEmpAdd').show();
			layer.confirm("您确定要授权该用户为管理员吗？", {
				skin : 'layui-layer-molv',
				btn : [ '确定', '取消' ]
			}, function() {
				console.log("value:" + value + ",row:" + JSON.stringify(row)
						+ ",index:" + index);
				var addJson = {
					roleId : row.id,
					checkStationName : row.checkStationName
				};
				$.ajax({
					type : "post",
					url : getRootPath() + "roleMember/save",
					data : addJson,
					dataType : "json",
					success : function(data) {
						if (data.status == "1") {
							layer.msg(data.message, {
								time : 2000
							}, function() {
								$('#roleInfo').bootstrapTable('refresh');
							});
						} else {
							layer.msg(data.message, {
								time : 2000
							});
						}
					}
				});
			}, function() {
				return;
			});
		},

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
	dateFormate = function(value) {
		if (value == null || value == "") {
			return "";
		} else {
			return json2Date(value);
		}

	};
	return oTableInit;

};

// 查看
function editOrCheck(obj, type) {
	$('#myForm1').validationEngine('hide');
	$('#btEmpAdd').hide();

	$("#myModalTitle").html("查看");
	$("#name").attr("disabled", true);
	$("#desc").attr("disabled", true);

	$("#roleId").val(obj.id);
	$("#name").val(obj.innerUserName);
	$("#desc").val(obj.innerUserRole);

	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
}
