package com.bj.scb.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.LoginDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.LoginService;

@Service
@Transactional
public class LoginServiceImpl implements LoginService {

	@Resource
	LoginDaoImpl loginDaoImpl;

	public InnerUser login(String account, String pwd) {
		return loginDaoImpl.login(account, pwd);
	}

}
