package com.shihui.openpf.home.model;

/**
 * Created by zhoutc on 2016/1/21.
 */
public class OrderForm {

    private int serviceId;
    private long groupId;
    private long userId;
    private long goodsId;
    private int categoryId;
    private int costSh;
    private String actPay;
    private String actOffset;
    private String serviceTime;
    private String longitude;
    private String latitude;
    private String contactName;
    private String serviceAddress;
    private String detailAddress;
    private String servicePhone;
    private int payType;
    private String merchants;
    private String remark;

    public OrderForm() {
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCostSh() {
        return costSh;
    }

    public void setCostSh(int costSh) {
        this.costSh = costSh;
    }

    public String getActPay() {
        return actPay;
    }

    public void setActPay(String actPay) {
        this.actPay = actPay;
    }

    public String getActOffset() {
        return actOffset;
    }

    public void setActOffset(String actOffset) {
        this.actOffset = actOffset;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public String getMerchants() {
        return merchants;
    }

    public void setMerchants(String merchants) {
        this.merchants = merchants;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
