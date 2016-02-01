package com.shihui.openpf.home.service.api;

import com.shihui.api.common.model.PaymentTypeEnum;
import com.shihui.api.oms.sale.model.OrderPaymentMapping;
import com.shihui.api.oms.sale.model.vo.OrderDetailVo;
import com.shihui.api.payment.model.Payment;

/**
 * Created by zhoutc on 2016/1/29.
 */
public interface OrderDubboService {

    /**
     * 查询订单支付信息
     *
     * @param orderId           订单ID
     * @param paymentTypeEnum   支付类型
     *
     * @return 订单支付信息
     */
    OrderPaymentMapping queryPaymentMapping(long orderId , PaymentTypeEnum paymentTypeEnum);

    /**
     * 查询订单支付信息
     *
     * @param transId           交易ID
     *
     *
     * @return 订单支付信息
     */
    Payment queryPayMentInfo(long transId);

    /**
     *查询订单详情
     *
     * @param orderId           订单ID
     *
     *
     * @return 订单详情
     */
    OrderDetailVo queryOrderDetail(long orderId);
}
