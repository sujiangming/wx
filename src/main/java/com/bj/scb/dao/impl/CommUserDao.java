package com.bj.scb.dao.impl;

import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.InnerUser;

@Repository
public class CommUserDao extends BaseDaoImpl {

	public InnerUser getUserByMobileAndPwd(String account, String pwd) {
		String hql = "from " + InnerUser.class.getName() + " where innerUserMobile='" + account + "' and innerUserPwd='"
				+ pwd + "'";
		return this.findObject(hql);
	}

	public InnerUser getUserByStationId(String checkStationName) {
		boolean is = false;
		String hql = "from " + InnerUser.class.getName() + " where isManager=" + is + " and checkStationName='"
				+ checkStationName + "'";
		return this.findObject(hql);
	}

	public InnerUser getUserById(String id) {
		String hql = "from " + InnerUser.class.getName() + " where id='" + id + "'";
		return this.findObject(hql);
	}

}
