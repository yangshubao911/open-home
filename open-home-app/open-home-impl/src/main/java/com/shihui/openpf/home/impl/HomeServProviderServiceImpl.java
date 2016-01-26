/**
 * 
 */
package com.shihui.openpf.home.impl;

import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.model.HomeResponse;

import java.util.Map;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月15日 下午4:02:54
 */
public class HomeServProviderServiceImpl implements HomeServProviderService{

	@Override
	public HomeResponse getServiceAvailableTime(String key, int serviceType, int cityId, String longitude,
	        String latitude) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse createOrder(String key, int serviceType, Map<String, Object> orderInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse cancelOrder(String key, int serviceType, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse payNotice(String key, int serviceType, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse evaluateOrder(String key, int serviceType, String orderId, int score, String comments) {
		// TODO Auto-generated method stub
		return null;
	}

}
