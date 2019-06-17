package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.TimeCount;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.utils.PageList;

@Repository
public class TimeCountDaoImpl extends BaseDaoImpl {

	/**
	 * @category 分页查询
	 * @param parameter
	 * @return
	 */
	public PageList<TimeCount> selectList(Map<String, Object> parameter) {

		String checkStationName = parameter.get("checkStationName") == null ? "" : parameter.get("checkStationName").toString();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();

		String hql = "from " + TimeCount.class.getName();
		String whereCase = " a where 1=1 and checkStationName='" + checkStationName + "'";
		Map<String, Object> map = new HashMap<String, Object>();

		if (!search.equals("")) {
			whereCase += " and a.orderTime like:orderTime " + " or a.orderCount like:orderCount ";
			map.put("orderTime", "%" + search + "%");
			map.put("orderCount", "%" + search + "%");
		}

		// 最后的hql
		hql += whereCase + " order by orderStartTime";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	/**
	 * @category 不分页查询所有记录
	 * @return
	 */
	public List<TimeCount> selectAll() {
		return this.findList("from " + TimeCount.class.getName());
	}

	/**
	 * 保存或更新
	 * 
	 * @param freeLearn
	 */
	public void saveOrUpdate(TimeCount freeLearn) {
		this.saveOrUpdateObject(freeLearn);
	}

	/**
	 * 保存
	 * 
	 * @param freeLearn
	 */
	public void save(TimeCount freeLearn) {
		this.saveObject(freeLearn);
	}
	
	/**
	 * @category根据id查询
	 * @param id
	 * @return
	 */
	public TimeCount getById(String id) {
		return this.findObject("from " + TimeCount.class.getName() + " where id='" + id + "'");
	}
	
	/**
	 * @category 通过检车站和时间段来查询
	 * @param orderTime
	 * @param checkStationName
	 * @return
	 */
	public TimeCount getByOrderTimeAndName(String orderTime,String checkStationName) {
		return this.findObject("from " + TimeCount.class.getName() + " where checkStationName='" + checkStationName + "' and orderTime='" + orderTime +"'");
	}

	/**
	 * @category 根据id删除
	 * @param id
	 */
	public void delete(String id) {
		Session session = this.getSession();
		try {
			TimeCount role = this.getById(id);
			if (role != null) {
				session.delete(role);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @category 根据检车站名称来查询该检车站下所有的时间段
	 * @param map
	 * @return
	 */
	public List<TimeCount> selectByName(String checkStationName) {
		String hql = " from " + TimeCount.class.getName() + " where checkStationName ='" + checkStationName + "' order by orderStartTime";
		return this.findList(hql);
	}
	

}
