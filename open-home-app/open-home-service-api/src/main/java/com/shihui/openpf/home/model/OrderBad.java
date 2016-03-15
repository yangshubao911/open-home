package com.shihui.openpf.home.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * The persistent class for the order database table.
 * 
 */
@Entity(name = "order_bad")
public class OrderBad implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "order_id")
	private Long orderId;

	@Column(name = "order_status")
	private Integer orderStatus;

	@JSONField(name = "update_time", format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "update_time")
	private Date updateTime;

	@JSONField(name = "create_time", format = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "create_time")
	private Date createTime;

	@JSONField(name = "bad_comment")
	@Column(name = "bad_comment")
	private String badComment;

	public OrderBad() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBadComment() {
		return badComment;
	}

	public void setBadComment(String badComment) {
		this.badComment = badComment;
	}

}