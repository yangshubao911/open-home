package com.shihui.openpf.home.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name = "request_history")
public class RequestHistory implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long historyId;
	private String requestId;
	private Date changeTime;
	private Integer requestStatus;
	private String statusName;
	private String serverName;
	private String serverPhone;
	private String comment;
	private String serviceStartTime;

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

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getServiceStartTime() {
		return serviceStartTime;
	}

	public void setServiceStartTime(String serviceStartTime) {
		this.serviceStartTime = serviceStartTime;
	}

}
