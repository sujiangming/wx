package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.InnerUser;
import com.bj.scb.pojo.Order;
import com.bj.scb.utils.PageList;

public interface OrderService {

	PageList<Order> selectAll(Map<String, Object> parameter);

	void saveOrder(Order order);

	void saveOrderIs(String carOwnerMobile,String insertTime);
	
	List<Order> getAllOrderByOpenId(String openid);

	void saveOrderTY(String carOwnerMobile,String insertTime);
	
	InnerUser selectAdminByStationName(String checkStationName);
	
	List<Order> selectOrdersByName(String name);

}
