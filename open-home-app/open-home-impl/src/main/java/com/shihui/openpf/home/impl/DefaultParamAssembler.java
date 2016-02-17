/**
 * 
 */
package com.shihui.openpf.home.impl;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

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
			String longitude, String latitude) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("cityId", String.valueOf(cityId));
		param.put("serviceType", String.valueOf(serviceType));
		param.put("longitude", longitude);
		param.put("latitude", latitude);
		param.put("sign", this.genSign(param));

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
	public Map<String, String> createOrderParam(Merchant merchant, int serviceType, OrderInfo orderInfo) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		BeanMap beanmap = new BeanMap(orderInfo);
		@SuppressWarnings("unchecked")
		Set<Entry<Object, Object>> set = beanmap.entrySet();
		for(Entry<Object, Object> entry : set){
			param.put(entry.getKey().toString(), entry.getValue().toString());
		}
		param.put("sign", this.genSign(param));

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
	public Map<String, String> cancelOrderParam(Merchant merchant, int serviceType, String orderId) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		param.put("orderId", orderId);
		param.put("sign", this.genSign(param));

		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.shihui.openpf.home.api.ParamAssembler#payNoticeParam(com.shihui.
	 * openpf.common.model.Merchant, int, java.lang.String)
	 */
	@Override
	public Map<String, String> payNoticeParam(Merchant merchant, int serviceType, String orderId) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		param.put("orderId", orderId);
		param.put("sign", this.genSign(param));
		
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
			String comments) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("key", merchant.getMerchantKey());
		param.put("serviceType", String.valueOf(serviceType));
		param.put("orderId", orderId);
		param.put("score",String.valueOf(score));
		param.put("comments", comments);
		param.put("sign", this.genSign(param));
		
		return param;
	}

	@Override
	public String getAdapterName() {
		return HomeServProviderServiceImpl.DEFAULT_ADAPTER;
	}

	/**
	 * 计算签名
	 * @param param
	 * @return
	 */
	private String genSign(TreeMap<String, String> param) {
		StringBuilder temp = new StringBuilder();
		for (Entry<String, String> entry : param.entrySet()) {
			temp.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
		}
		if (temp.length() > 0) {
			temp.deleteCharAt(temp.length() - 1);
		}

		String sign = this.MD5(temp.toString());
		return sign;
	}

	/**
	 * 计算md5
	 * 
	 * @param s
	 * @return
	 */
	private char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private String MD5(String s) {
		try {
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
