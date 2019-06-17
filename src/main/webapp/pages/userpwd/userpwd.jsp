<%@page import="com.bj.scb.pojo.InnerUser"%>
<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	request.setAttribute("ContextPath", path);
	//获取用户信息,从session 里面
	HttpSession httpSession = request.getSession();
	InnerUser user = (InnerUser) httpSession.getAttribute("user");
	request.setAttribute("loginUser", user);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>密码管理</title>
<%@ include file="/common/taglibs.jsp"%>
<link href="${ContextPath}/UI-lib/plugins/fileinput/fileinput.min.css"
	rel="stylesheet" />
<script src="${ContextPath}/UI-lib/plugins/fileinput/fileinput.min.js"
	type="text/javascript"></script>
<style type="text/css">
.form-group-sm .form-control {
	height: 34px
}
</style>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">

		<!-- 按钮及查询区域，此处按钮为占位符，实际加载的为view中的按钮控件  -->
		<div
			style="width: 100%; height: 55px; background-color: #f3f3f4; position: fixed; left: 0; top: 0; padding: 0 10px; z-index: 1000">
			<div class="row">
				<div class="col-md-6" style="float: left; margin-top: 10px;">
					<!--  <button id="btn_add" class="btn btn-primary" type="button"><i class="fa fa-plus"></i>&nbsp;新增</button>
		   			 <button id="btn_del" class="btn btn-primary" type="button"><i class="fa fa-minus"></i>&nbsp;删除</button> -->
				</div>
			</div>
		</div>
		<i class="fa fa-search"
			style="position: fixed; right: 20px; top: 19px; z-index: 1001; color: #e7eaec; font-size: 15px;"></i>
		<!-- 占位div -->
		<div class="" style="width: 100%; height: 45px;"></div>
		<!-- 占位div -->

		<!-- 数据表格区域  -->
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>
							修改密码<small></small>
						</h5>
						<div class="ibox-tools">
							<a class="collapse-link"> <i class="fa fa-chevron-up"></i>
							</a> <a class="dropdown-toggle" data-toggle="dropdown"
								href="table_data_tables.html#"> <i class="fa fa-wrench"></i>
							</a> <a class="close-link"> <i class="fa fa-times"></i>
							</a>

						</div>
					</div>
					<div class="ibox-content" id="tempTable">
						<div id="toolbar"></div>
						<div style="width: 100%; height: 200px;">
							<div style="width: 100%;">

								<div style="margin-top: 20px;">
									<label style="width: 100px;"><span style="color: red;">*</span>
										旧密码</label> <input id="pwd" type="text" placeholder="请输入旧密码" />
								</div>
								<div style="margin-top: 20px;">
									<label style="width: 100px;"><span style="color: red;">*</span>
										新密码</label> <input id="newpwd" type="text" placeholder="请输入新密码" />
								</div>
								<div style="margin-top: 20px;">
									<label style="width: 100px;"><span style="color: red;">*</span>确认新密码</label>
									<input id="newpwd1" type="text" placeholder="确认新密码" />
								</div>
								<div style="margin-top: 20px;">
									<label style="width: 100px;"><input type="text" style="display:none;" value="${loginUser.innerUserMobile}" id="lospan" /></label>
									<input
										style="border: 0; width: 100px; background-color: blue; color: white;"
										value="提交" onclick="clickPwd()" type="button" />
								</div>
							</div>


						</div>

					</div>

				</div>
			</div>
		</div>


	</div>
	<script type="text/javascript">
		function clickPwd() {
			var pwd = $("#pwd").val();
			var newpwd = $("#newpwd").val();
			var newpwd1 = $("#newpwd1").val()
			var mobel = $("#lospan").val();
			if (pwd == "" || newpwd == "" || newpwd1 == "") {
				alert("内容不能为空，请重新输入");
			} else if (newpwd != newpwd1) {
				alert("两次输入密码不一致，请重新输入");
			} else {
				data = {
					"pwd" : pwd,
					"newpwd" : newpwd,
					"mobel" : mobel
				};
				$.ajax({
					url : getRootPath() + "innerUser/newpwd",
					data : data,
					type : "post",
					dataType : "json",
					success : function(data) {
						if (data.messige == "success") {
							alert("密码修改成功，请重新登录");
							parent.location.href = "${ContextPath}/login/login.jsp";
						}else{
							alert("旧密码错误");
						}
					}

				});
			}
		}
		$('#enterYear').datetimepicker({
			format : 'yyyy',
			language : 'zh-CN',
			autoclose : 1,
			startView : 'decade',
			minView : 'decade',
			maxView : 'decade',
			autoclose : true
		});
	</script>
</body>

</html>