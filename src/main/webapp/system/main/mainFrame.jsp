<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.bj.scb.pojo.InnerUser"%>
<%
	//获取上下文路径
	String context = request.getContextPath();
	request.setAttribute("ContextPath", context);

	//获取用户信息,从session 里面
	HttpSession httpSession = request.getSession();
	InnerUser user = (InnerUser) httpSession.getAttribute("user");
	request.setAttribute("loginUser", user);
	boolean is = user.isManager();
	boolean isS = user.isSuperManager();
	if (isS) {//isS为true 即超级管理
		request.setAttribute("manager", 0);
	} else if (is) {//is 为true 即客服人员
		request.setAttribute("manager", 1);
	} else {//其他情况 即区域管理人员
		request.setAttribute("manager", 2);
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>快捷审车管理平台</title>
<!-- Tell the browser to be responsive to screen width -->
<meta
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"
	name="viewport">
<link rel="shortcut icon"
	href="${ContextPath}/UI-lib/dist/img/favicon.ico">
<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/bootstrap/css/bootstrap.min.css">
<!-- Font Awesome -->
<link
	href="${ContextPath}/UI-lib/bootstrap/css/font-awesome.min.css?v=4.4.0"
	rel="stylesheet" />
<!-- animate -->
<link href="${ContextPath}/UI-lib/dist/css/animate.min.css"
	rel="stylesheet" />
<!-- Ionicons -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">
<!-- Theme style -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/dist/css/AdminLTE.css">
<!-- AdminLTE Skins. Choose a skin from the css/skins
       folder instead of downloading all of them to reduce the load. -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/dist/css/skins/_all-skins.min.css">
<!-- iCheck -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/iCheck/flat/blue.css">
<!-- Morris chart -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/morris/morris.css">
<!-- jvectormap -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/jvectormap/jquery-jvectormap-1.2.2.css">
<!-- Date Picker -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/datepicker/datepicker3.css">
<!-- Daterange picker -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/daterangepicker/daterangepicker.css">
<!-- bootstrap wysihtml5 - text editor -->
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css">
<link rel="stylesheet"
	href="${ContextPath}/UI-lib/plugins/validate/css/validationEngine.jquery.css">
<!-- 后台推送 -->
<link href="${ContextPath}/UI-lib/plugins/toastr/toastr.min.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${ContextPath}/system/ajax-pushlet-client.js"></script>
</head>
<body class="hold-transition skin-blue sidebar-mini">

	<div class="wrapper">
		<header class="main-header">
			<!-- Logo -->
			<a href="#" class="logo"> <!-- mini logo for sidebar mini 50x50 pixels -->
				<span class="logo-mini"><b>I</b>MC</span> <!-- logo for regular state and mobile devices -->
				<span class="logo-lg"
				style="font-family: Microsoft YaHei; font-size: 20px; vertical-align: middle; text-align: left;">
					<img src="${ContextPath}/UI-lib/dist/img/logo1.png"
					style="position: relative; top: -1px; left: 2px; height: 50px;" />
					<font style="font-size: 16px; font-weight: bold;">快捷审车帮</font>
			</span>
			</a>
			<!-- Header Navbar: style can be found in header.less -->
			<nav class="navbar navbar-static-top">
				<!-- Sidebar toggle button-->
				<a href="#" class="sidebar-toggle" style="font-size: 14px"
					data-toggle="offcanvas" role="button"> <span class="sr-only">Toggle
						navigation</span>
				</a>

				<div class="navbar-custom-menu">
					<ul class="nav navbar-nav">
						<!-- Tasks: style can be found in dropdown.less -->
						<li class="dropdown user user-menu"><a href="#"
							class="dropdown-toggle" data-toggle="dropdown"> <img
								src="${ContextPath}/UI-lib/dist/img/user2-160x160.jpg"
								class="user-image" alt="User Image"> <span
								class="hidden-xs">${loginUser.innerUserName}</span>
						</a>
							<ul class="dropdown-menu">
								<!-- User image -->
								<li class="user-header"><img
									src="${ContextPath}/UI-lib/dist/img/user2-160x160.jpg"
									class="img-circle" alt="User Image">
									<p>${loginUser.innerUserName}</p></li>
								<!-- Menu Body -->

								<!-- Menu Footer-->
								<li class="user-footer">
									<!-- <div class="pull-left">
										<a onclick="changePwd()" class="btn btn-default btn-flat">修改密码</a>
									</div> -->
									<div class="pull-right">
										<a href="#" id="exit" class="btn btn-default btn-flat">退出</a>
									</div>
								</li>
							</ul></li>
						<!-- Control Sidebar Toggle Button -->
						<li><a href="#" data-toggle="control-sidebar"> <i
								class="fa fa-gears"></i>
						</a></li>
					</ul>
				</div>
			</nav>
		</header>
		<!-- Left side column. contains the logo and sidebar -->
		<aside class="main-sidebar" style="overflow: auto;">
			<!-- sidebar: style can be found in sidebar.less -->
			<section class="sidebar" id="sidebar">
				<!-- Sidebar user panel -->
				<div class="user-panel">
					<div class="pull-left image">
						<img src="${ContextPath}/UI-lib/dist/img/user2-160x160.jpg"
							class="img-circle" alt="User Image">
					</div>
					<div class="pull-left info">
						<p style="width: 150px; text-overflow: ellipsis; overflow: hidden">${loginUser.innerUserName}</p>
						<a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
					</div>
				</div>
				<!-- sidebar menu: : style can be found in sidebar.less -->
				<ul class="sidebar-menu" id="sidebar-menu">
					<li class="treeview" id="yuyue"><a url="#" name="预约管理"
						nav="预约管理" class="menu-link"> <i class="fa fa-check"></i> <span>预约管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/order/order.jsp" name="预约" nav="预约管理,预约"
								class="menu-link"> <i class="fa fa-circle-o"></i> <span>未处理订单</span>
							</a></li>
							<li><a url="pages/order/historyOrder.jsp" name="预约"
								nav="预约管理,订单" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>已处理订单</span>
							</a></li>
							<li><a url="pages/order/orderNickName.jsp" name="预约"
								nav="预约管理,订单" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>查询订单</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="dingdan"><a url="#" name="订单管理"
						nav="订单管理" class="menu-link"> <i class="fa fa-envelope"></i> <span>订单管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/orderinfo/orderInfo.jsp" name="订单"
								nav="订单管理,订单" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>未处理订单</span>
							</a></li>
							<li><a url="pages/orderinfo/historyOrderInfo.jsp" name="订单"
								nav="订单管理,订单" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>已处理订单</span>
							</a></li>
						</ul></li>
					<!-- 新增 消息回复管理 -->
					<li class="treeview" id="msgReply"><a url="#" name="回复消息"
						nav="客户管理" class="menu-link"> <i class="fa fa-user"></i> <span>消息回复</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/msg/msg.jsp" name="消息"
								nav="消息回复,消息" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>消息回复</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="kefu"><a url="#" name="客户管理"
						nav="客户管理" class="menu-link"> <i class="fa fa-user"></i> <span>客户管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/customerinfo/customerinfo.jsp" name="客户"
								nav="客户管理,客户" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>客户信息列表</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="shijian"><a url="#" name="时间管理"
						nav="时间管理" class="menu-link"> <i class="fa fa-th-large"></i> <span>时间管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/timeordercount/timeordercount.jsp"
								name="时间" nav="时间管理,时间" class="menu-link"> <i
									class="fa fa-circle-o"></i> <span>查看时间及人数</span>
							</a></li>
							<li><a url="pages/timeordercount/timeCount.jsp" name="时间"
								nav="时间管理,时间" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>管理时间及人数</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="cezan"><a url="#" name="站点管理"
						nav="站点管理" class="menu-link"> <i class="fa fa-map-marker"></i>
							<span>站点管理</span> <span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/checkStation/checkStation.jsp" name="站点"
								nav="站点，管理" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>站点信息</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="cexing"><a url="#" name="车型管理"
						nav="车型管理" class="menu-link"> <i class="fa fa-th-list"></i> <span>车型管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/cartypeprive/cartypeprive.jsp" name="车型"
								nav="车型管理,车型" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>车型价格管理</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="renyuan"><a url="#" name="人员管理"
						nav="人员管理" class="menu-link"> <i class="fa fa-home"></i> <span>人员管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/inneruser/inneruser.jsp" name="人员"
								nav="人员管理,人员" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>人员列表</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="yingji"><a url="#" name="应急救援管理"
						nav="应急救援管理" class="menu-link"> <i class="fa fa-th-list"></i>
							<span>应急救援管理</span> <span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li id="yj_jg"><a url="pages/emergency/emergencyprive.jsp"
								name="应急救援" nav="应急救援管理,价格" class="menu-link"> <i
									class="fa fa-circle-o"></i> <span>价格管理</span>
							</a></li>
							<li id="yj_yd"><a url="pages/emergency/emergencyOrder.jsp"
								name="应急救援" nav="应急救援管理,预约订单" class="menu-link"> <i
									class="fa fa-circle-o"></i> <span>预约订单</span>
							</a></li>
							<li id="yj_ld"><a
								url="pages/emergency/emergencyHistoryOrder.jsp" name="应急救援"
								nav="应急救援管理,历史订单" class="menu-link"> <i
									class="fa fa-circle-o"></i> <span>历史订单</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="pingjia"><a url="#" name="评价管理"
						nav="评价管理" class="menu-link"> <i class="fa fa-list-alt"></i> <span>评价管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/comment/comment.jsp" name="评价"
								nav="评价管理,评价" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>评价列表</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="mima"><a url="#" name="密码管理"
						nav="密码管理" class="menu-link"> <i class="fa fa-lock"></i> <span>密码管理</span>
							<span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/userpwd/userpwd.jsp" name="密码"
								nav="密码管理,修改" class="menu-link"> <i class="fa fa-circle-o"></i>
									<span>修改密码</span>
							</a></li>
						</ul></li>
					<li class="treeview" id="wxMenu"><a url="#" name="微信自定义菜单"
						nav="微信自定义菜单" class="menu-link"> <i class="fa fa-envelope"></i>
							<span>微信自定义菜单</span> <span class="pull-right-container"> <i
								class="fa fa-angle-left pull-right"></i>
						</span>
					</a>
						<ul class="treeview-menu">
							<li><a url="pages/wxMenu/fistMenu.jsp" name="菜单"
								nav="微信自定义菜单,一级菜单" class="menu-link"> <i
									class="fa fa-circle-o"></i> <span>一级菜单</span>
							</a></li>
							<li><a url="pages/wxMenu/secondMenu.jsp" name="菜单"
								nav="微信自定义菜单,二级菜单" class="menu-link"> <i
									class="fa fa-circle-o"></i> <span>二级菜单</span>
							</a></li>
						</ul></li>

					<li class="treeview" id="xitong"><a url="#" name="系统管理"
						nav="系统管理" class="menu-link"> <i class="fa fa-cogs"></i> <span>系统管理</span>
							<span class="pull-right-container"><i
								class="fa fa-angle-left pull-right"></i></span>
					</a>
						<ul class="treeview-menu" style="display: none;">
							<li><a url="#" name="权限管理" nav="系统管理,权限管理" class="menu-link">
									<i class="fa fa-folder-o"></i><span>权限管理</span> <span
									class="pull-right-container"> <i
										class="fa fa-angle-left pull-right"></i>
								</span>
							</a>
								<ul class="treeview-menu menu-open" style="display: block;">
									<li class=""><a url="system/role/roleMaintain.jsp"
										name="角色维护" nav="系统管理,权限管理,角色维护" class="menu-link"> <i
											class="fa fa-circle-o"></i><span>角色维护</span>
									</a></li>
									<li class=""><a url="system/roleMember/roleMember.jsp"
										name="角色成员维护" nav="系统管理,权限管理,角色成员维护" class="menu-link"><i
											class="fa fa-circle-o"></i><span>角色成员维护</span></a></li></li>
						</ul></li>

				</ul>
			</section>
			<!-- /.sidebar -->
		</aside>

		<!-- Content Wrapper. Contains page content -->
		<div class="content-wrapper" style="font-family: Microsoft YaHei;">
			<section class="content-header" style="background: #fff;">
				<h1 id="pagetitle"
					style="font-family: Microsoft YaHei; font-size: 15px; font-weight: bold">Welcome</h1>
				<ol class="breadcrumb" id="breadcrumb">
					<li><a href="#"><i class="fa fa-dashboard"></i> 首页</a></li>
					<li class="active">welcome</li>
				</ol>
			</section>
			<iframe width="100%" height="100%" id="content_iframe"
				src="../welcome/Welcome.jsp" frameborder="0" data-id=""
				style="min-height: 100%;"> </iframe>
		</div>

		<!-- Control Sidebar -->
		<aside class="control-sidebar control-sidebar-dark">
			<!-- Create the tabs -->
			<ul class="nav nav-tabs nav-justified control-sidebar-tabs"></ul>
			<!-- Tab panes -->
			<div class="tab-content" id="tab-content" style="overflow-y: scroll">
				<!-- Home tab content -->
				<div class="tab-pane" id="control-sidebar-home-tab"></div>
				<!-- /.tab-pane -->
				<!-- Stats tab content -->
			</div>
		</aside>
		<!-- /.control-sidebar -->
		<!-- Add the sidebar's background. This div must be placed
       immediately after the control sidebar -->
		<div class="control-sidebar-bg"></div>
		<!--修改密码-->
		<!-- 弹出窗口区域，触发事件后弹出  -->
		<div class="modal fade" id="changePwdModal" tabindex="-1"
			role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" style="width: 500px; margin-top: 200px;">
				<div class="modal-content">
					<div class="modal-header" style="background: #18a689">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalTitle" style="color: white"></h4>
					</div>
					<div class="modal-body">
						<div class="tab-content">
							<div class="tab-pane active">
								<form id="myForm1" class="form-horizontal" role="form">

									<fieldset id="basicInfo">
										<legend style="font-size: 15px; border: 0;"></legend>
										<div class="form-group form-group-sm ">
											<label class="col-sm-3 control-label">原密码</label>
											<div class="col-sm-9">
												<input
													class="form-control validate[required,ajax[checkPwdIndex]]"
													id="oldPwd" type="password" placeholder="必填项" />
											</div>
										</div>
										<div class="form-group form-group-sm ">
											<label class="col-sm-3 control-label">新密码</label>
											<div class="col-sm-9">
												<input class="form-control validate[required]"
													id="newPassword" type="password" placeholder="必填项" />
											</div>
										</div>
										<div class="form-group form-group-sm ">
											<label class="col-sm-3 control-label">重复新密码</label>
											<div class="col-sm-9">
												<input
													class="form-control validate[required,custom[checkRePwd]]"
													id="renewPassword" type="password" placeholder="必填项" />
											</div>
										</div>

									</fieldset>

									<div class="form-group form-group-sm "></div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default"
											data-dismiss="modal">关闭</button>
										<button id="btContractAdd" onclick="openSavePwdButton()"
											class="btn btn-primary " type="button">保存</button>
									</div>
									<input class="form-control" id="contractId" type="hidden" /> <input
										class="form-control" id="fileIds" type="hidden" />
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<!-- 弹出窗口区域，触发事件后弹出   结束 -->
		<!--修改密码结束-->

	</div>
	<p id="ismanager" style="display: none;">${manager}</p>
	<!-- ./wrapper -->
	<!-- jQuery 2.2.3 -->
	<script src="${ContextPath}/UI-lib/plugins/jQuery/jquery-2.2.3.min.js"></script>
	<!-- Bootstrap 3.3.6 -->
	<script src="${ContextPath}/UI-lib/bootstrap/js/bootstrap.min.js"></script>
	<!-- 后台推送 -->
	<script src="${ContextPath}/UI-lib/plugins/toastr/content.min.js"></script>
	<script src="${ContextPath}/UI-lib/plugins/toastr/toastr.min.js"></script>
	<script src="${ContextPath}/UI-lib/plugins/layer/layer.js"></script>
	<script
		src="${ContextPath}/UI-lib/plugins/validate/js/jquery.validationEngine-zh_CN.js"></script>
	<script
		src="${ContextPath}/UI-lib/plugins/validate/js/jquery.validationEngine.js"></script>
	<script
		src="${ContextPath}/UI-lib/plugins/slimScroll/jquery.slimscroll.min.js"></script>
	<script src="${ContextPath}/system/main/mainFrame.js"></script>
	<!-- AdminLTE App -->
	<script src="${ContextPath}/UI-lib/dist/js/app.min.js"></script>
	<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
	<!--<script src="${ContextPath}/UI-lib/dist/js/pages/dashboard.js"></script>-->
	<!-- AdminLTE for demo purposes -->
	<script src="${ContextPath}/UI-lib/dist/js/demo.js"></script>
	<script src="${ContextPath}/UI-lib/plugins/layer/layer.js"></script>
	<script src="${ContextPath}/common/js/jquery.blockUI.js"></script>
</body>
</html>