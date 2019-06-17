package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.WxFirstMenuDaoImpl;
import com.bj.scb.pojo.WxFirstMenu;
import com.bj.scb.service.WxFirstMenuService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class WxFirstMenuServiceImpl implements WxFirstMenuService {
	@Resource
	WxFirstMenuDaoImpl wxFistMenuDaoImpl;

	@Override
	public PageList<WxFirstMenu> queryFirstMenuPageList(Map<String, Object> parameter) {
		return wxFistMenuDaoImpl.queryFirstMenuPageList(parameter);
	}

	@Override
	public WxFirstMenu selectName(String name) {
		return wxFistMenuDaoImpl.selectName(name);
	}

	@Override
	public void saveName(WxFirstMenu wxFirstMenu) {
		wxFistMenuDaoImpl.saveName(wxFirstMenu);

	}

	@Override
	public void deleteMenu() {
		wxFistMenuDaoImpl.deleteMenu();
	}
}
