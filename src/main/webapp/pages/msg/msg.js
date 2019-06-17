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
	$(".search input").attr("placeholder", "只能输入站点名称");
	$(".search input").css({
		'padding-right' : '23px'
	});
	$('#myForm1').validationEngine();
	initBtnEvent();
});

function selectChange(el) {
	var value = el.value;
	if ("text" == value) { // 显示文本消息
		$("#picDiv").hide();
		$("#clickUrlDiv").hide();
	} else if("image" == value){ // 显示图片消息
		$("#picDiv").show();
		$("#clickUrlDiv").hide();
	}else if("news" == value){// 显示图文消息
		$("#picDiv").show();
		$("#clickUrlDiv").show();
	}
};

var isAdd = "add"; //默认是新增

function initBtnEvent() {
	// 新增
	$("#btn_add").bind('click', function() {
		$('#myForm1').validationEngine('hide');
		isAdd = "add";
		clearForm();
		$('#myModal1').modal({
			backdrop : 'static',
			keyboard : false
		});
	});
	// 删除
	$("#btn_del").bind('click', function() {
		var selections = getIdSelections();
		layer.confirm("您确定要删除所选信息吗?", {
			skin : 'layui-layer-molv',
			btn : [ '确定', '取消' ]
		}, function() {
			var url = getRootPath() + "msg/delect";
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
			url : getRootPath() + "msg/selectList", // 请求后台的URL（*）
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
				field : 'title',
				title : '标题'
			}, {
				field : 'keyword',
				align : 'left',
				title : '关键字',
				formatter : function(value, row, index) { // 单元格格式化函数
					var div = "<div style='width:100px;word-break: break-all;'>"
							+ value + "</div>";// 调列宽，在td中嵌套一个div，调整div大小
					return div;
				}
			}, {
				field : 'description',
				title : '描述',
				formatter : function(value, row, index) { // 单元格格式化函数
					var div = "<div style='width:100px;max-width: 110px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;'>"
							+ value + "</div>";// 调列宽，在td中嵌套一个div，调整div大小
					return div;
				}
			}, {
				field : 'content',
				title : '内容',
				formatter : function(value, row, index) { // 单元格格式化函数
					var div = "<div style='width:100px;max-width: 110px;overflow: hidden;text-overflow: ellipsis;white-space: nowrap;'>"
							+ value + "</div>";// 调列宽，在td中嵌套一个div，调整div大小
					return div;
				}
			}, {
				field : 'picUrl',
				align: 'center',
				title : '图片',
				formatter: function (value, row, index) {
                 	var html = "<img width=30  src='"+value+"' />";
             		return html;
                 }
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
	dateFormate = function(value) {
		if (value == null || value == "") {
			return "";
		} else {
			return json2Date(value);
		}

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
function openSaveButton(type) {
	var flag = $('#myForm1').validationEngine('validate');
	if (flag) {
		var addJson = getDataForm();
		$.ajax({
			type : "post",
			url : getRootPath() + "msg/save",
			data : addJson,
			dataType : "json",
			async : true,
			success : function(data) {
				if (data.status == "1") {
					layer.msg(data.message, {
						time : 2000
					}, function() {
						layerTip(data.message);
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
	} else {
		layer.msg('表单验证未通过！', {
			time : 3000
		});
	}

}

/**
 * 获取表单数据
 */
function getDataForm() {
	var addJson = {
		id : $("#id").val(),
		title : $("#msgTitle").val(),
		content : $("#msgContent").val(),
		desc : $("#msgDesc").val(),
		keyword : $("#msgKeyword").val(),
		picUrl : $("#picUrl").attr("src"),
		clickUrl : $("#msgClickUrl").val(),
		isAddFlag : isAdd,
		mediaId : mediaId
	};
	return addJson;
}

// 清空表单
function clearForm() {
	imagePath1 = null;
	$("#msgTitle").val("");
	$("#msgContent").val("");
	$("#msgDesc").val("");
	$("#msgKeyword").val("");
	$("#picUrl").attr("src","");
	$("#msgClickUrl").val("");
	
	$('#btEmpAdd').show();
	$("#myModalTitle").html("新增");
	$("#msgTitle").attr("disabled", false);
	$("#msgContent").attr("disabled", false);
	$("#msgDesc").attr("disabled", false);
	$("#msgKeyword").attr("disabled", false);
	$("#msgType").attr("disabled", false);
	$("#input1").attr("disabled", false);
	$("#picUrl").attr("disabled", false);
	
	$("#picDiv").hide();
	$("#clickUrlDiv").hide();
}

// 查看和编辑
function editOrCheck(obj, type) {
	isAdd = "update";//更新操作时设置为 2
	$('#myForm1').validationEngine('hide');
	if (type == 1) {
		$('#btEmpAdd').hide();
		$("#myModalTitle").html("查看");
		$("#msgTitle").attr("disabled", true);
		$("#msgContent").attr("disabled", true);
		$("#msgDesc").attr("disabled", true);
		$("#msgKeyword").attr("disabled", true);
		$("#msgType").attr("disabled", true);
		$("#input1").attr("disabled", true);
		$("#picUrl").attr("disabled", true);
	} else {
		$('#btEmpAdd').show();
		$("#myModalTitle").html("编辑");
		$("#msgTitle").attr("disabled", false);
		$("#msgContent").attr("disabled", false);
		$("#msgDesc").attr("disabled", false);
		$("#msgKeyword").attr("disabled", false);
		$("#msgType").attr("disabled", false);
		$("#input1").attr("disabled", false);
		$("#picUrl").attr("disabled", false);
	}
	$("#id").val(obj.id);
	$("#msgTitle").val(obj.title);
	$("#msgContent").val(obj.content);
	$("#msgDesc").val(obj.description);
	$("#msgClickUrl").val(obj.clickUrl);
	$("#msgKeyword").val(obj.keyword);
	$("#picUrl").attr("src", obj.picUrl);

	$('#myModal1').modal({
		backdrop : 'static',
		keyboard : false
	});
};
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

var imagePath1 = null;
var mediaId = null;

/**
 * 上传图片
 * 
 * @returns
 */
function uploadImage(el) {
	var that = el;
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
			url : getRootPath() + "uploadPicToWx",
			data : submitData,
			dataType : "json",
			success : function(data) {
				if ("success" == data.mse) {
					imagePath1 = data.path;
					mediaId = data.mediaId;
					$("#picUrl").attr("src", imagePath1);
					$("#picUrl").show();
					$("#input1").attr("disabled", true);
					return;
				}
			}
		});
		return rst;
	});
};

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