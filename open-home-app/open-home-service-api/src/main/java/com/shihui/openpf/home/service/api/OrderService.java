package com.shihui.openpf.home.service.api;

import java.util.List;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderCancelType;

public interface OrderService {
	
	/**
	 * 创建订单
	 * @param order
	 * @return 创建成功返回订单号
	 */
	boolean createOrder(Order order);
	
	/**
	 * @param orderId 订单id
	 * @param cancelType 订单取消类型
	 * @param comment 备注
	 * @return
	 */
	boolean cancelOrder(long orderId, OrderCancelType cancelType, String comment);
	
	/**
	 * 退款
	 * @param orderId
	 * @param refundAmount
	 * @return
	 */
	boolean refund(long orderId, double refundAmount);


	/**
	 * 更新订单状态
	 * @param orderId
	 * @param orderStatus
	 * @return 插入结果
	 */
	boolean updateOrder(long orderId, OrderStatusEnum orderStatus);

	/**
	 * 根据条件分页查询订单
	 * @param order
	 * @return 订单列表
	 */
	List<Order> queryOrderList(Order order, String startTime , String endTime, Integer page, Integer size);


	/**
	 * 查询订单总数
	 * @param order
	 * @return 订单数
	 */
	int countQueryOrder(Order order, String startTime , String endTime);

	/**
	 * 根据条件分页查询订单
	 * @param orderId    订单Id
	 * @return 订单列表
	 */
	Order queryOrder(long orderId);

	/**
	 * 查询异常订单总数
	 * @return 订单数
	 */
	int countUnusual();

	/**
	 * 查询异常订单
	 * @return 订单列表
	 */
	List<Order>  queryUnusual();

	/**
	 * 导出异常订单
	 * @return 订单列表
	 */
	public String exportUnusual();
	
	/**
	 * 更新
	 * @param order
	 * @return
	 */
	boolean update(Order order);

	/**
	 * 查询同一用户或者同一设备有效订单数量
	 * @param userId
	 * @param serviceId
	 * @param deviceId
	 * @return
	 */
	int countOrders(long userId,int serviceId, String deviceId);

}
