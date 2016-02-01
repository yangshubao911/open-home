package com.shihui.openpf.home.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.shihui.api.clearing.dubbo.ClearingDubbo;
import com.shihui.api.common.model.PaymentTypeEnum;
import com.shihui.api.oms.sale.dubbo.OrderDubbo;
import com.shihui.api.oms.sale.dubbo.OrderPaymentMappingDubbo;
import com.shihui.api.oms.sale.dubbo.QueryOrderDubbo;
import com.shihui.api.oms.sale.model.OrderPaymentMapping;
import com.shihui.api.oms.sale.model.vo.OrderDetailVo;
import com.shihui.api.payment.dubbo.PaymentDubbo;
import com.shihui.api.payment.model.Payment;
import com.shihui.openpf.home.service.api.OrderDubboService;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/1/29.
 */
@Service
public class OrderDubboServiceImpl implements OrderDubboService {

    @Resource
    OrderDubbo orderDubbo;

    @Resource
    ClearingDubbo clearingDubbo;

    @Resource
    OrderPaymentMappingDubbo orderPaymentMappingDubbo;

    @Resource
    PaymentDubbo paymentDubbo;

    @Resource
    QueryOrderDubbo queryOrderDubbo;

    /**
     * 查询订单支付信息
     *
     * @param orderId           订单ID
     * @param paymentTypeEnum   支付类型
     *
     * @return 订单支付信息
     */
    @Override
    public OrderPaymentMapping queryPaymentMapping(long orderId, PaymentTypeEnum paymentTypeEnum) {
        try{
            return orderPaymentMappingDubbo.query(orderId, paymentTypeEnum);
        }catch (Exception e){

        }
        return null;
    }

    /**
     * 查询订单支付信息
     *
     * @param transId           交易ID
     *
     *
     * @return 订单支付信息
     */
    @Override
    public Payment queryPayMentInfo(long transId) {
        try{
            return paymentDubbo.queryPaymentByTransId(transId);
        }catch (Exception e){
        }
        return null;
    }

    /**
     *查询订单详情
     *
     * @param orderId           订单ID
     *
     *
     * @return 订单详情
     */
    @Override
    public OrderDetailVo queryOrderDetail(long orderId) {
        try{
            return queryOrderDubbo.queryOrderDetailByOrderId(orderId);
        }catch (Exception e){
        }
        return null;
    }
}
