/**
 * 
 */
package com.shihui.openpf.home.impl;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.shihui.openpf.common.tools.SignUtil;
import org.apache.commons.beanutils.BeanMap;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.api.ParamAssembler;
import com.shihui.openpf.home.model.OrderInfo;

/**
 * @author zhouqisheng
 *
 */
public class DefaultParamAssembler implements ParamAssembler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shihui.openpf.home.api.ParamAssembler#getServiceAvailableTimeParam(
	 * com.shihui.openpf.common.model.Merchant, int, int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Map<String, String> getServiceAvailableTimeParam(Merchant merchant, int serviceType, int cityId,
			String longitude, String latitude, String version , int categoryId , int amount) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("cityId", String.valueOf(cityId));
		param.put("serviceType", String.valueOf(serviceType));
		param.put("longitude", longitude);
		param.put("latitude", latitude);
		param.put("version", version);
		param.put("goodsId", String.valueOf(categoryId));
		param.put("amount", String.valueOf(amount));
		param.put("sign", SignUtil.genSign(param, merchant.getMd5Key()));
		return param;
	}

	@Override
	public Map<String, String> isServiceAvailableParam(Merchant merchant, int serviceType, int cityId,
			String longitude, String latitude, String serviceStartTime, String version,int categoryId , int amount) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("cityId", String.valueOf(cityId));
		param.put("serviceType", String.valueOf(serviceType));
		param.put("longitude", longitude);
		param.put("latitude", latitude);
		param.put("version", version);
		param.put("goodsId", String.valueOf(categoryId));
		param.put("amount", String.valueOf(amount));
		param.put("serviceStartTime", serviceStartTime);
		//计算签名，必须最后计算
		param.put("sign", SignUtil.genSign(param, merchant.getMd5Key()));

		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shihui.openpf.home.api.ParamAssembler#createOrderParam(com.shihui.
	 * openpf.common.model.Merchant, int,
	 * com.shihui.openpf.home.model.OrderInfo)
	 */
	@Override
	public Map<String, String> createOrderParam(Merchant merchant, int serviceType, OrderInfo orderInfo, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		BeanMap beanmap = new BeanMap(orderInfo);
		@SuppressWarnings("unchecked")
		Set<Entry<Object, Object>> set = beanmap.entrySet();
		for(Entry<Object, Object> entry : set){
			param.put(entry.getKey().toString(), entry.getValue() == null ? "" : entry.getValue().toString());
		}
		param.remove("class");
		param.put("version", version);
		//计算签名，必须最后计算
		param.put("sign", SignUtil.genSign(param, merchant.getMd5Key()));
		
		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shihui.openpf.home.api.ParamAssembler#cancelOrderParam(com.shihui.
	 * openpf.common.model.Merchant, int, java.lang.String)
	 */
	@Override
	public Map<String, String> cancelOrderParam(Merchant merchant, int serviceType, String orderId, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		param.put("orderId", orderId);
		param.put("version", version);
		//计算签名，必须最后计算
		param.put("sign", SignUtil.genSign(param, merchant.getMd5Key()));
		
		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shihui.openpf.home.api.ParamAssembler#payNoticeParam(com.shihui.
	 * openpf.common.model.Merchant, int, java.lang.String)
	 */
	@Override
	public Map<String, String> payNoticeParam(Merchant merchant, int serviceType, String orderId, String settlePrice, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		param.put("orderId", orderId);
		param.put("version", version);
		//计算签名，必须最后计算
		param.put("sign", SignUtil.genSign(param, merchant.getMd5Key()));
		
		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.shihui.openpf.home.api.ParamAssembler#evaluateOrderParam(com.shihui.
	 * openpf.common.model.Merchant, int, java.lang.String, int,
	 * java.lang.String)
	 */
	@Override
	public Map<String, String> evaluateOrderParam(Merchant merchant, int serviceType, String orderId, int score,
			String comments, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		param.put("orderId", orderId);
		param.put("score",String.valueOf(score));
		param.put("comments", comments);
		param.put("version", version);
		//计算签名，必须最后计算
		param.put("sign", SignUtil.genSign(param, merchant.getMd5Key()));
		
		return param;
	}

	@Override
	public String getAdapterName() {
		return HomeServProviderServiceImpl.DEFAULT_ADAPTER;
	}

}
