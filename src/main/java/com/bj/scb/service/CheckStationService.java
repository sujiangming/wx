package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.CheckStation;
import com.bj.scb.utils.PageList;

public interface CheckStationService {
	
	List<CheckStation> getStationListByType(String type);

	PageList<CheckStation> selectlist(Map<String, Object> parameter);

	CheckStation getCheckStationById(String id);
	
	CheckStation getCheckStationByName(String name);

	void saveCheckStation(CheckStation checkStation);

	void deleteCheckStation(String id);

	List<CheckStation> listAll();
}
