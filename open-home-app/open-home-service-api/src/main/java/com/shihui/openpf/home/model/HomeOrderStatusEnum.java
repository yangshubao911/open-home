package com.shihui.openpf.home.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoutc on 2016/3/4.
 */
public enum HomeOrderStatusEnum {

    UnPay(1,"待付款"),
    OrderUnConfirm(5,"待确认"),
    OrderConfirmed(15,"已确认"),
    OrderComplete(20,"已完成"),
    OrderCancel(30,"取消");


    private Integer value;
    private String name;

    HomeOrderStatusEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    private static Map<Integer, HomeOrderStatusEnum> values = new HashMap<>();
    static{
        for(HomeOrderStatusEnum homeOrderStatusEnum: HomeOrderStatusEnum.values()){
            values.put(homeOrderStatusEnum.getValue(),homeOrderStatusEnum);
        }
    }

    public static HomeOrderStatusEnum parse(Integer value){
        return values.get(value);
    }


    public Integer getValue(){return value;}
    public String getName(){return name;}




}
