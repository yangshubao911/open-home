package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.common.model.PaymentTypeEnum;
import com.shihui.api.oms.sale.model.OrderPaymentMapping;
import com.shihui.api.payment.model.Payment;
import com.shihui.openpf.home.api.OrderManage;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderForm;
import com.shihui.openpf.home.service.api.GoodsService;
import com.shihui.openpf.home.service.api.OrderDubboService;
import com.shihui.openpf.home.service.api.OrderService;
import me.weimi.api.commons.context.RequestContext;

import org.joda.time.DateTime;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoutc on 2016/1/21.
 */
public class OrderManageImpl implements OrderManage {

    @Resource
    OrderService orderService;

    @Resource
    OrderDubboService orderDubboService;

    @Resource
    GoodsService goodsService;


    /**
     * 客户端创建订单
     *
     * @param json 创建订单请求数据
     * @return 返回结果
     */
    @Override
    public String createOrder(RequestContext rc, String json) {
        OrderForm orderForm = JSON.parseObject(json, OrderForm.class);


        return null;
    }

    /**
     * OPS查询订单
     *
     * @param queryOrder 查询订单条件
     * @return 返回结果
     */
    @Override
    public String queryOrderList(RequestContext rc,Order queryOrder,Long startTime , Long endTime , int page, int size) {
        JSONObject result = new JSONObject();
        JSONArray orders_json = new JSONArray();
      //  Order queryOrder = JSON.parseObject(json, Order.class);
        int total = orderService.countQueryOrder(queryOrder,startTime,endTime);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        if (total <= 0) return result.toJSONString();
        List<Order> orderList = orderService.queryOrderList(queryOrder,startTime,endTime, page, size);

        List<Integer> merchants = new ArrayList<>();
        for (Order order : orderList) {
            merchants.add(order.getMerchantId());
        }

        for (Order order : orderList) {
            JSONObject order_json = new JSONObject();
            order_json.put("orderId", String.valueOf(order.getOrderId()));
            order_json.put("userId", order.getUserId());
            order_json.put("phone", order.getPhone());

            Goods goods = goodsService.findById(order.getGoodsId());
            order_json.put("price", goods.getPrice());
            order_json.put("shOffset", goods.getShOffSet());
            order_json.put("due", new BigDecimal(goods.getPrice()).
                    subtract(new BigDecimal(goods.getShOffSet())).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString());

            order_json.put("merchantId", order.getMerchantId());
            //order_json.put("merchantName", );
            //order_json.put("settlement", merchantGoods.getPrice());
            DateTime dateTime = new DateTime(order.getCreateTime());
            order_json.put("createTime", dateTime.toString("yyyyMMddHHmmss"));
            order_json.put("pay", order.getPay());
            order_json.put("status", order.getOrderStatus());
            orders_json.add(order_json);
        }
        result.put("orders", orders_json);
        return result.toJSONString();
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @return 返回订单详情
     */
    @Override
    public String queryOrder(long orderId) {
        JSONObject result = new JSONObject();
        Order order = orderService.queryOrder(orderId);

        PaymentTypeEnum paymentTypeEnum = null;
        if (order.getPaymentType() == PaymentTypeEnum.Alipay.getValue())
            paymentTypeEnum = PaymentTypeEnum.Alipay;
        if (order.getPaymentType() == PaymentTypeEnum.Wxpay.getValue())
            paymentTypeEnum = PaymentTypeEnum.Wxpay;

        OrderPaymentMapping orderPaymentMapping = orderDubboService.queryPaymentMapping(orderId, paymentTypeEnum);
        result.put("payType", order.getPaymentType());
        if (orderPaymentMapping != null) {
            result.put("transId", orderPaymentMapping.getTransId());
            Payment payment = orderDubboService.queryPayMentInfo(orderPaymentMapping.getTransId());
            if (payment != null) {
                if (payment.getPaymentTime() != null && !"".equals(payment.getPaymentTime())) {
                    DateTime date = new DateTime(payment.getPaymentTime().getTime());
                    result.put("payTime", date.toString("yyyy-MM-dd HH:mm:ss"));
                }
            }

        }
        result.put("createTime", new DateTime(order.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        result.put("userId", order.getUserId());
        result.put("phone", order.getPhone());

        Goods goods = goodsService.findById(order.getGoodsId());
        result.put("price", goods.getPrice());

        //Merchant merchant = rechargeMerchantService.queryMerchant(order.getMerchantid());
        result.put("merchantId", order.getMerchantId());
        // result.put("merchantName", merchant.getMechantname());

        //  MerchantGoods merchantGoods = merchantGoodsService.queryProvider(order.getProviderid());
//        result.put("originalPrice", produce.getDue());
        result.put("pay", order.getPay());
        result.put("shOffset", order.getShOffSet());
        result.put("userStatus", order.getOrderStatus());
        // result.put("userStatusName", order.getStatus().getName());

        return result.toJSONString();
    }

}
