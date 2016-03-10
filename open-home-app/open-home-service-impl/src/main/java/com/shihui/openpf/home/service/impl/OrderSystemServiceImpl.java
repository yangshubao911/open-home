package com.shihui.openpf.home.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.emodel.OperatorTypeEnum;
import com.shihui.api.order.emodel.RefundModeEnum;
import com.shihui.api.order.service.OpenService;
import com.shihui.api.order.service.OrderRefundService;
import com.shihui.api.order.service.OrderService;
import com.shihui.api.order.vo.ApiResult;
import com.shihui.api.order.vo.SimpleResult;
import com.shihui.api.order.vo.SingleGoodsCreateOrderParam;
import com.shihui.openpf.home.service.api.OrderSystemService;

/**
 * Created by zhoutc on 2016/2/29.
 */
@Service
public class OrderSystemServiceImpl implements OrderSystemService {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    OpenService openService;

    @Resource(name = "omsOrderService")
    OrderService orderService;
    
    @Resource
    OrderRefundService orderRefundService;

    @Override
    public ApiResult submitOrder(SingleGoodsCreateOrderParam singleGoodsCreateOrderParam) {

        try {
            ApiResult result = openService.createOrder(singleGoodsCreateOrderParam);
            String json = JSON.toJSONString(result);
            log.info(json);
            return result;
        } catch (Exception e) {
            log.error("OrderSystemServiceImpl userId：{} submitOrder error", singleGoodsCreateOrderParam.getUserId(), e);
        }
        return null;
    }

    @Override
    public boolean updateOrderStatus(long orderId, OrderStatusEnum oldStatus, OrderStatusEnum newStatus, OperatorTypeEnum operatorTypeEnum, long operatorId, String adminEmail) {
        try {
            int count = orderService.updateOrderStatus(orderId, oldStatus, newStatus, operatorTypeEnum, operatorId, adminEmail);
            if (count > 0) {
                return true;
            }

        } catch (Exception e) {
            log.error("updateOrderStatus -- orderId:{} update error!!!", orderId, e);
        }
        return false;
    }

    @Override
    public boolean success(int orderType, long orderId, long goodsId, String settlementInfo, int nowStatus) {
        try {
            boolean result = openService.success(orderType, orderId, goodsId, settlementInfo, nowStatus);
            return result;
        } catch (Exception e) {
            log.error("success exception !!!", e);
        }
        return false;
    }

    @Override
    public boolean complete(long orderId, long goodsId, String settlementInfo, int nowStatus) {
        try {
            boolean result = openService.complete(orderId, goodsId, settlementInfo, nowStatus);
            return result;
        } catch (Exception e) {
            log.error("complete exception !!!", e);
        }
        return false;
    }

    @Override
    public boolean fail(int orderType , long orderId, long goodsId, long merchantCode,long refundsPrice,int nowStatus, String reason) {
        try {
            boolean result = openService.fail(orderType, orderId, goodsId ,merchantCode, refundsPrice, nowStatus, reason);
            return result;
        } catch (Exception e) {
            log.error("complete exception !!!", e);
        }
        return false;
    }

    @Override
    public SimpleResult backendOrderDetail(long orderId) {
        try {
            SimpleResult simpleResult = openService.backendOrderDetail(orderId);
            return simpleResult;
        } catch (Exception e) {
            log.error("backendOrderDetail exception !!!", e);
        }
        return null;
    }

	@Override
	public SimpleResult openRefund(RefundModeEnum refundMode, long orderId, long price, String reason, int isReview,
			int isShihui) {
		SimpleResult result = null;
		try {
			result = this.orderRefundService.openRefund(refundMode, orderId, price, reason, isReview, isShihui);
		} catch (Exception e) {
			log.error("order refund exception !!!, order_id={}", orderId, e);
		}
		return result;
	}
}
