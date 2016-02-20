package com.shihui.openpf.home.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name="merchant_goods")
public class MerchantGoods implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JSONField(name="goods_id")
    @Column(name="goods_id")
    private Integer goodsId;

    private Integer status;

    @JSONField(name="settlement")
    @Column(name="settlement")
    private String settlement;
    @Id
    @JSONField(name="merchant_id")
    @Column(name="merchant_id")
    private Integer merchantId;
    @Id
    @JSONField(name="service_id")
    @Column(name="service_id")
    private Integer serviceId;

    @JSONField(name="category_id")
    @Column(name="category_id")
    private Integer categoryId;


    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getSettlement() {
        return settlement;
    }

    public void setSettlement(String settlement) {
        this.settlement = settlement;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }
}
