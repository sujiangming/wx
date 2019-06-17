package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.EmergencyPrice;
import com.bj.scb.pojo.TimeCount;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.utils.PageList;

@Repository
public class EmergencyDaoImpl extends BaseDaoImpl {

	/**
	 * @category 分页查询
	 * @param parameter
	 * @return
	 */
	public PageList<EmergencyPrice> selectList(Map<String, Object> parameter) {

//		String checkStationName = parameter.get("checkStationName") == null ? ""
//				: parameter.get("checkStationName").toString();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();

		String hql = "from " + EmergencyPrice.class.getName();
		String whereCase = " a where 1=1 ";
		Map<String, Object> map = new HashMap<String, Object>();

		if (!search.equals("")) {
			whereCase += " and a.unitPrice like:unitPrice ";
			map.put("unitPrice", "%" + search + "%");
		}

		// 最后的hql
		hql += whereCase + " order by createTime desc";
		return this.findPageList(hql, currentPage, showCount, map);
	}

	/**
	 * @category 不分页查询所有记录
	 * @return
	 */
	public List<EmergencyPrice> selectAll() {
		return this.findList("from " + EmergencyPrice.class.getName());
	}

	/**
	 * 保存或更新
	 * 
	 * @param freeLearn
	 */
	public void saveOrUpdate(EmergencyPrice freeLearn) {
		this.saveOrUpdateObject(freeLearn);
	}

	/**
	 * 保存
	 * 
	 * @param freeLearn
	 */
	public void save(EmergencyPrice freeLearn) {
		this.saveObject(freeLearn);
	}

	/**
	 * @category根据id查询
	 * @param id
	 * @return
	 */
	public EmergencyPrice getById(String id) {
		return this.findObject("from " + EmergencyPrice.class.getName() + " where id='" + id + "'");
	}

	/**
	 * @category 根据id删除
	 * @param id
	 */
	public void delete(String id) {
		Session session = this.getSession();
		String[] idString = id.split(",");
		try {
			for (String string : idString) {
				EmergencyPrice role = this.getById(string);
				if (role != null) {
					session.delete(role);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public EmergencyPrice getPrice() {
		return this.findObject("from " + EmergencyPrice.class.getName());
	}
}
