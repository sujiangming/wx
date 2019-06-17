package com.bj.scb.service.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.CustomerInfoDaoImpl;
import com.bj.scb.pojo.CustomerInfo;
import com.bj.scb.service.CustomerInfoService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class CustomerInfoServiceImpl implements CustomerInfoService{
	
	@Resource
	CustomerInfoDaoImpl customerInfoDaoImpl;
	
	@Override
	public PageList<CustomerInfo> selectAll(Map<String, Object> parameter) {
		return customerInfoDaoImpl.selectAll(parameter);
	}
	@Override
	public CustomerInfo getCustomerInfo(String id) {
		return customerInfoDaoImpl. getCustomerInfo(id);
	}
	@Override
	public void saveCustomerInfo(CustomerInfo customerInfo) {
		customerInfoDaoImpl.saveCustomerInfo(customerInfo);
		
	}
	@Override
	public void deleteCustomerInfo(String id) {
		customerInfoDaoImpl.deleteCustomerInfo(id);
		
	}
	@Override
	public CustomerInfo selectNickName(String orderUserId) {
		return customerInfoDaoImpl.selectNicName(orderUserId);
	}

}
