/**
 * 
 */
package com.shihui.openpf.home.model;

import java.io.Serializable;

import com.shihui.openpf.common.model.MerchantApiName;

/**
 * @author zhouqisheng
 * @date 2016年3月4日 上午11:55:59
 *
 */
public class HomeMQMsg implements Serializable {

	private static final long serialVersionUID = -6717734006185743786L;

	private Integer merchantId;
	private Long goodsId;
	private String price;
	private Long orderId;
	private Integer serviceId;
	private MerchantApiName merchantApiName;//商户接口名称

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public MerchantApiName getMerchantApiName() {
		return merchantApiName;
	}

	public void setMerchantApiName(MerchantApiName merchantApiName) {
		this.merchantApiName = merchantApiName;
	}

}
