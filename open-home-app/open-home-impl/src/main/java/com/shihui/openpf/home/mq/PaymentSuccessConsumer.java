package com.shihui.openpf.home.mq;

import javax.annotation.Resource;

import com.shihui.openpf.home.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.common.enums.OrderTypeEnum;
import com.shihui.commons.mq.RocketProducer;
import com.shihui.commons.mq.annotation.ConsumerConfig;
import com.shihui.commons.mq.api.Consumer;
import com.shihui.commons.mq.api.Topic;
import com.shihui.openpf.common.model.MerchantApiName;
import com.shihui.openpf.home.service.api.OrderService;
import com.shihui.openpf.home.service.api.RequestService;

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
	private RocketProducer openHomeMQProducer;

	@Resource(name = "openOrderService")
	private OrderService orderService;

	@Resource
	private RequestService requestService;

	@Override
	public boolean doit(String topic, String tags, String key, String msg) {
		try {
			JSONObject jo = JSONObject.parseObject(msg);
			JSONObject orderJo = jo.getJSONObject("order");
			if (orderJo == null) {
				log.warn("订单消息无法处理，msg={}", msg);
				return true;
			}

			com.shihui.api.order.po.Order order_vo = jo.getObject("order", com.shihui.api.order.po.Order.class);
			long orderId = order_vo.getOrderId();
			OrderStatusEnum status = order_vo.getOrderStatus();
			OrderTypeEnum orderTypeEnum = order_vo.getOrderType();
			if (orderTypeEnum.compareTo(OrderTypeEnum.DoorTDoor) != 0) {
				return true;
			}

			Order order = orderService.queryOrder(orderId);
			if (order == null) {
				return true;
			} else {
				Request request = requestService.queryOrderRequest(orderId);


				// 更新订单状态
				orderService.updateOrder(orderId, status);

				/*Request request_update =new Request();
				request.setRequestId(request.getRequestId());
				request.setMerchantId(request.getMerchantId());

				int third_status = OrderMappingEnum.parse(status.getValue()).getValue();
				request.setRequestStatus(third_status);
				requestService.updateStatus(request_update);*/

				if (status == OrderStatusEnum.OrderCancelByCustom || status == OrderStatusEnum.OrderCloseByOutTime
						|| status == OrderStatusEnum.BackClose) {// 取消订单
					HomeMQMsg homeMsg = new HomeMQMsg();
					homeMsg.setGoodsId(order.getGoodsId());
					homeMsg.setMerchantId(order.getMerchantId());
					homeMsg.setOrderId(orderId);
					homeMsg.setPrice(order.getPrice());
					homeMsg.setServiceId(order.getService_id());
					homeMsg.setMerchantApiName(MerchantApiName.CANCEL_ORDER);
					homeMsg.setThirdOrderId(request.getRequestId());

					return openHomeMQProducer.send(Topic.Open_Home_Pay_Notice, String.valueOf(orderId),
							JSON.toJSONString(homeMsg));

				} else if (status == OrderStatusEnum.OrderUnStockOut) {// 支付通知
					HomeMQMsg homeMsg = new HomeMQMsg();
					homeMsg.setGoodsId(order.getGoodsId());
					homeMsg.setMerchantId(order.getMerchantId());
					homeMsg.setOrderId(orderId);
					homeMsg.setPrice(order.getPrice());
					homeMsg.setServiceId(order.getService_id());
					homeMsg.setMerchantApiName(MerchantApiName.PAY_NOTICE);
					homeMsg.setThirdOrderId(request.getRequestId());

					return openHomeMQProducer.send(Topic.Open_Home_Pay_Notice, String.valueOf(orderId),
							JSON.toJSONString(homeMsg));
				} else {
					return true;// 默认消息正确处理
				}
			}
		} catch (Exception e) {
			log.error("处理mq订单消息异常, msg={}", msg, e);
		}

		return false;
	}

}
