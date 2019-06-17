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
import com.bj.scb.pojo.CarTypePrice;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.CarTypePriceService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "carTypePrice", produces = "application/json; charset=utf-8")
public class CarTypePriceController {
	@Resource
	private CarTypePriceService carTypePriceService;

	@RequestMapping(value = "selectList")
	@ResponseBody
	public String selectAll(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		//search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		//获取用户的信息
		InnerUser user = (InnerUser)httpSession.getAttribute("user");
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", user.getCheckStationName());

		PageList<CarTypePrice> list = carTypePriceService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;
	}

	@RequestMapping(value = "save", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveCarTypePrice(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		String carType = null == arg0.getParameter("carType") ? "" : arg0.getParameter("carType").toString();
		String carStandard = null == arg0.getParameter("carStandard") ? ""
				: arg0.getParameter("carStandard").toString();
		String deposit = null == arg0.getParameter("deposit") ? "" : arg0.getParameter("deposit").toString();
		String carPrice = null == arg0.getParameter("carPrice") ? "" : arg0.getParameter("carPrice").toString();
		//获取用户的信息
		InnerUser user = (InnerUser)httpSession.getAttribute("user");
		String checkStationName=user.getCheckStationName();
		Map<String, Object> result = new HashMap<String, Object>();
		CarTypePrice carTypePrice = new CarTypePrice();
		if (id != null && !id.isEmpty()) {
			carTypePrice = carTypePriceService.getRoleById(id);
		}
		carTypePrice.setCarPrice(carPrice);
		carTypePrice.setCarStandard(carStandard);
		carTypePrice.setCarType(carType);
		carTypePrice.setDeposit(deposit);
		carTypePrice.setCheckStationName(checkStationName);

		try {
			carTypePriceService.saveCarTypePrice(carTypePrice);
			result.put("status", "1");
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
	}

	@RequestMapping(value = "delete", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String deleteCarTypePrice(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (!id.isEmpty()) {
				carTypePriceService.deleteCarTypePrice(id);
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

	@RequestMapping(value = "list", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String list(HttpServletRequest request, HttpServletResponse response,@RequestParam String checkStationName) {
		JSONObject jsonObject = new JSONObject();
		List<CarTypePrice> list = carTypePriceService.list(checkStationName);
		if (null == list || list.size() == 0) {
			jsonObject.put("status", 0);
			jsonObject.put("data", "");
			jsonObject.put("message", "没有查到车型价格数据~");// massige
			return jsonObject.toString();
		}
		jsonObject.put("status", 1);
		jsonObject.put("message", "已查到车型价格数据！！");
		jsonObject.put("data", JSON.toJSONString(list));
		return jsonObject.toString();
	}

	@RequestMapping(value = "money", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String money(HttpServletRequest request, HttpServletResponse response,@RequestParam String carType,@RequestParam String checkStationName) {	
		JSONObject jsonObject = new JSONObject();
		if("".equals(carType) || null == carType) {
			jsonObject.put("status", 0);
			jsonObject.put("data", "");
			jsonObject.put("message", "没有查到车型价格数据~");// massige
			return jsonObject.toString();
		}
		
		if("".equals(checkStationName) || null == checkStationName) {
			jsonObject.put("status", 0);
			jsonObject.put("data", "");
			jsonObject.put("message", "没有查到车型价格数据~");// massige
			return jsonObject.toString();
		}
		
		CarTypePrice carTypePrice = carTypePriceService.money(carType,checkStationName);
		jsonObject.put("status", 1);
		jsonObject.put("data", JSON.toJSONString(carTypePrice));
		jsonObject.put("message", "没有查到车型价格数据~");// massige
		
		return jsonObject.toString();
	}
}
