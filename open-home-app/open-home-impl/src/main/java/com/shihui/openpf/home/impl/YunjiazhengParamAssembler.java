/**
 * 
 */
package com.shihui.openpf.home.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.tools.AlgorithmUtil;
import com.shihui.openpf.common.tools.Coordinates;
import com.shihui.openpf.common.tools.LocalCoordConvertUtil;
import com.shihui.openpf.common.tools.StringUtil;
import com.shihui.openpf.home.api.ParamAssembler;
import com.shihui.openpf.home.model.OrderInfo;

/**
 * @author zhouqisheng
 * @date 2016年2月22日 下午4:19:02
 *
 */
public class YunjiazhengParamAssembler implements ParamAssembler {
	private Map<Integer, Integer> cityMap;
	
	@SuppressWarnings("unchecked")
	public YunjiazhengParamAssembler(){
		//初始化城市对应信息
		String text = "{1:1,2:2,5:187,6:350,7:188,8:310,9:44,10:147,11:289,23:164,25:355,27:189,29:19}";
		cityMap = JSON.parseObject(text, HashMap.class);
	}

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
			String longitude, String latitude, String version,int categoryId ,int amount, String productId) {
		TreeMap<String, String> param = new TreeMap<>();
		
		param.put("cityId", String.valueOf(getYJCityId(cityId)));
		//param.put("longitude", longitude);
		//param.put("latitude", latitude);
		//基础参数
		param.put("methodName", "queryAvailableTimeslots");
		param.put("pId", merchant.getMerchantKey());
		param.put("version", version);

		param.put("sign", genSign(param, merchant.getMd5Key()));
		return param;
	}

	@Override
	public Map<String, String> isServiceAvailableParam(Merchant merchant, int serviceType, int cityId, String longitude,
			String latitude, String serviceStartTime, String version,int categoryId ,int amount, String productId) {
		TreeMap<String, String> param = new TreeMap<>();
		//转为云家政城市码
		param.put("cityId", String.valueOf(getYJCityId(cityId)));
		//坐标系转换，云家政使用百度坐标系
		Coordinates co = new Coordinates(Double.parseDouble(longitude), Double.parseDouble(latitude));
		co = LocalCoordConvertUtil.google2baidu(co);
		param.put("longitude", String.valueOf(co.getLongitude()));
		param.put("latitude", String.valueOf(co.getLatitude()));
		String formate_string = "";
		try {
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(serviceStartTime);
			formate_string = new SimpleDateFormat("yyyyMMddHHmm").format(date);
		}catch (Exception e){
			return null;
		}
		param.put("serviceTime", formate_string);
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
		//转为云家政城市码
		param.put("cityId", String.valueOf(getYJCityId(orderInfo.getCityId())));
		//坐标系转换，云家政使用百度坐标系
		Coordinates co = new Coordinates(Double.parseDouble(orderInfo.getLongitude()), Double.parseDouble(orderInfo.getLatitude()));
		co = LocalCoordConvertUtil.google2baidu(co);
		param.put("longitude", String.valueOf(co.getLongitude()));
		param.put("latitude", String.valueOf(co.getLatitude()));
		param.put("serviceTime", orderInfo.getServiceStartTime());
		param.put("serviceAddress", orderInfo.getServiceAddress());
		param.put("houseNumber", "");
		param.put("mobile", orderInfo.getPhone());
		param.put("username", orderInfo.getContactName());
		param.put("quantity", String.valueOf(orderInfo.getAmount()));
		param.put("price", orderInfo.getPrice());
		if(!StringUtil.isEmpty(orderInfo.getRemark()))
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
    
    /**
     * 获得云家政城市码
     * @param cityId
     * @return
     */
    private int getYJCityId(int cityId){
    	Integer yjCityId = cityMap.get(cityId);
		if(yjCityId == null)
			yjCityId = -1;
		return yjCityId;
    }

}
