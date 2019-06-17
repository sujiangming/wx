package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.Order;
import com.bj.scb.pojo.OrderNickName;
import com.bj.scb.utils.PageList;

@Repository
public class OrderNickNameDaoImpl extends BaseDaoImpl{

	public PageList<OrderNickName> selectAll(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName").toString();
		String hql = "from " + OrderNickName.class.getName();
		String where = " where checkStationName='" + checkStationName + "' ";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.orderUserNickName like:username";
			map.put("username", "%" + search + "%");
		}
		hql += whereCase + " order by a.insertTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public void saveObj(OrderNickName oNickName) {
		this.saveOrUpdateObject(oNickName);
		
	}

}
