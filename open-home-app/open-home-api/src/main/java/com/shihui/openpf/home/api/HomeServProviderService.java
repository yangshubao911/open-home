/**
 * 
 */
package com.shihui.openpf.home.api;

import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.model.HomeResponse;
import com.shihui.openpf.home.model.OrderInfo;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月14日 下午5:15:15
 */
public interface HomeServProviderService {
	
	/**
	 * @param serviceType 上门服务业务类型
	 * @param goodsId 商品id
	 * @param gid 小区id 
	 * @param longitude 高德坐标经度
	 * @param latitude 高德坐标纬度
	 * @return
	 */
	public HomeResponse getServiceAvailableTime(int serviceType, int goodsId, int gid, String longitude, String latitude);
	
	/**
	 * 创建订单
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderInfo 订单信息
	 * @return
	 */
	public HomeResponse createOrder(Merchant merchant , int serviceType, OrderInfo orderInfo);
	
	/**
	 * 取消订单接口
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	public HomeResponse cancelOrder(Merchant merchant , int serviceType, String orderId);
	
	/**
	 * 订单支付成功通知
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @return
	 */
	public HomeResponse payNotice(Merchant merchant , int serviceType, String orderId);
	
	/**
	 * 评价订单接口
	 * @param merchant 商户标识，开放平台为商户分配
	 * @param serviceType 上门服务业务类型
	 * @param orderId 订单ID
	 * @param score 评分（10分制）
	 * @param comments 评价内容
	 * @return
	 */
	public HomeResponse evaluateOrder(Merchant merchant , int serviceType, String orderId, int score, String comments);

}
