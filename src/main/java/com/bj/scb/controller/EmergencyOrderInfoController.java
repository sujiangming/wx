package com.bj.scb.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bj.scb.pojo.CustomerInfo;
import com.bj.scb.pojo.EmergencyOrderInfo;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Order;
import com.bj.scb.pojo.OrderNickName;
import com.bj.scb.service.CustomerInfoService;
import com.bj.scb.service.EmergencyBeanService;
import com.bj.scb.service.EmergencyOrderInfoService;
import com.bj.scb.service.InnerUserService;
import com.bj.scb.service.OrderNickNameService;
import com.bj.scb.service.OrderService;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;
import com.bj.scb.weixin.AuthUtil;
import com.bj.scb.weixin.TemplateMessageManager;
import com.bj.scb.weixin.Util;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "emergencyOrderInfo", produces = "application/json; charset=utf-8")
public class EmergencyOrderInfoController {

	@Resource
	EmergencyOrderInfoService emergencyOrderInfoService;

	@Resource
	OrderService orderService;

	@Resource
	InnerUserService innerUserService;

	@Resource
	CustomerInfoService customerInfoService;

	@Resource
	OrderNickNameService orderNickNameService;

	/**
	 * 
	 * @category 预约功能
	 */
	@RequestMapping(value = "saveOrderInfo", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveOrderInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> parameter) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "*");// 解决跨域问题
		// 预约人的微信号
		String orderUserId = parameter.get("openid");
		String carOwnerName = URLDecoder.decode(parameter.get("carOwnerName"), "UTF-8");
		String carOwnerMobile = parameter.get("carOwnerMobile");
		String checkStationName = URLDecoder.decode(parameter.get("checkStationName"), "UTF-8");
		String checkStationId = parameter.get("checkStationId");
		String orderLocation = URLDecoder.decode(parameter.get("orderLocation"), "UTF-8");
		String deposit = parameter.get("deposit");
		String kilometer = parameter.get("kilometer");
		String sumPrice = parameter.get("sumPrice");
		String carCode = parameter.get("carCode");

		EmergencyOrderInfo order = new EmergencyOrderInfo();

		order.setCarOwnerMobile(carOwnerMobile);
		order.setCarOwnerName(carOwnerName);
		order.setCheckStationName(checkStationName);
		order.setIsRefund("否");
		order.setTypeState("已预约");
		order.setInsertTime(JdryTime.getFullTimeBySec((new Date()).getTime()));
		order.setOrderUserId(orderUserId);
		order.setCheckStationId(checkStationId);
		order.setKilometer(kilometer);
		order.setPayFee(deposit);
		order.setSumPrice(sumPrice);
		order.setCarCode(carCode);
		order.setOrderLocation(orderLocation);

		// 保存订单信息
		emergencyOrderInfoService.saveOdInfo(order);
		// 保存客户预约记录及客户昵称
		savenickname(orderUserId, checkStationName, carOwnerName, carOwnerMobile);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "success");

		try {
			// 发送给预约人
			TemplateMessageManager.sendTemplateMessageToCustomer(orderUserId, carOwnerName);
			// 发送给区域管理员
			InnerUser innerUser = orderService.selectAdminByStationName(checkStationName);
			if (null != innerUser) {
				TemplateMessageManager.sendYJAdminTemplateMessage(innerUser.getInnerUserId(), carOwnerName,
						checkStationName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return jsonObject.toString();

	}

	// 保存客户预约记录及客户昵称
	public void savenickname(String orderUserId, String checkStationName, String carOwnerName, String carOwnerMobile) {
		CustomerInfo cInfo = customerInfoService.selectNickName(orderUserId);
		if (cInfo != null) {
			OrderNickName oNickName = new OrderNickName();
			oNickName.setOrderUserId(orderUserId);
			oNickName.setOrderUserNickName(cInfo.getCustomerNickName());
			oNickName.setCustomerPic(cInfo.getCustomerPic());
			oNickName.setCarOwnerMobile(carOwnerMobile);
			oNickName.setCarOwnerName(carOwnerName);
			oNickName.setCheckStationName(checkStationName);
			oNickName.setInsertTime((new Date()).getTime());
			orderNickNameService.saveObj(oNickName);
		}
	}

	/**
	 * @category 在后台进行派单
	 */
	@RequestMapping(value = "sendOrder")
	@ResponseBody
	public String saveod(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) {
		String kefuObj = null == arg0.getParameter("kefuObj") ? "" : arg0.getParameter("kefuObj").toString();
		String openId = null == arg0.getParameter("orderUserId") ? "" : arg0.getParameter("orderUserId").toString();
		String orderId = null == arg0.getParameter("orderId") ? "" : arg0.getParameter("orderId").toString();

		// 客服人员的信息
		InnerUser innerUser = JSON.parseObject(kefuObj, InnerUser.class);

		EmergencyOrderInfo orderInfo = emergencyOrderInfoService.getOrderInfoById(orderId);

		Map<String, Object> result = new HashMap<String, Object>();

		if ("派单中".equals(orderInfo.getTypeState())) {
			result.put("status", "1");
			result.put("message", "手机端已经完成了派单，请勿再派单~");
			return JSON.toJSONString(result);
		}

		orderInfo.setStaffName(innerUser.getInnerUserName());
		orderInfo.setStaffId(innerUser.getId());
		orderInfo.setIsRefund("否");
		orderInfo.setTypeState("派单中");
		orderInfo.setSendTime(JdryTime.getFullTimeBySec(new Date().getTime()));

		try {
			emergencyOrderInfoService.saveOdInfo(orderInfo);
			result.put("status", "1");
			result.put("message", "派单成功");
			try {
				// 给客服发送接单消息并完成服务
				TemplateMessageManager.sendYJStaffMessage(innerUser.getInnerUserId(), innerUser.getInnerUserName(),
						innerUser.getCheckStationName(), innerUser.getId());
				// 给预约人发送客服人员已经接单的通知
				TemplateMessageManager.sendYuYueMessage(openId, innerUser.getInnerUserName(),
						innerUser.getCheckStationName(), innerUser.getInnerUserMobile());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "派单失败");
		}
		return JSON.toJSONString(result);

	}

	/**
	 * @category 根据ID查询应急救援订单信息
	 * @param request
	 * @param response
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryYJOrderByStaffId")
	@ResponseBody
	public String queryYJOrderById(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> parameter) throws Exception {
		String staffId = parameter.get("staffId");
		List<EmergencyOrderInfo> orderInfo = emergencyOrderInfoService.queryYJOrderByStaffId(staffId);
		JSONObject object = new JSONObject();
		if (null == orderInfo) {
			object.put("status", 0);
			object.put("message", "没有查到任何订单");
			object.put("data", "");
			return object.toString();
		}
		object.put("status", 1);
		object.put("message", "查询成功");
		object.put("data", orderInfo);
		return object.toString();
	}

	/**
	 * @category 根据救援点的名称，查询应急救援点下的所有状态为"已预约"的订单
	 * @param request
	 * @param response
	 * @param session
	 * @param parameter
	 * @return
	 * @throws Exception
	 */

	@RequestMapping(value = "queryYJOrderByName")
	@ResponseBody
	public String selectOrders(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam Map<String, String> parameter) throws Exception {
		String checkStation = parameter.get("checkStation");
		JSONObject object = new JSONObject();
		List<EmergencyOrderInfo> list = emergencyOrderInfoService
				.queryYJOrderByName(URLDecoder.decode(checkStation, "utf-8"));
		if (null == list || list.size() == 0) {
			object.put("status", 0);
			object.put("m", list);
			return object.toString();
		}
		object.put("status", 1);
		object.put("m", list);
		return object.toString();
	}

	/**
	 * @category 修改应急救援订单状态，即派单中调用这个方法
	 * @param request
	 * @param response
	 * @param parameter
	 * @return
	 */
	@RequestMapping("updateOrderInfo")
	@ResponseBody
	public String updateOrderInfo(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> parameter) {
		String orderId = parameter.get("id");
		String staffId = parameter.get("staffId");// 客服的Id
		String staffName = parameter.get("staffName");// 客服的名称
		String typeState = parameter.get("typeState"); // 订单状态
		String staffWxId = parameter.get("staffWxIdValue");// 客服的微信号
		String staffMobile = parameter.get("staffMobile");// 客服的手机号
		String carOwnerName = parameter.get("carOwnerName");// 预约人的名称
		String checkStationName = parameter.get("checkStationName");// 应急救援点的名称
		String orderWxId = parameter.get("orderWxId");// 预约人的微信号

		EmergencyOrderInfo orderInfo = emergencyOrderInfoService.getOrderInfoById(orderId);

		Map<String, Object> result = new HashMap<String, Object>();

		if ("派单中".equals(orderInfo.getTypeState())) {
			result.put("status", "1");
			result.put("message", "后台管理系统已经完成了派单，请勿再派单~");
			return JSON.toJSONString(result);
		}

		try {
			emergencyOrderInfoService.updateOrderInfo(orderId, staffId, staffName, typeState);
			result.put("status", 1);
			result.put("message", "派单成功");

			try {
				// 给客服发送接单消息并完成服务
				TemplateMessageManager.sendYJStaffMessage(staffWxId, carOwnerName, checkStationName, staffId);
				// 给预约人发送客服人员已经接单的通知
				TemplateMessageManager.sendYuYueMessage(orderWxId, staffName, checkStationName, staffMobile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			result.put("status", 0);
			result.put("message", "派单失败");
		}
		return JSON.toJSONString(result);
	}

	/**
	 * @category 服务完成的方法
	 */
	@RequestMapping(value = "save")
	@ResponseBody
	public String emergencyOrderFinish(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		// 订单号
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		// 根据id查询
		EmergencyOrderInfo emergencyOrderInfo = emergencyOrderInfoService.getOrderInfoById(id);
		// 更新操作
		emergencyOrderInfo.setIsRefund("是");
		emergencyOrderInfo.setTypeState("已完成");
		emergencyOrderInfo.setFinishTime(JdryTime.getFullTimeBySec(new Date().getTime()));
		JSONObject jsonObject = new JSONObject();
		try {
			emergencyOrderInfoService.saveOdInfo(emergencyOrderInfo);
			jsonObject.put("status", "1");
			jsonObject.put("message", "服务完成");
			try {
				// 发送给预约人的评价消息
				TemplateMessageManager.sendCommentMessage(emergencyOrderInfo.getOrderUserId(),
						emergencyOrderInfo.getCarOwnerName(), emergencyOrderInfo.getCheckStationName(), id, 2);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			jsonObject.put("status", "0");
			jsonObject.put("message", "服务失败");
		}
		return jsonObject.toString();
	}

	/**
	 * @category 微信公众号上我的预约页面调用此方法，根据OpenId进行订单的查询
	 * @param request
	 * @param response
	 * @param session
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "queryYJOrderByOpenId")
	@ResponseBody
	public String queryYJOrderByOpenId(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam Map<String, String> parameter) throws Exception {
		String openId = parameter.get("openId");
		JSONObject object = new JSONObject();
		List<EmergencyOrderInfo> list = emergencyOrderInfoService.getAllOrderByOpenId(openId);
		if (null == list || list.size() == 0) {
			object.put("status", 0);
			object.put("message", "还没有订单");
			object.put("m", list);
			return object.toString();
		}
		object.put("status", 1);
		object.put("message", "订单查询成功");
		object.put("m", list);
		return object.toString();
	}

	// ============================以下是之前复制预约管理的方法进行修改的============================
	/**
	 * @category 查询检车站下所有未完成订单
	 */
	@RequestMapping(value = "listAll")
	@ResponseBody
	public String listOrderAll(HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search").toString();
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
		parameter.put("isManager", user.isManager());
		parameter.put("id", user.getId());
		
		PageList<EmergencyOrderInfo> list = emergencyOrderInfoService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	/**
	 * @category 查询单个客服所有的未完成的订单
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public String listOrder(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search").toString();
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
		PageList<EmergencyOrderInfo> list = emergencyOrderInfoService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	/**
	 * @category 查询单个客服所有未完成订单
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

		List<EmergencyOrderInfo> list = emergencyOrderInfoService.selectOneAll(checkStationName, kefuid);
		// 成功返回的结果
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", 1);
		jsonObject.put("list", list);

		return jsonObject.toString();
	}

	/**
	 * 
	 * @category 查询单个客服已完成订单
	 */
	@RequestMapping(value = "oneList")
	@ResponseBody
	public String oneList(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		// search = CharacterUtil.getUTF_8String(search);
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
		PageList<EmergencyOrderInfo> list = emergencyOrderInfoService.selectOnesAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	/**
	 * 
	 * @category 查询整个检车站客服已完成订单
	 */
	@RequestMapping(value = "oneAllList")
	@ResponseBody
	public String oneAllList(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
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
		parameter.put("isManager", user.isManager());
		parameter.put("id", user.getId());
		
		PageList<EmergencyOrderInfo> list = emergencyOrderInfoService.selectOnesAllList(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	/**
	 * 用户授权事件
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "shouQuan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String shouQuan(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam Map<String, String> parameter) throws Exception {
		String backUrl = "https://www.kjscb.com/wx/emergencyOrderInfo/callBack";
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + Util.APPID + "&redirect_uri="
				+ URLEncoder.encode(backUrl, "utf-8") + "&response_type=code" + "&scope=snsapi_base"
				+ "&state=STATE#wechat_redirect";
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("msg", "success");
		jsonObject.put("url", url);
		return jsonObject.toString();
	}

	@RequestMapping(value = "callBack")
	public String Callback(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		String code = request.getParameter("code");
		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Util.APPID + "&secret="
				+ Util.APPSECRET + "&code=" + code + "&grant_type=authorization_code";
		try {
			JSONObject json = AuthUtil.doGetJson(url);
			String orderUserId = json.getString("openid");
			session.setAttribute("oppind", orderUserId);
			return "/weixinweb/emergencyOrder";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/";
	}
}
