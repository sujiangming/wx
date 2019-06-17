package com.bj.scb.controller;

import java.util.Date;
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
import com.bj.scb.pojo.MsgReply;
import com.bj.scb.service.MsgService;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;

import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "msg")
public class MsgReplyController {

	@Resource
	MsgService msgService;

	@RequestMapping(value = "list", produces = "application/json; charset=utf-8")
	public @ResponseBody String getMsgList(HttpServletRequest request, HttpServletResponse response,
			HttpSession httpSession, @RequestParam Map<String, String> paramter) {
		List<MsgReply> list = msgService.getMsgList();
		JSONObject jsonObject = new JSONObject();
		if (list == null || list.size() == 0) {
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
		// search = CharacterUtil.getUTF_8String(search);
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;

		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		PageList<MsgReply> list = msgService.selectList(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";

		return r;

	}

	@RequestMapping(value = "save", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveMsgReply(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		String title = null == arg0.getParameter("title") ? "" : arg0.getParameter("title").toString();
		String desc = null == arg0.getParameter("desc") ? "" : arg0.getParameter("desc").toString();
		String content = null == arg0.getParameter("content") ? "" : arg0.getParameter("content").toString();
		String keyword = null == arg0.getParameter("keyword") ? "" : arg0.getParameter("keyword").toString();
		String picUrl = null == arg0.getParameter("picUrl") ? "" : arg0.getParameter("picUrl").toString();
		String clickUrl = null == arg0.getParameter("clickUrl") ? "" : arg0.getParameter("clickUrl").toString();
		String isAddFlag = null == arg0.getParameter("isAddFlag") ? "" : arg0.getParameter("isAddFlag").toString();
		String mediaId = null == arg0.getParameter("mediaId") ? "" : arg0.getParameter("mediaId").toString();

		Map<String, Object> result = new HashMap<String, Object>();

		MsgReply checkMsg = msgService.getMsgReplyByName(keyword);

		if (null != checkMsg) {
			result.put("status", "0");
			result.put("message", "关键字不能重复");
			return JSON.toJSONString(result);
		}

		if ("update".equals(isAddFlag)) {
			checkMsg = msgService.getMsgReplyById(id);
			checkMsg.setUpdateTime(JdryTime.getFullTimeBySec(new Date().getTime()));
		} else {
			checkMsg = new MsgReply();
			checkMsg.setInsertTime(JdryTime.getFullTimeBySec(new Date().getTime()));
		}
		checkMsg.setTitle(title);
		checkMsg.setDescription(desc);
		checkMsg.setContent(content);
		checkMsg.setKeyword(keyword);
		checkMsg.setPicUrl(picUrl);
		checkMsg.setClickUrl(clickUrl);
		if (null != mediaId) {
			checkMsg.setMediaId(mediaId);
		}

		try {
			msgService.saveMsgReply(checkMsg);
			result.put("status", "1");
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);

	}

	@RequestMapping(value = "delect", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String delectMsgReply(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			if (!id.isEmpty()) {
				msgService.deleteMsgReply(id);
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

	@RequestMapping(value = "listAll", produces = "application/json; charset=utf-8")
	@ResponseBody
	public JSONObject listAll(HttpServletRequest arg0, HttpServletResponse resp) {
		JSONObject jsonObject = new JSONObject();
		List<MsgReply> list = msgService.listAll();
		jsonObject.put("list", list);
		return jsonObject;

	}
}
