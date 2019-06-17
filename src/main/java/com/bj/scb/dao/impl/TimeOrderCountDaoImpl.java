package com.bj.scb.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.TimeCount;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;

@Repository
public class TimeOrderCountDaoImpl extends BaseDaoImpl {

	@Resource
	TimeCountDaoImpl timeCountDaoImpl;

	public PageList<TimeOrderCount> listAll(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName").toString();
		String hql = " from " + TimeOrderCount.class.getName();
		String where = " where 1=1 ";
		String whereCase = " a " + where + " and checkStationName='" + checkStationName + "'";
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.checkStationName like:username" + " or a.checkStationName like:checkStationName ";
			map.put("username", "%" + search + "%");
			map.put("checkStationName", "%" + search + "%");
		}
		hql += whereCase + " order by orderStartTime";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public TimeOrderCount getTimeOrderCountById(String id) {
		return this.findObject("from " + TimeOrderCount.class.getName() + " where id='" + id + "'");
	}

	public void saveTimeOrderCount(TimeOrderCount tCount) {
		this.saveOrUpdateObject(tCount);
	}

	public void deleteTimeOrderCount(String id) {
		String[] ids = id.split(",");
		Session session = this.getSession();
		try {
			for (int i = 0; i < ids.length; i++) {
				TimeOrderCount timeOrderCount = this.getTimeOrderCountById(ids[i]);
				if (timeOrderCount != null) {
					session.delete(timeOrderCount);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public TimeOrderCount select(Map<String, String> map) {
		String orderTime = map.get("orderTime");
		String orderDate = map.get("orderDate");
		String checkStationName = map.get("checkStationName");
		String hql = " from " + TimeOrderCount.class.getName() + " where orderCount != 0 and checkStationName ='"
				+ checkStationName + "' and orderDate='" + orderDate + "' and orderTime='" + orderTime + "'";
		return this.findObject(hql);
	}

	public void updateOrderCount(String orderTime, String orderDate, String checkStationName) {
		// 先查找，有就更新；没有就新插入
		String hql = "from " + TimeOrderCount.class.getName() + " where orderTime='" + orderTime + "' and orderDate='"
				+ orderDate + "' and checkStationName='" + checkStationName + "'";
		Object object = this.findObject(hql);
		if (null == object) {
			// 没有查到，则插入
			TimeOrderCount newInstance = new TimeOrderCount();
			newInstance.setCheckStationName(checkStationName);// 检车站名称
			newInstance.setOrderDate(orderDate);// 日期
			newInstance.setOrderTime(orderTime);// 时间段
			newInstance.setUpdateTime(JdryTime.getFormatDate(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));

			TimeCount timeCount = timeCountDaoImpl.getByOrderTimeAndName(orderTime, checkStationName);
			if (null == timeCount) {
				newInstance.setOrderCount("30");// 需要根据时段表来获取到人数，默认给每一个检车站每个时间段可以预约的人数为30人
			} else {
				newInstance.setOrderCount(String.valueOf(timeCount.getOrderCount()));
			}
			this.saveObject(newInstance);
		} else {
			// 查到了，直接更新
			TimeOrderCount timeOrderCount = (TimeOrderCount) object;
			// 前端做为0的判断
			// 先取出预约数量，再进行减1操作
			String count = String.valueOf(Integer.parseInt(timeOrderCount.getOrderCount()) - 1);
			timeOrderCount.setOrderCount(count);
			timeOrderCount.setUpdateTime(JdryTime.getFormatDate(new Date().getTime(), "yyyy-MM-dd HH:mm:ss"));
			this.saveOrUpdateObject(timeOrderCount);
		}
	}

	public void saveOrUpdate(TimeOrderCount count) {
		this.saveOrUpdateObject(count);
	}

	public void delete(String timeCountId) {
		Session session = this.getSession();
		try {
			TimeOrderCount role = this.getByTimeCountId(timeCountId);
			if (role != null) {
				session.delete(role);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TimeOrderCount getByTimeCountId(String timeCountId) {
		return this.findObject("from " + TimeOrderCount.class.getName() + " where timeCountId='" + timeCountId + "'");
	}

	public List<TimeOrderCount> selectAll() {
		return this.findList("from " + TimeOrderCount.class.getName());
	}

	public TimeOrderCount queryByConditaion(String orderTime, String orderDate, String checkStationName) {
		String hql = "from " + TimeOrderCount.class.getName() + " where orderTime='" + orderTime + "' and orderDate='"
				+ orderDate + "' and checkStationName='" + checkStationName + "'";
		return this.findObject(hql);
	}

}
