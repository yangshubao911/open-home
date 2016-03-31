package com.shihui.openpf.home.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoutc on 2016/3/8.
 */
public enum  OrderMappingEnum {

    OrderUnpaid(10, 1 ,"待支付"),
    OrderCancelByCustom(20, 30 , "用户取消订单"),
    OrderCloseByOutTime(30, 30 ,"订单超时关闭"),
    OrderUnStockOut(60, 5 , "待出库"),
    OrderCancelStockOut(70, 30 ,"取消出库"),
    OrderDistribute(80,15, "配送中"),
    OrderHadReceived(90,20, "已确认收货"),
    PaiedCancel(95, 30 ,"已支付取消"),
    BackClose(100, 30,"后台关闭");

    private Integer value;
    private String name;
    private Integer homeValue;



    OrderMappingEnum(Integer value,  Integer homeValue, String name) {
        this.value = value;
        this.name = name;
        this.homeValue = homeValue;
    }

    private static Map<Integer, OrderMappingEnum> values = new HashMap<>();

    static{
        for(OrderMappingEnum orderMappingEnum: OrderMappingEnum.values()){
            values.put(orderMappingEnum.value,orderMappingEnum);
        }
    }

    public Integer getValue(){return value;}
    public String getName(){return name;}

    public static OrderMappingEnum parse(Integer value){
        return values.get(value);
    }

    public Integer getHomeValue() {
        return homeValue;
    }

    public void setHomeValue(Integer homeValue) {
        this.homeValue = homeValue;
    }
}
