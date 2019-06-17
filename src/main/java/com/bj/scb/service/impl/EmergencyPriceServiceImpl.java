package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.EmergencyDaoImpl;
import com.bj.scb.pojo.EmergencyPrice;
import com.bj.scb.service.EmergencyPriceService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class EmergencyPriceServiceImpl implements EmergencyPriceService {

	@Resource
	EmergencyDaoImpl emergencyDaoImpl;

	@Override
	public PageList<EmergencyPrice> selectList(Map<String, Object> parameter) {

		return emergencyDaoImpl.selectList(parameter);
	}

	@Override
	public List<EmergencyPrice> selectAll() {

		return emergencyDaoImpl.selectAll();
	}

	@Override
	public void saveOrUpdate(EmergencyPrice freeLearn) {
		emergencyDaoImpl.saveOrUpdate(freeLearn);
	}

	@Override
	public void save(EmergencyPrice freeLearn) {
		emergencyDaoImpl.save(freeLearn);
	}

	@Override
	public void delete(String id) {
		emergencyDaoImpl.delete(id);
	}

	@Override
	public EmergencyPrice getEmergencyById(String id) {
		return emergencyDaoImpl.getById(id);
	}

	@Override
	public EmergencyPrice getPrice() {
		return emergencyDaoImpl.getPrice();
	}

}
