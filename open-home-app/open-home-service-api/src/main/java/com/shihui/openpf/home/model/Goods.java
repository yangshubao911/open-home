package com.shihui.openpf.home.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;


/**
 * The persistent class for the goods database table.
 *
 */
@Entity(name="goods")
public class Goods implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@JSONField(name="goods_id")
	@Column(name="goods_id")
	private Long goodsId;

	@JSONField(name="")
	@Column(name="category_id")
	private Integer categoryId;

	@JSONField(name="city_id")
	@Column(name="city_id")
	private Integer cityId;
	
	@JSONField(name = "city_name")
	@Column(name="city_name")
	private String cityName;

	@JSONField(name="create_time", format="yyyy-MM-dd HH:mm:ss")
	@Column(name="create_time")
	private Date createTime;

	@JSONField(name="goods_desc")
	@Column(name="goods_desc")
	private String goodsDesc;

	@JSONField(name="goods_name")
	@Column(name="goods_name")
	private String goodsName;

	@JSONField(name="goods_status")
	@Column(name="goods_status")
	private Integer goodsStatus;

	@JSONField(name="goods_version")
	@Column(name="goods_version")
	private Integer goodsVersion;

	@JSONField(name="img_url")
	@Column(name="img_url")
	private String imgUrl;

	private String price;

	@JSONField(name="service_id")
	@Column(name="service_id")
	private Integer serviceId;

	@JSONField(name="sh_off_set")
	@Column(name="sh_off_set")
	private String shOffSet;

	@JSONField(name="update_time", format="yyyy-MM-dd HH:mm:ss")
	@Column(name="update_time")
	private Date updateTime;

	public Goods() {
	}

	public Long getGoodsId() {
		return this.goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getGoodsDesc() {
		return this.goodsDesc;
	}

	public void setGoodsDesc(String goodsDesc) {
		this.goodsDesc = goodsDesc;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getGoodsStatus() {
		return this.goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Integer getGoodsVersion() {
		return this.goodsVersion;
	}

	public void setGoodsVersion(Integer goodsVersion) {
		this.goodsVersion = goodsVersion;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getPrice() {
		return this.price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public Integer getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

}