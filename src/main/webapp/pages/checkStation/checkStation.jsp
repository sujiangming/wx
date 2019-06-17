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
<title>站点管理</title>
<%@ include file="/common/taglibs.jsp"%>
<link href="${ContextPath}/UI-lib/plugins/fileinput/fileinput.min.css"
	rel="stylesheet" />
<!-- 高德地图样式及插件 start -->
<!-- <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css" /> -->
<script type="text/javascript"
	src="https://cache.amap.com/lbs/static/addToolbar.js"></script>
<!-- 高德地图样式及插件 end -->	
<script src="${ContextPath}/UI-lib/plugins/fileinput/fileinput.min.js"
	type="text/javascript"></script>
<script src="${ContextPath}/pages/checkStation/checkStation.js"
	type="text/javascript"></script>
<style type="text/css">
.form-group-sm .form-control {
	height: 34px
}

/* #container {
	width: 100%;
	height: 280px;
	margin: 0 auto;
} */
.map {
	height: 100%;
	width: 100%;
	float: left;
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
						<h4 class="modal-title" id="myModalTitle">新增(编辑)站点信息</h4>
					</div>
					<div class="modal-body">
						<form id="myForm1" class="form-horizontal" role="form">
							<fieldset id="customerInfo">
								<legend style="font-size: 15px; border: 0;"></legend>

								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										站点名称</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="checkStationName" type="text" />
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										站点地址</label>
									<div class="col-sm-6">
										<textarea class="form-control validate[required]"
											id="checkStationAddress"></textarea>
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										站点经度</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="checkStationX" type="text" placeholder="点击地图获取经纬度"/>
									</div>
								</div>
								<div class="form-group form-group-sm ">
									<label class="col-sm-2 control-label"><span
										style="color: red; position: relative; top: 2px;">*</span>
										站点纬度</label>
									<div class="col-sm-6">
										<input class="form-control validate[required]"
											id="checkStationY" type="text" placeholder="点击地图获取经纬度"/>
									</div>
								</div>
								<br>
							</fieldset>
						</form>
					</div>
					<div style="width: 100%; height: 280px; background: #FDFBF3;position: relative;">
						<div id="container" class="map"></div>
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
	<!-- 添加地图插件 -->
	<script type="text/javascript"
		src="https://webapi.amap.com/maps?v=1.4.11&key=8cf316f903f4807def2226d9b323db0a"></script>
	<script type="text/javascript">
		
		//地图获取所点击的地址的坐标
		var map = new AMap.Map("container", {
			resizeEnable : true,
			center : [105.303795, 27.29951],
			zoom: 12 //初始地图级别
		});
		//为地图注册click事件获取鼠标点击出的经纬度坐标
		map.on('click', function(e) {
			document.getElementById("checkStationX").value = e.lnglat.getLng();
			document.getElementById("checkStationY").value = e.lnglat.getLat();
		});
		var auto = new AMap.Autocomplete({
			input : "tipinput"
		});
		AMap.event.addListener(auto, "select", select);//注册监听，当选中某条记录时会触发
		function select(e) {
			if (e.poi && e.poi.location) {
				map.setZoom(15);
				map.setCenter(e.poi.location);
			}
		}
	</script>
</body>

</html>