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
    @JSONField(name="m_s_c_id")
    @Column(name="m_s_c_id")
    private Integer m_s_c_id;

    @JSONField(name="merchant_id")
    @Column(name="merchant_id")
    private Integer merchantId;

    @JSONField(name="category_id")
    @Column(name="category_id")
    private Integer categoryId;

    @JSONField(name="m_s_c_status")
    @Column(name="m_s_c_status")
    private Integer m_s_c_status;

    @JSONField(name="service_id")
    @Column(name="service_id")
    private Integer serviceId;

    @JSONField(name="m_s_id")
    @Column(name="m_s_id")
    private Integer m_s_id;

    public Integer getM_s_c_id() {
        return m_s_c_id;
    }

    public void setM_s_c_id(Integer m_s_c_id) {
        this.m_s_c_id = m_s_c_id;
    }

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

    public Integer getM_s_c_status() {
        return m_s_c_status;
    }

    public void setM_s_c_status(Integer m_s_c_status) {
        this.m_s_c_status = m_s_c_status;
    }



    public Integer getM_s_id() {
        return m_s_id;
    }

    public void setM_s_id(Integer m_s_id) {
        this.m_s_id = m_s_id;
    }
}
