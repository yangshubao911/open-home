package com.shihui.openpf.home.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name="request_history")
public class RequestHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name="history_id")
    private Long historyId;

    @Column(name="request_id")
    private String requestId;

    @Column(name="order_id")
    private Long orderId;

    @Column(name="change_time")
    private Date changeTime;

    @Column(name="request_status")
    private Integer requestStatus;

    @Column(name="merhcant_id")
    private Integer merchantId;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Integer getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(Integer requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }
}
