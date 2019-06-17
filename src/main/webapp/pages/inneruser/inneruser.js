$(function () {
	init();
	//定位表格查询框
	$(".search").css({'position':'fixed','right':'15px','top':'0','z-index':'1000','width':'240px'});
	$(".search input").attr("placeholder","只能输入姓名");
	$(".search input").css({'padding-right':'23px'});
	$('#myForm1').validationEngine();
	initBtnEvent();
	//initUpload();
});

function initBtnEvent(){

	$("#btn_del").bind('click',function(){
		var selections = getIdSelections();
		layer.confirm("您确定要删除所选信息吗?",{
			skin: 'layui-layer-molv', 
			btn: ['确定','取消']
		},function(){
	        var url=getRootPath()+"innerUser/deleteInnerUser";
			$.post(url,
					{id:selections+""},
    		        function(data){
						if(data.status=="1"){
							layer.msg(data.message,{time: 2000}, function(){
								$('#materialsTableInfo').bootstrapTable('refresh');
							});
						}else{
							layer.msg(data.message,{time:2000});
						}
					},"json");
		},function(){
			return;
		});
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
			url: getRootPath()+"innerUser/selectList",         //请求后台的URL（*）
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
                },{
            	field: 'id',
				visible:false,
            	title: 'ID'
            },{
            	field: 'innerUserPic',
				align: 'center',
            	title: '头像',
            	formatter: function (value, row, index) {
                 	var html = "<img width=30  src='"+value+"' />";
             		return html;
                 }
            }, {
                field: 'innerUserName',
                title: '姓名',
            }, {
                field: 'innerUserGender',
                title: '性别',
            }, {
            	field: 'innerUserMobile',
            	title: '手机号'
            },{
            	field: 'innerUserRole',
            	title: '角色'
            },{
            	field: 'checkStationName',
            	title: '所属检查站'
            },{
            	field: 'operate',
                title: '操作',
                width: '10%',
                align: 'center',
                formatter: operateFormatter,
                events: operateEvents
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
		var html = '<a id="a_show">查看 </a>';
        return html;
    }
	//操作列的事件
	window.operateEvents = {
        'click #a_edit': function (e, value, row, index) {
        	editOrCheck(row,1);
        },
        'click #a_show': function (e, value, row, index) {
        	editOrCheck(row,0);
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
	
	
	return oTableInit;
	
};

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
function openSaveButton(type){
	var flag = $('#myForm1').validationEngine('validate');
	if(flag){
    	var addJson = getDataForm();
    	$.ajax({
            type: "post",
            url: getRootPath()+"innerUser/saveInnerUser",
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
	}else{
		layer.msg('表单验证未通过！',{
			time: 3000
		});
	}
}
    
/**
 * 获取表单数据
 */
function getDataForm(){
	var addJson = {
		id:$("#id").val(),
		innerUserPic:$("#innerUserPic").val(),
		innerUserName:$("#innerUserName").val(),
		innerUserGender:$("#innerUserGender").val(),
		innerUserMobile:$("#innerUserMobile").val(),
		innerUserPosition:$("#innerUserPosition").val(),
		innerUserRole:$("#innerUserRole").val(),
		checkStationName:$("#checkStationName").val(),
	};
	return addJson;
}
    
//清空表单
function clearForm(){
	$("#innerUserPic").val(""),
	$("#innerUserName").val(""),
	$("#innerUserGender").val(""),
	$("#innerUserMobile").val(""),
	$("#innerUserRole").val(""),
	$("#checkStationName").val("")
}
    
//查看和编辑
function editOrCheck(obj,type){
	$('#myForm1').validationEngine('hide');
	if(type==0){
		$('#btEmpAdd').hide();
		$("#myModalTitle").html("查看");
		$("#innerUserPic").attr("disabled",true);
		$("#innerUserName").attr("disabled",true);
		$("#innerUserGender").attr("disabled",true);
		$("#innerUserMobile").attr("disabled",true);
		$("#innerUserRole").attr("disabled",true);
		$("#checkStationName").attr("disabled",true);
	}else{
		$('#btEmpAdd').show();
		$("#myModalTitle").html("编辑");
		$("#innerUserPic").attr("disabled",false);
		$("#innerUserName").attr("disabled",false);
		$("#innerUserGender").attr("disabled",false);
		$("#innerUserMobile").attr("disabled",false);
		$("#innerUserRole").attr("disabled",false);
		$("#checkStationName").attr("disabled",false);
	}
	$("#id").val(obj.id);
	$("#innerUserPic").val(obj.innerUserPic);
	$("#innerUserName").val(obj.innerUserName);
	$("#innerUserGender").val(obj.innerUserGender);
	$("#innerUserMobile").val(obj.innerUserMobile);
	$("#innerUserRole").val(obj.innerUserRole);
	$("#checkStationName").val(obj.checkStationName);
	
	$('#myModal1').modal({backdrop: 'static', keyboard: false});
}


