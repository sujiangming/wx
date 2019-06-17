package com.bj.scb.dao.impl;
import org.springframework.stereotype.Repository;
import com.bj.scb.dao.base.BaseDaoImpl;
import com.bj.scb.pojo.InnerUser;

@Repository
public class LoginDaoImpl extends BaseDaoImpl{

	public InnerUser login(String account, String pwd) {
		String hql = "from " + InnerUser.class.getName() + " where innerUserMobile='" + account +  "' and innerUserPwd='" + pwd + "'";
		return this.findObject(hql);
	}

}
