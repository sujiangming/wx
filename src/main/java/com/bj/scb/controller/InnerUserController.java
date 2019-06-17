package com.bj.scb.controller;

import java.net.URLDecoder;
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
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.InnerUserService;
import com.bj.scb.service.LoginService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.PageList;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "innerUser", produces = "application/json; charset=utf-8")
public class InnerUserController {

	@Resource
	InnerUserService innserUserService;
	@Resource
	LoginService loginService;

	@RequestMapping(value = "selectList")
	public @ResponseBody String selectList(HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession, @RequestParam Map<String, Object> paramter) throws Exception {
		int currentPage = request.getParameter("offset") == null ? 1 : Integer.parseInt(request.getParameter("offset"));// 每页行数
		int showCount = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
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

		PageList<InnerUser> list = innserUserService.selectList(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;

	}

	@RequestMapping(value = "saveInnerUser", produces = "application/json; charset=utf-8")
	public @ResponseBody String saveCarTypePrice(HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession, @RequestParam Map<String, Object> paramter) {
		String id = null == request.getParameter("id") ? "" : request.getParameter("id").toString();
		String innerUserId = null == request.getParameter("innerUserId") ? ""
				: request.getParameter("innerUserId").toString();
		String innerUserPic = null == request.getParameter("innerUserPic") ? ""
				: request.getParameter("innerUserPic").toString();
		String innerUserName = null == request.getParameter("innerUserName") ? ""
				: request.getParameter("innerUserName").toString();
		String innerUserGender = null == request.getParameter("innerUserGender") ? ""
				: request.getParameter("innerUserGender").toString();
		String innerUserMobile = null == request.getParameter("innerUserMobile") ? ""
				: request.getParameter("innerUserMobile").toString();
		String innerUserRole = null == request.getParameter("innerUserRole") ? ""
				: request.getParameter("innerUserRole").toString();
		String checkStationName = null == request.getParameter("checkStationName") ? ""
				: request.getParameter("checkStationName").toString();
		String isMana = null == request.getParameter("isManager") ? "" : request.getParameter("isManager").toString();
		String isSuperMana = null == request.getParameter("isSuperManager") ? ""
				: request.getParameter("isSuperManager").toString();
		boolean isManager = Boolean.parseBoolean(isMana);
		boolean isSuperManager = Boolean.parseBoolean(isSuperMana);

		Map<String, Object> result = new HashMap<String, Object>();
		InnerUser innerUser = new InnerUser();
		if (id != null && !id.isEmpty()) {
			innerUser = innserUserService.getInnerUserById(id);
		}
		innerUser.setInnerUserId(innerUserId);
		innerUser.setInnerUserPic(innerUserPic);
		innerUser.setInnerUserName(innerUserName);
		innerUser.setInnerUserGender(innerUserGender);
		innerUser.setInnerUserMobile(innerUserMobile);
		innerUser.setInnerUserRole(innerUserRole);
		innerUser.setCheckStationName(checkStationName);
		innerUser.setManager(isManager);
		innerUser.setSuperManager(isSuperManager);

		try {
			innserUserService.saveInnerUser(innerUser);
			result.put("status", "1");
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
	}

	@RequestMapping(value = "deleteInnerUser", produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteComment(HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession, @RequestParam Map<String, Object> paramter) {

		String id = null == request.getParameter("id") ? "" : request.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (!id.isEmpty()) {
				innserUserService.deleteInnerUser(id);
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

	// 修改密码
	@RequestMapping(value = "newpwd", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String newuserpwd(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
			@RequestParam Map<String, String> paramter) {
		String pwd = paramter.get("pwd");
		String newpwd = paramter.get("newpwd");
		String mobel = paramter.get("mobel");
		JSONObject jsonObject = new JSONObject();
		InnerUser innerUser = loginService.login(mobel, pwd);
		if (innerUser != null) {
			innserUserService.selectNewPwd(pwd, newpwd, mobel);
			jsonObject.put("messige", "success");
		} else {
			jsonObject.put("messige", "error");
		}
		return jsonObject.toString();
	}

	@RequestMapping(value = "selectcs", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String selectCs(HttpServletRequest request, HttpServletResponse response, @RequestParam String checkStation)
			throws Exception {
		String checkStationName = URLDecoder.decode(checkStation, "UTF-8");
		List<InnerUser> list = innserUserService.selectCS(checkStationName);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("messige", "success");
		jsonObject.put("list", list);
		return jsonObject.toString();
	}

}
