package com.bj.scb.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.stream.events.EndDocument;

import org.apache.catalina.startup.Tomcat;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.EmergencyOrderInfo;
import com.bj.scb.pojo.Order;
import com.bj.scb.utils.JdryTime;
import com.bj.scb.utils.PageList;

@Repository
public class EmergencyOrderInfoDaoImpl extends BaseDaoImpl {

	/**
	 * @category 根据权限来查询检车站所有未完成订单，区域管理员全部查询，客服人人员需要根据ID查询
	 */
	public PageList<EmergencyOrderInfo> selectAll(Map<String, Object> parameter) {
		boolean isManager = (boolean) parameter.get("isManager");
		String checkStationName = parameter.get("checkStationName").toString();
		String id = parameter.get("id").toString();
		String hql = "from " + EmergencyOrderInfo.class.getName();
		String where = " where a.isRefund='否'   and a.checkStationName='" + checkStationName + "'";
		if (isManager) { // true即客户人员
			where += " and a.staffId='" + id + "'";
		}
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerName like:carOwnerName "
					+ " or a.carOwnerMobile like:carOwnerMobile ";
			map.put("username", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
		}
		hql += whereCase + " order by a.insertTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	/**
	 * @category 保存订单
	 */
	public void saveOdInfo(EmergencyOrderInfo orderInfo) {
		this.saveOrUpdateObject(orderInfo);
	}

	/**
	 * 
	 * @category 查询单个客服已完成订单
	 */
	public PageList<EmergencyOrderInfo> selectOneAll(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName").toString();
		String orderUserName = parameter.get("orderUserName").toString();
		String hql = "from " + EmergencyOrderInfo.class.getName();
		String where = " where isRefund='是' and checkStationName='" + checkStationName + "' and orderUserName='"
				+ orderUserName + "'";
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerName like:carOwnerName "
					+ " or a.carOwnerMobile like:carOwnerMobile ";
			map.put("username", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
		}
		hql += whereCase + " order by a.finishTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	/**
	 * 
	 * @category 查询整个检车站客服已完成订单
	 */
	public PageList<EmergencyOrderInfo> selectOnesAllList(Map<String, Object> parameter) {
		boolean isManager = (boolean) parameter.get("isManager");
		String checkStationName = parameter.get("checkStationName").toString();
		String id = parameter.get("id").toString();
		String hql = "from " + EmergencyOrderInfo.class.getName();
		String where = " where a.isRefund='是'   and checkStationName='" + checkStationName + "'";
		
		if (isManager) { // true即客户人员
			where += " and a.staffId='" + id + "'";
		}
		
		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.carOwnerName like:username " + " or a.carOwnerName like:carOwnerName "
					+ " or a.carOwnerMobile like:carOwnerMobile ";
			map.put("username", "%" + search + "%");
			map.put("carOwnerName", "%" + search + "%");
			map.put("carOwnerMobile", "%" + search + "%");
		}
		hql += whereCase + " order by a.finishTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public List<EmergencyOrderInfo> selectOneAll(String checkStationName, String isInnerPeople) {
		String hql = " from " + EmergencyOrderInfo.class.getName() + " where isRefund='否'   and staffId='"
				+ isInnerPeople + "' and checkStationName='" + checkStationName + "'";
		return this.findList(hql);
	}

	public EmergencyOrderInfo getOrderInfoById(String id) {
		String hql = " from " + EmergencyOrderInfo.class.getName() + " where id='" + id + "'";
		return this.findObject(hql);
	}

	/**
	 * @category 查询“已预约”状态的救援点下的所有订单
	 * @param checkStationName
	 * @return
	 */
	public List<EmergencyOrderInfo> queryYJOrderByName(String checkStationName) {
		String hql = " from " + EmergencyOrderInfo.class.getName() + " where checkStationName='" + checkStationName
				+ "' and typeState='已预约'";
		return this.findList(hql);
	}

	/**
	 * @category 根据客服微信号和订单状态为“派单中”以及isRefund=‘否’查询出所有订单
	 * @param checkStationName
	 * @return
	 */
	public List<EmergencyOrderInfo> queryYJOrderByStaffId(String staffId) {
		String hql = " from " + EmergencyOrderInfo.class.getName() + " where staffId='" + staffId
				+ "' and typeState='派单中'  and isRefund='否' ";
		return this.findList(hql);
	}

	/**
	 * @category 更新应急救援订单的状态
	 * @param staffId
	 * @param StaffName
	 */
	public void updateOrderInfo(String id, String staffId, String StaffName, String state) {
		String time = JdryTime.getFullTimeBySec(new Date().getTime());
		String startStr = "update t_emergency_order_info set staffId='" + staffId + "'" + ",staffName='" + StaffName
				+ "',typeState='" + state + "'";
		String endStr = " where id='" + id + "'";
		String sql = startStr + ",sendTime='" + time + "'" + endStr;
		getSession().createSQLQuery(sql).executeUpdate();
	}

	/**
	 * @category 根据openId查询用户的订单
	 * @param openId
	 * @return
	 */
	public List<EmergencyOrderInfo> getAllOrderByOpenId(String openId) {
		String hql = "from " + EmergencyOrderInfo.class.getName() + " where orderUserId='" + openId + "'"
				+ " order by insertTime desc";
		return this.findList(hql);
	}

}
