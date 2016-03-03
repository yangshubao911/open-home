/**
 * 
 */
package com.shihui.openpf.home.resource;

import java.io.File;

import javax.annotation.Resource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


import com.shihui.openpf.common.tools.StringUtil;
import org.springframework.stereotype.Controller;

import com.shihui.openpf.home.api.OrderManage;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderCancelType;

import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月19日 下午3:00:15
 */
@Controller
@Path("/v2/openpf/home/order")
public class OrderResource {

    @Resource
    OrderManage orderManage;


    @Path("/listById")
    @GET
    @BaseInfo(desc = "根据条件查询订单列表", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({ MediaType.APPLICATION_JSON })
    public String listById(@Context RequestContext rc,
                           @ParamDesc(desc = "实惠用户id", isRequired = false) @QueryParam("userId") String userId,
                           @ParamDesc(desc = "充值手机号码", isRequired = false) @QueryParam("phoneNum") String phoneNum,
                           @ParamDesc(desc = "商户id", isRequired = false) @QueryParam("merchantId") String merchantId,
                           @ParamDesc(desc = "订单id", isRequired = false) @QueryParam("orderId") String orderId,
                           @ParamDesc(desc = "订单状态", isRequired = false) @QueryParam("status") String status,
                           @ParamDesc(desc = "开始时间", isRequired = false) @QueryParam("startTime") String startTime,
                           @ParamDesc(desc = "结束时间", isRequired = false) @QueryParam("endTime") String endTime,
                           @ParamDesc(desc = "页标", isRequired = false) @QueryParam("cursor") @DefaultValue("1") int cursor ,
                           @ParamDesc(desc = "每页显示数量", isRequired = false) @QueryParam("count") @DefaultValue("10") int count) {


        try {
            Order queryOrder = new Order();
            if(!StringUtil.isEmpty(orderId))
            queryOrder.setOrderId(Long.parseLong(orderId));
            if(!StringUtil.isEmpty(status))
            queryOrder.setOrderStatus(Byte.valueOf(status));
            if(!StringUtil.isEmpty(phoneNum))
            queryOrder.setPhone(phoneNum);
            if(!StringUtil.isEmpty(userId))
            queryOrder.setUserId(Long.parseLong(userId));
            if(!StringUtil.isEmpty(merchantId))
            queryOrder.setMerchantId(Integer.parseInt(merchantId));
            return orderManage.queryOrderList(rc,queryOrder,startTime,endTime,cursor,count );
        }catch (Exception e){

        }
        return null;
    }

    @GET
         @Path("/detail")
         @BaseInfo(desc = "查询订单详情", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
         public String detail(
            @Context RequestContext rc,
            @ParamDesc(isRequired = true, desc = "订单ID") @QueryParam("orderId") long orderId
    ) {
        return orderManage.queryOrder(orderId);
    }

    @GET
    @Path("/thirdOrder/detail")
    @Produces({MediaType.TEXT_HTML})
    @BaseInfo(desc = "查询第三方订单详情", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
    public String thirdOrderDetail(
            @Context RequestContext rc,
            @ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
            @ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
            @ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
            @ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
            @ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign
    ) {
        return orderManage.queryThirdOrder(key, serviceType, orderId, version, sign);
    }

    @POST
    @Path("/thirdOrder/cancel")
    @Produces({MediaType.TEXT_HTML})
    @BaseInfo(desc = "第三方取消订单接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
    public String thirdOrderCancel(
            @Context RequestContext rc,
            @ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
            @ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
            @ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
            @ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
            @ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign
    ) {
        return orderManage.cancelThirdOrder(key, serviceType, orderId, version, sign);
    }


    @POST
    @Path("/thirdOrder/update")
    @Produces({MediaType.TEXT_HTML})
    @BaseInfo(desc = "第三方更新订单接口", status = ApiStatus.PUBLIC, needAuth = AuthType.OPTION)
    public String thirdOrderUpdate(
            @Context RequestContext rc,
            @ParamDesc(isRequired = true, desc = "商户key") @QueryParam("key") String key,
            @ParamDesc(isRequired = true, desc = "业务类型") @QueryParam("serviceType") int serviceType,
            @ParamDesc(isRequired = true, desc = "第三方订单ID") @QueryParam("orderId") String orderId,
            @ParamDesc(isRequired = true, desc = "接口版本") @QueryParam("version") String version,
            @ParamDesc(isRequired = true, desc = "签名") @QueryParam("sign") String sign,
            @ParamDesc(isRequired = true, desc = "订单状态") @QueryParam("status") int status
    ) {
        return orderManage.updateThirdOrder(key, serviceType, orderId, version, sign, status);
    }


    @GET
    @Path("/cancel")
    @BaseInfo(desc = "取消订单接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
    public String cancel(
            @Context RequestContext rc,
            @ParamDesc(isRequired = true, desc = "订单ID") @QueryParam("orderId") long orderId,
            @ParamDesc(isRequired = true, desc = "取消订单类型") @QueryParam("cancelType") String cancelType
    ) {
        OrderCancelType orderCancelType = null;
        try {
            orderCancelType = OrderCancelType.valueOf("REFUND_PARTIAL");
        } catch (Exception e) {

        }
        return orderManage.cancelLocalOrder(orderId, orderCancelType);

    }

    @GET
    @Path("/unusualOrder/count")
    @BaseInfo(desc = "取消订单接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
    public String count(
            @Context RequestContext rc) {
        return orderManage.countunusual();
    }

    @GET
    @Path("/unusualOrder/query")
    @BaseInfo(desc = "取消订单接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
    public String query(
            @Context RequestContext rc) {
        return orderManage.queryUnusual();
    }

    @GET
    @Path("/unusualOrder/export")
    @Produces("application/vnd.ms-excel; charset=UTF-8")
    @BaseInfo(desc = "取消订单接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
    public  Response export(
            @Context RequestContext rc) {
        String fileName = orderManage.exportUnusual();
        File file = new File(fileName);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=\"unusualOrder.xlsx\"");
        response.header("content-transfer-encoding", "binary");

        return response.build();
    }



}
