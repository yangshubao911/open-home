package com.shihui.openpf.home.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.service.OpenService;
import com.shihui.api.order.service.OrderService;
import com.shihui.api.order.vo.ApiResult;
import com.shihui.api.order.vo.SingleGoodsCreateOrderParam;
import com.shihui.openpf.home.http.FastHttpUtils;
import com.shihui.openpf.home.http.HttpCallbackHandler;
import com.shihui.openpf.home.service.api.OrderSystemService;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoutc on 2016/2/29.
 */
@Service
public class OrderSystemServiceImpl implements OrderSystemService {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Resource
    OpenService openService;

    @Resource(name="omsOrderService")
    OrderService orderService;

    @Override
    public ApiResult submitOrder(SingleGoodsCreateOrderParam singleGoodsCreateOrderParam) {

        CloseableHttpAsyncClient client = FastHttpUtils.defaultHttpAsyncClient();
        List<HttpCallbackHandler<String>> rs = new ArrayList<>();

        try{
            ApiResult result =  openService.createOrder(singleGoodsCreateOrderParam);
            String json = JSON.toJSONString(result);
            log.info(json);
            return  result;
        }catch (Exception e){
           log.error("OrderSystemServiceImpl userId：{} submitOrder error",singleGoodsCreateOrderParam.getUserId(),e);
        }
        return null;
    }

    @Override
    public boolean merchantCancelOrder(long orderId , OrderStatusEnum orderStatus , int operatorId) {
        try {
            JSONObject jsonObject = orderService.internalMerchantCancleOrder( orderId,  orderStatus,  operatorId);

            if(jsonObject.getInteger("result_status")==1){
                return true;
            }

        }catch (Exception e){
             log.error("merchantCancelOrder -- orderId:{} cancel error!!!",orderId,e);
        }
        return false;
    }
}
