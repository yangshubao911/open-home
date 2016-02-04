package com.shihui.openpf.home.service.api;

import com.shihui.api.common.model.PaymentTypeEnum;
import com.shihui.api.common.model.SettleMethodEnum;
import com.shihui.api.common.model.UserTypeEnum;
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

    /**
     *用户取消待支付订单
     *
     * @param orderId           订单ID
     *
     *
     * @return 取消待支付订单结果
     */
    public boolean cancelOrderByUser(long uid, long orderId);

    /**
     * 用户取消付款订单
     *
     * @param  orderId  订单ID
     * @param  userId   用户ID
     *
     * @return 取消订单结果
     */
    public boolean userRefund(long orderId, long userId);

    /**
     * 商户取消订单
     *
     * @param orderId      订单状态
     * @param merchantCode 商户ID
     * @return 发起充值结果
     */
    public boolean merchantCancel(long orderId, long merchantCode, long userId);

    /**
     * 部分退款
     * @param orderId       订单ID
     * @param userId        用户ID
     * @param price         退款金额
     * @param settlePrice   结算金额
     * @param settleMethod  结算方式
     * @param settleMerchantId  结算商户
     * @param reason    退款原因
     * @param ext       扩展字段
     * @param uid       操作者id
     * @param userType  操作用户类型
     * @param email     操作者邮箱

     *
     * @return 发起充值结果
     */
    public boolean partialRefund(long orderId, long userId, long price, long settlePrice, SettleMethodEnum settleMethod, long settleMerchantId, String reason, String ext, long uid, UserTypeEnum userType, String email);


    }
