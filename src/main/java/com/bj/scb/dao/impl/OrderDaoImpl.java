package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.Order;
import com.bj.scb.utils.PageList;

@Repository
public class OrderDaoImpl extends BaseDaoImpl {

	public PageList<Order> selectAll(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName").toString();
		String hql = "from " + Order.class.getName();
		String where = " where isRefund='否' and checkStationName='" + checkStationName + "' ";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerMobile like:carOwnerMobile"
		+ " or a.carOwnerName like:carOwnerName";
			map.put("username", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
		}
		hql += whereCase + " order by a.insertTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public void saveOrder(Order order) {
		this.saveOrUpdateObject(order);
	}

	public void saveOrderIs(String carOwnerMobile,String insertTime) {
		long inseTime=Long.valueOf(insertTime).longValue();
		Session session = this.getSession();
		String hql = "update t_order t set t.isRefund='是'   where t.carOwnerMobile='" + carOwnerMobile + "' and insertTime="+inseTime;
		Query query = session.createSQLQuery(hql);
		query.executeUpdate();
	}

	public List<Order> getAllOrderByOpenId(String openId) {
		String hql = "from " + Order.class.getName() + " where orderUserId='" + openId + "'"
				+ " order by insertTime desc";
		return this.findList(hql);
	}

	public void saveOrderTY(String carOwnerMobile,String insertTime) {
		long intime=Long.valueOf(insertTime).longValue();
		Session session = this.getSession();
		String hql = "update t_order set typeState='派单中'  where carOwnerMobile='" + carOwnerMobile + "' and insertTime=" + intime;
		Query query = session.createSQLQuery(hql);
		query.executeUpdate();
	}

	public List<Order> selectOrdersByName(String name) {
		String hql = "from " + Order.class.getName() + " where checkStationName='" + name + "'" + " and isRefund='否' "
				+ " and typeState='已预约' order by insertTime desc";
		return this.findList(hql);
	}

}
