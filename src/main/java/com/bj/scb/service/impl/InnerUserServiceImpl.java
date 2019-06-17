package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.InnerUserDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.service.InnerUserService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class InnerUserServiceImpl implements InnerUserService {

	@Resource
	InnerUserDaoImpl innerUserDaoImpl;
	
	@Override
	public PageList<InnerUser> selectList(Map<String, Object> parameter) {
		
		return innerUserDaoImpl.selectList(parameter);
	}
	@Override
	public InnerUser getInnerUserById(String id) {
		
		return innerUserDaoImpl.getInnerUserById(id);
	}
	@Override
	public void saveInnerUser(InnerUser innerUser) {
		innerUserDaoImpl.saveInnerUser(innerUser);
		
	}
	@Override
	public void deleteInnerUser(String id) {
		innerUserDaoImpl.deleteInnerUser(id);
		
	}
	@Override
	public void selectNewPwd(String pwd, String newpwd, String mobel) {
		innerUserDaoImpl.selectNewPwd(pwd,newpwd,mobel);
		
	}
	@Override
	public List<InnerUser> selectCS(String checkStationName) {
		return innerUserDaoImpl.selectCS(checkStationName);
	}
	@Override
	public InnerUser getInnerUserMB(String customerMobile) {
		return innerUserDaoImpl.getInnerUserMB(customerMobile);
	}
	@Override
	public InnerUser selsecOneInnerUser(String isInnerPeople, String checkStationName) {
		return innerUserDaoImpl.selsecOneInnerUser(isInnerPeople,checkStationName);
	}

}
