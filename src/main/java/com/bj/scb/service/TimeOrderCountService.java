package com.bj.scb.service;

import java.util.List;
import java.util.Map;

import com.bj.scb.pojo.TimeOrderCount;
import com.bj.scb.utils.PageList;

public interface TimeOrderCountService {

	PageList<TimeOrderCount> listAll(Map<String, Object> parameter);

	TimeOrderCount getTimeOrderCountById(String id);

	void saveTimeOrderCount(TimeOrderCount tCount);

	void deleteTimeOrderCount(String id);

	TimeOrderCount select(Map<String,String> map);

	void updateOrderCount(String orderTime, String orderDate, String checkStationName);

	/**
	 * @category 新增或者保存
	 * @param count 表示TimeOrderCount 对象
	 */
	void saveOrUpdate(TimeOrderCount count);

	/**
	 * @category 根据时间人数表id,进行级联删除操作
	 * @param timeCountId
	 */
	void delete(String timeCountId);

	/**
	 * @category 根据TimeCount表的ID查询
	 * @param timeCountId
	 */
	TimeOrderCount getByTimeCountId(String timeCountId);

	
	TimeOrderCount queryByConditaion(String orderTime, String currentDate, String checkStationName);

}
