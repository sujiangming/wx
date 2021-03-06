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
<title>订单记录管理</title>
<%@ include file="/common/taglibs.jsp"%>
<link href="${ContextPath}/UI-lib/plugins/fileinput/fileinput.min.css"
	rel="stylesheet" />
<script src="${ContextPath}/UI-lib/plugins/fileinput/fileinput.min.js"
	type="text/javascript"></script>
<script src="${ContextPath}/pages/orderinfo/orderInfo.js"
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
					<!-- <button id="btn_add" class="btn btn-primary" type="button"><i class="fa fa-plus"></i>&nbsp;新增</button> -->
					<!--  <button id="btn_del" class="btn btn-primary" type="button"><i class="fa fa-minus"></i>&nbsp;删除</button> -->
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
							订单记录<small></small>
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
						<table class="table-no-bordered" id="materialsTableInfo"></table>

					</div>

				</div>
			</div>
		</div>

		<!-- 弹出窗口区域，触发事件后弹出  -->
		<div class="modal fade" id="myModal1" tabindex="-1" role="dialog"
			aria-labelledby="myModalLabel">
			<div class="modal-dialog" style="width: 800px">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal"
							aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
						<h4 class="modal-title" id="myModalTitle">新增(编辑)资料</h4>
					</div>
					<div class="modal-body">
						<form id="myForm1" class="form-horizontal" role="form">
							<fieldset id="customerInfo">
								<legend style="font-size: 15px; border: 0;"></legend>

								<div class="form-group form-group-sm " style="display:none;">
									  <label class="col-sm-2 control-label"><span style="color:red;position:relative;top:2px;">*</span> </label>
									  <div class="col-sm-6">
											<input class="form-control validate[required]" id="csname" value="${loginUser.checkStationName}" type="text" />
									  </div>
							   </div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 姓名</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="carOwnerName" type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 车牌号</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]" id="carCode"
											type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 行车证</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]" id="carLicence"
											type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 手机号</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="carOwnerMobile" type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										车辆保险时间</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="carInsurance" type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										预约时段</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]" id="orderTime"
											type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										预约车型</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="orderProject" type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										订单完成</label>
									<div class="col-sm-6">
											<input class="form-control validate[required]"
											id="isRefund" type="text" />
									</div>
								</div>
								<br>
							</fieldset>
						</form>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button id="btEmpAdd" onclick="openSaveButton()"
							class="btn btn-primary" type="button">保存</button>
					</div>
					<input class="form-control" id="id" type="hidden" />
				</div>
			</div>
		</div>
		<!-- 弹出窗口区域，触发事件后弹出   结束 -->
	</div>
</body>

</html>