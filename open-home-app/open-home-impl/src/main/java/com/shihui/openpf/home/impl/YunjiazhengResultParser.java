/**
 * 
 */
package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.api.ResultParser;
import com.shihui.openpf.home.model.HomeResponse;

/**
 * @author zhouqisheng
 * @date 2016年2月23日 上午11:21:17
 *
 */
public class YunjiazhengResultParser implements ResultParser {

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#getAdapterName()
	 */
	@Override
	public String getAdapterName() {
		return "yunjiazheng.com";
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#getServiceAvailableTimeResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse getServiceAvailableTimeResult(Merchant merchant, String result) {
		HomeResponse response = new HomeResponse();
		JSONObject jo = JSONObject.parseObject(result);
		Integer code = jo.getInteger("code");
		if(code == null){
			response.setCode(1004);
			response.setMsg("接口请求失败");
			response.setMerchant(merchant);
			
			return response;
		}
		response.setCode(code);
		response.setMsg(jo.getString("msg"));
		
		if(code == 0){
			//JSONArray resultJo = new JSONArray();
			JSONObject times = new JSONObject();
			/*resultJo.put("result", times);*/

			JSONArray timelist = jo.getJSONObject("body").getJSONArray("timeList");

			for(int i = 0 ; i < timelist.size() ; i ++){
				JSONObject jsonObject = timelist.getJSONObject(i);
				String date = jsonObject.getString("date");
				date = date.replace("-","");
				jsonObject.remove("date");
				jsonObject.put("date",date);


			}

			times.put("times", timelist);
			
			response.setResult(times.toJSONString());
		}
		return response;
	}

	@Override
	public HomeResponse isServiceAvailableResult(Merchant merchant, String result) {
		JSONObject jo = JSONObject.parseObject(result);
		HomeResponse response = new HomeResponse();
		response.setCode(jo.getIntValue("code"));
		response.setMsg(jo.getString("msg"));
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#createOrderResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse createOrderResult(Merchant merchant, String result) {
		JSONObject jo = JSONObject.parseObject(result);
		HomeResponse response = new HomeResponse();
		response.setCode(jo.getIntValue("code"));
		response.setMsg(jo.getString("msg"));
		//JSONObject resultJo = new JSONObject();
		//resultJo.put("result", jo.getJSONObject("body"));
		response.setResult( jo.getJSONObject("body").toJSONString());
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#cancelOrderResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse cancelOrderResult(Merchant merchant, String result) {
		JSONObject jo = JSONObject.parseObject(result);
		HomeResponse response = new HomeResponse();
		response.setCode(jo.getIntValue("code"));
		response.setMsg(jo.getString("msg"));
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#payNoticeResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse payNoticeResult(Merchant merchant, String result) {
		JSONObject jo = JSONObject.parseObject(result);
		HomeResponse response = new HomeResponse();
		response.setCode(jo.getIntValue("code"));
		response.setMsg(jo.getString("msg"));
		return response;
	}

	/* (non-Javadoc)
	 * @see com.shihui.openpf.home.api.ResultParser#evaluateOrderResult(com.shihui.openpf.common.model.Merchant, java.lang.String)
	 */
	@Override
	public HomeResponse evaluateOrderResult(Merchant merchant, String result) {
		JSONObject jo = JSONObject.parseObject(result);
		HomeResponse response = new HomeResponse();
		response.setCode(jo.getIntValue("code"));
		response.setMsg(jo.getString("msg"));
		return response;
	}

}
