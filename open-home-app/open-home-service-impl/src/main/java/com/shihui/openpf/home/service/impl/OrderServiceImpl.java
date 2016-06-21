package com.shihui.openpf.home.service.impl;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.openpf.home.dao.OrderDao;
import com.shihui.openpf.home.dao.OrderHistoryDao;
import com.shihui.openpf.home.model.OrderHistory;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderCancelType;
import com.shihui.openpf.home.service.api.OrderService;



/**
 * Created by zhoutc on 2016/1/25.
 */
@Service("openOrderService")
public class OrderServiceImpl implements OrderService {

    @Resource
    OrderDao orderDao;

    @Resource
    OrderHistoryDao orderHistoryDao;

    /**
     * 创建订单
     * @param order
     * @return 创建成功返回订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createOrder(Order order) {
        if(orderDao.save(order)>0) {
            Date date = new Date();
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setChange_time(date);
            orderHistory.setOrder_id(order.getOrderId());
            orderHistory.setOrder_status(order.getOrderStatus());
            if (orderHistoryDao.save(orderHistory) > 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean cancelOrder(long orderId, OrderCancelType cancelType, String comment) {
        return false;
    }

    @Override
    public boolean refund(long orderId, double refundAmount) {
        return false;
    }


    /**
     * 更新订单状态
     * @param orderId
     * @param orderStatus
     * @return 插入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrder(long orderId, OrderStatusEnum orderStatus) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus(orderStatus.getValue());
        if(orderDao.update(order)>0) {
            Date date = new Date();
            OrderHistory orderHistory = new OrderHistory();
            orderHistory.setChange_time(date);
            orderHistory.setOrder_id(order.getOrderId());
            orderHistory.setOrder_status(order.getOrderStatus());
            if (orderHistoryDao.insert(orderHistory) > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据条件查询订单
     * @param order
     * @return 订单列表
     */
    @Override
    public List<Order> queryOrderList(Order order , String startTime , String endTime, Integer page , Integer size) {
        return orderDao.queryOrder(order , startTime , endTime , page , size);
    }



    /**
     * 查询订单总数
     * @param order
     * @return 订单数
     */
    @Override
    public int countQueryOrder(Order order,String startTime , String endTime) {
        return orderDao.countQueryOrder(order,startTime,endTime);
    }

    /**
     * 根据条件分页查询订单
     * @param orderId    订单Id
     * @return 订单列表
     */
    @Override
    public Order queryOrder(long orderId) {
        return orderDao.queryOrder(orderId);
    }

    /**
     * 查询异常订单总数
     * @return 订单数
     */
    @Override
    public int countUnusual() {
        return orderDao.countUnusual();
    }

    /**
     * 查询异常订单
     * @return 订单列表
     */
    @Override
    public List<Order> queryUnusual() {
        return orderDao.queryUnusual();
    }

    @Override
    public String exportUnusual() {
        return null;
    }

	@Override
	public boolean update(Order order) {
		return orderDao.update(order) > 0;
	}

    @Override
    public int countOrders(long userId,int serviceId, String deviceId) {
        return orderDao.countOrders(userId,serviceId, deviceId);
    }
}
