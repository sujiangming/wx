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
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Role;
import com.bj.scb.service.CheckStationService;
import com.bj.scb.service.RoleService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

@Controller
@RequestMapping(value="role",produces = "application/json; charset=utf-8")
public class RoleController {
	
	@Resource
	RoleService  roleService;
	
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
		parameter.put("isManager", user.isManager());
		parameter.put("checkStationId", user.getCheckStationId());
		
		PageList<Role> list = roleService.queryRolePageList(parameter);
		
		
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}"; // 服务端分页必须返回total和rows,rows为每页记录
		return r;
	}
	
	@RequestMapping(value = "save", produces = "application/json; charset=utf-8")
	public @ResponseBody String saveRole(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String roleId= null == arg0.getParameter("roleId")?"":arg0.getParameter("roleId").toString();
		String companyId= null == arg0.getParameter("companyId")?"":arg0.getParameter("companyId").toString();
		String name= null == arg0.getParameter("name")?"":arg0.getParameter("name").toString();
		String desc= null == arg0.getParameter("desc")?"":arg0.getParameter("desc").toString();
		Map<String,Object> result = new HashMap<String,Object>();
		Role role = new Role();
		if(roleId !=null && !roleId.isEmpty()){
			role = roleService.getRoleById(roleId);
		}
		role.setRoleName(name);
		role.setRoleDesc(desc);
		role.setCheckStationId(companyId);//这里需要添加一个检车站的字段，用于指定该角色是属于哪个检车站的
		//role.setRoleType((int)para.get("type"));
		try{
			roleService.saveRole(role);
			result.put("status", "1");
			result.put("message", "保存成功");
		}catch(Exception e){
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
	}
	
	@RequestMapping(value = "delete", produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteRole(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String roleId= null == arg0.getParameter("roleId")?"":arg0.getParameter("roleId").toString();
		Map<String,Object> result = new HashMap<String,Object>();
		try{
			if(!roleId.isEmpty()){
				roleService.deleteRole(roleId);
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
