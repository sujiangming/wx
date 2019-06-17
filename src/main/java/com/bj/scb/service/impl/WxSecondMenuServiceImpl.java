package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.WxSecondMenuDaoImpl;
import com.bj.scb.pojo.WxSecondMenu;
import com.bj.scb.service.WxSecondMenuService;
import com.bj.scb.utils.PageList;
@Service
@Transactional
public class WxSecondMenuServiceImpl implements WxSecondMenuService{
	
	@Resource
	WxSecondMenuDaoImpl wxSecondMenuDaoImpl;
	
	@Override
	public PageList<WxSecondMenu> querySecondMenuPageList(Map<String, Object> parameter) {
		return wxSecondMenuDaoImpl.querySecondMenuPageList(parameter);
	}
	
	@Override
	public WxSecondMenu selectName(String firstMenuName,String secondMenuName) {
		return wxSecondMenuDaoImpl.selectName(firstMenuName,secondMenuName);
	}
	@Override
	public void saveSecondMenu(WxSecondMenu wxSecondMenu) {
		wxSecondMenuDaoImpl.saveSecondMenu(wxSecondMenu);
	}

	@Override
	public void deleteMenu() {
		wxSecondMenuDaoImpl.deleteMenu();
	}

}
