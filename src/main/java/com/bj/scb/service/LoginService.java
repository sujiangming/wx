package com.bj.scb.service;

import com.bj.scb.pojo.InnerUser;

public interface LoginService {
	public InnerUser login(String account,String pwd);
}
