package com.bj.scb.dao.impl;

import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Order;
import com.bj.scb.pojo.OrderInfo;
import com.bj.scb.utils.PageList;

@Repository
public class OrderInfoDaoImpl extends BaseDaoImpl {

	/**
	 * 查询单个客服未完成订单
	 */
	public PageList<OrderInfo> selectAll(Map<String, Object> parameter){
		String checkStationName = parameter.get("checkStationName").toString();
		String orderUserName = parameter.get("orderUserName").toString();
		String hql = "from " + OrderInfo.class.getName();
		String where = " where isRefund='否' and checkStationName='" + checkStationName + "' and orderUserName='"
				+ orderUserName + "'";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerName like:carOwnerName " + " or a.carOwnerMobile like:carOwnerMobile ";
			map.put("username", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
		}
		hql += whereCase + " order by a.insertTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	/**
	 * 服务完成的方法
	 */
	public void saveOrderInfo(String id) {
		Session session = this.getSession();
		long time = (new Date()).getTime();
		String hql = "update t_order_info t set t.isRefund='是',t.upTime='" + time + "' where t.id='" + id + "'";
		Query query = session.createSQLQuery(hql);
		query.executeUpdate();
	}

	/**
	 * 
	 * 保存订单
	 */
	public void saveOdInfo(OrderInfo orderInfo) {
		this.saveOrUpdateObject(orderInfo);
	}

	/**
	 * 
	 * 查询单个客服已完成订单
	 */
	public PageList<OrderInfo> selectOneAll(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName").toString();
		String orderUserName = parameter.get("orderUserName").toString();
		String hql = "from " + OrderInfo.class.getName();
		String where = " where isRefund='是' and checkStationName='" + checkStationName + "' and orderUserName='"
				+ orderUserName + "'";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerName like:carOwnerName " + " or a.carOwnerMobile like:carOwnerMobile ";
			map.put("username", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
		}
		hql += whereCase + " order by a.upTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	/**
	 * 
	 * 查询整个检车站客服已完成订单
	 */
	public PageList<OrderInfo> selectOnesAllList(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName").toString();
		String hql = "from " + OrderInfo.class.getName();
		String where = " where isRefund='是' and checkStationName='" + checkStationName + "'";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerName like:carOwnerName " + " or a.carOwnerMobile like:carOwnerMobile ";
			map.put("username", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
		}
		hql += whereCase + " order by a.upTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public List<OrderInfo> selectOneAll(String checkStationName, String isInnerPeople) {
		String hql = " from " + OrderInfo.class.getName() + " where isRefund='否' and kefuid='" + isInnerPeople
				+ "' and checkStationName='" + checkStationName + "'";
		return this.findList(hql);
	}

}
