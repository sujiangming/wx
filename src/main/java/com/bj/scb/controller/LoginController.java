package com.bj.scb.controller;

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
import com.bj.scb.service.LoginService;

@Controller
public class LoginController {

	private JSONObject jsonObject = new JSONObject();

	@Resource
	LoginService loginService;

	@RequestMapping(value="/login")
	public @ResponseBody String login(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
			@RequestParam Map<String, String> paramter) {
		String account = paramter.get("account");
		String pwd = paramter.get("pwd");

		InnerUser InnerUser = loginService.login(account, pwd);

		if (null == InnerUser) { // 登录失败
			jsonObject.put("status", 0);
			jsonObject.put("message", "用户名或密码不对~");
			jsonObject.put("data", "");
			return jsonObject.toJSONString();
		}
		//登录成功，将用户信息存在session中
		httpSession.setAttribute("user", InnerUser);
		// 登录成功返回信息
		jsonObject.put("status", 1);
		jsonObject.put("message", "登录成功~");
		jsonObject.put("data", JSON.toJSONString(InnerUser));

		return jsonObject.toJSONString();
	}
}
