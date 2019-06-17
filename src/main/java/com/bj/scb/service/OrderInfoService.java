package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.OrderInfo;
import com.bj.scb.utils.PageList;

public interface OrderInfoService {

	PageList<OrderInfo> selectAll(Map<String, Object> parameter);

	void saveOrderInfo(String id);

	void saveOdInfo(OrderInfo orderInfo);

	PageList<OrderInfo> selectOnesAll(Map<String, Object> parameter);

	PageList<OrderInfo> selectOnesAllList(Map<String, Object> parameter);

	List<OrderInfo> selectOneAll(String checkStationName, String isInnerPeople);

}
