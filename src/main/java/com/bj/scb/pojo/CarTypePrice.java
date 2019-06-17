package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 车型价格表
 * 
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_car_type_price")
public class CarTypePrice {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String carType;// 车型
	@Column
	private String carStandard;// 车型标准
	@Column
	private String checkStationName;// 检车站名称
	@Column
	private String deposit;// 定金
	@Column
	private String carPrice;// 价格

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCarType() {
		return carType;
	}

	public void setCarType(String carType) {
		this.carType = carType;
	}
	
	

	public String getCarStandard() {
		return carStandard;
	}

	public void setCarStandard(String carStandard) {
		this.carStandard = carStandard;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(String carPrice) {
		this.carPrice = carPrice;
	}

	public String getCheckStationName() {
		return checkStationName;
	}

	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}

}
