/**
 * 
 */
package com.shihui.openpf.home.resource;

import com.shihui.openpf.home.api.OrderManage;
import com.shihui.openpf.home.model.Order;
import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

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
                           @ParamDesc(desc = "开始时间", isRequired = false) @QueryParam("startTime") Long startTime,
                           @ParamDesc(desc = "结束时间", isRequired = false) @QueryParam("endTime") Long endTime,
                           @ParamDesc(desc = "页标", isRequired = false) @QueryParam("cursor") @DefaultValue("1") int cursor ,
                           @ParamDesc(desc = "每页显示数量", isRequired = false) @QueryParam("count") @DefaultValue("10") int count) {


        try {
            Order queryOrder = new Order();
            queryOrder.setOrderId(Long.parseLong(orderId));
            queryOrder.setOrderStatus(Byte.valueOf(status));
            queryOrder.setPhone(phoneNum);
            queryOrder.setUserId(Long.parseLong(userId));

            return orderManage.queryOrderList(rc,queryOrder,startTime,endTime,cursor,count );
        }catch (Exception e){

        }
        return null;
    }

    @GET
    @Path("/detail")
    @BaseInfo(desc = "查询订单详情", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
    public String cityOrderDetail(
            @Context RequestContext rc,
            @ParamDesc(isRequired = true, desc = "订单ID") @QueryParam("orderId") long orderId
    ) {
        return orderManage.queryOrder(orderId);
    }

}
