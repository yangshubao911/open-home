package com.shihui.openpf.home.service.api;

import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.vo.ApiResult;
import com.shihui.api.order.vo.SingleGoodsCreateOrderParam;

/**
 * Created by zhoutc on 2016/2/29.
 */
public interface OrderSystemService {

    public ApiResult submitOrder(SingleGoodsCreateOrderParam singleGoodsCreateOrderParam);

    public boolean merchantCancelOrder(long orderId , OrderStatusEnum orderStatus , int operatorId);
}
