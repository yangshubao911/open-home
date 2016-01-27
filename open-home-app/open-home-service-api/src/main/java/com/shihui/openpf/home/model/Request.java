package com.shihui.openpf.home.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name="request")
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="request_id")
    private long requestId;

    @Column(name="order_id")
    private long orderId;

    @Column(name="merchant_id")
    private Date merchantId;

    @Column(name="create_time")
    private Date createTime;

    @Column(name="update_time")
    private Date updateTime;

    @Column(name="request_status")
    private Integer requestStatus;

    @Column(name="service_id")
    private Integer serviceId;


    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public Date getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Date merchantId) {
        this.merchantId = merchantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
}
