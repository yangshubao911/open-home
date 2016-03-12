/**
 * 
 */
package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.api.ResultParser;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.service.impl.HomeServProviderServiceImpl;

/**
 * @author zhouqisheng
 *
 */
public class DefaultResultParser implements ResultParser {

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#getServiceAvailableTimeResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse getServiceAvailableTimeResult(Merchant merchant, String result) {
		return JSON.parseObject(result, HomeResponse.class);
	}

	@Override
	public HomeResponse isServiceAvailableResult(Merchant merchant, String result) {
		HomeResponse response = JSON.parseObject(result, HomeResponse.class);
		response.setMerchant(merchant);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#createOrderResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse createOrderResult(Merchant merchant, String result) {
		HomeResponse response = JSON.parseObject(result, HomeResponse.class);
		response.setMerchant(merchant);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#cancelOrderResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse cancelOrderResult(Merchant merchant, String result) {
		HomeResponse response = JSON.parseObject(result, HomeResponse.class);
		response.setMerchant(merchant);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#payNoticeResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse payNoticeResult(Merchant merchant, String result) {
		HomeResponse response = JSON.parseObject(result, HomeResponse.class);
		response.setMerchant(merchant);
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#evaluateOrderResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse evaluateOrderResult(Merchant merchant, String result) {
		HomeResponse response = JSON.parseObject(result, HomeResponse.class);
		response.setMerchant(merchant);
		return response;
	}

	@Override
	public String getAdapterName() {
		return HomeServProviderServiceImpl.DEFAULT_ADAPTER;
	}

}
