package com.bj.scb.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 预约表
 * 
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_order")
public class Order {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String orderUserId;// 预约人ID-》微信号
	@Column
	private String carCode;// 车牌号
	@Column
	private String carOwnerName;// 车主姓名
	@Column
	private String carOwnerMobile;// 车主电话
	@Column
	private String carLicence;// 行车证
	@Column
	private String carInsurance;// 车辆保险时间
	@Column
	private String orderTime;// 预约时间
	@Column
	private String orderProject;// 预约车型
	@Column
	private int payWay;// 支付方式
	@Column
	private String payFee;// 支付费用
	@Column
	private int isPayFee;// 是否已经支付费用
	@Column
	private String isRefund;// 服务是否完成
	@Column
	private String checkStationName;// 检车站名称
	@Column
	private String typeState;// 状态
	@Column
	private Long insertTime;// 插入时间
	@Column
	private Long upTime;// 完成时间

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

	public String getCarCode() {
		return carCode;
	}

	public void setCarCode(String carCode) {
		this.carCode = carCode;
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

	public String getCarLicence() {
		return carLicence;
	}

	public void setCarLicence(String carLicence) {
		this.carLicence = carLicence;
	}

	public String getCarInsurance() {
		return carInsurance;
	}

	public void setCarInsurance(String carInsurance) {
		this.carInsurance = carInsurance;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderProject() {
		return orderProject;
	}

	public void setOrderProject(String orderProject) {
		this.orderProject = orderProject;
	}

	public int getPayWay() {
		return payWay;
	}

	public void setPayWay(int payWay) {
		this.payWay = payWay;
	}


	public String getPayFee() {
		return payFee;
	}

	public void setPayFee(String payFee) {
		this.payFee = payFee;
	}

	public int getIsPayFee() {
		return isPayFee;
	}

	public void setIsPayFee(int isPayFee) {
		this.isPayFee = isPayFee;
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

	public String getTypeState() {
		return typeState;
	}

	public void setTypeState(String typeState) {
		this.typeState = typeState;
	}

	public Long getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Long insertTime) {
		this.insertTime = insertTime;
	}

	public Long getUpTime() {
		return upTime;
	}

	public void setUpTime(Long upTime) {
		this.upTime = upTime;
	}

	

}
