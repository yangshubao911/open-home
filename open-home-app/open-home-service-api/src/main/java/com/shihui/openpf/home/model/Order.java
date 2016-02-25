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
	private Long orderId;

	@Column(name="campaign_id")
	private Integer campaignId;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="extend")
	private String extend;

	@Column(name="gid")
	private Long gid;

	@Column(name="goods_id")
	private Integer goodsId;

	@Column(name="goods_num")
	private Integer goodsNum;

	@Column(name="goods_version")
	private Integer goodsVersion;

	@Column(name="merchant_id")
	private Integer merchantId;

	@Column(name="order_status")
	private Byte orderStatus;

	@Column(name="pay")
	private String pay;

	@Column(name="payment_type")
	private Integer paymentType;

	@Column(name="price")
	private String price;

	@Column(name="sh_off_set")
	private String shOffSet;

	@Column(name="update_time")
	private Date updateTime;

	@Column(name="user_id")
	private Long userId;

	@Column(name="service_id")
	private Integer service_id;

	@Column(name="remark")
	private String remark;

	@Column(name="phone")
	private String phone;

	public Order() {
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getCampaignId() {
		return campaignId;
	}

	public void setCampaignId(Integer campaignId) {
		this.campaignId = campaignId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public Integer getGoodsVersion() {
		return goodsVersion;
	}

	public void setGoodsVersion(Integer goodsVersion) {
		this.goodsVersion = goodsVersion;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Byte getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Byte orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getPay() {
		return pay;
	}

	public void setPay(String pay) {
		this.pay = pay;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getShOffSet() {
		return shOffSet;
	}

	public void setShOffSet(String shOffSet) {
		this.shOffSet = shOffSet;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getService_id() {
		return service_id;
	}

	public void setService_id(Integer service_id) {
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