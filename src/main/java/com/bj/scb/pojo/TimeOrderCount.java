package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 时间人数表
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name="t_time_order_count")
public class TimeOrderCount {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String orderDate;// 预约日期
	@Column
	private String orderTime;// 预约时间段
	@Column
	private String orderCount;// 预约人数
	@Column
	private String checkStationName;//检车站名称 TimeCountId
	@Column
	private String timeCountId;//外键 
	@Column
	private int orderStartTime;// 预约时间段开始时间
	@Column
	private String updateTime;// 更新时间
	
	public String getCheckStationName() {
		return checkStationName;
	}
	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getOrderTime() {
		return orderTime;
	}
	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}
	public String getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(String orderCount) {
		this.orderCount = orderCount;
	}
	public String getTimeCountId() {
		return timeCountId;
	}
	public void setTimeCountId(String timeCountId) {
		this.timeCountId = timeCountId;
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
