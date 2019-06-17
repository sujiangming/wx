package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.CarTypePrice;
import com.bj.scb.utils.PageList;

public interface CarTypePriceService {
	 PageList<CarTypePrice> selectAll(Map<String, Object> parameter);

	CarTypePrice getRoleById(String id);

	void saveCarTypePrice(CarTypePrice carTypePrice);

	void deleteCarTypePrice(String id);

	List<CarTypePrice> list(String checkStationName);

	CarTypePrice money(String carType, String checkStationName);


}
