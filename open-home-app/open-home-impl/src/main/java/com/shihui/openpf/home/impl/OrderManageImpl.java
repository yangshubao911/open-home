package com.shihui.openpf.home.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;


import com.shihui.api.order.common.enums.OrderStatusEnum;
import com.shihui.api.order.common.enums.OrderTypeEnum;
import com.shihui.api.order.vo.SimpleResult;
import com.shihui.openpf.common.tools.AlgorithmUtil;
import com.shihui.openpf.common.tools.SignUtil;
import com.shihui.openpf.common.tools.StringUtil;
import com.shihui.openpf.home.model.*;
import com.shihui.openpf.home.service.api.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shihui.openpf.common.dubbo.api.MerchantBusinessManage;
import com.shihui.openpf.common.dubbo.api.MerchantManage;
import com.shihui.openpf.common.dubbo.api.ServiceManage;
import com.shihui.openpf.common.model.Group;
import com.shihui.openpf.common.model.Merchant;
import com.shihui.openpf.common.model.MerchantBusiness;
import com.shihui.openpf.common.service.api.GroupManage;
import com.shihui.openpf.common.tools.DataExportUtils;
import com.shihui.openpf.home.api.HomeServProviderService;
import com.shihui.openpf.home.api.OrderManage;

import me.weimi.api.commons.context.RequestContext;

/**
 * Created by zhoutc on 2016/1/21.
 */
@Service
public class OrderManageImpl implements OrderManage {

    @Resource(name="openOrderService")
    OrderService orderService;

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

    @Resource
    MerchantGoodsService merchantGoodsService;

    @Resource
    ServiceManage serviceManage;

    @Resource
    MerchantBusinessManage merchantBusinessManage;

    @Resource
    GroupManage groupManage;


    @Resource
    OrderSystemService orderSystemService;

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
    public String queryOrderList(RequestContext rc, Order queryOrder, String startTime, String endTime, int page, int size) {
        try {
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
                JSONObject order_json = buildOrderVo(order);
                if (order_json != null)
                    orders_json.add(order_json);
            }
            result.put("orders", orders_json);
            return result.toJSONString();
        } catch (Exception e) {
            log.error("OrderManageImpl queryOrderList error!!", e);
        }
        return "";
    }

    public JSONObject buildOrderVo(Order order) {
        try {
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
            Merchant merchant = merchantManage.getById(order.getMerchantId());
            order_json.put("merchantName", merchant.getMerchantName());

            MerchantGoods merchantGoods = new MerchantGoods();
            merchantGoods.setGoodsId(order.getGoodsId());
            merchantGoods.setMerchantId(order.getMerchantId());
            MerchantGoods db_merchantGoods = merchantGoodsService.queryMerchantGoods(merchantGoods);

            order_json.put("settlement", db_merchantGoods.getSettlement());
            DateTime dateTime = new DateTime(order.getCreateTime());
            order_json.put("createTime", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
            order_json.put("pay", order.getPay());
            order_json.put("status", order.getOrderStatus());
            order_json.put("statusName", OrderStatusEnum.parse(order.getOrderStatus()).getName());
            return order_json;
        } catch (Exception e) {
            log.error("OrderManageImpl buildOrderVo error!!", e);
        }

        return null;
    }

    /**
     * 查询订单详情
     *
     * @param orderId 订单ID
     * @return 返回订单详情
     */

    @Override
    public String queryOrder(long orderId) {
        try {
            JSONObject result = new JSONObject();
            result.put("orderId", orderId);
            Order order = orderService.queryOrder(orderId);

            if (order == null) return null;
         /*   PaymentTypeEnum paymentTypeEnum = null;
            if (order.getPaymentType() == PaymentTypeEnum.Alipay.getValue())
                paymentTypeEnum = PaymentTypeEnum.Alipay;
            if (order.getPaymentType() == PaymentTypeEnum.Wxpay.getValue())
                paymentTypeEnum = PaymentTypeEnum.Wxpay;*/

         /*   OrderPaymentMapping orderPaymentMapping = orderDubboService.queryPaymentMapping(orderId, paymentTypeEnum);
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

            }*/
            result.put("createTime", new DateTime(order.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"));
            result.put("userId", order.getUserId());
            result.put("phone", order.getPhone());

            Goods goods = goodsService.findById(order.getGoodsId());
            result.put("price", goods.getPrice());

            Merchant merchant = merchantManage.getById(order.getMerchantId());
            result.put("merchantId", order.getMerchantId());
            result.put("merchantName", merchant.getMerchantName());

            MerchantGoods merchantGoods = new MerchantGoods();
            merchantGoods.setGoodsId(order.getGoodsId());
            merchantGoods.setMerchantId(order.getMerchantId());
            MerchantGoods db_merchantGoods = merchantGoodsService.queryMerchantGoods(merchantGoods);

            result.put("settlement", db_merchantGoods.getSettlement());
            BigDecimal originalPrice = new BigDecimal(order.getPay()).add(new BigDecimal(order.getShOffSet()));
            result.put("originalPrice", originalPrice.stripTrailingZeros().toPlainString());
            result.put("pay", order.getPay());
            result.put("shOffset", order.getShOffSet());
            result.put("status", order.getOrderStatus());
            result.put("statusName", OrderStatusEnum.parse(order.getOrderStatus()).getName());
            result.put("due", new BigDecimal(goods.getPrice()).
                    subtract(new BigDecimal(goods.getShOffSet())).
                    setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            return result.toJSONString();
        } catch (Exception e) {
            log.error("OrderManageImpl queryOrder error!!", e);
        }

        return "";
    }

    /**
     * 查询第三方订单
     *
     * @param orderId 第三方订单ID
     * @return 返回订单详情
     */
    @Override
    public String queryThirdOrder(String key, Integer serviceType, String orderId, String version, String sign) {
        HomeResponse response = new HomeResponse();
        try {
            Merchant merchant = merchantManage.getByKey(key);
            if (merchant == null) {
                return buildHomeResponse(1001, "参数错误");
            }
            com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceType);
            if (service == null) {
                return buildHomeResponse(2003, "服务类型不支持");
            }
            MerchantBusiness search = new MerchantBusiness();
            search.setMerchantId(merchant.getMerchantId());
            search.setServiceId(service.getServiceId());
            MerchantBusiness merchantBusiness = merchantBusinessManage.queryById(search);
            if (merchantBusiness == null) {
                return buildHomeResponse(2003, "服务类型不支持");
            }
            if (merchant.getMerchantStatus() != 1 || service.getServiceStatus() != 1 ||
                    merchantBusiness.getStatus() != 1) {
                return buildHomeResponse(2003, "服务类型不支持");
            }

            TreeMap<String, String> param = new TreeMap<>();
            param.put("key", key);
            param.put("serviceType", String.valueOf(serviceType));
            param.put("orderId", orderId);
            param.put("version", version);
            String server_sign = SignUtil.genSign(param, merchant.getMd5Key());
            if (server_sign.compareTo(sign) != 0) {
                return buildHomeResponse(1002, "签名错误");
            }
            Request request = new Request();
            request.setRequestId(orderId);
            Request db_request = requestService.queryById(request);
            if (db_request == null) {
                return buildHomeResponse(3001, "未查询到订单");
            }
            Order order = orderService.queryOrder(db_request.getOrderId());
            if (order == null) {
                return buildHomeResponse(3001, "未查询到订单");
            }

            Goods goods = goodsService.findById(order.getGoodsId());
            if (goods == null) {
                return buildHomeResponse(1004, "其他错误");
            }
            Contact contact = contactService.queryByOrderId(db_request.getOrderId());

            JSONObject result = new JSONObject();
            result.put("code", 0);
            result.put("msg", "查询成功");

            JSONObject order_json = new JSONObject();
            order_json.put("orderId", orderId);

            Group group = groupManage.getGroupInfoByGid(order.getGid());
            if (group != null) {
                order_json.put("cityId", group.getCityId());
                order_json.put("cityName", group.getName());
            }
            order_json.put("serviceAddress", contact.getServiceAddress());
            order_json.put("detailAddress", contact.getDetailAddress());
            order_json.put("longitude", contact.getLongitude());
            order_json.put("latitude", contact.getLatitude());
            order_json.put("phone", contact.getPhoneNum());
            order_json.put("amount", 1);
            order_json.put("goodsId", goods.getCategoryId());
            order_json.put("serviceStartTime", contact.getServiceStartTime());
            order_json.put("remark", order.getRemark());
            order_json.put("extend", order.getExtend());
            result.put("result", order_json);
            return result.toJSONString();
        } catch (Exception e) {
            log.error("OrderManageImpl queryThirdOrder error", e);
            return buildHomeResponse(1004, "其他错误");
        }

    }

    /**
     * 取消第三方订单
     *
     * @param orderId 订单ID
     * @return 返回取消订单结果
     */
    @Override
    public String cancelThirdOrder(String key, int serviceType, String orderId, String version, String sign) {
        HomeResponse response = new HomeResponse();
        try {
            Merchant merchant = merchantManage.getByKey(key);
            if (merchant == null) {
                return buildHomeResponse(1001, "参数错误");
            }
            com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceType);
            if (service == null) {
                return buildHomeResponse(2003, "服务类型不支持");
            }
            MerchantBusiness search = new MerchantBusiness();
            search.setMerchantId(merchant.getMerchantId());
            search.setServiceId(service.getServiceId());
            MerchantBusiness merchantBusiness = merchantBusinessManage.queryById(search);
            if (merchantBusiness == null) {
                return buildHomeResponse(2003, "服务类型不支持");
            }
            if (merchant.getMerchantStatus() != 1 || service.getServiceStatus() != -1 ||
                    merchantBusiness.getStatus() != -1) {
                return buildHomeResponse(2003, "服务类型不支持");
            }

            TreeMap<String, String> param = new TreeMap<>();
            param.put("key", key);
            param.put("serviceType", String.valueOf(serviceType));
            param.put("orderId", orderId);
            param.put("version", version);
            String server_sign = SignUtil.genSign(param, merchant.getMd5Key());
            if (server_sign.compareTo(sign) != 0) {
                return buildHomeResponse(1002, "签名错误");
            }
            Request request = new Request();
            request.setRequestId(orderId);
            Request db_request = requestService.queryById(request);
            if (db_request == null) {
                return buildHomeResponse(3001, "未查询到订单");
            }
            Order order = orderService.queryOrder(request.getOrderId());
            if (order == null) {
                return buildHomeResponse(3001, "未查询到订单");
            }
            OrderCancelType orderCancelType = null;

            Integer status = order.getOrderStatus();
            switch (OrderStatusEnum.parse(status)) {
                //TODO 未完成
            }
            return cancelOrder(order, orderCancelType);
        } catch (Exception e) {
            log.error("OrderManageImpl queryThirdOrder error", e);
            return buildHomeResponse(1004, "其他错误");
        }
    }

    /**
     * 更新第三方订单
     *
     * @param orderId 订单ID
     * @return 返回更新订单结果
     */
    @Override
    public String updateThirdOrder(String key, int serviceType, String orderId, String version, String sign, int status) {
        HomeResponse response = new HomeResponse();
        try {
            Merchant merchant = merchantManage.getByKey(key);
            if (merchant == null) {
                return buildHomeResponse(1001, "参数错误");
            }
            com.shihui.openpf.common.model.Service service = serviceManage.findById(serviceType);
            if (service == null) {
                return buildHomeResponse(2003, "服务类型不支持");
            }
            MerchantBusiness search = new MerchantBusiness();
            search.setMerchantId(merchant.getMerchantId());
            search.setServiceId(service.getServiceId());
            MerchantBusiness merchantBusiness = merchantBusinessManage.queryById(search);
            if (merchantBusiness == null) {
                return buildHomeResponse(2003, "服务类型不支持");
            }
            if (merchant.getMerchantStatus() != 1 || service.getServiceStatus() != 1 ||
                    merchantBusiness.getStatus() != 1) {
                return buildHomeResponse(2003, "服务类型不支持");
            }

            TreeMap<String, String> param = new TreeMap<>();
            param.put("key", key);
            param.put("serviceType", String.valueOf(serviceType));
            param.put("orderId", orderId);
            param.put("version", version);
            param.put("status", String.valueOf(status));
            String server_sign = SignUtil.genSign(param, merchant.getMd5Key());
            if (server_sign.compareTo(sign) != 0) {
                return buildHomeResponse(1002, "签名错误");
            }
            Request request = new Request();
            request.setRequestId(orderId);
            Request db_request = requestService.queryById(request);
            if (db_request == null) {
                return buildHomeResponse(3001, "未查询到订单");
            }
            Order order = orderService.queryOrder(db_request.getOrderId());
            if (order == null) {
                return buildHomeResponse(3001, "未查询到订单");
            }

            MerchantGoods merchantGoods_search = new MerchantGoods();
            merchantGoods_search.setMerchantId(order.getMerchantId());
            merchantGoods_search.setGoodsId(order.getGoodsId());
            MerchantGoods merchantGoods = merchantGoodsService.queryMerchantGoods(merchantGoods_search);

            OrderCancelType orderCancelType = null;
            Integer order_status = order.getOrderStatus();
            HomeOrderStatusEnum db_statusEnum = HomeOrderStatusEnum.parse(db_request.getRequestStatus());
            HomeOrderStatusEnum statusEnum = HomeOrderStatusEnum.parse(status);
            JSONObject settlementJson = new JSONObject();
            settlementJson.put("settlePrice", StringUtil.yuan2hao(merchantGoods.getSettlement()));
            settlementJson.put("settleMerchantId", merchant.getMerchantCode());

            switch (statusEnum) {

                case OrderConfirmed:
                    if (db_statusEnum.getValue() != HomeOrderStatusEnum.OrderUnConfirm.getValue()) {
                        return buildHomeResponse(1, "状态流转错误");
                    }
                    boolean updateRequest = updateRequest(orderId, statusEnum.getValue());

                    if (updateRequest) {
                        boolean success = orderSystemService.success(OrderTypeEnum.DoorTDoor.getValue(), order.getOrderId(),order.getGoodsId(),
                                settlementJson.toString(), OrderStatusEnum.OrderUnStockOut.getValue());

                        if (success) {
                            return buildHomeResponse(0, "success");
                        } else {
                            return buildHomeResponse(1, "更新失败");
                        }

                    } else {
                        return buildHomeResponse(1,"更新失败");
                    }
                case OrderComplete:
                    if (db_statusEnum.getValue() != HomeOrderStatusEnum.OrderConfirmed.getValue()) {
                       return buildHomeResponse(1, "状态流转错误");
                    }
                    boolean updateRequest1 = updateRequest(orderId, statusEnum.getValue());
                    if (updateRequest1) {
                        boolean success = orderSystemService.complete(order.getOrderId(), order.getGoodsId(), settlementJson.toString(),
                                OrderStatusEnum.OrderDistribute.getValue());

                        if (success) {
                            return buildHomeResponse(0, "success");
                        } else {
                            return buildHomeResponse(1, "更新订单失败");
                        }

                    } else {
                        return buildHomeResponse(1,"更新订单失败");
                    }

                case OrderCancel:
                    long refundsPrice = StringUtil.yuan2hao(order.getPay());

                    switch (db_statusEnum) {
                        case UnPay:
                            //取消订单


                        case OrderUnConfirm:
                            //商户取消订单流程
                            boolean cancel = orderSystemService.fail(OrderTypeEnum.DoorTDoor.getValue(), order.getOrderId(), order.getGoodsId(),
                                    merchant.getMerchantCode(),refundsPrice, status, "商户发起取消");
                            if (cancel) {
                                Request request1 = new Request();
                                request1.setRequestId(orderId);
                                request1.setRequestStatus(statusEnum.getValue());
                                boolean update_status = requestService.updateStatus(request1);
                                if (update_status) {
                                    return buildHomeResponse(0,"success");
                                } else {
                                    return buildHomeResponse(1,"更新订单失败");
                                }

                            } else {
                                return buildHomeResponse(1,"更新订单失败");
                            }
                        case OrderConfirmed:
                            //不允许取消
                        case OrderComplete:
                            //不允许取消
                    }
                default:
                    return buildHomeResponse(1, "状态流转错误");

            }
        } catch (Exception e) {
            log.error("OrderManageImpl queryThirdOrder error", e);
            return buildHomeResponse(1004, "其他错误");
        }
    }

    /**
     * 取消平台订单
     *
     * @param orderId 订单ID
     * @return 返回取消订单结果
     */
    @Override
    public String cancelLocalOrder(long orderId, OrderCancelType orderCancelType) {
        Order order = orderService.queryOrder(orderId);
        if (order == null) {
            return buildHomeResponse(3001, "未查询到订单");
        }
        return cancelOrder(order, orderCancelType);
    }

    /**
     * 取消订单
     *
     * @param order 订单
     * @return 返回订单详情
     */
    public String cancelOrder(Order order, OrderCancelType orderCancelType) {
        if (orderCancelType == null) return buildHomeResponse(1004, "其他错误");
        //Order order = orderService.queryOrder(orderId);
        long orderId = order.getOrderId();
        com.shihui.api.order.vo.SimpleResult orderDetailVo = orderSystemService.backendOrderDetail(orderId);
        if (order == null || orderDetailVo == null)
            return buildHomeResponse(3001, "未查询到订单");

/*        if (order.getOrderStatus() == OrderStatusEnum.OrderBackClose.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderCloseByMerchant.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderCloseByManual.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderCloseOfOutTime.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderHadRefund.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderMrchantClose.getValue() ||
                order.getOrderStatus() == OrderStatusEnum.OrderRefunding.getValue()) {
            return buildHomeResponse(3101, "取消订单失败");
        }*/

        Contact contact = contactService.queryByOrderId(orderId);
        if (contact == null) return buildHomeResponse(1004, "其他错误");
        DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMddHHmmss");
        DateTime serviceStartTime = DateTime.parse(contact.getServiceStartTime(), format);
        DateTime now = new DateTime();

        if (serviceStartTime.getMillis() - 2 * 60 * 60 * 1000 <= now.getMillis()) {
            if (orderCancelType.getValue() != OrderCancelType.REFUND_PARTIAL.getValue())
                return buildHomeResponse(3102, "取消订单超时");
        }

        String result = null;

       /* switch (orderCancelType) {
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
        }*/


        return result;
    }

    /**
     * 查询异常订单
     *
     * @return 返回订单详情
     */
    @Override
    public String countunusual() {
        int total = orderService.countUnusual();
        JSONObject result = new JSONObject();
        result.put("total", total);
        return result.toJSONString();

    }

    /**
     * 查询异常订单
     *
     * @return 订单列表
     */
    @Override
    public String queryUnusual() {

        List<Order> orders = orderService.queryUnusual();
        JSONArray orders_json = new JSONArray();
        for (Order order : orders) {
            orders_json.add(buildOrderVo(order));
        }
        JSONObject result = new JSONObject();
        result.put("orders", orders_json);
        return result.toJSONString();
    }

    private String NonPaymentCancel(Order order, com.shihui.api.order.po.Order orderDetailVo) {

        if (order.getOrderStatus() != OrderStatusEnum.OrderUnpaid.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderUnpaid.getValue()) {
            return buildHomeResponse(3101, "订单状态不为" + OrderStatusEnum.OrderUnpaid.getName());
        }

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        //2.调用dubbo接口取消未支付订单
        return "";
    }

    private String PaymentCancel(Order order, com.shihui.api.order.po.Order orderDetailVo) {

        if (order.getOrderStatus() != OrderStatusEnum.OrderDistribute.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderDistribute.getValue()) {
            return buildHomeResponse(3101, "订单状态不为" + OrderStatusEnum.OrderDistribute.getName());
        }

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        //2.调用dubbo接口取消未支付订单
        return "";
    }

    private String MerchantCancel(Order order, com.shihui.api.order.po.Order orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderUnConfirm.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderUnConfirm.getValue()) {
            return buildHomeResponse(3101, "订单状态不为" + OrderStatusEnum.OrderUnConfirm.getName());
        }
        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        long merchantCode = merchant.getMerchantCode();
        //2.调用dubbo接口取消未支付订单
        return "";
    }

    private String OutOfTimeCancel(Order order, com.shihui.api.order.po.Order orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderUnpaid.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderUnpaid.getValue()) {
            return buildHomeResponse(3101, "订单状态不为" + OrderStatusEnum.OrderUnpaid.getName());
        }
        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        //2.更新订单表
        return buildHomeResponse(0, "取消订单成功");
    }

    private String MerchantOutOfTimeCancel(Order order, com.shihui.api.order.po.Order orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderUnConfirm.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderUnConfirm.getValue()) {
            return buildHomeResponse(3101, "订单状态不为" + OrderStatusEnum.OrderUnConfirm.getName());
        }
        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        //2.更新订单表
        return buildHomeResponse(0, "取消订单成功");
    }

    private String PhpCloseCancel(Order order, com.shihui.api.order.po.Order orderDetailVo) {

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        //2.更新订单表
        boolean update = orderService.updateOrder(order.getOrderId(), OrderStatusEnum.BackClose);
        if (update)
            return buildHomeResponse(0, "取消订单成功");
        else
            return buildHomeResponse(3101, "取消订单失败");
    }

    private String RefundPartial(Order order, com.shihui.api.order.po.Order orderDetailVo) {
        if (order.getOrderStatus() != OrderStatusEnum.OrderDistribute.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderDistribute.getValue() ||
                order.getOrderStatus() != OrderStatusEnum.OrderUnConfirm.getValue() ||
                orderDetailVo.getOrderStatus().getValue() != OrderStatusEnum.OrderUnConfirm.getValue()) {
            return buildHomeResponse(3101, "订单状态不为" + OrderStatusEnum.OrderUnConfirm.getName() + "或" + OrderStatusEnum.OrderDistribute.getName());
        }

        //1.取消第三方订单
        Merchant merchant = merchantManage.getById(order.getMerchantId());
        if (!cancelThirdPartOrder(order, merchant)) {
            return buildHomeResponse(3101, "取消第三方订单失败");
        }
        long merchantCode = merchant.getMerchantCode();
        long refundMoney_l = 0l;
        long settlementMoney_l = 0l;
        //2.调用部分退款接口

        return "";
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
            if (requestService.updateStatus(new_request)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 导出异常订单
     *
     * @return 订单列表
     */
    @Override
    public String exportUnusual() {

        List<String> title = new ArrayList<>();
        title.add("序号");
        title.add("订单号");
        title.add("下单时间");
        title.add("业务类型");
        title.add("服务提供商");
        title.add("服务商结算价");
        title.add("实惠价（元）");
        title.add("实惠现金补贴（元）");
        title.add("实际用户支付（元）");
        title.add("下单状态");


        List<List<Object>> data = new ArrayList<>();
        List<Order> orders = orderService.queryUnusual();
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            List<Object> list = new ArrayList<>();
            list.add(i);
            list.add(order.getOrderId());
            list.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getCreateTime()));
            com.shihui.openpf.common.model.Service service = serviceManage.findById(order.getService_id());
            list.add(service.getServiceName());
            Merchant merchant = merchantManage.getById(order.getMerchantId());
            list.add(merchant.getMerchantName());
            MerchantGoods merchantGoods = new MerchantGoods();
            merchantGoods.setMerchantId(order.getMerchantId());
            merchantGoods.setGoodsId(order.getGoodsId());
            MerchantGoods db_merchantGoods = merchantGoodsService.queryMerchantGoods(merchantGoods);
            list.add(db_merchantGoods.getSettlement());
            BigDecimal price = new BigDecimal(order.getShOffSet()).add(new BigDecimal(order.getPay()));
            list.add(price.stripTrailingZeros().toPlainString());
            list.add(order.getShOffSet());
            list.add(order.getPay());
            list.add(OrderStatusEnum.parse(order.getOrderStatus()).getName());
            data.add(list);
        }

        String fileName = null;
        try {
            fileName = DataExportUtils.genExcel(String.valueOf(System.currentTimeMillis()), "unusualOrder", title, data,
                    "utf-8");
        } catch (Exception e) {

        }

        return fileName;
    }


    @Override
    public String yjzConverter(String orderId, int status, String pId, String version, String methodName, String sign) {

        Merchant merchant = merchantManage.getByKey(pId);
        if (merchant == null) {
            log.info("yjzConverter -- can't find merchant by key:{} requestId:{}", pId, orderId);
        }
        TreeMap<String, String> map = new TreeMap<>();
        map.put("orderId", orderId);
        map.put("status", String.valueOf(status));
        if (pId != null)
            map.put("pId", pId);
        if (version != null)
            map.put("version", version);
        if (methodName != null)
            map.put("methodName", methodName);
        map.put("sign", sign);

        Request request = new Request();
        request.setRequestId(orderId);
        request.setMerchantId(merchant.getMerchantId());
        Request db_request = requestService.queryById(request);

        Order order = orderService.queryOrder(request.getOrderId());


        MerchantGoods merchantGoods_search = new MerchantGoods();
        merchantGoods_search.setMerchantId(order.getMerchantId());
        merchantGoods_search.setGoodsId(order.getGoodsId());
        MerchantGoods merchantGoods = merchantGoodsService.queryMerchantGoods(merchantGoods_search);

        if (db_request == null) {
            log.info("yjzConverter -- can't find request by key:{} requestId:{}", pId, orderId);
        }

        if (!genSign(map, merchant.getMd5Key()).equalsIgnoreCase(sign))
            return JSONObject.toJSONString(new YjzUpdateResult(5, "签名错误", new String[0]));

        try {
            YjzOrderStatusEnum db_statusEnum = YjzOrderStatusEnum.parseServerValues(request.getRequestStatus());
            YjzOrderStatusEnum statusEnum = YjzOrderStatusEnum.parse(status);


            JSONObject settlementJson = new JSONObject();
            settlementJson.put("settlePrice", StringUtil.yuan2hao(merchantGoods.getSettlement()));
            settlementJson.put("settleMerchantId", merchant.getMerchantCode());

            switch (statusEnum) {
                case OrderConfirmed:
                    if (db_statusEnum.getValue() != YjzOrderStatusEnum.OrderUnConfirm.getValue()) {
                        return JSONObject.toJSONString(new YjzUpdateResult(2, "状态流转错误:" + statusEnum.getValue(), new String[0]));
                    }
                    boolean updateRequest = updateRequest(orderId, statusEnum.getServerValue());

                    if (updateRequest) {
                        boolean success = orderSystemService.success(OrderTypeEnum.DoorTDoor.getValue(), order.getOrderId(), order.getGoodsId(),
                                settlementJson.toString(), OrderStatusEnum.OrderUnStockOut.getValue());

                        if (success) {
                            return JSONObject.toJSONString(new YjzUpdateResult(0, "success", new String[0]));
                        } else {
                            return JSONObject.toJSONString(new YjzUpdateResult(1, "更新订单失败", new String[0]));
                        }

                    } else {
                        return JSONObject.toJSONString(new YjzUpdateResult(1, "更新失败", new String[0]));
                    }
                case OrderComplete:
                    if (db_statusEnum.getValue() != YjzOrderStatusEnum.OrderConfirmed.getValue()) {
                        return JSONObject.toJSONString(new YjzUpdateResult(2, "状态流转错误:" + statusEnum.getValue(), new String[0]));
                    }
                    boolean updateRequest1 = updateRequest(orderId, statusEnum.getServerValue());
                    if (updateRequest1) {
                        boolean success = orderSystemService.complete(order.getOrderId(),order.getGoodsId(), settlementJson.toJSONString(),
                                OrderStatusEnum.OrderHadReceived.getValue());

                        if (success) {
                            return JSONObject.toJSONString(new YjzUpdateResult(0, "success", new String[0]));
                        } else {
                            return JSONObject.toJSONString(new YjzUpdateResult(1, "更新订单失败", new String[0]));
                        }

                    } else {
                        return JSONObject.toJSONString(new YjzUpdateResult(1, "更新失败", new String[0]));
                    }

                case OrderCancel:
                    long refundsPrice = StringUtil.yuan2hao(order.getPay());

                    switch (db_statusEnum) {
                        case UnPay:
                            //取消订单


                        case OrderUnConfirm:
                            //商户取消订单流程
                            boolean cancel = orderSystemService.fail(OrderTypeEnum.DoorTDoor.getValue(), order.getOrderId(), order.getGoodsId(),
                                    merchant.getMerchantCode(),refundsPrice, status, "商户发起取消");
                            if (cancel) {
                                Request request1 = new Request();
                                request1.setRequestId(orderId);
                                request1.setRequestStatus(statusEnum.getServerValue());
                                boolean update_status = requestService.updateStatus(request1);
                                if (update_status) {
                                    return JSONObject.toJSONString(new YjzUpdateResult(0, "success", new String[0]));
                                } else {
                                    return JSONObject.toJSONString(new YjzUpdateResult(1, "更新订单失败", new String[0]));
                                }

                            } else {
                                return JSONObject.toJSONString(new YjzUpdateResult(1, "更新订单失败", new String[0]));
                            }
                        case OrderConfirmed:
                            //不允许取消
                            return JSONObject.toJSONString(new YjzUpdateResult(3, "当前状态不允许取消订单" + statusEnum.getValue(), new String[0]));

                        case OrderComplete:
                            //不允许取消
                            return JSONObject.toJSONString(new YjzUpdateResult(3, "当前状态不允许取消订单" + statusEnum.getValue(), new String[0]));

                    }
                default:
                    return JSONObject.toJSONString(new YjzUpdateResult(2, "状态流转错误:" + statusEnum.getValue(), new String[0]));
            }


        } catch (Exception e) {
            log.error("yjzConverter -- orderId:{} update status error!!!", e);
        }

        return JSONObject.toJSONString(new YjzUpdateResult(6, "状态码错误", new String[0]));

    }

    public boolean updateRequest(String requestId, int status) {
        Request update_request = new Request();
        update_request.setRequestId(requestId);
        update_request.setRequestStatus(status);
        return requestService.updateStatus(update_request);
    }


    public String buildResponse(int status, String msg) {
        return JSONObject.toJSONString(new SimpleResult(status, msg));
    }

    public String buildHomeResponse(int code, String msg) {
        HomeResponse response = new HomeResponse();
        response.setMsg(msg);
        response.setCode(code);
        return JSONObject.toJSONString(response);
    }

    /**
     * 计算签名
     *
     * @param param
     * @return
     */
    private String genSign(TreeMap<String, String> param, String md5Key) {
        StringBuilder temp = new StringBuilder();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            temp.append(entry.getKey()).append(entry.getValue());
        }
        temp.append(md5Key);
        String sign = AlgorithmUtil.MD5(temp.toString());
        return sign;
    }

}
