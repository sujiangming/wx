package com.bj.scb.controller;


import java.net.URLDecoder;
import java.util.Date;
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
import com.bj.scb.pojo.Comment;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.CommentService;
import com.bj.scb.utils.CharacterUtil;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;


@Controller
@RequestMapping(value="comment",produces = "application/json; charset=utf-8")
public class CommentController {

	@Resource
	CommentService commentService;
	
	@RequestMapping(value="selectList")
	public  @ResponseBody String   selectList(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
			@RequestParam Map<String, Object> paramter) throws Exception {
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
		parameter.put("checkStationName", user.getCheckStationName());
		
		PageList<Comment> list = commentService.selectList(parameter);
		// 成功返回的结果
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}";
		
		return r;
		
	}
	
	@RequestMapping(value="deleteComment",produces = "application/json; charset=utf-8")
	public  @ResponseBody String deleteComment(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession,
			@RequestParam Map<String, Object> paramter) {
		
		String id= null == request.getParameter("id")?"":request.getParameter("id").toString();
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			if(!id.isEmpty()) {
				commentService.deleteComment(id);
				result.put("status", "1");
				result.put("message", "删除成功");
			}else {		
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
	

	@RequestMapping(value = "save", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String saveComment(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> parameter) throws Exception{
		// 预约人的微信号
		String commentUserId = parameter.get("openid");
		String datas=parameter.get("data");
		String name= parameter.get("name");
		String order= parameter.get("order");
		String checkStationName= parameter.get("checkStationName");
		Comment comment=commentService.selectOne(order);
		
		Map<String,Object> result = new HashMap<String,Object>();
		
		if(null != comment ) {
			result.put("status", "2");
			result.put("message", "已评价，不能重复评价~");
			return JSON.toJSONString(result);
		}
		
		comment = new Comment();
		comment.setCommentUserScore(datas);
		comment.setCommentUserId(commentUserId);
		comment.setCommentOrder(order);
		comment.setCommentUserName(name);
		comment.setCommentUserContent(checkStationName);
		comment.setCommentTime(JdryTime.getFullTimeBySec((new Date()).getTime()));
		comment.setCommentStatus("1");//1.代表已评价，0，代表为评价
		
		try {
			if(null != datas && !datas.isEmpty()) {
				commentService.save(comment);
				result.put("status", "1");
				result.put("message", "评价成功！");
			}else {		
				result.put("status", "0");
				result.put("message", "保存失败！");
			}
			
			return JSON.toJSONString(result);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败！");
			return JSON.toJSONString(result);
		}
		
		
	}
}
