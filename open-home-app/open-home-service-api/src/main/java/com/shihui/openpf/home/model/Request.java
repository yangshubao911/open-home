package com.shihui.openpf.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name="request")
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private String requestId;

    private Long orderId;
    @Id
    private Integer merchantId;

    private Date createTime;

    private Date updateTime;

    private Integer requestStatus;

    private Integer serviceId;
    
    private String serverName;
    
    private String serverPhone;

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

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
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

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerPhone() {
		return serverPhone;
	}

	public void setServerPhone(String serverPhone) {
		this.serverPhone = serverPhone;
	}
}
