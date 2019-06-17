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
	
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>角色维护</title>
<%@ include file="/common/taglibs.jsp"%>

<script type="text/javascript">
	loginUserName = "${loginUser.getInnerUserMobile()}";
	loginUserCname = "${loginUser.getInnerUserName()}";
	loginUserIdentity = "${loginUser.isManager()}";
	loginUserCompany = "${loginUser.getCheckStationId()}";
	loginUserJson = "${loginUser.getId()}";
</script>
<script type="text/javascript"
	src="${ContextPath}/system/roleMember/roleMember.js"></script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">

		<!-- 按钮及查询区域，此处按钮为占位符，实际加载的为view中的按钮控件  -->
		<div
			style="width: 100%; height: 55px; background-color: #f3f3f4; position: fixed; left: 0; top: 0; padding: 0 10px; z-index: 1000">
			<div class="row">
				<div class="col-md-6" style="float: left; margin-top: 10px;"
					id="topToolbar">
					<button id="btn_add" class="btn btn-primary" type="button" style="display: block;">
						<i class="fa fa-plus"></i>&nbsp;按关键字搜索
					</button>
					<button id="btn_del" class="btn btn-primary" disabled type="button" style="display: none;">
						<i class="fa fa-trash-o"></i> 删除
					</button>
				</div>
			</div>
		</div>
		<i class="fa fa-search"
			style="position: fixed; right: 15px; top: 19px; z-index: 1001; color: #e7eaec; font-size: 15px;"></i>
		<!-- 占位div -->
		<div class="" style="width: 100%; height: 45px;"></div>
		<!-- 占位div -->

		<!-- 数据表格区域  -->
		<div class="row">
			<div class="col-sm-12">
				<div class="ibox float-e-margins">
					<div class="ibox-title">
						<h5>
							用户信息<small></small>
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
						<table class="table-no-bordered" id="roleInfo"></table>

					</div>

				</div>
			</div>
		</div>

		<!-- 弹出窗口区域，触发事件后弹出  -->
		<div class="modal fade" id="myModal1" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel">
			<div class="modal-dialog" style="width: 800px">
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
							<div id="tab-1" class="tab-pane active">
								<form id="myForm1" class="form-horizontal" role="form">

									<fieldset id="basicInfo">
										<legend style="font-size: 15px; border: 0;"></legend>
										<div class="form-group form-group-sm ">
											<div id="companyDiv" style="display: none">
												<label class="col-sm-2 control-label">检车站名</label>
												<div class="col-sm-10">
													<select id="companyDrop" class="selectpicker form-control">
													</select>
												</div>
											</div>
											<input id="companyId" type="hidden" />
										</div>
										<div class="form-group form-group-sm ">
											<label class="col-sm-2 control-label">用户名称</label>
											<div class="col-sm-10">
												<input class="form-control" id="name" type="text" />
											</div>
										</div>
										<div class="form-group form-group-sm ">
											<label class="col-sm-2 control-label">角色</label>
											<div class="col-sm-10">
												<input class="form-control" id="desc" type="text" />
											</div>
										</div>
										<br>
									</fieldset>

								</form>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<!-- onclick="openSaveButton()" -->
						<button id="btEmpAdd" class="btn btn-primary " type="button">保存</button>
					</div>
					<input class="form-control" id="roleId" type="hidden" />
				</div>
			</div>
		</div>
		<!-- 弹出窗口区域，触发事件后弹出   结束 -->
	</div>
</body>

</html>