package com.bj.scb.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bj.scb.dao.impl.EmergencyOrderInfoDaoImpl;
import com.bj.scb.pojo.EmergencyOrderInfo;
import com.bj.scb.service.EmergencyOrderInfoService;
import com.bj.scb.utils.PageList;

@Service
@Transactional
public class EmergencyOrderInfoServiceImpl implements EmergencyOrderInfoService {
	@Resource
	EmergencyOrderInfoDaoImpl emergencyOrderInfoDaoImpl;

	@Override
	public PageList<EmergencyOrderInfo> selectAll(Map<String, Object> parameter) {
		return emergencyOrderInfoDaoImpl.selectAll(parameter);
	}

	@Override
	public void saveOdInfo(EmergencyOrderInfo orderInfo) {
		emergencyOrderInfoDaoImpl.saveOdInfo(orderInfo);
	}

	@Override
	public PageList<EmergencyOrderInfo> selectOnesAll(Map<String, Object> parameter) {
		return emergencyOrderInfoDaoImpl.selectOneAll(parameter);
	}

	@Override
	public PageList<EmergencyOrderInfo> selectOnesAllList(Map<String, Object> parameter) {
		return emergencyOrderInfoDaoImpl.selectOnesAllList(parameter);
	}

	@Override
	public List<EmergencyOrderInfo> selectOneAll(String checkStationName, String isInnerPeople) {
		return emergencyOrderInfoDaoImpl.selectOneAll(checkStationName, isInnerPeople);
	}

	@Override
	public EmergencyOrderInfo getOrderInfoById(String id) {

		return emergencyOrderInfoDaoImpl.getOrderInfoById(id);
	}

	@Override
	public List<EmergencyOrderInfo> queryYJOrderByName(String checkStationName) {
		return emergencyOrderInfoDaoImpl.queryYJOrderByName(checkStationName);
	}

	/**
	 * @category 更新应急救援订单的状态
	 * @param staffId
	 * @param StaffName
	 * @param state     1、派单中；2、已完成
	 */
	@Override
	public void updateOrderInfo(String id, String staffId, String StaffName, String state) {
		emergencyOrderInfoDaoImpl.updateOrderInfo(id, staffId, StaffName, state);
	}

	@Override
	public List<EmergencyOrderInfo> queryYJOrderByStaffId(String staffId) {
		return emergencyOrderInfoDaoImpl.queryYJOrderByStaffId(staffId);
	}

	/**
	 * @category 根据openId查询用户的订单
	 * @param openId
	 * @return
	 */
	@Override
	public List<EmergencyOrderInfo> getAllOrderByOpenId(String openId) {
		return emergencyOrderInfoDaoImpl.getAllOrderByOpenId(openId);
	}

}
