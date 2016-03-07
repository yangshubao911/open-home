package com.shihui.openpf.home.service.api;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.vo.ApiResult;
import com.shihui.api.order.vo.SimpleResult;
import com.shihui.api.order.vo.SingleGoodsCreateOrderParam;

/**
 * Created by zhoutc on 2016/2/29.
 */
public interface OrderSystemService {

    public ApiResult submitOrder(SingleGoodsCreateOrderParam singleGoodsCreateOrderParam);

    public boolean merchantCancelOrder(long orderId , OrderStatusEnum orderStatus , int operatorId);

    public boolean success(int orderType,long orderId ,long goodsId, String settlementInfo, int nowStatus);

    public boolean complete(long orderId ,long goodsId ,String settlementInfo, int nowStatus);

    public boolean fail(int orderType , long orderId, long goodsId, long merchantCode,long refundsPrice,int nowStatus, String reason);

    public SimpleResult backendOrderDetail(long orderId);
}
