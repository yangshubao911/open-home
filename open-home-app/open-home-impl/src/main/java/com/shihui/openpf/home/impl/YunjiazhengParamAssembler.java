/**
 * 
 */
package com.shihui.openpf.home.impl;

import java.util.Map;
import java.util.TreeMap;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.util.AlgorithmUtil;
import com.shihui.openpf.home.api.ParamAssembler;
import com.shihui.openpf.home.model.OrderInfo;

/**
 * @author zhouqisheng
 * @date 2016年2月22日 下午4:19:02
 *
 */
public class YunjiazhengParamAssembler implements ParamAssembler {

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ParamAssembler#getAdapterName()
	 */
	@Override
	public String getAdapterName() {
		return "yunjiazheng.com";
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ParamAssembler#getServiceAvailableTimeParam(com.shihui.openpf.common.model.Merchant, int, int, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> getServiceAvailableTimeParam(Merchant merchant, int serviceType, int cityId,
			String longitude, String latitude, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("cityId", String.valueOf(cityId));
		param.put("longitude", longitude);
		param.put("latitude", latitude);
		//基础参数
		param.put("methodName", "queryAvailableTimeslots");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}

	@Override
	public Map<String, String> isServiceAvailableParam(Merchant merchant, int serviceType, int cityId, String longitude,
			String latitude, String serviceStartTime, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("cityId", String.valueOf(cityId));
		param.put("longitude", longitude);
		param.put("latitude", latitude);
		param.put("serviceTime", serviceStartTime);
		//基础参数
		param.put("methodName", "canService");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ParamAssembler#createOrderParam(com.shihui.openpf.common.model.Merchant, int, com.shihui.openpf.home.model.OrderInfo, java.lang.String)
	 */
	@Override
	public Map<String, String> createOrderParam(Merchant merchant, int serviceType, OrderInfo orderInfo,
			String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("cityId", String.valueOf(orderInfo.getCityId()));
		param.put("longitude", orderInfo.getLongitude());
		param.put("latitude", orderInfo.getLatitude());
		param.put("serviceTime", orderInfo.getServiceStartTime());
		param.put("serviceAddress", orderInfo.getServiceAddress());
		param.put("houseNumber", "");
		param.put("mobile", orderInfo.getPhone());
		param.put("username", orderInfo.getContactName());
		param.put("quantity", String.valueOf(orderInfo.getAmount()));
		param.put("price", orderInfo.getPrice());
		param.put("comment", orderInfo.getRemark());
		//基础参数
		param.put("methodName", "createOrder");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ParamAssembler#cancelOrderParam(com.shihui.openpf.common.model.Merchant, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> cancelOrderParam(Merchant merchant, int serviceType, String orderId, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("orderId", orderId);
		//基础参数
		param.put("methodName", "cancelOrder");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ParamAssembler#payNoticeParam(com.shihui.openpf.common.model.Merchant, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> payNoticeParam(Merchant merchant, int serviceType, String orderId, String settlePrice, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("orderId", orderId);
		param.put("settlePrice", settlePrice);
		//基础参数
		param.put("methodName", "orderPaied");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ParamAssembler#evaluateOrderParam(com.shihui.openpf.common.model.Merchant, int, java.lang.String, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Map<String, String> evaluateOrderParam(Merchant merchant, int serviceType, String orderId, int score,
			String comments, String version) {
		TreeMap<String, String> param = new TreeMap<>();
		param.put("orderId", orderId);
		param.put("score", String.valueOf(score));
		param.put("review", comments);
		//基础参数
		param.put("methodName", "orderReview");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}
	
	 /**
     * 计算签名
     * @param param
     * @return
     */
    private String genSign(TreeMap<String, String> param, String md5Key) {
        StringBuilder temp = new StringBuilder();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            temp.append(entry.getKey()).append(entry.getValue());
        }
        temp.append(md5Key);
        String sign = AlgorithmUtil.MD5(temp.toString());
        return sign;
    }

}
