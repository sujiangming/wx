package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @author JDRY-SJM
 * @category 时间段及其对应的人数表
 */
@Entity
@Table(name = "t_time_count")
public class TimeCount {
	@Id
	@Column(unique = true,nullable = false)
	private String id;// 主键
	@Column
	private String orderTime;// 预约时间段
	@Column
	private int orderCount;// 预约人数
	@Column
	private String checkStationName;// 所属检车站的名字
	@Column
	private int orderStartTime;// 预约时间段开始时间
	@Column
	private String updateTime;// 更新时间

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public String getCheckStationName() {
		return checkStationName;
	}

	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}

	public int getOrderStartTime() {
		return orderStartTime;
	}

	public void setOrderStartTime(int orderStartTime) {
		this.orderStartTime = orderStartTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
