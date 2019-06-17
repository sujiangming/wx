package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.InnerUserDaoImpl;
import com.bj.scb.dao.impl.OrderDaoImpl;
import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Order;
import com.bj.scb.service.OrderService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class OrderServiceImpl implements OrderService{
	
	@Resource
	OrderDaoImpl orderDaoImpl;
	
	@Resource
	InnerUserDaoImpl innerUserDaoImpl;
	
	@Override
	public PageList<Order> selectAll(Map<String, Object> parameter) {
		return orderDaoImpl.selectAll(parameter);
	}
	@Override
	public void saveOrder(Order order) {
		orderDaoImpl.saveOrder(order);
		
	}
	@Override
	public void saveOrderIs(String carOwnerMobile,String insertTime) {
		orderDaoImpl.saveOrderIs(carOwnerMobile,insertTime);
		
	}
	@Override
	public List<Order> getAllOrderByOpenId(String openId) {
		return orderDaoImpl.getAllOrderByOpenId(openId);
	}
	@Override
	public void saveOrderTY(String carOwnerMobile,String insertTime) {
		orderDaoImpl.saveOrderTY(carOwnerMobile,insertTime);
		
	}
	
	@Override
	public InnerUser selectAdminByStationName(String checkStationName) {
		return innerUserDaoImpl.selectAdminByStationName(checkStationName);
	}
	
	@Override
	public List<Order> selectOrdersByName(String name) {
		return orderDaoImpl.selectOrdersByName(name);
	}

}
