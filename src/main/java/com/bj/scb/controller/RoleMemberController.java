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
import com.bj.scb.dao.impl.CommUserDao;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.RoleMemberService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

@Controller
@RequestMapping(value="roleMember",produces = "application/json; charset=utf-8")
public class RoleMemberController {
	
	@Resource
	RoleMemberService  roleMemberService;
	
	@Resource 
	CommUserDao commUserDao;
	
	@RequestMapping(value = "list", produces = "application/json; charset=utf-8")
	public @ResponseBody String queryRolePageList(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
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
		parameter.put("isSuperManager", user.isSuperManager());
		parameter.put("checkStationName", user.getCheckStationName());
		
		PageList<InnerUser> list = roleMemberService.queryRolePageList(parameter);
		
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}"; // 服务端分页必须返回total和rows,rows为每页记录
		return r;
	}
	
	@RequestMapping(value = "save", produces = "application/json; charset=utf-8")
	public @ResponseBody String saveRole(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String userId = arg0.getParameter("roleId") == null?"":arg0.getParameter("roleId").toString();
		String checkStationName= null == arg0.getParameter("checkStationName")?"":arg0.getParameter("checkStationName").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		if (userId != null && !userId.isEmpty()) {
			//先将该检车站下的管理员设置成普通客服人员
			InnerUser oldAdmin = commUserDao.getUserByStationId(checkStationName);
			if(null != oldAdmin){
				roleMemberService.updateRoleMember(oldAdmin);
			}
			
			InnerUser newAdmin = commUserDao.getUserById(userId);
			newAdmin.setManager(false);
			newAdmin.setInnerUserRole("区域管理员");
			
			//后将该检车站下的普通客服人员设置成管理员
			roleMemberService.saveRole(newAdmin);

			result.put("status", "1");
			result.put("message", "保存成功");
		} else {
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
	}
}
