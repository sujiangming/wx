package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.OrderNickNameDaoImpl;
import com.bj.scb.pojo.OrderNickName;
import com.bj.scb.service.OrderNickNameService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class OrderNickNameServiceImpl implements OrderNickNameService{
	
	@Resource
	OrderNickNameDaoImpl orderNicNameDaoImpl;
	
	@Override
	public PageList<OrderNickName> selectAll(Map<String, Object> parameter) {
		// TODO Auto-generated method stub
		return orderNicNameDaoImpl.selectAll(parameter);
	}

	@Override
	public void saveObj(OrderNickName oNickName) {
		orderNicNameDaoImpl.saveObj(oNickName);
		
	}

}
