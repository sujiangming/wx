package com.bj.scb.controller;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.OrderInfo;
import com.bj.scb.service.InnerUserService;
import com.bj.scb.service.OrderInfoService;
import com.bj.scb.service.OrderService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;
import com.bj.scb.weixin.TemplateMessageManager;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "orderinfo", produces = "application/json; charset=utf-8")
public class OrderInfoController {
	@Resource
	private OrderInfoService orderInfoService;
	@Resource
	private OrderService orderService;
	@Resource
	private InnerUserService innerUserService;

	/**
	 * 查询单个客服未完成订单
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public String listOrder(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		//search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		// 获取用户的信息
		InnerUser user = (InnerUser) httpSession.getAttribute("user");
		String checkStationName = user.getCheckStationName();
		String orderUserName = user.getInnerUserName();

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", checkStationName);
		parameter.put("orderUserName", orderUserName);
		PageList<OrderInfo> list = orderInfoService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	/**
	 * 查询单个客服所有未完成订单
	 */
	@RequestMapping(value = "onelist")
	@ResponseBody
	public String listOneOrder(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String checkStation = null == arg0.getParameter("checkStationName") ? ""
				: arg0.getParameter("checkStationName").toString();
		String kfid = null == arg0.getParameter("kfid") ? "" : arg0.getParameter("kfid").toString();
		String checkStationName = URLDecoder.decode(checkStation, "utf-8");
		String kefuid = URLDecoder.decode(kfid, "utf-8");

		List<OrderInfo> list = orderInfoService.selectOneAll(checkStationName, kefuid);
		// 成功返回的结果
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", 1);
		jsonObject.put("list", list);

		return jsonObject.toString();
	}

	/**
	 * 服务完成的方法
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public String saveOrder(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		// 订单号
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		// 微信号
		String openid = null == arg0.getParameter("openid") ? "" : arg0.getParameter("openid").toString();
		// 车主姓名
		String carOwnerName = null == arg0.getParameter("carOwnerName") ? ""
				: arg0.getParameter("carOwnerName").toString();
		// 检车站
		String checkStationName = null == arg0.getParameter("checkStationName") ? ""
				: arg0.getParameter("checkStationName").toString();
		// 车主手机号
		String carOwnerMobile = null == arg0.getParameter("carOwnerMobile") ? ""
				: arg0.getParameter("carOwnerMobile").toString();
		String insertTime = null == arg0.getParameter("insertTime") ? "" : arg0.getParameter("insertTime").toString();
		//入库操作
		orderInfoService.saveOrderInfo(id);
		orderService.saveOrderIs(carOwnerMobile,insertTime);
		// 发送给预约人的评价消息
		TemplateMessageManager.sendCommentMessage(openid, carOwnerName, checkStationName, id,1);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "1");
		return jsonObject.toString();
	}

	/**
	 * 派单 保存订单
	 */
	@RequestMapping(value = "saveOrder")
	@ResponseBody
	public String saveod(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) {

		String carOwnerName = null == arg0.getParameter("carOwnerName") ? ""
				: arg0.getParameter("carOwnerName").toString();
		String carCode = null == arg0.getParameter("carCode") ? "" : arg0.getParameter("carCode").toString();
		String carLicence = null == arg0.getParameter("carLicence") ? "" : arg0.getParameter("carLicence").toString();
		String carOwnerMobile = null == arg0.getParameter("carOwnerMobile") ? ""
				: arg0.getParameter("carOwnerMobile").toString();
		String carInsurance = null == arg0.getParameter("carInsurance") ? ""
				: arg0.getParameter("carInsurance").toString();
		String orderTime = null == arg0.getParameter("orderTime") ? "" : arg0.getParameter("orderTime").toString();
		String orderProject = null == arg0.getParameter("orderProject") ? ""
				: arg0.getParameter("orderProject").toString();
		String checkStationName = null == arg0.getParameter("checkStationName") ? ""
				: arg0.getParameter("checkStationName").toString();
		String kefuid = null == arg0.getParameter("kefuid") ? "" : arg0.getParameter("kefuid").toString();
		String insertTime = null == arg0.getParameter("insertTime") ? "" : arg0.getParameter("insertTime").toString();
		// 通过姓名查询客服人员的信息
		InnerUser innerUser = innerUserService.selsecOneInnerUser(kefuid, checkStationName);
		String openId = null == arg0.getParameter("orderUserId") ? "" : arg0.getParameter("orderUserId").toString();

		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCarCode(carCode);
		orderInfo.setCarInsurance(carInsurance);
		orderInfo.setCarLicence(carLicence);
		orderInfo.setCarOwnerMobile(carOwnerMobile);
		orderInfo.setCarOwnerName(carOwnerName);
		orderInfo.setOrderProject(orderProject);
		orderInfo.setOrderTime(orderTime);
		orderInfo.setCheckStationName(checkStationName);
		orderInfo.setOrderUserName(innerUser.getInnerUserName());
		orderInfo.setKefuid(kefuid);
		orderInfo.setOrderUserId(openId);
		orderInfo.setIsRefund("否");
		long intime=Long.valueOf(insertTime).longValue();
		orderInfo.setInsertTime(intime);
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			orderService.saveOrderTY(carOwnerMobile,insertTime);
			orderInfoService.saveOdInfo(orderInfo);
			result.put("status", "1");
			result.put("message", "派单成功");

			// 给客服发送订单通知
			TemplateMessageManager.sendAdminTemplateMessageKehu(innerUser.getInnerUserId(), carOwnerName,
					checkStationName, kefuid);
			// 给预约人发送接单通知
			TemplateMessageManager.sendYuYueMessage(openId, innerUser.getInnerUserName(), checkStationName,
					innerUser.getInnerUserMobile());
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "派单失败");
		}
		return JSON.toJSONString(result);

	}

	/**
	 * 
	 * 查询单个客服已完成订单
	 */
	@RequestMapping(value = "oneList")
	@ResponseBody
	public String oneList(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		//search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		// 获取用户的信息
		InnerUser user = (InnerUser) httpSession.getAttribute("user");
		String checkStationName = user.getCheckStationName();
		String orderUserName = user.getInnerUserName();

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", checkStationName);
		parameter.put("orderUserName", orderUserName);
		PageList<OrderInfo> list = orderInfoService.selectOnesAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	/**
	 * 
	 * 查询整个检车站客服已完成订单
	 */
	@RequestMapping(value = "oneAllList")
	@ResponseBody
	public String oneAllList(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		//search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		// 获取用户的信息
		InnerUser user = (InnerUser) httpSession.getAttribute("user");
		String checkStationName = user.getCheckStationName();

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", checkStationName);
		PageList<OrderInfo> list = orderInfoService.selectOnesAllList(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}
}
