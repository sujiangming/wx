package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.utils.PageList;

@Repository
public class InnerUserDaoImpl extends BaseDaoImpl {

	public PageList<InnerUser> selectList(Map<String, Object> parameter){
		String checkStationName = parameter.get("checkStationName") == null ? ""
				: parameter.get("checkStationName").toString();
		Boolean isS = (Boolean) parameter.get("isSuperManager");
		String hql = "from " + InnerUser.class.getName();
		String where = null;
		if (isS.booleanValue()) {
			where = " where innerUserRole != '超级管理员'";
		} else {
			where = " where innerUserRole != '超级管理员' and checkStationName ='" + checkStationName + "'";
		}

		String whereCase = " a " + where;
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = Integer.parseInt(parameter.get("currentPage").toString());
		int showCount = Integer.parseInt(parameter.get("showCount").toString());
		String search = parameter.get("search") == null ? "" : parameter.get("search").toString();
		if (!search.equals("")) {
			whereCase += " and a.innerUserName like:username" + " or a.innerUserMobile like:innerUserMobile "
		+  " or a.innerUserName like:innerUserName ";
			map.put("username", "%" + search + "%");
			map.put("innerUserMobile", "%" + search + "%");
			map.put("innerUserName", "%" + search + "%");
		}
		hql += whereCase;
		return this.findPageList(hql, currentPage, showCount, map);

	}

	public InnerUser getInnerUserById(String id) {
		return this.findObject("from " + InnerUser.class.getName() + " where id='" + id + "'");
	}

	public void saveInnerUser(InnerUser innerUser) {
		this.saveOrUpdateObject(innerUser);
	}

	public void deleteInnerUser(String id) {
		String[] ids = id.split(",");
		Session session = this.getSession();
		for (int i = 0; i < ids.length; i++) {
			InnerUser innerUser = this.getInnerUserById(ids[i]);
			if (innerUser != null) {
				session.delete(innerUser);
			}
		}
	}

	public void selectNewPwd(String pwd, String newpwd, String mobel) {
		Session session = this.getSession();
		String sql = "update t_inner_user set innerUserPwd=" + newpwd + " where innerUserMobile='" + mobel + "'";
		Query query = session.createSQLQuery(sql);
		query.executeUpdate();
	}

	/**
	 * @category 根据检查站名称和角色来获取客服，即获取指定下的客服人员
	 * @param checkStationName
	 * @return
	 */
	public List<InnerUser> selectCS(String checkStationName) {
		String hql = " from " + InnerUser.class.getName() + " where checkStationName='" + checkStationName
				+ "' and isManager=true";
		return this.findList(hql);
	}

	public InnerUser getInnerUserMB(String customerMobile) {
		return this.findObject("from " + InnerUser.class.getName() + " where innerUserMobile='" + customerMobile + "'");
	}

	public InnerUser selectAdminByStationName(String checkStationName) {
		return this.findObject("from " + InnerUser.class.getName() + " where checkStationName='" + checkStationName
				+ "'" + " and innerUserRole='区域管理员'");
	}

	public InnerUser selsecOneInnerUser(String isInnerPeople, String checkStationName) {
		String hql = " from " + InnerUser.class.getName() + " where id='" + isInnerPeople + "' and checkStationName='"
				+ checkStationName + "'";
		return this.findObject(hql);
	}

}
