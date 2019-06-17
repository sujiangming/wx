package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.CarTypePrice;
import com.bj.scb.utils.PageList;


@Repository
public class CarTypePriceDaoImpl extends BaseDaoImpl {

	public PageList<CarTypePrice> selectAll(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName") == null ? ""
				: parameter.get("checkStationName").toString();
		String hql = "from " + CarTypePrice.class.getName();
		String where = " where checkStationName ='" + checkStationName + "'";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carType like:username " + "or a.checkStationName like:checkStationName ";
			map.put("username", "%" + search + "%");
			map.put("checkStationName", "%" + search + "%");
		}
		hql += whereCase;
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public CarTypePrice getRoleById(String id) {
		return this.findObject("from " + CarTypePrice.class.getName() + " where id='" + id + "'");
	}

	public void saveCarTypePrice(CarTypePrice carTypePrice) {
		this.saveOrUpdateObject(carTypePrice);
	}

	public void deleteCarTypePrice(String id) {
		String[] ids = id.split(",");
		Session session = this.getSession();
		for (int i = 0; i < ids.length; i++) {
			CarTypePrice carTypePrice = this.getRoleById(ids[i]);
			if (carTypePrice != null) {
				session.delete(carTypePrice);
			}
		}
	}

	public List<CarTypePrice> list(String checkStationName) {
		String hql = " from " + CarTypePrice.class.getName() + " where checkStationName = '" + checkStationName + "'";
		return this.findList(hql);
	}

	public CarTypePrice money(String carType, String checkStationName) {
		String hql = " from " + CarTypePrice.class.getName() + " where carType = '" + carType
				+ "' and checkStationName = '" + checkStationName + "'";
		return this.findObject(hql);
	}

}
