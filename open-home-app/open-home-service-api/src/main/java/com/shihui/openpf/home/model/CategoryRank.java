/**
 * 
 */
package com.shihui.openpf.home.model;

import javax.persistence.Id;

/**
 * @author zhouqisheng
 *
 */
public class CategoryRank {
	@Id
	private Integer serviceId;
	@Id
	private Integer categoryId;
	private Integer rank;
	
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
	public Integer getRank() {
		return rank;
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}

}
