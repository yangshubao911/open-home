/**
 * 
 */
package com.shihui.openpf.home.impl;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.model.OrderInfo;


/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月15日 下午4:02:54
 */
public class DefaultHomeServProviderServiceImpl implements HomeServProviderService{
	@Override
	public HomeResponse getServiceAvailableTime(int serviceType, int gid, String longitude, String latitude) {
		// TODO Auto-generated method stub
		return null;                                                                                                                                                                                                                                                                                                                                     
	}

	@Override
	public HomeResponse createOrder(Merchant merchant, int serviceType, OrderInfo orderInfo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse cancelOrder(Merchant merchant, int serviceType, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse payNotice(Merchant merchant, int serviceType, String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HomeResponse evaluateOrder(Merchant merchant, int serviceType, String orderId, int score, String comments) {
		// TODO Auto-generated method stub
		return null;
	}

}
