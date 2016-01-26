package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.home.api.OrderManage;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderForm;
import com.shihui.openpf.home.service.api.OrderService;
import me.weimi.api.commons.context.RequestContext;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoutc on 2016/1/21.
 */
public class OrderManageImpl implements OrderManage {

    @Resource
    OrderService orderService;
    /**
     * 客户端创建订单
     * @param json 创建订单请求数据
     *
     * @return 返回结果
     */
    @Override
    public String createOrder(RequestContext rc, String json) {
        OrderForm orderForm = JSON.parseObject(json, OrderForm.class);




        return null;
    }

    /**
     * OPS查询订单
     * @param json 创建订单请求数据
     *
     * @return 返回结果
     */
    @Override
    public String queryOrder(RequestContext rc, String json , int page , int size) {
        JSONObject result = new JSONObject();
        JSONArray orders_json = new JSONArray();
        Order queryOrder = JSON.parseObject(json, Order.class);
        int total = orderService.countQueryOrder(queryOrder);
        result.put("total",total);
        result.put("page",page);
        result.put("size",size);
        if(total<=0) return result.toJSONString();
        List<Order> orderList =  orderService.queryOrder(queryOrder, page , size);

        List<Integer> merchants = new ArrayList<>();
        for(Order order : orderList){
            merchants.add(order.getMerchantId());
        }


        for(Order order : orderList){
            orders_json.add(order);
        }
        result.put("orders",orders_json);
        return result.toJSONString();
    }
}
