package com.bj.scb.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Role;
import com.bj.scb.utils.PageList;

@Repository
public class RoleMemberDaoImpl extends BaseDaoImpl {

	public PageList<InnerUser> queryRolePageList(Map<String, Object> parameter) {
		String checkStationName = parameter.get("checkStationName") == null ? ""
				: parameter.get("checkStationName").toString();
		Boolean isS = (Boolean) parameter.get("isSuperManager");
		String hql = " from " + InnerUser.class.getName();

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
			whereCase += " and a.innerUserName like:innerUserName " + "or a.innerUserMobile like:innerUserMobile";
			map.put("innerUserName", "%" + search + "%");
			map.put("innerUserMobile", "%" + search + "%");
		}
		hql += whereCase;
		return this.findPageList(hql, currentPage, showCount, map);
	}

	public Role getRoleById(String roleId) {
		return this.findObject("from InnerUser where innerUserMobile='" + roleId + "'");
	}

	public void saveRole(InnerUser role) {
		this.saveOrUpdateObject(role);
	}

	public void deleteRole(String roleId) {
		String[] roleIds = roleId.split(",");
		Session session = this.getSession();
		try {
			for (int i = 0; i < roleIds.length; i++) {
				Role role = this.getRoleById(roleIds[i]);
				if (role != null) {
					session.delete(role);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int updateRoleMember(InnerUser role) {
		int result = 0;
		boolean is = false;
		boolean in = true;
		Session session = this.getSession();
		Query query = session.createQuery("update InnerUser set isManager=" + in + ",innerUserRole='客服人员' where isManager=" + is
						+ " and checkStationName='" + role.getCheckStationName() + "'");
		result = query.executeUpdate();
		return result;
	}

}
