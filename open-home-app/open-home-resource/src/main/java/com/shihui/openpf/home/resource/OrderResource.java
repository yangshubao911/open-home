/**
 * 
 */
package com.shihui.openpf.home.resource;

import com.shihui.openpf.home.api.OrderManage;
import com.shihui.openpf.home.model.Order;
import com.shihui.openpf.home.model.OrderCancelType;
import com.shihui.openpf.home.util.DataExportUtils;
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
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        return orderManage.cancelOrder(orderId, orderCancelType);

    }

    @GET
    @Path("/unusualOrder/count")
    @BaseInfo(desc = "取消订单接口", status = ApiStatus.INTERNAL, needAuth = AuthType.OPTION)
    public String count(
            @Context RequestContext rc) {
        return orderManage.countunusual();
    }

    @POST
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
    public static Response export(
            @Context RequestContext rc) {
        List<String> title = new ArrayList<>();
        title.add("1");
        title.add("2");
        title.add("3");
        List<List<Object>> data = new ArrayList<>();
        List<Object> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        data.add(list);
        String fileName = "";
        try {
             fileName = DataExportUtils.genExcel(String.valueOf(System.currentTimeMillis()), "unusualOrder", title, data,
                    "utf-8");
        }catch (Exception e){
            e.printStackTrace();
        }
        File file = new File(fileName);
        Response.ResponseBuilder response = Response.ok((Object) file);
        response.header("Content-Disposition",
                "attachment; filename=\"test.xls\"");
        return response.build();
    }



}
