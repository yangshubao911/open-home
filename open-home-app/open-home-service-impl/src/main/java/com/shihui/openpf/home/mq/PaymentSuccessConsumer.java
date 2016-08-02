package com.shihui.openpf.home.mq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.common.enums.OrderTypeEnum;
import com.shihui.api.order.common.enums.PayTypeEnum;
import com.shihui.api.order.service.OpenService;
import com.shihui.api.order.vo.SimpleResult;
import com.shihui.commons.mq.RocketProducer;
import com.shihui.commons.mq.annotation.ConsumerConfig;
import com.shihui.commons.mq.api.Consumer;
import com.shihui.commons.mq.api.Topic;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.dubbo.api.ServiceManage;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantApiName;
import com.shihui.openpf.common.model.Service;
import com.shihui.openpf.home.cache.GoodsCache;
import com.shihui.openpf.home.http.FastHttpUtils;
import com.shihui.openpf.home.model.Goods;
import com.shihui.openpf.home.model.HomeMQMsg;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderMappingEnum;
import com.shihui.openpf.home.model.Request;
import com.shihui.openpf.home.service.api.GoodsService;
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
	@Resource
	private ServiceManage serviceManage;
	@Resource
	private GoodsService goodsService;
	@Resource
	private MerchantManage merchantManage;
	@Resource
	private GoodsCache goodsCache;
	@Resource
	private OpenService openService;
	
	private CloseableHttpAsyncClient httpClient;
	
	@Value("${app_push_url}")
	private String appPushUrl;
	
	@PostConstruct
	public void init(){
		this.httpClient = FastHttpUtils.getClient(2000);
	}

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
			if (!orderTypeEnum.isOpenPlatformStandardType()) {
				return true;
			}

			Order order = orderService.queryOrder(orderId);
			if (order == null) {
				return true;
			} else {
				log.info("消费订单状态变更消息 topic:"+ topic +",key:"+ key + ",msg:"+ msg);
				
				if(status == OrderStatusEnum.OrderUnStockOut){
					SimpleResult simpleResult = openService.backendOrderDetail(orderId);
					if(simpleResult.getStatus()==1) {
						com.shihui.api.order.po.Order order_vo_detail = (com.shihui.api.order.po.Order) simpleResult.getData();
						PayTypeEnum payType = order_vo_detail.getPayType();
						String transId = String.valueOf(order_vo_detail.getTransId());
						
						//更新订单支付信息
					    Order orderUpdate = new Order();
					    orderUpdate.setOrderId(orderId);
					    orderUpdate.setPaymentType(payType.getValue());
					    orderUpdate.setPayTime(new Date(order_vo_detail.getPaymentTime()));
					    orderUpdate.setTransId(transId);
					    orderUpdate.setUpdateTime(new Date());
					    orderService.update(orderUpdate);
					}

				} else if(status == OrderStatusEnum.OrderHadReceived){
					//更新订单消费时间
				    Order orderUpdate = new Order();
				    orderUpdate.setOrderId(orderId);
				    orderUpdate.setConsumeTime(new Date(order_vo.getLastStatusTime()));
				    orderUpdate.setUpdateTime(new Date());
				    orderService.update(orderUpdate);
				}
				
				Request request = requestService.queryOrderRequest(orderId);
				// 更新订单状态
				orderService.updateOrder(orderId, status);

				Request request_update =new Request();
				request_update.setRequestId(request.getRequestId());
				request_update.setMerchantId(request.getMerchantId());

				int third_status = OrderMappingEnum.parse(status.getValue()).getHomeValue();
				request_update.setRequestStatus(third_status);
				requestService.updateStatus(request_update);

				//推送客户端消息
				if(status == OrderStatusEnum.OrderCancelByCustom || status == OrderStatusEnum.OrderUnStockOut
						|| status == OrderStatusEnum.BackClose || status == OrderStatusEnum.OrderHadReceived
						|| status == OrderStatusEnum.PayedCancel){
					String pushMsg = null;
					Service service = serviceManage.findById(order.getService_id());
					if(service == null){
						log.error("订单处理-push消息：业务信息未查到，serviceId={}, orderId={}, orderStatus={}", order.getService_id(), orderId, order.getOrderStatus());
					} else {
						if(status == OrderStatusEnum.PayedCancel || status == OrderStatusEnum.BackClose){
							pushMsg = "订单(" + orderId + ")已取消，我们已为您办理退款，通常需要1-3个工作日内到账。请耐心等待！";
						}else if(status == OrderStatusEnum.OrderUnStockOut){
							Goods goods = goodsService.findById(order.getGoodsId());
							Merchant merchant = merchantManage.getById(order.getMerchantId());
							
							if(goods == null || merchant == null){
								log.error("订单处理-push消息：商品或者商户信息未查到，serviceId={}, orderId={}, orderStatus={}", order.getService_id(), orderId, order.getOrderStatus());
							}else {
								SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								pushMsg = "尊敬的用户，您在"+ sdf.format(order.getCreateTime())
										+"通过实惠平台预约了" + goods.getGoodsName()
										+"，服务提供方为"+ merchant.getMerchantName()
										+"，如有疑问，请致电实惠客服：4006611388";
							}
							
						}else if(status == OrderStatusEnum.OrderHadReceived){
							pushMsg = "亲，服务已完成，小惠会再接再厉，感谢您的使用。";
						}
					}
					if(pushMsg != null){
						this.pushMsg(pushMsg, order.getUserId(), service.getServiceMerchantCode());
					}
					
				}
				
				
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
					//缓存售出数量
					Goods goods=goodsService.findById(order.getGoodsId());
					goodsCache.increaseSell(goods.getCategoryId());
					
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
	
	/**
	 * push app通知
	 * @param msg
	 * @param userId
	 * @param merchantCode
	 */
	private void pushMsg(String msg, long userId, long merchantCode) {
		try {
			HttpPost httpPost = new HttpPost(appPushUrl);
			httpPost.addHeader("X-Matrix-UID", "1000");
			
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("touid", String.valueOf(userId)));
			nvps.add(new BasicNameValuePair("dataid", "10001"));
			nvps.add(new BasicNameValuePair("data", msg));
			nvps.add(new BasicNameValuePair("fromuid",  String.valueOf(merchantCode)));
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nvps, "utf-8");
			httpPost.setEntity(urlEncodedFormEntity);
			
			FastHttpUtils.executePostReturnString(httpClient, httpPost);
		} catch (Exception e) {
			log.error("push app消息异常", e);
		}
	}

}
