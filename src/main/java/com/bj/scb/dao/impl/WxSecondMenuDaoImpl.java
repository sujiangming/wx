package com.bj.scb.dao.impl;

import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.WxSecondMenu;
import com.bj.scb.utils.PageList;

@Repository
public class WxSecondMenuDaoImpl extends BaseDaoImpl {

	public PageList<WxSecondMenu> querySecondMenuPageList(Map<String, Object> parameter) {
		String hql = "from " + WxSecondMenu.class.getName();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		return this.findPageList(hql, currentPage, showCount);
	}

	public WxSecondMenu selectName(String firstMenuName, String secondMenuName) {
		String hql = "from " + WxSecondMenu.class.getName() + " where menuName='" + secondMenuName
				+ "' and parentMenuName='" + firstMenuName + "'";
		return this.findObject(hql);
	}

	public void saveSecondMenu(WxSecondMenu wxSecondMenu) {
		this.saveOrUpdateObject(wxSecondMenu);
	}
	
	public void deleteMenu() {
		Session session = this.getSession();
		String hql = "delete from t_second_menu ";
		SQLQuery sqlQuery = session.createSQLQuery(hql);
		sqlQuery.executeUpdate();
	}
}
