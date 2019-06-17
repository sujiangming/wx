$(function () {
	init();
	//定位表格查询框
	$(".search").css({'position':'fixed','right':'15px','top':'0','z-index':'1000','width':'240px'});
	$(".search input").attr("placeholder","只能输入昵称");
	$(".search input").css({'padding-right':'23px'});
	$('#myForm1').validationEngine();
	//initBtnEvent();
	//initUpload();
	
});

function checkStatic(){
	var checkStationName=$("#checkStationName").val();
	$.ajax({
		url:getRootPath()+"checkStation/listAll",
		type:"post",
		dataType:"json",
		success:function(msg){
			if ($("#checkStationName").length > 0) {
				$("#checkStationName").empty();
			}
			for (var i = 0; i < msg.list.length; i++) {
				$("#checkStationName").append(
						"<option value='"+msg.list[i].checkStationName+"' code='"+msg.list[i].id+"'>"
								+ msg.list[i].checkStationName+"</option>");

			}
		}
	});
}



function init(){
    //初始化Table
    oTable = new TableInit();
    oTable.Init();
};

var TableInit = function (){
	var oTableInit = new Object();
	oTableInit.Init = function (){
		$('#materialsTableInfo').bootstrapTable({
			url: getRootPath()+"customerInfo/list",         //请求后台的URL（*）
			toolbar: '#toolbar',                //工具按钮用哪个容器
    		striped: true,
    		search: true,
    		searchOnEnterKey: true,
    		showColumns: true,                  //是否显示所有的列
            showRefresh: true,                  //是否显示刷新按钮
            showToggle:true,                    //是否显示详细视图和列表视图的切换按钮
            sortable: false,
            sortOrder: "asc",
            sidePagination: "server",			//分页方式：client客户端分页，server服务端分页（*）
            cache: false,
            clickToSelect: false,
            queryParams: oTableInit.queryParams,
            showExport: "true",
            minimumCountColumns: 2,     //最少允许的列数
            buttonsAlign: "left",
            buttonsClass: 'white',
            showPaginationSwitch: false,
            pagination: true,
            pageNumber: 1,                       //初始化加载第一页，默认第一页
            pageSize: 10,                       //每页的记录行数（*）
            pageList: '[10, 25, 50, 100,ALL]',        //可供选择的每页的行数（*）         
    		columns: [ 
    		{
                field: 'state',
                checkbox: true,
            }, {
            	field: 'customerId',
            	visible:false,
            	title: '微信号',
            }, {
            	field: 'id',
				visible:false,
            	title: 'id'
            }, {
                field: 'customerName',
                title: '姓名'
            }, {
            	field: 'customerNickName',
				align: 'left',
            	title: '昵称',
            }, {
                field: 'customerPic',
                title: '头像',
                formatter: function (value, row, index) {
                	var html = "<img width=30  src='"+value+"' />";
            		return html;
                }
            }, {
                field: 'customerMobile',
                title: '手机号',
            }, {
                field: 'customerGender',
                title: '性别',
            },{
                field: 'customerAddress',
                title: '地址',
            },{
                field: 'isInnerPeople',
                title: '内部人员',
            },{
            	field: 'id',
                title: '操作',
                align: 'center',
                events: operateEvents,
                formatter: operateFormatter
            }
            ],
            onCheck:function(row,e){
            	tableCheckEvents();
            },
            onUncheck: function(row,e){
            	tableCheckEvents();
            },
            onCheckAll: function(rows){
        		$("#btn_del").attr("disabled",false);
            },
            onUncheckAll: function(rows){
        		$("#btn_del").attr("disabled",true);
            },
            onLoadSuccess: function(rows){
        		$("#btn_del").attr("disabled",true);
            }
		});
	};
	
	function operateFormatter(value, row, index) {
		var html = '<a id="a_check">查看 <span style="color:#CCC">|</span> </a>'+
		'<a id="a_edit" onclick="checkStatic()">修改</a>';
        return html;
    }
	//操作列的事件
	window.operateEvents = {
        'click #a_check': function (e, value, row, index) {
        	editOrCheck(row,1);
        },
        'click #a_edit': function (e, value, row, index) {
        	editOrCheck(row,2);
        }
    };
	
	oTableInit.queryParams = function (params) {
	    var temp = {   //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
	        limit: params.limit,   //页面大小
	        offset: params.offset,  //页码
	        search: params.search   //表格搜索框的值
	    };
	    return temp;
	};
	dateFormate = function(value){
		if(value==null || value==""){
		    return "";
		}else{
			return json2Date(value);
		}
		
	};
	return oTableInit;
	
}
//表格选择事件
function tableCheckEvents(){
	var r = $('#materialsTableInfo').bootstrapTable('getSelections');
	if(r.length==0){
		$("#btn_del").attr("disabled",true);
	}
	if(r.length==1){
		$("#btn_del").attr("disabled",false);
	}
}

/**选择框
 * 
 * @returns
 */
function getIdSelections() {
    return $.map($('#materialsTableInfo').bootstrapTable('getSelections'), function (row) {
        return row.id;
    });
}

/**
 * 保存操作
 */
function openSaveButton(){
	var moble=$("#customerMobile").val();
	if(!(/^1(3|4|5|7|8)\d{9}$/.test(moble))){
		layer.msg("手机号码有误,请重新输入~", {
			time : 2000
		});
		return;
	}
	
    	var addJson = getDataForm();
    	
    	$.ajax({
            type: "post",
            url: getRootPath()+"customerInfo/save",
            data: addJson,
            dataType: "json",
    		async : true,
            success: function(data)
            {
            	if(data.status=="1"){
            		layer.msg(data.message,{
	    				time: 2000
	    			}, function(){
	    				$('#materialsTableInfo').bootstrapTable('refresh');
	    				$('#myModal1').modal('hide');
	    			});
            	}else{
            		layer.msg(data.message,{
	    				time: 2000
	    			});
            	}
            }
        });
	
	
}
    
/**
 * 获取表单数据customerNickName
 */
function getDataForm(){
		var addJson = {
				id:$("#id").val(),
				customerId:$("#customerId").val(),
				customerName:$("#customerName").val(),
				customerNickName:$("#customerNickName").val(),
				customerPic:$("#customerPic").val(),
				customerGender:$("#customerGender").val(),
				
				customerMobile:$("#customerMobile").val(),
				isInnerPeople:$("#isInnerPeople").val(),
				checkStationName:$("#checkStationName").val(),
				checkStationId:$("#checkStationName").find("option:selected").attr("code"),
			};
		return addJson;
	
	
}
    
//清空表单
function clearForm(){
	$("#customerName").val("");
	$("#customerMobile").val("");
}
    
//查看和编辑
function editOrCheck(obj,type){
	$('#myForm1').validationEngine('hide');
	if(type==1){
		$('#btEmpAdd').hide();
		$("#myModalTitle").html("查看");
		$("#customerName").attr("disabled",true);
   		$("#customerNickName").attr("disabled",true);
   		$("#customerPic").attr("disabled",true);
   		$("#customerMobile").attr("disabled",true);
   		$("#customerGender").attr("disabled",true);
   		$("#customerAddress").attr("disabled",true);
   		$("#isInnerPeople").attr("disabled",true);
	}else{
		$('#btEmpAdd').show();
		$("#myModalTitle").html("编辑");
		$("#customerNickName").attr("disabled",true);
		$("#customerPic").attr("disabled",true);
		$("#customerGender").attr("disabled",true);
		$("#customerAddress").attr("disabled",true);
		$("#customerName").attr("disabled",false);
   		$("#customerMobile").attr("disabled",false);
   		$("#isInnerPeople").attr("disabled",false);
	}
	$("#id").val(obj.id);
	$("#customerName").val(obj.customerName);
	$("#customerNickName").val(obj.customerNickName);
	$("#customerPic").val(obj.customerPic);
	$("#customerMobile").val(obj.customerMobile);
	$("#customerGender").val(obj.customerGender);
	$("#customerId").val(obj.customerId);
	$("#isInnerPeople").val(obj.isInnerPeople);
	$("#customerAddress").val(obj.customerAddress);
	
	$('#myModal1').modal({backdrop: 'static', keyboard: false});
}


