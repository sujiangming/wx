$(function () {
	init();
	//定位表格查询框
	$(".search").css({'position':'fixed','right':'10px','top':'0','z-index':'1000','width':'240px'});
	$(".search input").attr("placeholder","搜索");
	$(".search input").css({'padding-right':'23px'});
	$('#myForm1').validationEngine();
	initBtnEvent();
	initDropdown();
	validateCurUser();
});

function initBtnEvent(){
	//新增
	$("#btn_add").bind('click',function(){
		$('#myForm1').validationEngine('hide');
		$('#btEmpAdd').show();
		
		clearForm();
		$("#name").attr("disabled",false);
   		$("#desc").attr("disabled",false);
		$("#myModalTitle").html("新增");
		$('#myModal1').modal({backdrop: 'static', keyboard: false});
		
	});
	//删除
	$("#btn_del").bind('click',function(){
		$('#myForm1').validationEngine('hide');
		$('#btEmpAdd').show();
		var selections = getIdSelections();
		layer.confirm("您确定要删除所选信息吗?",{
			skin: 'layui-layer-molv', 
			btn: ['确定','取消']
		},function(){
			var url=getRootPath()+"role/delete";
			$.post(url,
					{roleId:selections+""},
					function(data){
						if(data.status=="1"){
							layer.msg(data.message,{time: 2000}, function(){
								$('#roleInfo').bootstrapTable('refresh');
							});
						}else{
							layer.msg(data.message,{time: 2000});
						}
					},"json");
		},function(){
			return;
		});
	});
}

//验证登陆用户是否超级管理员or公司管理员
function validateCurUser(){
	if(loginUserIdentity){//管理员
		$('#isAdmin').show();
		$('#companyDiv').show();
	}else{
		$('#isAdmin').hide();
		$('#companyDiv').hide();
		$('#companyId').val(loginUserCompany);
	}
}

function initDropdown(){
	$('.selectpicker').selectpicker({
		  style: 'btn-white',
		  size: 10,
		  liveSearch: true,
		  title: "请选择公司",
		  width: "100%",
	});
	$.ajax({
        type: "post",
        url: getRootPath()+"checkStation/list",
        dataType: "json",
		async: true,
        success: function(data)
        {
        	if (!data || data.length === 0) {
	            return false;
	        }
	        var len = data.length;
	        for(var i=0;i<len;i++){
	        	$("#companyDrop").append("<option value='"+data[i].id+"'>"+data[i].checkStationName+"</option>");
	        }
	        $('.selectpicker').selectpicker('refresh');
        }
    });
	$("#companyDrop").on('changed.bs.select', function (e) {
		$("#companyId").val($("#companyDrop").val());
	});
}

function init(){
    //初始化Table
    oTable = new TableInit();
    oTable.Init();
    var i=0;
    $("#moreSearch").bind('click',function(){
   	 $("#more_search").slideToggle("slow",function(){
   		if(i==0){					
   			i=1;
   			$("#moreSearch").html('更多查询&nbsp;<span class="caret"></span>');
   			$("#moreSearch").addClass("dropup");
   		}else{
   			i=0;
   			$("#moreSearch").html('更多查询 <span class="caret"></span>');
   			$("#moreSearch").removeClass("dropup");
   		}
   	});
   });
};

var TableInit = function (){
	var oTableInit = new Object();
	oTableInit.Init = function (){
		$('#roleInfo').bootstrapTable({
			url: getRootPath()+"role/list",         //请求后台的URL（*）
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
            	field: 'id',
            	visible:false,
            	title: 'id'
            }, {
            	field: 'roleName',
            	title: '角色名'
            }, {
                field: 'roleDesc',
                title: '描述'
            }, {
            	field: 'operate',
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
		'<a id="a_edit">修改</a>';
		return html;
    }
	//操作列的事件
	window.operateEvents = {
        'click #a_check': function (e, value, row, index) {
        	editOrCheck(row,1);
        },
		'click #a_edit': function (e, value, row, index) {
			editOrCheck(row,2);
	    },
        
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
	
};
	//表格选择事件
	function tableCheckEvents(){
		var r = $('#roleInfo').bootstrapTable('getSelections');
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
        return $.map($('#roleInfo').bootstrapTable('getSelections'), function (row) {
            return row.id;
        });
    }
    
    function getNameSelections() {
        return $.map($('#roleInfo').bootstrapTable('getSelections'), function (row) {
            return row.name;
        });
    }
    /**
     * 保存操作
     */
    function openSaveButton(){
    	
    	var flag = $('#myForm1').validationEngine('validate');
    	if(flag){
        	var addJson = getDataForm();
        	$.ajax({
                type: "post",
                url: getRootPath()+"role/save",
                data: addJson,
                dataType: "json",
                success: function(data)
                {
                	if(data.status=="1"){
                		layer.msg(data.message,{time: 2000}, function(){
                			$('#roleInfo').bootstrapTable('refresh');
                			$('#myModal1').modal('hide');
                		});
                	}else{
                		layer.msg(data.message,{time: 2000});
                	}
                }
            });
    	}else{
    		layer.msg('表单验证未通过！',{
				time: 2000
			});
    	}
    	
    }
    
    /**
     * 获取表单数据
     * @returns {___anonymous7092_7851}
     */
    function getDataForm(){
    	
    	var addJson = {
    			roleId:$("#roleId").val(),
    			companyId:$("#companyId").val(),
    			name:$("#name").val(),
    			desc:$("#desc").val()
    		};
    	return addJson;
    }
    
    //清空表单
    function clearForm(){
    	$("#roleId").val("");
    	$("#name").val("");
		$("#desc").val("");
    }
    
    
    function getNowFormatDate(flag,vardate) {
    	if(flag == false && (vardate==null || vardate==''))
    		return null;
    	var date;
    	if(flag==true){
        	date = new Date();
    	}else{
    		date=new Date(vardate);
    	}
        var seperator1 = "-";
        var seperator2 = ":";
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        var hour=date.getHours();       //获取当前小时数(0-23)
        var min=date.getMinutes();     //获取当前分钟数(0-59)
        var sec=date.getSeconds();     //获取当前秒数(0-59)
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        if (hour >= 1 && hour <= 9) {
            hour = "0" + hour;
        }
        if (min >= 0 && min <= 9) {
        	min = "0" + min;
        }if (sec >= 0 && sec <= 9) {
        	sec = "0" + sec;
        }
        var currentdate = year + seperator1 + month + seperator1 + strDate
                + " " + hour + seperator2 + min
                + seperator2 + sec;
        return currentdate;
    }
    
    
    //查看和编辑
    function editOrCheck(obj,type){
    	$('#myForm1').validationEngine('hide');
    	if(type==1){
    		$('#btEmpAdd').hide();
    		$("#myModalTitle").html("查看");
    		$("#name").attr("disabled",true);
	   		$("#desc").attr("disabled",true);
    	}else{
    		$('#btEmpAdd').show();
    		$("#myModalTitle").html("编辑");
    		$("#name").attr("disabled",false);
       		$("#desc").attr("disabled",false);
    	}
    	$("#roleId").val(obj.id);
    	$("#name").val(obj.roleName);
    	$("#desc").val(obj.roleDesc);
		
		$('#myModal1').modal({backdrop: 'static', keyboard: false});
	}
