package com.bj.scb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pojo.CheckStation;
import com.bj.scb.service.CheckStationService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "checkStation")
public class CheckStationController {

	@Resource
	CheckStationService checkStationService;

	@RequestMapping(value = "list", produces = "application/json; charset=utf-8")
	public @ResponseBody String getStationList(HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession, @RequestParam Map<String, String> paramter) {
		List<CheckStation> list = checkStationService.getStationList();
		JSONObject jsonObject=new JSONObject();
		if (list==null||list.size()==0) {
			jsonObject.put("status", 0);
			jsonObject.put("message", "没有查到检车站数据");
			jsonObject.put("data", "");
		}
		jsonObject.put("status", 1);
		jsonObject.put("message", "已查到检车站数据");
		jsonObject.put("data", JSON.toJSONString(list));
		return jsonObject.toString();
	}

	@RequestMapping(value = "selectList", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String selectList(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		//search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		PageList<CheckStation> list = checkStationService.selectlist(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;

	}
	
	@RequestMapping(value="save", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveCheckStation(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id= null == arg0.getParameter("id")?"":arg0.getParameter("id").toString();
		String checkStationName= null == arg0.getParameter("checkStationName")?"":arg0.getParameter("checkStationName").toString();
		String checkStationAddress= null == arg0.getParameter("checkStationAddress")?"":arg0.getParameter("checkStationAddress").toString();
		String checkSX= null == arg0.getParameter("checkStationX")?"":arg0.getParameter("checkStationX").toString();
		String checkSY= null == arg0.getParameter("checkStationY")?"":arg0.getParameter("checkStationY").toString();
		double checkStationX=Double.parseDouble(checkSX);
		double checkStationY=Double.parseDouble(checkSY);
		Map<String,Object> result = new HashMap<String,Object>();
		CheckStation checkStation=new CheckStation();
		if(id !=null && !id.isEmpty()){
			checkStation = checkStationService.getCheckStationById(id);
		}
		checkStation.setCheckStationAddress(checkStationAddress);
		checkStation.setCheckStationName(checkStationName);
		checkStation.setCheckStationX(checkStationX);
		checkStation.setCheckStationY(checkStationY);
		try{
			checkStationService.saveCheckStation(checkStation);
			result.put("status", "1");
			result.put("message", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
		
	}
	
	@RequestMapping(value="delect", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String delectCheckStation(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id= null == arg0.getParameter("id")?"":arg0.getParameter("id").toString();		
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			if(!id.isEmpty()){
				checkStationService.deleteCheckStation(id);
				result.put("status", "1");
				result.put("message", "删除成功");
			}else{
				result.put("status", "0");
				result.put("message", "删除失败");
			}
			return JSON.toJSONString(result);
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "删除失败");
			return JSON.toJSONString(result);
		}
	}
	
	@RequestMapping(value="listAll", produces = "application/json; charset=utf-8")
	@ResponseBody
	public JSONObject listAll(HttpServletRequest arg0,HttpServletResponse resp) {
		JSONObject jsonObject=new JSONObject();
		List<CheckStation> list =checkStationService.listAll();
		jsonObject.put("list", list);
		return jsonObject;
		
	}
}
