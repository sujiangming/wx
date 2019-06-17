package com.bj.scb.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Order;
import com.bj.scb.service.OrderService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;
import com.bj.scb.weixin.AuthUtil;
import com.bj.scb.weixin.Util;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "order", produces = "application/json; charset=utf-8")
public class OrderController {

	@Resource
	private OrderService orderService;

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

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", checkStationName);

		PageList<Order> list = orderService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;

	}

	@RequestMapping(value = "listAll")
	@ResponseBody
	public String getAllOrder(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam Map<String, String> parameter) {
		JSONObject object = new JSONObject();
		List<Order> list = orderService.getAllOrderByOpenId(parameter.get("opind").toString());
		object.put("m", list);
		return object.toString();
	}

	@RequestMapping(value = "selectOrders")
	@ResponseBody
	public String selectOrders(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam Map<String, String> parameter) throws Exception {
		String checkStation = parameter.get("checkStation") == null ? "" : parameter.get("checkStation").toString();
		JSONObject object = new JSONObject();
		List<Order> list = orderService.selectOrdersByName(URLDecoder.decode(checkStation, "utf-8"));
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
	 * 用户授权事件
	 * 
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "shouQuan", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String shouQuan(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam Map<String, String> parameter) throws Exception {
		String backUrl = "https://www.kjscb.com/wx/order/callBack";
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
			return "/weixinweb/me";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/";
	}
}
