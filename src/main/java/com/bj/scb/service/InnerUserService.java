package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.InnerUser;
import com.bj.scb.utils.PageList;

public interface InnerUserService {

	public PageList<InnerUser> selectList(Map<String, Object> parameter);
	
	// 根据ID查询
	InnerUser getInnerUserById(String id);
		
	// 保存
	void saveInnerUser(InnerUser innerUser);
		
	// 删除
	void deleteInnerUser(String id);

	void selectNewPwd(String pwd, String newpwd, String mobel);

	 List<InnerUser> selectCS(String checkStationName);

	 InnerUser getInnerUserMB(String customerMobile);

	public InnerUser selsecOneInnerUser(String isInnerPeople, String checkStationName);
}
