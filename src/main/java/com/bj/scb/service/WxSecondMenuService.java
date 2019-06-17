package com.bj.scb.service;

import java.util.Map;

import com.bj.scb.pojo.WxSecondMenu;
import com.bj.scb.utils.PageList;

public interface WxSecondMenuService {

	PageList<WxSecondMenu> querySecondMenuPageList(Map<String, Object> parameter);

	WxSecondMenu selectName(String firstMenuName,String secondMenuName);

	void saveSecondMenu(WxSecondMenu wxSecondMenu);

	void deleteMenu();

}
