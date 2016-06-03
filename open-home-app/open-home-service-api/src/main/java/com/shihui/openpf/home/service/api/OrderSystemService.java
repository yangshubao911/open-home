package com.shihui.openpf.home.service.api;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.emodel.OperatorTypeEnum;
import com.shihui.api.order.emodel.RefundModeEnum;
import com.shihui.api.order.vo.ApiResult;
import com.shihui.api.order.vo.SimpleResult;
import com.shihui.api.order.vo.SingleGoodsCreateOrderParam;

/**
 * Created by zhoutc on 2016/2/29.
 */
public interface OrderSystemService {

    public ApiResult submitOrder(SingleGoodsCreateOrderParam singleGoodsCreateOrderParam);

    public boolean success(int orderType,long orderId ,long goodsId, String settlementInfo, int nowStatus);

    public boolean complete(long orderId ,long goodsId ,String settlementInfo, int nowStatus);

    /**
     * 商户退款接口
     * @param orderId
     * @param merchantCode
     * @param refundsPrice
     * @param nowStatus
     * @param reason
     * @return
     */
    public SimpleResult merchantCancel(long orderId, long merchantCode,long refundsPrice,int nowStatus, String reason);
    
    /**
     * 用户退款
     * @param orderId
     * @param merchantCode
     * @param userId
     * @param refundsPrice
     * @param nowStatus
     * @param reason
     * @return
     */
    public SimpleResult customCancel(long orderId, long merchantCode, long userId, long refundsPrice,int nowStatus, String reason);

    public SimpleResult backendOrderDetail(long orderId);
    
    /**
     * 修改订单状态
     * @param orderId 订单状态
     * @param oldStatus 当前状态
     * @param newStatus 要更新到的状态
     * @param operatorTypeEnum　操作人类型
     * @param operatorId 操作人Id
     * @param adminEmail 操作人邮件
     * @return
     */
    boolean updateOrderStatus(long orderId, OrderStatusEnum oldStatus, OrderStatusEnum newStatus, OperatorTypeEnum operatorTypeEnum, long operatorId, String adminEmail);
    
    /**
     * 开放平台订单退款
     *
     * @param refundMode    退款来源
     * @param orderId       订单ID
     * @param price         退款rmb金额
     * @param reason    退款原因
     * @param isReview  是否需审核 1需审核  2无需审核
     * @param isShihui  是否退实惠现金  1是  2否
     * @return
     */
    SimpleResult openRefund(RefundModeEnum refundMode, long orderId, long price, String reason, int isReview, int isShihui);
}
