package com.shihui.openpf.home.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.common.enums.OrderTypeEnum;
import com.shihui.commons.mq.RocketProducer;
import com.shihui.commons.mq.annotation.ConsumerConfig;
import com.shihui.commons.mq.api.Consumer;
import com.shihui.commons.mq.api.Topic;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.service.api.OrderService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/3/3.
 */

/**
 * 订单消息消费者
 */
@Component("paymentSuccessConsumer")
@ConsumerConfig(consumerName = "settlementPaymentSuccessConsumer", topic = Topic.UPDATE_ORDER_STATUS)
public class PaymentSuccessConsumer implements Consumer {

    @Resource(name = "openHomeMQProducer")
    RocketProducer openHomeMQProducer;

    @Resource
    OrderService orderService;

    @Override
    public boolean doit(String topic, String tags, String key, String msg) {

        com.shihui.api.order.po.Order order_vo = JSON.parseObject(msg, com.shihui.api.order.po.Order.class);
        long orderId = order_vo.getOrderId();
        OrderStatusEnum status = order_vo.getOrderStatus();
        OrderTypeEnum orderTypeEnum = order_vo.getOrderType();
        if (orderTypeEnum.compareTo(OrderTypeEnum.DoorTDoor) != 0) {
            return true;
        }

        Order order = orderService.queryOrder(orderId);
        if (order == null) {
            return true;
        }


        return true;
    }



}


