package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @category 应急救援订单表
 * 
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_emergency_order_info")
public class EmergencyOrderInfo {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String orderUserId;// 预约人ID-》微信号
	@Column
	private String carOwnerName;// 车主姓名
	@Column
	private String carOwnerMobile;// 车主电话
	@Column
	private String carCode;// 车牌号
	@Column
	private String orderLocation; // 预约地点的坐标，即经纬度
	@Column
	private String payFee;// 支付定金
	@Column
	private String sumPrice;// 总价
	@Column
	private String kilometer;// 公里数
	@Column
	private String isRefund;// 服务是否完成
	@Column
	private String checkStationName;// 检车站名称
	@Column
	private String checkStationId;// 检车站Id
	@Column
	private String staffName;// 客服人员名称
	@Column
	private String staffId;// 客服人员名称
	@Column
	private String insertTime;// 插入时间
	@Column
	private String sendTime;// 派单时间
	@Column
	private String finishTime;// 完成时间
	@Column
	private String typeState;// 订单状态：派单，派单中，服务完成

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderUserId() {
		return orderUserId;
	}

	public void setOrderUserId(String orderUserId) {
		this.orderUserId = orderUserId;
	}

	public String getCarOwnerName() {
		return carOwnerName;
	}

	public void setCarOwnerName(String carOwnerName) {
		this.carOwnerName = carOwnerName;
	}

	public String getCarOwnerMobile() {
		return carOwnerMobile;
	}

	public void setCarOwnerMobile(String carOwnerMobile) {
		this.carOwnerMobile = carOwnerMobile;
	}

	public String getCarCode() {
		return carCode;
	}

	public void setCarCode(String carCode) {
		this.carCode = carCode;
	}
	
	public String getOrderLocation() {
		return orderLocation;
	}

	public void setOrderLocation(String orderLocation) {
		this.orderLocation = orderLocation;
	}

	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public String getSumPrice() {
		return sumPrice;
	}

	public void setSumPrice(String sumPrice) {
		this.sumPrice = sumPrice;
	}

	public String getKilometer() {
		return kilometer;
	}

	public void setKilometer(String kilometer) {
		this.kilometer = kilometer;
	}

	public String getIsRefund() {
		return isRefund;
	}

	public void setIsRefund(String isRefund) {
		this.isRefund = isRefund;
	}

	public String getCheckStationName() {
		return checkStationName;
	}

	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}

	public String getCheckStationId() {
		return checkStationId;
	}

	public void setCheckStationId(String checkStationId) {
		this.checkStationId = checkStationId;
	}

	public String getStaffName() {
		return staffName;
	}

	public void setStaffName(String staffName) {
		this.staffName = staffName;
	}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getTypeState() {
		return typeState;
	}

	public void setTypeState(String typeState) {
		this.typeState = typeState;
	}

}
