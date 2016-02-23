/**
 * 
 */
package com.shihui.openpf.home.api;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.model.HomeResponse;

/**
 * 供应商接口结果解析器
 * 
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年2月1日 下午7:03:53
 */
public interface ResultParser {
	
	/**
	 * 获得适配器名称，一对参数组转器与结果解析器返回值必须相同
	 * @return
	 */
	public String getAdapterName();
	
	/**
	 * 获取服务商针对某个地域提供服务时间
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param cityId 城市Id 
	 * @param longitude 高德坐标经度
	 * @param latitude 高德坐标纬度
	 * @return
	 */
	HomeResponse getServiceAvailableTimeResult(Merchant merchant, String result);
	
	/**
	 * 服务是否可用
	 * @param merchant
	 * @param result
	 * @return
	 */
	HomeResponse isServiceAvailableResult(Merchant merchant, String result);
	
	/**
	 * 创建订单
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderInfo 订单信息
	 * @return
	 */
	HomeResponse createOrderResult(Merchant merchant, String result);
	
	/**
	 * 取消订单接口
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	HomeResponse cancelOrderResult(Merchant merchant, String result);
	
	/**
	 * 订单支付成功通知
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	HomeResponse payNoticeResult(Merchant merchant, String result);
	
	/**
	 * 评价订单接口
	 * @param key 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @param score 评分（10分制）
	 * @param comments 评价内容
	 * @return
	 */
	HomeResponse evaluateOrderResult(Merchant merchant, String result);


}
