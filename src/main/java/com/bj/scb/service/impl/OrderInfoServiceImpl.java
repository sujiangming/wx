package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.OrderInfoDaoImpl;
import com.bj.scb.pojo.OrderInfo;
import com.bj.scb.service.OrderInfoService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class OrderInfoServiceImpl implements OrderInfoService{
	@Resource
	OrderInfoDaoImpl orderInfoDaoImpl;

	@Override
	public PageList<OrderInfo> selectAll(Map<String, Object> parameter) {
		return orderInfoDaoImpl.selectAll(parameter);
	}

	@Override
	public void saveOrderInfo(String id) {
		orderInfoDaoImpl.saveOrderInfo(id);
		
	}

	@Override
	public void saveOdInfo(OrderInfo orderInfo) {
		orderInfoDaoImpl.saveOdInfo(orderInfo);
		
	}

	@Override
	public PageList<OrderInfo> selectOnesAll(Map<String, Object> parameter) {
		return orderInfoDaoImpl.selectOneAll(parameter);
	}

	@Override
	public PageList<OrderInfo> selectOnesAllList(Map<String, Object> parameter) {
		return orderInfoDaoImpl.selectOnesAllList(parameter);
	}

	@Override
	public List<OrderInfo> selectOneAll(String checkStationName, String isInnerPeople) {
		return orderInfoDaoImpl.selectOneAll(checkStationName,isInnerPeople);
	}


}
