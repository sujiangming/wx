package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 检车站表
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name="t_check_station")
public class CheckStation {
	
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String checkStationName;// 检车站名称
	@Column
	private String checkStationAddress;// 检车站地址
	@Column
	private Double checkStationX;// 检车站的经度
	
	@Column
	private Double checkStationY;// 检车站的纬度
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCheckStationName() {
		return checkStationName;
	}
	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}
	public String getCheckStationAddress() {
		return checkStationAddress;
	}
	public void setCheckStationAddress(String checkStationAddress) {
		this.checkStationAddress = checkStationAddress;
	}
	public double getCheckStationX() {
		return checkStationX;
	}
	public void setCheckStationX(double checkStationX) {
		this.checkStationX = checkStationX;
	}
	public double getCheckStationY() {
		return checkStationY;
	}
	public void setCheckStationY(double checkStationY) {
		this.checkStationY = checkStationY;
	}
	
}
