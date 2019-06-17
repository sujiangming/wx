package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.CarTypePrice;
import com.bj.scb.pojo.EmergencyPrice;
import com.bj.scb.utils.PageList;

public interface EmergencyPriceService {
	PageList<EmergencyPrice> selectList(Map<String, Object> parameter);
	List<EmergencyPrice> selectAll();
	void saveOrUpdate(EmergencyPrice freeLearn);
	void save(EmergencyPrice freeLearn);
	void delete(String id);
	EmergencyPrice getEmergencyById(String id);
	EmergencyPrice getPrice();
}
