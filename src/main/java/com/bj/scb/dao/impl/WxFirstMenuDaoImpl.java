package com.bj.scb.dao.impl;

import java.util.Map;

import javax.management.Query;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.WxFirstMenu;
import com.bj.scb.utils.PageList;

@Repository
public class WxFirstMenuDaoImpl extends BaseDaoImpl{

	public PageList<WxFirstMenu> queryFirstMenuPageList(Map<String, Object> parameter) {
		String hql = "from " + WxFirstMenu.class.getName();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		return this.findPageList(hql, currentPage, showCount);
	}

	public WxFirstMenu selectName(String name) {
		String hql = "from " + WxFirstMenu.class.getName()+" where name='"+name+"'";
		return this.findObject(hql);
	}

	public void saveName(WxFirstMenu wxFirstMenu) {
		this.saveOrUpdateObject(wxFirstMenu);
	}

	public void deleteMenu() {
		Session session = this.getSession();
		String hql = "delete from t_first_menu";
		SQLQuery sqlQuery = session.createSQLQuery(hql);
		sqlQuery.executeUpdate();
	}
}
