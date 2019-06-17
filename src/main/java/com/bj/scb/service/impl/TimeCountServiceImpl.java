package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.TimeCountDaoImpl;
import com.bj.scb.pojo.TimeCount;
import com.bj.scb.service.TimeCountService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class TimeCountServiceImpl implements TimeCountService {

	@Resource
	TimeCountDaoImpl timeCountDaoImpl;

	public PageList<TimeCount> selectList(Map<String, Object> parameter) {
		return timeCountDaoImpl.selectList(parameter);
	}

	public void delete(String id) {
		timeCountDaoImpl.delete(id);
	}

	public TimeCount findObjectById(String id) {
		return timeCountDaoImpl.getById(id);
	}

	public void saveOrUpdate(TimeCount TimeCount) {
		timeCountDaoImpl.saveOrUpdate(TimeCount);
	}

	@Override
	public List<TimeCount> selectAll() {
		return timeCountDaoImpl.selectAll();
	}

	@Override
	public void save(TimeCount freeLearn) {
		timeCountDaoImpl.save(freeLearn);
	}

	@Override
	public List<TimeCount> selectListByName(String name) {
		return timeCountDaoImpl.selectByName(name);
	}

}
