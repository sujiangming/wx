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
import com.bj.scb.pojo.CustomerInfo;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.CustomerInfoService;
import com.bj.scb.service.InnerUserService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

@Controller
@RequestMapping(value="customerInfo",produces = "application/json; charset=utf-8")
public class CustomerInfoController {

	@Resource
	private CustomerInfoService customerInfoService;
	
	@Resource
	private InnerUserService innerUserService;
	
	@RequestMapping(value="list")
	@ResponseBody
	public String listAll(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		int currentPage = arg0.getParameter("offset") == null ? 1 : Integer.parseInt(arg0.getParameter("offset"));// 每页行数
		int showCount = arg0.getParameter("limit") == null ? 10 : Integer.parseInt(arg0.getParameter("limit"));
		String search = arg0.getParameter("search") == null ? "" : arg0.getParameter("search");
		System.out.println(search);
		//search = CharacterUtil.getUTF_8String(search);
		System.out.println(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		
		PageList<CustomerInfo> list = customerInfoService.selectAll(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";
		
		return r;
		
	}
	
	@RequestMapping(value="save")
	@ResponseBody
	public String saveCustomerInfo(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id= null == arg0.getParameter("id")?"":arg0.getParameter("id").toString();
		String customerName= null == arg0.getParameter("customerName")?"":arg0.getParameter("customerName").toString();
		String customerMobile= null == arg0.getParameter("customerMobile")?"":arg0.getParameter("customerMobile").toString();
		String isInnerPeople= null == arg0.getParameter("isInnerPeople")?"":arg0.getParameter("isInnerPeople").toString();
		String customerPic= null == arg0.getParameter("customerPic")?"":arg0.getParameter("customerPic").toString();
		String customerGender= null == arg0.getParameter("customerGender")?"":arg0.getParameter("customerGender").toString();
		String customerId= null == arg0.getParameter("customerId")?"":arg0.getParameter("customerId").toString();
		String checkStationName= null == arg0.getParameter("checkStationName")?"":arg0.getParameter("checkStationName").toString();
		String checkStationId= null == arg0.getParameter("checkStationId")?"":arg0.getParameter("checkStationId").toString();
		
		Map<String,Object> result = new HashMap<String,Object>();
		InnerUser innerUser=new InnerUser();
		if(isInnerPeople.equals("是")) {
			if(customerMobile!=null && !customerMobile.isEmpty()) {
				innerUser=innerUserService.getInnerUserMB(customerMobile);
			}
			if(innerUser==null) {
				innerUser=new InnerUser();
			}
			innerUser.setInnerUserId(customerId);
			innerUser.setInnerUserMobile(customerMobile);
			innerUser.setInnerUserName(customerName);
			innerUser.setInnerUserGender(customerGender);
			innerUser.setInnerUserPic(customerPic);
			innerUser.setInnerUserPwd("123456");
			innerUser.setCheckStationName(checkStationName);
			innerUser.setCheckStationId(checkStationId);
			innerUser.setManager(true);
			innerUser.setSuperManager(false);
			innerUser.setInnerUserRole("客服人员");
			innerUserService.saveInnerUser(innerUser);
		}
		
		CustomerInfo customerInfo=new CustomerInfo();
		if(id !=null && !id.isEmpty()){
			customerInfo = customerInfoService.getCustomerInfo(id);
		}
		customerInfo.setCustomerMobile(customerMobile);
		customerInfo.setCustomerName(customerName);
		customerInfo.setIsInnerPeople(isInnerPeople);
		
		try{
			customerInfoService.saveCustomerInfo(customerInfo);
			result.put("status", "1");
			result.put("message", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
		
	}
	
	@RequestMapping(value="delete")
	@ResponseBody
	public String deleteCustomerInfo(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id= null == arg0.getParameter("id")?"":arg0.getParameter("id").toString();		
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			if(!id.isEmpty()){
				customerInfoService.deleteCustomerInfo(id);
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
}
