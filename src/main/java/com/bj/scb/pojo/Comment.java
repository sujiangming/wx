package com.bj.scb.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 评价表
 * @author JDRY-SJM
 *
 */
@Entity
@Table(name="t_comment")
public class Comment {
	@Id
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	@GeneratedValue(generator = "system-uuid")
	@Column
	private String id;// 主键
	@Column
	private String commentUserId;// 评价人ID
	@Column
	private String commentUserName;// 评价人姓名
	@Column
	private String commentUserScore;// 评价分数  差 中 好
	@Column
	private String commentUserContent;// 评价车站
	@Column
	private String commentOrder;// 评价订单号
	@Column
	private String commentStatus;// 评价状态
	@Column
	private String commentTime;// 评价时间
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCommentUserId() {
		return commentUserId;
	}
	public void setCommentUserId(String commentUserId) {
		this.commentUserId = commentUserId;
	}
	public String getCommentUserName() {
		return commentUserName;
	}
	public void setCommentUserName(String commentUserName) {
		this.commentUserName = commentUserName;
	}
	public String getCommentUserScore() {
		return commentUserScore;
	}
	public void setCommentUserScore(String commentUserScore) {
		this.commentUserScore = commentUserScore;
	}
	public String getCommentUserContent() {
		return commentUserContent;
	}
	public void setCommentUserContent(String commentUserContent) {
		this.commentUserContent = commentUserContent;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	public String getCommentOrder() {
		return commentOrder;
	}
	public void setCommentOrder(String commentOrder) {
		this.commentOrder = commentOrder;
	}
	public String getCommentStatus() {
		return commentStatus;
	}
	public void setCommentStatus(String commentStatus) {
		this.commentStatus = commentStatus;
	}
	
}
