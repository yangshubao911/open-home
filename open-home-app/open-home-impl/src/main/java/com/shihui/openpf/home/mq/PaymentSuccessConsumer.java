package com.shihui.openpf.home.mq;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.common.enums.OrderTypeEnum;
import com.shihui.commons.mq.RocketProducer;
import com.shihui.commons.mq.annotation.ConsumerConfig;
import com.shihui.commons.mq.api.Consumer;
import com.shihui.commons.mq.api.Topic;
import com.shihui.openpf.common.model.MerchantApiName;
import com.shihui.openpf.home.model.HomeMQMsg;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.service.api.OrderService;

/**
 * Created by zhoutc on 2016/3/3.
 */

/**
 * 订单消息消费者
 */
@Component("paymentSuccessConsumer")
@ConsumerConfig(consumerName = "paymentSuccessConsumer", topic = Topic.UPDATE_ORDER_STATUS)
public class PaymentSuccessConsumer implements Consumer {
	private Logger log = LoggerFactory.getLogger(getClass());

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

		try {
			Order order = orderService.queryOrder(orderId);
			if (order == null) {
				return true;
			} else {
				// 更新订单状态
				orderService.updateOrder(orderId, status);
				
				if (status == OrderStatusEnum.OrderCancelByCustom || status == OrderStatusEnum.OrderCloseByOutTime
						|| status == OrderStatusEnum.BackClose) {//取消订单
					HomeMQMsg homeMsg = new HomeMQMsg();
					homeMsg.setGoodsId(order.getGoodsId());
					homeMsg.setMerchantId(order.getMerchantId());
					homeMsg.setOrderId(orderId);
					homeMsg.setPrice(order.getPrice());
					homeMsg.setServiceId(order.getService_id());
					homeMsg.setMerchantApiName(MerchantApiName.CANCEL_ORDER);
					
					return openHomeMQProducer.send(Topic.Open_Home_Pay_Notice, String.valueOf(orderId), JSON.toJSONString(homeMsg));
                    
				} else if(status == OrderStatusEnum.OrderUnStockOut) {//支付通知
					HomeMQMsg homeMsg = new HomeMQMsg();
					homeMsg.setGoodsId(order.getGoodsId());
					homeMsg.setMerchantId(order.getMerchantId());
					homeMsg.setOrderId(orderId);
					homeMsg.setPrice(order.getPrice());
					homeMsg.setServiceId(order.getService_id());
					homeMsg.setMerchantApiName(MerchantApiName.PAY_NOTICE);
					
					return openHomeMQProducer.send(Topic.Open_Home_Pay_Notice, String.valueOf(orderId), JSON.toJSONString(homeMsg));
				} else {
					return true;//默认消息正确处理
				}
			}
		} catch (Exception e) {
			log.error("处理mq订单消息异常, msg={}", msg, e);
		}

		return false;
	}

}