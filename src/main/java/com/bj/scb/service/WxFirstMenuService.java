package com.bj.scb.service;

import java.util.Map;

import com.bj.scb.pojo.WxFirstMenu;
import com.bj.scb.utils.PageList;

public interface WxFirstMenuService {

	PageList<WxFirstMenu> queryFirstMenuPageList(Map<String, Object> parameter);

	WxFirstMenu selectName(String name);

	void saveName(WxFirstMenu wxFirstMenu);
	
	void deleteMenu();

}
