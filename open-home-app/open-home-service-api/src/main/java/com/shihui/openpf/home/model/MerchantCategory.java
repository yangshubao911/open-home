package com.shihui.openpf.home.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name="merchant_category")
public class MerchantCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JSONField(name="merchant_id")
    @Column(name="merchant_id")
    private Integer merchantId;

    @Id
    @JSONField(name="category_id")
    @Column(name="category_id")
    private Integer categoryId;

    private Integer status;

    @Id
    @JSONField(name="service_id")
    @Column(name="service_id")
    private Integer serviceId;



    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
