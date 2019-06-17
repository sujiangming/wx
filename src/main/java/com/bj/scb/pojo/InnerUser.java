package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 内部人员信息表
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name = "t_inner_user")
public class InnerUser {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键

	@Column
	private String innerUserId;// 微信号
	@Column
	private String innerUserPic;// 头像
	@Column
	private String innerUserName;// 姓名
	@Column
	private String innerUserGender;// 性别
	@Column
	private String innerUserMobile;// 手机号
	@Column
	private String innerUserPwd;// 密码
	@Column
	private String innerUserPosition;// 岗位
	@Column
	private String innerUserRole;// 角色
	@Column
	private String checkStationId;// 所属检查站ID
	@Column
	private String checkStationName;// 所属检查站名称
	@Column
	private Boolean isManager;// 是否是管理员
	@Column
	private Boolean isSuperManager;// 是否是超级管理员  只有一个，事先指定的
	
	public String getCheckStationId() {
		return checkStationId;
	}
	public void setCheckStationId(String checkStationId) {
		this.checkStationId = checkStationId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInnerUserId() {
		return innerUserId;
	}
	public void setInnerUserId(String innerUserId) {
		this.innerUserId = innerUserId;
	}
	public String getInnerUserPic() {
		return innerUserPic;
	}
	public void setInnerUserPic(String innerUserPic) {
		this.innerUserPic = innerUserPic;
	}
	public String getInnerUserName() {
		return innerUserName;
	}
	public void setInnerUserName(String innerUserName) {
		this.innerUserName = innerUserName;
	}
	public String getInnerUserGender() {
		return innerUserGender;
	}
	public void setInnerUserGender(String innerUserGender) {
		this.innerUserGender = innerUserGender;
	}
	public String getInnerUserMobile() {
		return innerUserMobile;
	}
	public void setInnerUserMobile(String innerUserMobile) {
		this.innerUserMobile = innerUserMobile;
	}
	
	public String getInnerUserPwd() {
		return innerUserPwd;
	}
	public void setInnerUserPwd(String innerUserPwd) {
		this.innerUserPwd = innerUserPwd;
	}
	public String getInnerUserPosition() {
		return innerUserPosition;
	}
	public void setInnerUserPosition(String innerUserPosition) {
		this.innerUserPosition = innerUserPosition;
	}
	public String getInnerUserRole() {
		return innerUserRole;
	}
	public void setInnerUserRole(String innerUserRole) {
		this.innerUserRole = innerUserRole;
	}
	
	public String getCheckStationName() {
		return checkStationName;
	}
	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}
	public boolean isManager() {
		return isManager;
	}
	public void setManager(boolean isManager) {
		this.isManager = isManager;
	}
	public boolean isSuperManager() {
		return isSuperManager;
	}
	public void setSuperManager(boolean isSuperManager) {
		this.isSuperManager = isSuperManager;
	}
}
