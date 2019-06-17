package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
/**
 * 下单用户记录表
 * @author Administrator
 *
 */
@Entity
@Table(name="t_ordernickname")
public class OrderNickName {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String orderUserId;// 预约人ID-》微信号
	@Column
	private String orderUserNickName;// 预约人微信昵称
	@Column
	private String customerPic;// 头像
	@Column
	private String carOwnerName;// 车主姓名
	@Column
	private String carOwnerMobile;// 车主电话
	@Column
	private String checkStationName;// 检车站名称
	@Column
	private Long insertTime;// 插入时间
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
	public String getOrderUserNickName() {
		return orderUserNickName;
	}
	public void setOrderUserNickName(String orderUserNickName) {
		this.orderUserNickName = orderUserNickName;
	}
	public String getCustomerPic() {
		return customerPic;
	}
	public void setCustomerPic(String customerPic) {
		this.customerPic = customerPic;
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
	public String getCheckStationName() {
		return checkStationName;
	}
	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}
	public Long getInsertTime() {
		return insertTime;
	}
	public void setInsertTime(Long insertTime) {
		this.insertTime = insertTime;
	}
	
}
