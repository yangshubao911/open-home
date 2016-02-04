/**
 * 
 */
package com.shihui.openpf.home.api;

import java.util.Map;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.model.OrderInfo;


/**
 * 供应商接口参数配装器
 * 
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年2月1日 下午7:04:44
 */
public interface ParamAssembler {
	
	/**
	 * 获取服务商针对某个地域提供服务时间
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param cityId 城市Id 
	 * @param longitude 高德坐标经度
	 * @param latitude 高德坐标纬度
	 * @return
	 */
	Map<String, Object> getServiceAvailableTimeParam(Merchant merchant, int serviceType, int cityId, String longitude, String latitude);
	
	/**
	 * 创建订单
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderInfo 订单信息
	 * @return
	 */
	Map<String, Object> createOrderParam(Merchant merchant, int serviceType, OrderInfo orderInfo);
	
	/**
	 * 取消订单接口
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	Map<String, Object> cancelOrderParam(Merchant merchant, int serviceType, String orderId);
	
	/**
	 * 订单支付成功通知
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	Map<String, Object> payNoticeParam(Merchant merchant, int serviceType, String orderId);
	
	/**
	 * 评价订单接口
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @param score 评分（10分制）
	 * @param comments 评价内容
	 * @return
	 */
	Map<String, Object> evaluateOrderParam(Merchant merchant, int serviceType, String orderId, int score, String comments);


}
