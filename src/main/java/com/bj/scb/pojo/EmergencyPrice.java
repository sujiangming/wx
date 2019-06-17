package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @category 应急救援价格表
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_emergency_price")
public class EmergencyPrice {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private Integer type;// 车型
	@Column
	private String name;// 车型名称
	@Column
	private Double unitPrice;// 单价
	@Column
	private Double deposit;// 定金
	@Column
	private Double startPrice;// 起步价
	@Column
	private Integer startPriceKilometer;// 起步价公里数
	@Column
	private String startPriceDesc;// 起步价描述
	@Column
	private String checkStationName;// 检车站名称
	@Column
	private String createTime;// 创建时间
	@Column
	private String updateTime;// 更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Double getDeposit() {
		return deposit;
	}

	public void setDeposit(Double deposit) {
		this.deposit = deposit;
	}

	public Double getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}

	public Integer getStartPriceKilometer() {
		return startPriceKilometer;
	}

	public void setStartPriceKilometer(Integer startPriceKilometer) {
		this.startPriceKilometer = startPriceKilometer;
	}

	public String getStartPriceDesc() {
		return startPriceDesc;
	}

	public void setStartPriceDesc(String startPriceDesc) {
		this.startPriceDesc = startPriceDesc;
	}

	public String getCheckStationName() {
		return checkStationName;
	}

	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
