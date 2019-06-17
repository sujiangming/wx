var userDept = "";
$(function(){
	caidan();
	$('#myForm1').validationEngine();
	initLayout();
	initMenu();
	initEvents();
});

/* 不同用户登录展示不同菜单*/
function caidan(){
	
	var yuyue=$("#yuyue");
	var dingdan=$("#dingdan");
	var kefu=$("#kefu");
	var shijian=$("#shijian");
	var cezan=$("#cezan");
	var cexing=$("#cexing");
	var renyuan=$("#renyuan");
	var pingjia=$("#pingjia");
	var mima=$("#mima");
	var xitong=$("#xitong");
	var wxMenu=$("#wxMenu");
	var yingji=$("#yingji");
	var yj_jg=$("#yj_jg");
	var yj_yd=$("#yj_yd");
	var yj_ld=$("#yj_ld");
	
	var isManager = $("#ismanager").html();
	
	if(isManager =="2"){  //区域管理员
		cezan.attr("style","display:none;");
		dingdan.attr("style","display:none;");
		wxMenu.attr("style","display:none;");
		yj_jg.attr("style","display:none;");
		
	}else if(isManager =="1"){ //客服人员
		yuyue.attr("style","display:none;");
		kefu.attr("style","display:none;");
		shijian.attr("style","display:none;");
		cezan.attr("style","display:none;");
		cexing.attr("style","display:none;");
		renyuan.attr("style","display:none;");
		pingjia.attr("style","display:none;");
		xitong.attr("style","display:none;");
		wxMenu.attr("style","display:none;");
		yj_jg.attr("style","display:none;");
		
	}else{                                //超级管理员
		yuyue.attr("style","display:none;");
		shijian.attr("style","display:none;");
		cexing.attr("style","display:none;");
		pingjia.attr("style","display:none;");
		dingdan.attr("style","display:none;");
		yj_yd.attr("style","display:none;");
		yj_ld.attr("style","display:none;");
	}
	
	
}

/**** 初始化页面布局 ****/
function initLayout(){
	$("#sidebar").css("height",$(window).height()-50+"px");
	$("#content_iframe").css("height",$(window).height()-90+"px");
	$("#tab-content").css("height",$(window).height()-90+"px");
}

var tempObj = null;
/**** 初始化菜单 ****/
function initMenu(){
	$(".menu-link").each(function(i,n){
		if($(this).attr("url")!="#"){
			$(this).click(function(){
				$("#content_iframe").attr("src",getRootPath()+$(this).attr("url"));
				window.scrollTo(0,0);
				if(tempObj!=null){
					tempObj.parent().removeClass("active");
					tempObj.find("i").removeClass("fa fa-circle").addClass("fa fa-circle-o");
				} 
				$(this).parent().addClass("active");
				tempObj = $(this);
				$(this).find("i").removeClass("fa fa-circle-o").addClass("fa fa-circle");
				$("#pagetitle").text($(this).attr("name"));
				var navstr = $(this).attr("nav");
				var navs = navstr.split(",");
				var nav = '<li><a href="mainFrame.jsp"><i class="fa fa-dashboard"></i> 首页</a></li>';
				for(var i=0;i<navs.length;i++){
					if(i==navs.length-1){
						nav += '<li class="active">'+$(this).attr("name")+'</li>';
					}else{
						nav += '<li>'+navs[i]+'</li>';
					}
				}
				$("#breadcrumb").html(nav);
			});
		};
	});
}

/*** 生成菜单树 ***/
function getMenuHtml(menuData,sub,nav){
	var html = [];
	if(sub){
		html.push('<ul class="treeview-menu">');
	}
	$(menuData).each(function(i,o){
		var url = o.url !=null && o.url !=""?o.url:"#";
		var name = o.name;
		var icon = o.icon;
		if(sub){
			if(i==0){
				nav += ","+name;
			}else{
				var navs = nav.split(",");
				navs.pop();
				navs.push(name);
				nav = navs.join(",");
			}
		}
		if(o.children&&o.children.length>0){
			if(sub){
				html.push('<li>');
			}else{
			    html.push('<li class="treeview">');
				nav = name;
			}
			html.push('<a url="'+url+'" name="'+name+'" nav="'+nav+'" class="menu-link"><i class="'+icon+'"></i><span>'+name+'</span>');
			html.push('<span class="pull-right-container">');
			html.push('<i class="fa fa-angle-left pull-right"></i>');
			html.push('</span>');
			html.push('</a>');
			html.push(getMenuHtml(o.children,true,nav));
			html.push('</li>');
		}else{
			//nav += ","+name;
			html.push('<li><a url="'+url+'" name="'+name+'" nav="'+nav+'" class="menu-link"><i class="'+icon+'"></i><span>'+name+'</span></a></li>');
		};
	});
	if(sub){
		html.push('</ul>');
	}
	
	console.log("getMenuHtml:" + html.join(""));
	
	return html.join("");
}

/***初始化点击事件***/
function initEvents(){
	//退出
	$("#exit").click(function(){
		layer.confirm("您确定要退出吗?",{
			skin: 'layui-layer-molv', 
			btn: ['确定','取消']
		},function(){
			window.location.href = getRootPath()+"/login/login.jsp"; //退出系统
		},function(){
			return;
		});
	});
	
	$(window).resize(function(){
		initLayout();
	});
}

//获取项目根路径
function getRootPath(){
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath=window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName=window.document.location.pathname;
    var pos=curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083
    var localhostPaht=curWwwPath.substring(0,pos);
    //获取带"/"的项目名，如：/uimcardprj
    var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+2);
    return(localhostPaht+projectName);
};

//修改密码
function changePwd(){
	$(".red-order").css("visibility","visible");
	$(".red-position").css("visibility","visible");
	$(".red-book").css("visibility","visible");
	clearForm();
	$('#myForm1').validationEngine('hide');
	$("#myModalTitle").html("修改密码");
	$("#changePwdModal").modal();
}
function clearForm(){
	$("#oldPwd").val("");
	$("#newPassword").val("");
	$("#renewPassword").val("");
}
