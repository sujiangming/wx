<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	request.setAttribute("ContextPath", path);
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>消息管理</title>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript"
	src="${ContextPath}/weixinweb/app/js/lrz.bundle.js"></script>
<script type="text/javascript"
	src="${ContextPath}/weixinweb/app/js/jweixin-1.4.0.js"></script>
<script type="text/javascript" src="${ContextPath}/pages/msg/msg.js"></script>
<style type="text/css">
.form-group-sm .form-control {
	height: 34px
}

.img_upload_container {
	height: 100px;
	background: white;
	line-height: 44px;
	margin-bottom: 1px;
	font-size: 14px;
	color: #686868;
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	-webkit-box-align: center;
}

.img_upload_container .img_upload_container_first_div {
	width: 30%;
	text-align: right;
}

.img_upload_container>.img_upload_container_second_div {
	display: -webkit-box;
	-webkit-box-orient: horizontal;
	width: 68%;
	padding-left: 5px;
	height: 100%;
	margin-left: 10px;
}

.img_upload_container_second_div>div {
	display: -webkit-box;
	-webkit-box-orient: vertical;
	-webkit-box-align: center;
	-webkit-box-pack: center;
	width: 45%;
	margin-right: 5px;
	vertical-align: middle;
}

.img_upload_container_second_div>div>img {
	display: block;
	width: 100%;
	height: 100%;
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
					<button id="btn_add" class="btn btn-primary" type="button">
						<i class="fa fa-plus"></i>&nbsp;新增
					</button>
					<button id="btn_del" class="btn btn-primary" type="button">
						<i class="fa fa-minus"></i>&nbsp;删除
					</button>
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
							站点信息<small></small>
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
						<table class="table-no-bordered" id="materialsTableInfo">
						</table>

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
						<h4 class="modal-title" id="myModalTitle">新增(编辑)回复消息</h4>
					</div>
					<div class="modal-body">
						<form id="myForm1" class="form-horizontal" role="form">
							<fieldset id="customerInfo">
								<legend style="font-size: 15px; border: 0;"></legend>

								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 标题</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]" id="msgTitle"
											type="text" placeholder="输入标题" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 关键字</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]" id="msgKeyword"
											type="text" placeholder="输入关键字" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 描述</label>
									<div class="col-sm-6">
										<textarea class="form-control validate[required]" id="msgDesc"
											placeholder="输入对关键的描述"></textarea>
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 内容</label>
									<div class="col-sm-6">
										<textarea class="form-control validate[required]"
											id="msgContent" placeholder="输入回复的内容"></textarea>
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>消息类型</label>
									<div class="col-sm-6">
										<select id="msgType" width=100% onchange="selectChange(this)">
											<option value="text" selected="selected">文本消息</option>
											<option value="image">图片消息</option>
											<option value="news">图文消息</option>
										</select>
									</div>
								</div>
								<!-- 图片或图文所需上传图片 -->
								<div class="form-group form-group-sm " id="picDiv"
									style="display: none;">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>上传图片</label>
									<div class="img_upload_container">
										<div class="img_upload_container_second_div">
											<div id="up_image_1"
												style="position: relative; border: 1px dashed #999999;">
												<font id="font_1"
													style="z-index: 150; position: absolute; width: 100%; height: 98px; line-height: 98px; font-size: 12px; text-align: center;">点击上传主页</font>
												<input type='file' accept="image/*" id="input1"
													name="carLicence"
													style="float: left; z-index: 150; position: absolute; width: 100%; height: 98px; opacity: 0;"
													onchange="uploadImage(this)" /> <img id="picUrl"
													style="z-index: 200; position: absolute; width: 80%; height: 80%; left: 10%; top: 10%; display: none;"
													src="../../weixinweb/img/photo.png" />
											</div>
										</div>
									</div>
								</div>
								<div class="form-group form-group-sm " id="clickUrlDiv" style="display: none;">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span> 图文地址</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]" id="msgClickUrl"
											type="text" placeholder="输入图文消息地址" />
									</div>
								</div>
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