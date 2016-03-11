/**
 * 
 */
package com.shihui.openpf.home.api;

/**
 * @author zhouqisheng
 * @date 2016年3月11日 下午1:44:00
 *
 */
public interface OpenHomeApi {
	
	/**
	 * 开放平台用户退款接口
	 * @param orderId 订单id
	 * @param userId 用户id
	 * @return
	 */
	SimpleHomeResponse userCancelOrder(long orderId, long userId);

}
