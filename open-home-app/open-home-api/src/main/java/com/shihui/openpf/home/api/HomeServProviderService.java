/**
 * 
 */
package com.shihui.openpf.home.api;

import com.shihui.openpf.home.model.HomeResponse;

import java.util.Map;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月14日 下午5:15:15
 */
public interface HomeServProviderService {
	
	/**
	 * 获取服务商针对某个地域提供服务时间
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param cityId 城市Id 
	 * @param longitude 高德坐标经度
	 * @param latitude 高德坐标纬度
	 * @return
	 */
	public HomeResponse getServiceAvailableTime(String key, int serviceType, int cityId, String longitude, String latitude);
	
	/**
	 * 创建订单
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderInfo 订单信息
	 * @return
	 */
	public HomeResponse createOrder(String key, int serviceType, Map<String, Object> orderInfo);
	
	/**
	 * 取消订单接口
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	public HomeResponse cancelOrder(String key, int serviceType, String orderId);
	
	/**
	 * 订单支付成功通知
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	public HomeResponse payNotice(String key, int serviceType, String orderId);
	
	/**
	 * 评价订单接口
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @param score 评分（10分制）
	 * @param comments 评价内容
	 * @return
	 */
	public HomeResponse evaluateOrder(String key, int serviceType, String orderId, int score, String comments);

}
