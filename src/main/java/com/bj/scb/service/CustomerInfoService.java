package com.bj.scb.service;

import java.util.Map;

import com.bj.scb.pojo.CustomerInfo;
import com.bj.scb.utils.PageList;

public interface CustomerInfoService {

	PageList<CustomerInfo> selectAll(Map<String, Object> parameter);

	CustomerInfo getCustomerInfo(String id);

	void saveCustomerInfo(CustomerInfo customerInfo);

	void deleteCustomerInfo(String id);

	CustomerInfo selectNickName(String orderUserId);

}
