package com.bj.scb.pojo;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 客户信息表
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_customer_info")
public class CustomerInfo {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键

	@Column
	private String customerId;// 微信号
	@Column
	private String customerName;// 名称
	@Column
	private String customerNickName;// 昵称
	@Column
	private String customerPic;// 头像
	@Column
	private String customerMobile;// 手机号
	@Column
	private String customerGender;// 性别
	@Column
	private String customerAddress;// 地址
	@Column
	private String isInnerPeople; //是否是内部人员

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerNickName() {
		return customerNickName;
	}

	public void setCustomerNickName(String customerNickName) {
		this.customerNickName = customerNickName;
	}

	public String getCustomerPic() {
		return customerPic;
	}

	public void setCustomerPic(String customerPic) {
		this.customerPic = customerPic;
	}

	public String getCustomerMobile() {
		return customerMobile;
	}

	public void setCustomerMobile(String customerMobile) {
		this.customerMobile = customerMobile;
	}

	public String getCustomerGender() {
		return customerGender;
	}

	public void setCustomerGender(String customerGender) {
		this.customerGender = customerGender;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getIsInnerPeople() {
		return isInnerPeople;
	}

	public void setIsInnerPeople(String isInnerPeople) {
		this.isInnerPeople = isInnerPeople;
	}

	
}
