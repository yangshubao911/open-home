package com.shihui.openpf.home.service.impl;

import com.shihui.api.common.model.OrderStatusEnum;
import com.shihui.openpf.home.dao.OrderDao;
import com.shihui.openpf.home.dao.OrderHistoryDao;
import com.shihui.openpf.home.model.OrderHistory;


import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderCancelType;
import com.shihui.openpf.home.service.api.OrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by zhoutc on 2016/1/25.
 */
@Service
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
    public boolean createOrder(Order order) {
        if(orderDao.insert(order)>0) {
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
    public boolean updateOrder(long orderId, OrderStatusEnum orderStatus) {
        Order order = new Order();
        order.setOrderId(orderId);
        order.setOrderStatus((byte)orderStatus.getValue());
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
    public List<Order> queryOrder(Order order , int page , int size) {
        return orderDao.queryOrder(order,page,size);
    }

    /**
     * 查询订单总数
     * @param order
     * @return 订单数
     */
    @Override
    public int countQueryOrder(Order order) {
        return orderDao.countQueryOrder(order);
    }
}
