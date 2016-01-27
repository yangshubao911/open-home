package com.shihui.openpf.home.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by zhoutc on 2016/1/26.
 */
@Entity(name="merchant_coverage")
public class MerchantCoverage implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @JSONField(name="coverage_id")
    @Column(name="coverage_id")
    private Integer coverageId;

    @JSONField(name="merhcant_id")
    @Column(name="merhcant_id")
    private Integer merchantId;

    @JSONField(name="city_id")
    @Column(name="city_id")
    private Integer cityId;

    @JSONField(name="city_name")
    @Column(name="city_name")
    private String cityName;

    @JSONField(name="district_id")
    @Column(name="district_id")
    private Integer districtId;

    @JSONField(name="district_name")
    @Column(name="district_name")
    private String districtName;

    @JSONField(name="plate_id")
    @Column(name="plate_id")
    private Integer plateId;

    @JSONField(name="plate_name")
    @Column(name="plate_name")
    private String plateName;

    @JSONField(name="m_s_id")
    @Column(name="m_s_id")
    private Integer m_s_id;

    @JSONField(name="service_id")
    @Column(name="service_id")
    private Integer serviceId;

    public Integer getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(Integer coverageId) {
        this.coverageId = coverageId;
    }

    public Integer getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Integer merchantId) {
        this.merchantId = merchantId;
    }

    public Integer getCityId() {
        return cityId;
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

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public Integer getPlateId() {
        return plateId;
    }

    public void setPlateId(Integer plateId) {
        this.plateId = plateId;
    }

    public String getPlateName() {
        return plateName;
    }

    public void setPlateName(String plateName) {
        this.plateName = plateName;
    }

    public Integer getM_s_id() {
        return m_s_id;
    }

    public void setM_s_id(Integer m_s_id) {
        this.m_s_id = m_s_id;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }
}
