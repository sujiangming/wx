package com.bj.scb.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bj.scb.utils.TestURL;
import com.bj.scb.weixin.Util;

@Controller
@RequestMapping(value = "nav", produces = "application/json; charset=utf-8")
public class NavgationController {

	@RequestMapping("signature")
	@ResponseBody
	public String getSignature(HttpServletRequest request, HttpServletResponse response,
			@RequestParam Map<String, String> map) {
		//String ret = TestURL.getSignature();
		return Util.getJSSignature(map.get("url"));
	}
}
