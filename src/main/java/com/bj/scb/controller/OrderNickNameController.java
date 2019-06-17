package com.bj.scb.controller;

import java.util.HashMap;
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
import com.bj.scb.pojo.Order;
import com.bj.scb.pojo.OrderNickName;
import com.bj.scb.service.OrderNickNameService;
import com.bj.scb.service.impl.OrderNickNameServiceImpl;
import com.bj.scb.utils.PageList;

@Controller
@RequestMapping(value="orderNicName", produces = "application/json; charset=utf-8")
public class OrderNickNameController {
	
	@Resource
	OrderNickNameService orderNickNameService;
	/**
	 * 查询所有预约客户的微信昵称
	 * @param para
	 * @param arg0
	 * @param resp
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list")
	@ResponseBody
	public String listOrderNicName(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
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

		PageList<OrderNickName> list = orderNickNameService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;

	}

}
