package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 检车站表
 * 
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_check_station")
public class CheckStation {

	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String checkStationName;// 站点名称
	@Column
	private String checkStationAddress;// 站点地址
	@Column
	private Double checkStationX;// 站点经度
	@Column
	private Double checkStationY;// 站点纬度
	@Column
	private String type;// 站点类型

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

	public Double getCheckStationY() {
		return checkStationY;
	}

	public void setCheckStationY(Double checkStationY) {
		this.checkStationY = checkStationY;
	}

	public void setCheckStationX(Double checkStationX) {
		this.checkStationX = checkStationX;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
