package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.CarTypePrice;
import com.bj.scb.pojo.CheckStation;
import com.bj.scb.utils.PageList;

@Repository
public class CheckStationDaoImpl extends BaseDaoImpl {

	public List<CheckStation> getStationListByType(String type) {
		return this.findList("from " + CheckStation.class.getName() + " where type='" + type + "'" );
	}

	public PageList<CheckStation> selectlist(Map<String, Object> parameter) {
		String hql = " from " + CheckStation.class.getName();
		String where = " where 1=1";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.checkStationName like:username" + " or a.checkStationName like:checkStationName ";
			map.put("username", "%" + search + "%");
			map.put("checkStationName", "%" + search + "%");
		}
		hql += whereCase;
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public CheckStation getCheckStationById(String id) {
		return this.findObject(" from " + CheckStation.class.getName() + " where id='" + id + "'");
	}

	public void saveCheckStation(CheckStation checkStation) {
		this.saveOrUpdateObject(checkStation);
	}

	public void deleteCheckStation(String id) {
		String[] ids = id.split(",");
		Session session = this.getSession();
		for (int i = 0; i < ids.length; i++) {
			CheckStation checkStation = this.getCheckStationById(ids[i]);
			if (checkStation != null) {
				session.delete(checkStation);
			}
		}
	}

	public List<CheckStation> listAll() {
		String hql = " from " + CheckStation.class.getName();
		return this.findList(hql);
	}

	public CheckStation getCheckStationByName(String name) {
		return this.findObject(" from " + CheckStation.class.getName() + " where checkStationName='" + name + "'");
	}

}
