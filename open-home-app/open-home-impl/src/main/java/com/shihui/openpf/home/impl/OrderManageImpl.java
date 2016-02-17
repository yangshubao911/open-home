package com.shihui.openpf.home.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.api.common.model.OrderStatusEnum;
import com.shihui.api.common.model.PaymentTypeEnum;
import com.shihui.api.common.model.SettleMethodEnum;
import com.shihui.api.common.model.UserTypeEnum;
import com.shihui.api.oms.sale.model.OrderPaymentMapping;
import com.shihui.api.oms.sale.model.SimpleResult;
import com.shihui.api.oms.sale.model.vo.OrderDetailVo;
import com.shihui.api.payment.model.Payment;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.api.OrderManage;
import com.shihui.openpf.home.model.*;
import com.shihui.openpf.home.service.api.*;
import com.shihui.openpf.home.util.SimpleResponse;
import me.weimi.api.commons.context.RequestContext;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoutc on 2016/1/21.
 */
public class OrderManageImpl implements OrderManage {

    @Resource
    OrderService orderService;

    @Resource
    OrderDubboService orderDubboService;

    @Resource
    GoodsService goodsService;

    @Resource
    ContactService contactService;

    @Resource
    MerchantManage merchantManage;

    @Resource
    HomeServProviderService homeServProviderService;

    @Resource
    RequestService requestService;

    private Logger log = LoggerFactory.getLogger(getClass());
    /**
     * 客户端创建订单
     *
     * @param json 创建订单请求数据
     * @return 返回结果
     */
    @Override
    public String createOrder(RequestContext rc, String json) {
        OrderForm orderForm = JSON.parseObject(json, OrderForm.class);


        return null;
    }

    /**
     * OPS查询订单
     *
     * @param queryOrder 查询订单条件
     * @return 返回结果
     */
    @Override
    public String queryOrderList(RequestContext rc, Order queryOrder, Long startTime, Long endTime, int page, int size) {
        JSONObject result = new JSONObject();
        JSONArray orders_json = new JSONArray();
        //  Order queryOrder = JSON.parseObject(json, Order.class);
        int total = orderService.countQueryOrder(queryOrder, startTime, endTime);
        result.put("total", total);
        result.put("page", page);
        result.put("size", size);
        if (total <= 0) return result.toJSONString();
        List<Order> orderList = orderService.queryOrderList(queryOrder, startTime, endTime, page, size);

        List<Integer> merchants = new ArrayList<>();
        for (Order order : orderList) {
            merchants.add(order.getMerchantId());
        }

        for (Order order : orderList) {
            JSONObject order_json = new JSONObject();
            order_json.put("orderId", String.valueOf(order.getOrderId()));
            order_json.put("userId", order.getUserId());
            order_json.put("phone", order.getPhone());

            Goods goods = goodsService.findById(order.getGoodsId());
            order_json.put("price", goods.getPrice());
            order_json.put("shOffset", goods.getShOffSet());
            order_json.put("due", new BigDecimal(goods.getPrice()).
                    subtract(new BigDecimal(goods.getShOffSet())).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString());

            order_json.put("merchantId", order.getMerchantId());

            //order_json.put("merchantName", );
            //order_json.put("settlement", merchantGoods.getPrice());
            DateTime dateTime = new DateTime(order.getCreateTime());
            order_json.put("createTime", dateTime.toString("yyyyMMddHHmmss"));
            order_json.put("pay", order.getPay());
            order_json.put("status", order.getOrderStatus());
            orders_json.add(order_json);
        }
        result.put("orders", orders_json);
        return result.toJSONString();
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @return 返回订单详情
     */
    @Override
    public String queryOrder(long orderId) {
        JSONObject result = new JSONObject();
        Order order = orderService.queryOrder(orderId);

        PaymentTypeEnum paymentTypeEnum = null;
        if (order.getPaymentType() == PaymentTypeEnum.Alipay.getValue())
            paymentTypeEnum = PaymentTypeEnum.Alipay;
        if (order.getPaymentType() == PaymentTypeEnum.Wxpay.getValue())
            paymentTypeEnum = PaymentTypeEnum.Wxpay;

        OrderPaymentMapping orderPaymentMapping = orderDubboService.queryPaymentMapping(orderId, paymentTypeEnum);
        result.put("payType", order.getPaymentType());
        if (orderPaymentMapping != null) {
            result.put("transId", orderPaymentMapping.getTransId());
            Payment payment = orderDubboService.queryPayMentInfo(orderPaymentMapping.getTransId());
            if (payment != null) {
                if (payment.getPaymentTime() != null && !"".equals(payment.getPaymentTime())) {
                    DateTime date = new DateTime(payment.getPaymentTime().getTime());
                    result.put("payTime", date.toString("yyyy-MM-dd HH:mm:ss"));
                }
            }

        }
        result.put("createTime", new DateTime(order.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
        result.put("userId", order.getUserId());
        result.put("phone", order.getPhone());

        Goods goods = goodsService.findById(order.getGoodsId());
        result.put("price", goods.getPrice());

        //Merchant merchant = rechargeMerchantService.queryMerchant(order.getMerchantid());
        result.put("merchantId", order.getMerchantId());
        // result.put("merchantName", merchant.getMechantname());

        //  MerchantGoods merchantGoods = merchantGoodsService.queryProvider(order.getProviderid());
//        result.put("originalPrice", produce.getDue());
        result.put("pay", order.getPay());
        result.put("shOffset", order.getShOffSet());
        result.put("userStatus", order.getOrderStatus());
        // result.put("userStatusName", order.getStatus().getName());

        return result.toJSONString();
    }

    /**
     * 取消订单
     *
     * @param orderId 订单ID
     * @return 返回订单详情
     */
    @Override
    public String cancelOrder(long orderId, OrderCancelType orderCancelType) {
        if(orderCancelType==null) return buildResponse(1, "取消订单类型错误");
        Order order = orderService.queryOrder(orderId);
        OrderDetailVo orderDetailVo = orderDubboService.queryOrderDetail(orderId);
        if (order == null || orderDetailVo == null)
            return buildResponse(1, "未找到订单");

        if (order.getOrderStatus() == OrderStatusEnum.OrderBackClose.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderCloseByMerchant.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderCloseByManual.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderCloseOfOutTime.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderHadRefund.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderMrchantClose.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderRefunding.getValue()) {
            return buildResponse(1, "订单已经取消");
        }

        Contact contact = contactService.queryByOrderId(orderId);
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        DateTime serviceStartTime = DateTime.parse(contact.getServiceStartTime(), format);
        DateTime now = new DateTime();


        if (serviceStartTime.getMillis() - 2 * 60 * 60 * 1000 <= now.getMillis()) {
            if (orderCancelType.getValue() != OrderCancelType.REFUND_PARTIAL.getValue())
                return buildResponse(2, "订单取消超时");
        }

        String result = null;

        switch (orderCancelType) {
            case NON_PAYMENT_CONSUMER:
                result = NonPaymentCancel(order, orderDetailVo);
                break;
            case PAYMENT_CONSUMER:
                result = PaymentCancel(order, orderDetailVo);
                break;
            case PAYMENT_MERCHANT:
                result = MerchantCancel(order, orderDetailVo);
                break;
            case PAYMENT_OUT_TIME:
                result = OutOfTimeCancel(order, orderDetailVo);
                break;
            case MERCHANT_OUT_TIME:
                result = MerchantOutOfTimeCancel(order, orderDetailVo);
                break;
            case SYS_INTERVERNE:
                result = PhpCloseCancel(order, orderDetailVo);
                break;
            case REFUND_PARTIAL:
                result = RefundPartial(order, orderDetailVo);
                break;
        }


        return result;
    }

    private String NonPaymentCancel(Order order, OrderDetailVo orderDetailVo) {

        if (order.getOrderStatus() != OrderStatusEnum.OrderUnpaid.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderUnpaid.getValue()) {
            return buildResponse(1, "订单状态不为" + OrderStatusEnum.OrderUnpaid.getName());
        }

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        //2.调用dubbo接口取消未支付订单
        if (orderDubboService.cancelOrderByUser(order.getUserId(), order.getOrderId())) {
            return buildResponse(0, "取消订单成功");
        } else {
            return buildResponse(1, "取消订单失败");
        }
    }

    private String PaymentCancel(Order order, OrderDetailVo orderDetailVo) {

        if (order.getOrderStatus() != OrderStatusEnum.OrderDistribute.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderDistribute.getValue()) {
            return buildResponse(1, "订单状态不为" + OrderStatusEnum.OrderDistribute.getName());
        }

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        //2.调用dubbo接口取消未支付订单
        if (orderDubboService.userRefund(order.getUserId(), order.getOrderId())) {
            return buildResponse(0, "取消订单成功");
        } else {
            return buildResponse(1, "取消订单失败");
        }
    }

    private String MerchantCancel(Order order, OrderDetailVo orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderUnConfirm.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderUnConfirm.getValue()) {
            return buildResponse(1, "订单状态不为" + OrderStatusEnum.OrderUnConfirm.getName());
        }
        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        long merchantCode = merchant.getMerchantCode();
        //2.调用dubbo接口取消未支付订单
        if (orderDubboService.merchantCancel(order.getOrderId(), merchantCode, order.getUserId())) {
            return buildResponse(0, "取消订单成功");
        } else {
            return buildResponse(1, "取消订单失败");
        }
    }

    private String OutOfTimeCancel(Order order, OrderDetailVo orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderUnpaid.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderUnpaid.getValue()) {
            return buildResponse(1, "订单状态不为" + OrderStatusEnum.OrderUnpaid.getName());
        }
        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        //2.更新订单表
        return buildResponse(0, "取消订单成功");
    }

    private String MerchantOutOfTimeCancel(Order order, OrderDetailVo orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderUnConfirm.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderUnConfirm.getValue()) {
            return buildResponse(1, "订单状态不为" + OrderStatusEnum.OrderUnConfirm.getName());
        }
        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        //2.更新订单表
        return buildResponse(0, "取消订单成功");
    }

    private String PhpCloseCancel(Order order, OrderDetailVo orderDetailVo) {

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        //2.更新订单表
        return buildResponse(0, "取消订单成功");
    }

    private String RefundPartial(Order order, OrderDetailVo orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderDistribute.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderDistribute.getValue() ||
                order.getOrderStatus() != OrderStatusEnum.OrderUnConfirm.getValue() ||
                Integer.parseInt(orderDetailVo.getOrders().get(0).getStatus()) != OrderStatusEnum.OrderUnConfirm.getValue()) {
            return buildResponse(1, "订单状态不为" + OrderStatusEnum.OrderUnConfirm.getName() + "或" + OrderStatusEnum.OrderDistribute.getName());
        }

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if(!cancelThirdPartOrder(order,merchant)){
            return buildResponse(1, "取消第三方订单失败");
        }
        long merchantCode = merchant.getMerchantCode();
        long refundMoney_l = 0l;
        long settlementMoney_l = 0l;
        //2.调用部分退款接口
        if (orderDubboService.partialRefund(order.getOrderId(), order.getUserId(), refundMoney_l,
                settlementMoney_l, SettleMethodEnum.UnSettle, merchantCode, "上门服务OPS部分退款",
                "{}", 111112, UserTypeEnum.ShihuiApp, "homeservice@17shihui.com")) {
            return buildResponse(0, "取消订单成功");
        } else {
            return buildResponse(1, "取消订单失败");
        }
    }

    public boolean cancelThirdPartOrder(Order order, Merchant merchant) {

        Request request = requestService.queryOrderRequest(order.getOrderId());
        if (request == null) {
            log.info("cancelThirdPartOrder--orderId = {} not found request!!!", order.getOrderId());
            return false;
        }
        HomeResponse homeResponse = homeServProviderService.cancelOrder(merchant, order.getService_id(), request.getRequestId());
        log.info("cancelThirdPartOrder--orderId = {} cancel request code = {} and msg = {}!!!",
                order.getOrderId(), homeResponse.getCode(), homeResponse.getMsg());

        if (homeResponse.getCode() == 0) {
            Request new_request = new Request();
            request.setRequestId(request.getRequestId());
            request.setRequestStatus(-1);
            if(requestService.updateStatus(new_request)) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }

    public String buildResponse(int status, String msg) {
        return JSONObject.toJSONString(new SimpleResult(status, msg));
    }


}
