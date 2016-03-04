/**
 * 
 */
package com.shihui.openpf.home.mq;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.shihui.commons.mq.RocketProducer;
import com.shihui.commons.mq.annotation.ConsumerConfig;
import com.shihui.commons.mq.api.Consumer;
import com.shihui.commons.mq.api.Topic;
import com.shihui.openpf.home.api.HomeServProviderService;

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
	RocketProducer openHomeMQProducer;
	
	/* (non-Javadoc)
	 * @see com.shihui.commons.mq.api.Consumer#doit(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean doit(String topic, String tags, String key, String msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
