package com.bj.scb.dao.impl;
import org.springframework.stereotype.Repository;

import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.InnerUser;

@Repository
public class UserDaoImpl extends BaseDaoImpl {

	public void insertUser(InnerUser user) {
		this.saveObject(user);
	}

	/**
	 * 通过前端注册的账号查询数据库中是否已经存在 如果存在则返回true 否则，返回false
	 */
	public boolean getUserByAccount(String account) {
		String hql = "from " + InnerUser.class.getName() + " where innerUserMobile = '" + account + "'";
		InnerUser user = this.findObject(hql);
		if (null == user) {
			return false;
		}
		return true;
	}

}
