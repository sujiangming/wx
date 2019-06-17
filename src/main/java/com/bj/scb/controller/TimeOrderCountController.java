package com.bj.scb.controller;

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
import com.alibaba.fastjson.JSONObject;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.service.TimeOrderCountService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

@Controller
@RequestMapping(value = "timeOrderCount", produces = "application/json; charset=utf-8")
public class TimeOrderCountController {
	@Resource
	private TimeOrderCountService timeOrderCountService;

	@RequestMapping(value = "list")
	@ResponseBody
	public String list(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		//search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;

		// 获取用户信息
		InnerUser user = (InnerUser) httpSession.getAttribute("user");

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", user.getCheckStationName());
		PageList<TimeOrderCount> list = timeOrderCountService.listAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;

	}

	@RequestMapping(value = "save")
	@ResponseBody
	public String saveTimeOrderCount(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		String checkStationName = null == arg0.getParameter("checkStationName") ? ""
				: arg0.getParameter("checkStationName").toString();
		String orderDate = null == arg0.getParameter("orderDate") ? "" : arg0.getParameter("orderDate").toString();
		String orderTime = null == arg0.getParameter("orderTime") ? "" : arg0.getParameter("orderTime").toString();
		String orderCount = null == arg0.getParameter("orderCount") ? "" : arg0.getParameter("orderCount").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		TimeOrderCount tCount = new TimeOrderCount();
		if (id != null && !id.isEmpty()) {
			tCount = timeOrderCountService.getTimeOrderCountById(id);
		}
		tCount.setCheckStationName(checkStationName);
		tCount.setOrderCount(orderCount);
		tCount.setOrderDate(orderDate);
		tCount.setOrderTime(orderTime);
		tCount.setOrderStartTime(Integer.valueOf(orderTime.split("-")[0].split(":")[0]));
		try {
			timeOrderCountService.saveTimeOrderCount(tCount);
			result.put("status", "1");
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);

	}

	@RequestMapping(value = "delete")
	@ResponseBody
	public String deleteTimeOrderCount(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (!id.isEmpty()) {
				timeOrderCountService.deleteTimeOrderCount(id);
				result.put("status", "1");
				result.put("message", "删除成功");
			} else {
				result.put("status", "0");
				result.put("message", "删除失败");
			}
			return JSON.toJSONString(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "删除失败");
			return JSON.toJSONString(result);
		}
	}

	@RequestMapping("select")
	@ResponseBody
	public String select(@RequestParam Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		TimeOrderCount timeOrderCount = timeOrderCountService.select(map);
		if (timeOrderCount == null) {
			jsonObject.put("status", 0);
			jsonObject.put("message", "没有查询到数据~");
			jsonObject.put("data", null);
			return jsonObject.toJSONString();
		}
		jsonObject.put("status", 1);
		jsonObject.put("message", "已查询到数据~");
		jsonObject.put("data", timeOrderCount);
		return jsonObject.toJSONString();

	}
}
