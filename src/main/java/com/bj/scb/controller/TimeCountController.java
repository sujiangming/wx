package com.bj.scb.controller;

import java.util.Date;
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
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.TimeCount;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.service.TimeCountService;
import com.bj.scb.service.TimeOrderCountService;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;
import com.bj.scb.utils.StringUtil;

@Controller
@RequestMapping(value = "timeCount")
public class TimeCountController {

	@Resource
	TimeCountService timeCountService;

	@Resource
	TimeOrderCountService timeOrderCountService;

	/**
	 * 查询所有数据
	 * 
	 * @param para
	 * @param request
	 * @param response
	 * @param session
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "list", produces = "application/json; charset=utf-8")
	public @ResponseBody String selectList(@RequestParam Map<String, Object> para, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		// 获取用户信息
		InnerUser user = (InnerUser) session.getAttribute("user");

		int currentPage = request.getParameter("offset") == null ? 1 : Integer.parseInt(request.getParameter("offset"));// 每页行数
		int showCount = request.getParameter("limit") == null ? 10 : Integer.parseInt(request.getParameter("limit"));
		String search = request.getParameter("search") == null ? "" : request.getParameter("search");
		if (currentPage != 0) {// 获取页数
			currentPage = currentPage / showCount;
		}
		currentPage += 1;
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("search", search);
		parameter.put("currentPage", currentPage);
		parameter.put("showCount", showCount);
		parameter.put("checkStationName", user.getCheckStationName());

		PageList<TimeCount> list = timeCountService.selectList(parameter);
		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList(), SerializerFeature.WriteMapNullValue,
				SerializerFeature.WriteNullStringAsEmpty);
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}"; // 服务端分页必须返回total和rows,rows为每页记录
		return r;
	}

	@RequestMapping(value = "selectListByName", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String selectListByName(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		String checkStationName = null == arg0.getParameter("checkStationName") ? ""
				: arg0.getParameter("checkStationName").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<TimeCount> list = timeCountService.selectListByName(checkStationName);
			result.put("status", "1");
			result.put("message", "删除成功");
			result.put("data", list);
		} catch (Exception e) {
			result.put("status", "0");
			result.put("message", "删除失败");
			result.put("data", null);
			e.printStackTrace();
		}
		return JSON.toJSONString(result);
	}

	@RequestMapping(value = "saveOrUpdate", produces = "application/json; charset=utf-8")
	public @ResponseBody String saveOrUpdate(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
		// 获取当前用户数据
		InnerUser user = (InnerUser) httpSession.getAttribute("user");

		String id = arg0.getParameter("id") == null ? "" : arg0.getParameter("id");
		String orderTime = arg0.getParameter("orderTime") == null ? "" : arg0.getParameter("orderTime");
		String orderCount = arg0.getParameter("orderCount") == null ? "" : arg0.getParameter("orderCount");
		String orderStart = arg0.getParameter("orderStart") == null ? "" : arg0.getParameter("orderStart");

		Map<String, Object> result = new HashMap<String, Object>();

		TimeCount sa = null;
		TimeOrderCount timeOrderCount = null;
		sa = timeCountService.findObjectById(id);
		if (null != sa) {
			// 已经存在则更新
			int saOldCount = sa.getOrderCount();
			sa.setOrderCount(Integer.valueOf(orderCount));
			sa.setOrderTime(orderTime);
			sa.setCheckStationName(user.getCheckStationName());
			sa.setOrderStartTime(Integer.valueOf(orderStart.split("-")[0].split(":")[0]));
			sa.setUpdateTime(JdryTime.getFullTimeBySec(new Date().getTime()));
			timeCountService.saveOrUpdate(sa);

			// 这里需要对关联表进行更新操作 40 30-4=26 40 - 4 = 36 40 -30 + 26 = 36
			timeOrderCount = timeOrderCountService.getByTimeCountId(id);
			if (null != timeOrderCount) {
				String oldCounts = timeOrderCount.getOrderCount() == null ? "0" : timeOrderCount.getOrderCount();
				int newCounts = Integer.valueOf(orderCount).intValue() - saOldCount
						+ Integer.valueOf(oldCounts).intValue();
				timeOrderCount.setOrderCount(String.valueOf(newCounts));
			} else {
				timeOrderCount = getTimeOrderCount(orderCount, id, user.getCheckStationName(), orderTime,orderStart);
			}
		} else {
			// 主键由程序控制
			String timeCountId = StringUtil.getRandomDigitStr(20);
			sa = new TimeCount();
			sa.setId(timeCountId);
			sa.setOrderCount(Integer.valueOf(orderCount));
			sa.setOrderTime(orderTime);
			sa.setCheckStationName(user.getCheckStationName());
			sa.setOrderStartTime(Integer.valueOf(orderStart.split("-")[0].split(":")[0]));
			sa.setUpdateTime(JdryTime.getFullTimeBySec(new Date().getTime()));
			timeCountService.save(sa);

			timeOrderCount = getTimeOrderCount(orderCount, timeCountId, user.getCheckStationName(), orderTime,orderStart);
		}

		try {
			timeOrderCountService.saveOrUpdate(timeOrderCount);
			result.put("status", "1");
			result.put("message", "保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", "0");
			result.put("message", "保存失败");
		}
		return JSON.toJSONString(result);
	}

	public TimeOrderCount getTimeOrderCount(String orderCount, String timeCountId, String checkStationName,
			String orderTime,String orderStart) {
		TimeOrderCount timeOrderCount = new TimeOrderCount();
		timeOrderCount.setOrderCount(orderCount);
		timeOrderCount.setTimeCountId(timeCountId);
		timeOrderCount.setOrderDate(JdryTime.getFormatDate(new Date().getTime(), "yyyy-MM-dd"));
		timeOrderCount.setCheckStationName(checkStationName);
		timeOrderCount.setOrderTime(orderTime);
		timeOrderCount.setOrderStartTime(Integer.valueOf(orderStart.split("-")[0].split(":")[0]));
		timeOrderCount.setUpdateTime(JdryTime.getFullTimeBySec(new Date().getTime()));
		return timeOrderCount;
	}

	@RequestMapping(value = "delete", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String delete(@RequestParam Map<String, Object> para, HttpServletRequest arg0, HttpServletResponse resp,
			HttpSession httpSession) throws Exception {
		String id = null == arg0.getParameter("id") ? "" : arg0.getParameter("id").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			timeCountService.delete(id);
			// 级联删除
			timeOrderCountService.delete(id);
			result.put("status", "1");
			result.put("message", "删除成功");
		} catch (Exception e) {
			result.put("status", "0");
			result.put("message", "删除失败");
			e.printStackTrace();
		}
		return JSON.toJSONString(result);
	}
}
