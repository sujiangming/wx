package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.CarTypePriceDaoImpl;
import com.bj.scb.pojo.CarTypePrice;
import com.bj.scb.service.CarTypePriceService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class CarTypePriceServiceImpl implements CarTypePriceService {

	@Resource
	CarTypePriceDaoImpl carTypePriceDao;

	public PageList<CarTypePrice> selectAll(Map<String, Object> parameter) {
		return carTypePriceDao.selectAll(parameter);
	}

	@Override
	public CarTypePrice getRoleById(String id) {
		return carTypePriceDao.getRoleById(id);
	}

	@Override
	public void saveCarTypePrice(CarTypePrice carTypePrice) {
		carTypePriceDao.saveCarTypePrice(carTypePrice);
	}

	@Override
	public void deleteCarTypePrice(String id) {
		carTypePriceDao.deleteCarTypePrice(id);
	}

	@Override
	public List<CarTypePrice> list(String checkStationName) {
		return carTypePriceDao.list(checkStationName);
	}

	@Override
	public CarTypePrice money(String carType, String checkStationName) {
		return carTypePriceDao.money(carType, checkStationName);
	}
}
