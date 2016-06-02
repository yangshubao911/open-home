package com.shihui.openpf.home.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhoutc on 2015/11/5.
 */
public enum YjzOrderStatusEnum {
    UnPay(1,"待付款",1),
    OrderUnConfirm(2,"待确认",5),
    OrderConfirmed(9,"已确认",15),
   // OrderServiceStart(15,"服务开始"),
    OrderComplete(18,"已完成",20),
    OrderCancel(20,"取消",30);


    private Integer value;
    private String name;
    private Integer serverValue;

    YjzOrderStatusEnum(Integer value, String name, Integer serverValue) {
        this.value = value;
        this.name = name;
        this.serverValue = serverValue;
    }

    private static Map<Integer, YjzOrderStatusEnum> values = new HashMap<>();
    private static Map<Integer, YjzOrderStatusEnum> serverValues = new HashMap<>();
    static{
        for(YjzOrderStatusEnum yjzOrderStatusEnum: YjzOrderStatusEnum.values()){
            values.put(yjzOrderStatusEnum.value,yjzOrderStatusEnum);
            serverValues.put(yjzOrderStatusEnum.serverValue,yjzOrderStatusEnum);
        }
    }

    public static YjzOrderStatusEnum parse(Integer value){
        return values.get(value);
    }

    public static YjzOrderStatusEnum parseServerValues(Integer value){
        return serverValues.get(value);
    }

    public Integer getValue(){return value;}
    public String getName(){return name;}
    public Integer getServerValue() {
        return serverValue;
    }

}
