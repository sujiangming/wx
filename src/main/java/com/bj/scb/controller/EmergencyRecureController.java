package com.bj.scb.controller;

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
import com.bj.scb.pojo.CheckStation;
import com.bj.scb.pojo.EmergencyPrice;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.CheckStationService;
import com.bj.scb.service.EmergencyPriceService;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "emergency")
public class EmergencyRecureController {

	@Resource
	CheckStationService checkStationService;

	@Resource
	EmergencyPriceService emergencyPriceService;

	@RequestMapping(value = "getStationList", produces = "application/json; charset=utf-8")
	public @ResponseBody String getStations(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");// 解决跨域问题
		JSONObject json = new JSONObject();
		List<CheckStation> list = checkStationService.getStationListByType("救援站点");
		if (null == list || list.size() == 0) {
			json.put("status", 0);
			json.put("message", "没有查到检车站数据，请重试~");
			return json.toString();
		}
		json.put("status", 1);
		json.put("message", "已查到检车站数据~");
		json.put("data", list);
		return json.toString();
	}

	@RequestMapping(value = "getPrice", produces = "application/json; charset=utf-8")
	public @ResponseBody String getPrice(HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		response.setHeader("Access-Control-Allow-Origin", "*");// 解决跨域问题
		JSONObject json = new JSONObject();
		EmergencyPrice emergencyPrice = emergencyPriceService.getPrice();
		if (null == emergencyPrice) {
			json.put("status", 0);
			json.put("message", "没有查到价格数据，请重试~");
			return json.toString();
		}
		json.put("status", 1);
		json.put("message", "已查到价格数据~");
		json.put("data", emergencyPrice);
		return json.toString();
	}

	@RequestMapping(value = "list", produces = "application/json; charset=utf-8")
	public @ResponseBody String queryRolePageList(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		// 获取用户的信息
		InnerUser user = (InnerUser) httpSession.getAttribute("user");
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		// parameter.put("checkStationName", user.getCheckStationName());

		PageList<EmergencyPrice> list = emergencyPriceService.selectList(parameter);

		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}"; // 服务端分页必须返回total和rows,rows为每页记录
		return r;
	}

	@RequestMapping(value = "saveOrUpdate", produces = "application/json; charset=utf-8")
	public @ResponseBody String saveRole(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String unitPrice = null == arg0.getParameter("unitPrice") ? "" : arg0.getParameter("unitPrice").toString();
		String deposit = null == arg0.getParameter("deposit") ? "" : arg0.getParameter("deposit").toString();
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		// 获取用户的信息
		InnerUser user = (InnerUser) httpSession.getAttribute("user");
		EmergencyPrice role = null;
		if (id != null && !id.isEmpty()) {
			role = emergencyPriceService.getEmergencyById(id);

			role.setDeposit(Double.valueOf(deposit));
			role.setUnitPrice(Double.valueOf(unitPrice));
			role.setCheckStationName(user.getCheckStationName());
			role.setUpdateTime(JdryTime.getFullTimeBySec(new Date().getTime()));

			try {
				emergencyPriceService.saveOrUpdate(role);
				result.put("status", "1");
				result.put("message", "更新成功");
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", "1");
				result.put("message", "更新失败");
			}

			return JSON.toJSONString(result);
		}
		role = new EmergencyPrice();
		role.setCreateTime(JdryTime.getFullTimeBySec(new Date().getTime()));
		role.setDeposit(Double.valueOf(deposit));
		role.setUnitPrice(Double.valueOf(unitPrice));
		role.setCheckStationName(user.getCheckStationName());

		try {
			emergencyPriceService.save(role);
			result.put("status", "1");
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "1");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
	}

	@RequestMapping(value = "delete", produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteRole(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String roleId = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (!roleId.isEmpty()) {
				emergencyPriceService.delete(roleId);
				result.put("status", "1");
				result.put("message", "删除成功");
			}
			return JSON.toJSONString(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "删除失败");
			return JSON.toJSONString(result);
		}
	}

}
