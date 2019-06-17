package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.TimeOrderCountDaoImpl;
import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.service.TimeOrderCountService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class TimeOrderCountServiceImpl implements TimeOrderCountService {
	
	@Resource
	TimeOrderCountDaoImpl timeOrderCountDaoImpl;

	@Override
	public PageList<TimeOrderCount> listAll(Map<String, Object> parameter) {
		return timeOrderCountDaoImpl.listAll(parameter);
	}

	@Override
	public TimeOrderCount getTimeOrderCountById(String id) {
		// TODO Auto-generated method stub
		return timeOrderCountDaoImpl.getTimeOrderCountById(id);
	}

	@Override
	public void saveTimeOrderCount(TimeOrderCount tCount) {
		// TODO Auto-generated method stub
		timeOrderCountDaoImpl.saveTimeOrderCount(tCount);
	}

	@Override
	public void deleteTimeOrderCount(String id) {
		// TODO Auto-generated method stub
		timeOrderCountDaoImpl.deleteTimeOrderCount(id);
	}

	@Override
	public TimeOrderCount select(Map<String,String> map) {
		return timeOrderCountDaoImpl.select(map);
	}

	@Override
	public void updateOrderCount(String orderTime, String orderDate, String checkStationName) {
		timeOrderCountDaoImpl.updateOrderCount(orderTime, orderDate, checkStationName);

	}

	@Override
	public void saveOrUpdate(TimeOrderCount count) {
		timeOrderCountDaoImpl.saveOrUpdate(count);
	}

	@Override
	public void delete(String timeCountId) {
		timeOrderCountDaoImpl.delete(timeCountId);
	}

	@Override
	public TimeOrderCount getByTimeCountId(String timeCountId) {
		return timeOrderCountDaoImpl.getByTimeCountId(timeCountId);
	}

	@Override
	public TimeOrderCount queryByConditaion(String orderTime, String currentDate, String checkStationName) {
		return timeOrderCountDaoImpl.queryByConditaion(orderTime, currentDate, checkStationName);
	}

}
