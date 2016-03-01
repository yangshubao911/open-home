package com.shihui.openpf.home.service.impl;


import com.shihui.api.clearing.dubbo.ClearingDubbo;
import com.shihui.api.common.model.PaymentTypeEnum;
import com.shihui.api.common.model.SettleMethodEnum;
import com.shihui.api.common.model.UserTypeEnum;
import com.shihui.api.oms.sale.dubbo.OrderDubbo;
import com.shihui.api.oms.sale.dubbo.OrderPaymentMappingDubbo;
import com.shihui.api.oms.sale.dubbo.QueryOrderDubbo;
import com.shihui.api.oms.sale.model.OrderPaymentMapping;
import com.shihui.api.oms.sale.model.SimpleResult;
import com.shihui.api.oms.sale.model.vo.OrderDetailVo;
import com.shihui.api.payment.dubbo.PaymentDubbo;
import com.shihui.api.payment.model.OriginalRefundResult;
import com.shihui.api.payment.model.Payment;
import com.shihui.openpf.home.service.api.OrderDubboService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by zhoutc on 2016/1/29.
 */
@Service
public class OrderDubboServiceImpl implements OrderDubboService {

    private Logger log = LoggerFactory.getLogger(getClass());

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
            log.error("orderId:{} PaymentType:{} queryPaymentMapping error!",orderId,paymentTypeEnum.getName(),e);
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
            log.error("transId:{} queryPayMentInfo error!",transId,e);
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
            log.error("orderid:{} queryOrderDetail error!",orderId,e);
        }
        return null;
    }

    /**
     *用户取消待支付订单
     *
     * @param orderId           订单ID
     *
     *
     * @return 取消待支付订单结果
     */
    @Override
    public boolean cancelOrderByUser(long uid, long orderId) {
        try {
            SimpleResult result = orderDubbo.cancelOrderByUser(uid,orderId);
            log.info("userid:{} orderid:{} cancelOrderByUser status:{} msg:{}",
                    uid,orderId,result.getStatus(),result.getMsg());
            if(result.getStatus()==1) return true;
        }catch (Exception e){
            log.error("userid:{} orderid:{} cancelOrderByUser error!",uid,orderId,e);
        }
        return false;

    }

    /**
     * 用户取消付款订单
     *
     * @param  orderId  订单ID
     * @param  userId   用户ID
     *
     * @return 取消订单结果
     */
    @Override
    public boolean userRefund(long orderId, long userId){
        try {
            OriginalRefundResult originalRefundResult = paymentDubbo.userRefundForOpen(orderId, userId, UserTypeEnum.ShihuiApp, "homecleaning");
            log.info("orderId:{} userId:{} refund result:{} msg:{}",
                    orderId, userId, originalRefundResult.getStatus(), originalRefundResult.getMsg());
            if(originalRefundResult.getStatus()==1)return true;
        }catch (Exception e){
            log.error("orderId:{} userId:{} refund exception!",orderId,userId,e);
        }

        return false;

    }

    /**
     * 商户取消订单
     *
     * @param orderId      订单状态
     * @param merchantCode 商户ID
     * @return 发起充值结果
     */
    @Override
    public boolean merchantCancel(long orderId, long merchantCode, long userId) {
        SimpleResult simpleResult = null;
        boolean refund = false;
        try {
            simpleResult = orderDubbo.merchantCancelOrder(
                    merchantCode,orderId);
            if (simpleResult != null)
                log.info("merchantCancel--merchant cancel order:{} status:{} msg:{}",orderId,simpleResult.getStatus(),simpleResult.getMsg());


        } catch (Exception e) {
            log.error("merchantCancel-- order:{} OrderDubbo ERROR!!!!!!!",orderId,e);
        }

        if (simpleResult == null) {
            refund = false;
        } else {
            if (simpleResult.getStatus() == 1) refund = true;
        }

        return refund;
    }

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
    @Override
    public boolean partialRefund(long orderId, long userId, long price, long settlePrice, SettleMethodEnum settleMethod, long settleMerchantId, String reason, String ext, long uid, UserTypeEnum userType, String email) {
        try {
            log.info("orderId:{} userId:{} price:{} settlePrice{} settleMethod:{} settleMerchantId:{} reason:{} ext:{} uid:{} userType:{}  email:{}",
                    orderId,userId,price,settlePrice,settleMethod.getName(),settleMerchantId,reason,ext,uid,userType.getName(),email);
            OriginalRefundResult refundResult = paymentDubbo.partialRefund(orderId, userId, price, settlePrice,
                    settleMethod, settleMerchantId, reason, ext, uid, userType, email);
            log.info("partialRefund--orderId:{} refundResult status:{} msg:{}",orderId,refundResult.getStatus(),refundResult.getMsg());
            if(refundResult.getStatus()==1)
                return true;
        } catch (Exception e) {
            log.error("orderId:{} partialRefund error!",orderId,e);
        }
        return false;
    }


}
