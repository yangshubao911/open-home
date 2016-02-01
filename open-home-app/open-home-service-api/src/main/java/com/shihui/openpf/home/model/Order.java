package com.shihui.openpf.home.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the order database table.
 * 
 */
@Entity(name="order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="order_id")
	private long orderId;

	@Column(name="campaign_id")
	private int campaignId;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="extend")
	private String extend;

	@Column(name="gid")
	private int gid;

	@Column(name="goods_id")
	private int goodsId;

	@Column(name="goods_num")
	private int goodsNum;

	@Column(name="goods_version")
	private int goodsVersion;

	@Column(name="merchant_id")
	private int merchantId;

	@Column(name="order_status")
	private byte orderStatus;

	@Column(name="pay")
	private String pay;

	@Column(name="payment_type")
	private int paymentType;

	@Column(name="price")
	private String price;

	@Column(name="sh_off_set")
	private String shOffSet;

	@Column(name="update_time")
	private Date updateTime;

	@Column(name="user_id")
	private int userId;

	@Column(name="service_id")
	private int service_id;

	@Column(name="remark")
	private String remark;

	@Column(name="phone")
	private String phone;

	public Order() {
	}

	public long getOrderId() {
		return this.orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public int getCampaignId() {
		return this.campaignId;
	}

	public void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExtend() {
		return this.extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public int getGid() {
		return this.gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public int getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(int goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsNum() {
		return this.goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public int getGoodsVersion() {
		return this.goodsVersion;
	}

	public void setGoodsVersion(int goodsVersion) {
		this.goodsVersion = goodsVersion;
	}

	public int getMerchantId() {
		return this.merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public byte getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(byte orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPay() {
		return this.pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public int getPaymentType() {
		return this.paymentType;
	}

	public void setPaymentType(int paymentType) {
		this.paymentType = paymentType;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getShOffSet() {
		return this.shOffSet;
	}

	public void setShOffSet(String shOffSet) {
		this.shOffSet = shOffSet;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getUserId() {
		return this.userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getService_id() {
		return service_id;
	}

	public void setService_id(int service_id) {
		this.service_id = service_id;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}