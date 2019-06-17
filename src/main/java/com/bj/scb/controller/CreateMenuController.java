package com.bj.scb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bj.scb.pojo.WxFirstMenu;
import com.bj.scb.pojo.WxSecondMenu;
import com.bj.scb.service.WxFirstMenuService;
import com.bj.scb.service.WxSecondMenuService;
import com.bj.scb.utils.PageList;
import com.bj.scb.weixin.FirstMenu;
import com.bj.scb.weixin.Menu;
import com.bj.scb.weixin.SecondMenu;
import com.bj.scb.weixin.Util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "createMenu")
public class CreateMenuController {

	@Resource
	WxSecondMenuService wxSecondMenuService;
	@Resource
	WxFirstMenuService wxFirstMenuService;

	/**
	 * 一级菜单查询方法
	 */
	@RequestMapping(value = "firstList", produces = "application/json; charset=utf-8")
	public @ResponseBody String queryFirstMenuPageList(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
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
		PageList<WxFirstMenu> list = wxFirstMenuService.queryFirstMenuPageList(parameter);

		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}"; // 服务端分页必须返回total和rows,rows为每页记录
		return r;
	}

	/**
	 * 二级菜单查询方法
	 */
	@RequestMapping(value = "secondList", produces = "application/json; charset=utf-8")
	public @ResponseBody String querySecondMenuPageList(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {
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

		PageList<WxSecondMenu> list = wxSecondMenuService.querySecondMenuPageList(parameter);

		long count = list.getTotal();
		String b = JSON.toJSONString(list.getList());
		String r = "{\"total\":" + count + ",\"rows\":" + b + "}"; // 服务端分页必须返回total和rows,rows为每页记录
		return r;
	}

	/**
	 * 创建一级菜单
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "createFirstMenu", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String createFirstMenu(@RequestParam Map<String, Object> para, HttpServletRequest arg0,
			HttpServletResponse resp, HttpSession httpSession) throws Exception {

		JSONObject jsonObject = new JSONObject();
		List<FirstMenu> firstMenuList = Util.menu.getButton();
		if (firstMenuList != null && firstMenuList.size() >= 3) {
			jsonObject.put("status", 0);
			jsonObject.put("message", "一级菜单创建已达到3个，不能再创建了~");
			return jsonObject.toString();
		}

		String name = null == arg0.getParameter("name") ? "" : arg0.getParameter("name").toString();
		String url = null == arg0.getParameter("url") ? "" : arg0.getParameter("url").toString();
		String type = null == arg0.getParameter("type") ? "" : arg0.getParameter("type").toString();
		// 1、查询数据库是否存在该一级菜单
		WxFirstMenu wxFirstMenu = wxFirstMenuService.selectName(name);
		if (null != wxFirstMenu) {
			jsonObject.put("status", 0);
			jsonObject.put("message", "菜单已存在，请勿重复创建~");
			return jsonObject.toString();
		}
		// 2、不存在，则创建，否则不能创建
		FirstMenu firstMenu = new FirstMenu();
		firstMenu.setName(name);
		if (url.equals("")) {
			firstMenu.setKey(Util.getRadomStr());
		} else {
			firstMenu.setUrl(url);
		}

		firstMenu.setType(type);

		if (firstMenuList == null) {
			firstMenuList = new ArrayList<>();
			firstMenuList.add(firstMenu);
			Util.menu.setButton(firstMenuList);
		} else {
			Util.menu.getButton().add(firstMenu);
		}
		// 转为json
		jsonObject = JSONObject.fromObject(Util.menu);
		String wxUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
		wxUrl = wxUrl.replace("ACCESS_TOKEN", (Util.getAccessToken()));
		String result = Util.post(wxUrl, jsonObject.toString());// 发送请求

		jsonObject = JSONObject.fromObject(result);
		String msg = (String) jsonObject.get("errmsg");
		if ("ok".equals(msg)) {
			// 入库操作
			wxFirstMenu = new WxFirstMenu();
			wxFirstMenu.setName(name);
			wxFirstMenu.setType(type);
			wxFirstMenu.setUrl(url);
			wxFirstMenu.setDeleteFlag(1);
			wxFirstMenuService.saveName(wxFirstMenu);
			// 返回前台数据格式
			jsonObject.put("status", 1);
			jsonObject.put("message", "菜单已创建成功~");
			return jsonObject.toString();
		}

		jsonObject.put("status", 0);
		jsonObject.put("message", "菜单已创建失败~");
		return jsonObject.toString();
	}

	/**
	 * 创建一级菜单对应的二级菜单
	 */
	@RequestMapping(value = "createSecondMenu", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String createSecondMenu(@RequestParam Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {

		String firstMenuName = map.get("firstMenuName").toString();
		String secondMenuName = map.get("secondMenuName").toString();
		String secondMenuUrl = map.get("secondMenuUrl").toString();
		String secondMenuType = map.get("secondMenuType").toString();

		JSONObject jsonObject = new JSONObject();
		// 1、查询数据库是否存在该二级菜单
		WxSecondMenu wxSecondMenu = wxSecondMenuService.selectName(firstMenuName, secondMenuName);
		if (wxSecondMenu != null) {
			jsonObject.put("status", 0);
			jsonObject.put("message", "菜单已存在，请勿重复创建~");
			return jsonObject.toString();
		}
		// 2、不存在，则创建，否则修改
		// 3、二级菜单是否达到5个，达到不能创建
		List<FirstMenu> firstMenus = Util.menu.getButton();
		boolean isOverFive = false;
		for (FirstMenu firstMenu : firstMenus) {
			String firstName = firstMenu.getName();
			if (firstName.equals(firstMenuName)) {
				List<SecondMenu> sList = firstMenu.getSub_button();
				if (null == sList) {
					sList = new ArrayList<>();
					sList.add(createSecondMenu(secondMenuName, secondMenuUrl, secondMenuType));
					firstMenu.setSub_button(sList);
					break;
				}

				if (sList.size() < 5) {
					firstMenu.getSub_button().add(createSecondMenu(secondMenuName, secondMenuUrl, secondMenuType));
					break;
				}
				isOverFive = true;
				break;
			}
		}

		if (isOverFive) {
			jsonObject.put("status", 0);
			jsonObject.put("message", "二级菜单已达到5个，不能再创建了~");
			return jsonObject.toString();
		}

		// 转为json
		jsonObject = JSONObject.fromObject(Util.menu);
		String url = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
		url = url.replace("ACCESS_TOKEN", (Util.getAccessToken()));
		String ret = Util.post(url, jsonObject.toString());// 发送请求
		jsonObject = JSONObject.fromObject(ret);
		String mString = jsonObject.getString("errmsg");
		if ("ok".equals(mString)) {
			wxSecondMenu = new WxSecondMenu();
			wxSecondMenu.setMenuName(secondMenuName);
			wxSecondMenu.setMenuUrl(secondMenuUrl);
			wxSecondMenu.setParentMenuName(firstMenuName);
			wxSecondMenuService.saveSecondMenu(wxSecondMenu);
			jsonObject.put("status", 1);
			jsonObject.put("message", "菜单已创建成功~");
			return jsonObject.toString();
		}
		jsonObject.put("status", 0);
		jsonObject.put("message", "菜单已创建失败~");
		return jsonObject.toString();
	}

	@RequestMapping(value = "deleteMenu", produces = "application/json; charset=utf-8")
	public @ResponseBody String deleteMenu(HttpServletRequest arg0, HttpServletResponse resp, HttpSession httpSession)
			throws Exception {
		String flag = null == arg0.getParameter("flag") ? "" : arg0.getParameter("flag").toString();
		Map<String, Object> result = new HashMap<String, Object>();
		// 删除微信端自定义菜单
		String url = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
		System.out.println("token:" + Util.getAccessToken());
		url = url.replace("ACCESS_TOKEN", (Util.getAccessToken()));
		String ret = Util.post(url, null);// 发送请求
		JSONObject jsonObject = JSONObject.fromObject(ret);
		String mString = jsonObject.getString("errmsg");
		if ("ok".equals(mString)) {
			// 删除一级菜单(本地)
			Util.menu.getButton().clear();
			wxFirstMenuService.deleteMenu();
			wxSecondMenuService.deleteMenu();
			result.put("status", "1");
			result.put("message", "删除成功~");
			return JSON.toJSONString(result);
		}

		result.put("status", "0");
		result.put("message", "删除失败~");
		return JSON.toJSONString(result);
	}

	@RequestMapping(value = "initMenu", produces = "application/json; charset=utf-8")
	@ResponseBody
	public String initMenu(HttpServletRequest request, HttpServletResponse response, HttpSession httpSession) {
		JSONObject jsonObject = new JSONObject();
		boolean isExistMenu = Util.initWxMenu();
		if (!isExistMenu) {
			jsonObject.put("status", "0");
			jsonObject.put("message", "您还没有创建一级菜单，请先创建一级菜单~");
			return jsonObject.toString();
		}
		jsonObject.put("status", "1");
		jsonObject.put("message", "已创建一级菜单，可以创建二级菜单啦~");
		return jsonObject.toString();
	}

	public SecondMenu createSecondMenu(String secondMenuName, String secondMenuUrl, String secondMenuType) {
		SecondMenu secondMenu = new SecondMenu();
		secondMenu.setName(secondMenuName);
		if (null == secondMenuUrl || "".equals(secondMenuUrl)) {
			secondMenu.setKey(Util.getRadomStr());
		} else {
			secondMenu.setUrl(secondMenuUrl);
		}
		secondMenu.setType(secondMenuType);

		return secondMenu;
	}

	public void removeListElement(String name) {
		List<FirstMenu> buttons = Util.menu.getButton();
		for (FirstMenu button2 : buttons) {
			String btnName = button2.getName();
			if (btnName.equals(name)) {
				buttons.remove(button2);
				break;
			}
		}
	}

	public void removeSecondMenuElement(String name) {
		List<FirstMenu> firstMenus = Util.menu.getButton();
		for (FirstMenu firstMenu : firstMenus) {
			List<SecondMenu> secondMenus = firstMenu.getSub_button();
			for (FirstMenu secondMenu : firstMenus) {
				String btnName = secondMenu.getName();
				if (btnName.equals(name)) {
					secondMenus.remove(secondMenu);
					break;
				}
			}
		}
	}

}
