package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.CheckStationDaoImpl;
import com.bj.scb.pojo.CheckStation;
import com.bj.scb.service.CheckStationService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class CheckStationServiceImpl implements CheckStationService {

	@Resource
	CheckStationDaoImpl checkStationDaoImpl;

	public List<CheckStation> getStationListByType(String type) {
		return checkStationDaoImpl.getStationListByType(type);
	}

	@Override
	public PageList<CheckStation> selectlist(Map<String, Object> parameter) {
		return checkStationDaoImpl.selectlist(parameter);
	}

	@Override
	public CheckStation getCheckStationById(String id) {
		return checkStationDaoImpl.getCheckStationById(id);
	}

	@Override
	public void saveCheckStation(CheckStation checkStation) {
		checkStationDaoImpl.saveCheckStation(checkStation);
	}

	@Override
	public void deleteCheckStation(String id) {
		checkStationDaoImpl.deleteCheckStation(id);
	}

	@Override
	public List<CheckStation> listAll() {
		return checkStationDaoImpl.listAll();
	}

	@Override
	public CheckStation getCheckStationByName(String name) {
		return checkStationDaoImpl.getCheckStationByName(name);
	}

}
