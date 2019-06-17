package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.TimeCount;
import com.bj.scb.utils.PageList;

public interface TimeCountService {
	/**
	 * @category 分页查询
	 * @param parameter
	 * @return
	 */
	PageList<TimeCount> selectList(Map<String, Object> parameter);

	/**
	 * @category 保存
	 * @param freeLearn
	 */
	void save(TimeCount freeLearn);

	
	/**
	 * @category 保存或更新
	 * @param freeLearn
	 */
	void saveOrUpdate(TimeCount freeLearn);

	/**
	 * @category 根据ID删除
	 * @param id
	 */
	void delete(String id);

	/**
	 * @category 根据id查询
	 * @param id
	 * @return
	 */
	TimeCount findObjectById(String id);

	/**
	 * 不分页查询
	 * @return
	 */
	List<TimeCount> selectAll();
	
	/**
	 * @author JDRY-SJM 根
	 * @return
	 */
	List<TimeCount> selectListByName(String name);
}
