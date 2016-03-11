/**
 * 
 */
package com.shihui.openpf.home.mq;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.shihui.commons.mq.RocketProducer;
import com.shihui.commons.mq.annotation.ConsumerConfig;
import com.shihui.commons.mq.api.Consumer;
import com.shihui.commons.mq.api.Topic;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantApiName;
import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.model.HomeMQMsg;
import com.shihui.openpf.home.model.HomeResponse;

/**
 * @author zhouqisheng
 * @date 2016年3月4日 下午5:56:53
 *
 */
@Component("homeMsgRetryConsumer")
@ConsumerConfig(consumerName = "homeMsgRetryConsumer", topic = Topic.Open_Home_Pay_Notice_Retry)
public class HomeMsgRetryConsumer implements Consumer {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Resource
	private HomeServProviderService homeServProviderService;
	@Resource(name = "openHomeMQProducer")
	private RocketProducer openHomeMQProducer;
	@Resource
	private MerchantManage merchantManage;
	
	@Value("${mq.home.msg.retry_wait_second}")
	private int waitTime = 5;//单位秒
	
	/* (non-Javadoc)
	 * @see com.shihui.commons.mq.api.Consumer#doit(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean doit(String topic, String tags, String key, String msg) {
		HomeResponse response = null;
		
		try {
			HomeMQMsg homeMsg = JSON.parseObject(msg, HomeMQMsg.class);
			if(System.currentTimeMillis() - homeMsg.getTimestamp().getTime() < waitTime * 1000){
				//若间隔时间未到不执行请求
				TimeUnit.MILLISECONDS.sleep(1000);
				return openHomeMQProducer.send(Topic.Open_Home_Pay_Notice_Retry, String.valueOf(homeMsg.getOrderId()), JSON.toJSONString(homeMsg));
			}
			
			
			Merchant merchant = merchantManage.getById(homeMsg.getMerchantId());
			if(homeMsg.getMerchantApiName() == MerchantApiName.CANCEL_ORDER){
				response = homeServProviderService.cancelOrder(merchant, homeMsg.getServiceId(), homeMsg.getThirdOrderId());
			}else if(homeMsg.getMerchantApiName() == MerchantApiName.PAY_NOTICE){
				response = homeServProviderService.payNotice(merchant, homeMsg.getServiceId(), homeMsg.getThirdOrderId(), homeMsg.getPrice());
			}else{
				log.warn("MQ消息没有对应处理方式，消息队列名称：{}，消息：{}", Topic.Open_Home_Pay_Notice.name(), msg);
				return false;
			}
			
			if(response != null){
				if(response.getCode() == 0){
					log.info("订单处理调用第三方接口成功，order_id={}, merchant_id={}, merchant_api_name={}", homeMsg.getOrderId(), homeMsg.getMerchantId(), homeMsg.getMerchantApiName().name());
					return true;
				}else{
					log.warn("订单处理调用第三方接口失败，order_id={}, merchant_id={}, merchant_api_name={}, 返回信息：{}", homeMsg.getOrderId(), homeMsg.getMerchantId(), homeMsg.getMerchantApiName().name(), response);
					//加入重试队列重试
					Date now = new Date();
					homeMsg.setTimestamp(now);
					return openHomeMQProducer.send(Topic.Open_Home_Pay_Notice_Retry, String.valueOf(homeMsg.getOrderId()), JSON.toJSONString(homeMsg));
				}
			} else {
				log.error("订单处理调用第三方接口失败，未返回信息，order_id={}, merchant_id={}, merchant_api_name={}", homeMsg.getOrderId(), homeMsg.getMerchantId(), homeMsg.getMerchantApiName().name());
			}
		} catch (Exception e) {
			log.error("MQ消息处理异常，消息队列名称：{}，消息：{}", Topic.Open_Home_Pay_Notice.getValue(), msg, e);
			return false;
		}
		return false;
	}

}
