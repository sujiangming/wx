package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.EmergencyOrderInfo;
import com.bj.scb.utils.PageList;

public interface EmergencyOrderInfoService {

	PageList<EmergencyOrderInfo> selectAll(Map<String, Object> parameter);

	void saveOdInfo(EmergencyOrderInfo orderInfo);

	PageList<EmergencyOrderInfo> selectOnesAll(Map<String, Object> parameter);

	PageList<EmergencyOrderInfo> selectOnesAllList(Map<String, Object> parameter);

	List<EmergencyOrderInfo> selectOneAll(String checkStationName, String isInnerPeople);
	
	EmergencyOrderInfo getOrderInfoById(String id);
	
	List<EmergencyOrderInfo> queryYJOrderByName(String checkStationName);

	void updateOrderInfo(String id,String staffId,String StaffName,String state);

	List<EmergencyOrderInfo> queryYJOrderByStaffId(String staffId);
	
	List<EmergencyOrderInfo> getAllOrderByOpenId(String openId);
}
