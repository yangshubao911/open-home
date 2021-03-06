package com.shihui.openpf.home.resource;


import com.shihui.openpf.home.model.OrderForm;
import com.shihui.openpf.home.service.api.ClientService;
import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * Created by zhoutc on 2016/2/20.
 */
@Controller
@Path("/v2/openpf/home/client")
public class ClientResource {
    @Resource
    ClientService clientService;

    @Path("/goods/list")
    @GET
    @BaseInfo(desc = "查询归属城市所有分类商品", needAuth = AuthType.REQUIRED, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String list(@Context RequestContext rc,
                       @Context HttpServletRequest request,
                       @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                       @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                       @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                       @ParamDesc(desc = "服务社Id", isRequired = false) @QueryParam("mid") Long mid,
                       @ParamDesc(desc = "城市Id", isRequired = false) @QueryParam("cityId") int cityId) {
        return clientService.listGoods(serviceId, userId, groupId, mid , rc, request, cityId);
    }


    @Path("/goods/detail")
    @GET
    @BaseInfo(desc = "查询商品详情", needAuth = AuthType.REQUIRED, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String detail(@Context RequestContext rc,
                         @Context HttpServletRequest request,
                         @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                         @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                         @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                         @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                         @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                         @ParamDesc(desc = "服务社Id", isRequired = false) @QueryParam("mid") Long mid,
                         @ParamDesc(desc = "城市Id", isRequired = false) @QueryParam("cityId") int cityId) {
        return clientService.detail(serviceId, userId, groupId, categoryId, goodsId, mid, rc, request, cityId);
    }

    @Path("/goods/h5/detail")
    @GET
    @BaseInfo(desc = "查询大类下所有商品", needAuth = AuthType.OPTION, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String h5Detail(@Context RequestContext rc,
                         @Context HttpServletRequest request,
                         @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId) {
        return clientService.detail(goodsId);
    }

    @Path("/order/confirm")
    @GET
    @BaseInfo(desc = "查询大类下所有商品", needAuth = AuthType.REQUIRED, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String orderConfirm(@Context RequestContext rc,
                               @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                               @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                               @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                               @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                               @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                               @ParamDesc(desc = "是否使用实惠现金", isRequired = true) @QueryParam("costSh") int costSh,
                               @ParamDesc(desc = "服务社Id", isRequired = false) @QueryParam("mid") Long mid) {
        return clientService.orderConfirm(rc,  serviceId, userId, groupId, categoryId, goodsId, costSh);
    }

    @Path("/area/time")
    @GET
    @BaseInfo(desc = "查询时间接口", needAuth = AuthType.REQUIRED, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String queryTime(@Context RequestContext rc,
                            @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                            @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                            @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                            @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                            @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                            @ParamDesc(desc = "谷歌经度", isRequired = true) @QueryParam("longitude") String longitude,
                            @ParamDesc(desc = "谷歌纬度", isRequired = true) @QueryParam("latitude") String latitude,
                            @ParamDesc(desc = "服务社Id", isRequired = false) @QueryParam("mid") Long mid) {

        return clientService.queryTime(serviceId, userId, groupId, categoryId, goodsId, longitude, latitude);
    }

    @Path("/order/create")
    @POST
    @BaseInfo(desc = "创建订单接口", needAuth = AuthType.REQUIRED, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String orderCreate(@Context RequestContext rc,
                              @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                              @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                              @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                              @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                              @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                              @ParamDesc(desc = "高德经度", isRequired = true) @QueryParam("longitude") String longitude,
                              @ParamDesc(desc = "高德纬度", isRequired = true) @QueryParam("latitude") String latitude,
                              @ParamDesc(desc = "是否使用实惠现金", isRequired = true) @QueryParam("costSh") int costSh,
                              @ParamDesc(desc = "实际支付金额", isRequired = true) @QueryParam("actPay") String actPay,
                              @ParamDesc(desc = "实际实惠现金抵扣", isRequired = true) @QueryParam("actOffset") String actOffset,
                              @ParamDesc(desc = "服务开始时间", isRequired = true) @QueryParam("serviceTime") String serviceTime,
                              @ParamDesc(desc = "联系人姓名", isRequired = true) @QueryParam("contactName") String contactName,
                              @ParamDesc(desc = "服务地址", isRequired = true) @QueryParam("serviceAddress") String serviceAddress,
                              @ParamDesc(desc = "详细地址", isRequired = true) @QueryParam("detailAddress") String detailAddress,
                              @ParamDesc(desc = "联系电话", isRequired = true) @QueryParam("servicePhone") String servicePhone,
                              @ParamDesc(desc = "可选商户", isRequired = true) @QueryParam("merchants") String merchants,
                              @ParamDesc(desc = "下单商品版本", isRequired = true) @QueryParam("goodsVersion") int goodsVersion,
                              @ParamDesc(desc = "备注信息", isRequired = false) @QueryParam("remark") String remark,
                              @ParamDesc(desc = "服务社Id", isRequired = false) @QueryParam("mid") Long mid) {

        String merchantArray = merchants.replace("[","").replace("]","");
        OrderForm orderForm = new OrderForm();
        orderForm.setServiceId(serviceId);
        orderForm.setGroupId(groupId);
        orderForm.setUserId(userId);
        orderForm.setCategoryId(categoryId);
        orderForm.setGoodsId(goodsId);
        orderForm.setLongitude(longitude);
        orderForm.setLatitude(latitude);
        orderForm.setCostSh(costSh);
        orderForm.setActPay(actPay);
        orderForm.setActOffset(actOffset);
        orderForm.setServiceTime(serviceTime);
        orderForm.setContactName(contactName);
        orderForm.setServiceAddress(serviceAddress);
        orderForm.setDetailAddress(detailAddress);
        orderForm.setMerchants(merchantArray);
        orderForm.setRemark(remark);
        orderForm.setGoodsVersion(goodsVersion);
        orderForm.setServicePhone(servicePhone);
        orderForm.setMid(mid);
        int appId = rc.getOriginRequest().getIntHeader("X-APP-ID");
        //实惠默认appid为5
        orderForm.setAppId(appId <= 0 ? 5 : appId);
        return clientService.orderCreate(orderForm,rc);
    }

    @Path("/order/test")
    @GET
    @BaseInfo(desc = "测试接单完成接口", needAuth = AuthType.OPTION, status = ApiStatus.PUBLIC, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public boolean orderTest(@Context RequestContext rc,
                              @ParamDesc(desc = "订单Id", isRequired = true) @QueryParam("orderId") long orderId,
                              @ParamDesc(desc = "状态变更", isRequired = true) @QueryParam("status") int status){

        return  clientService.testOrder(orderId,status);
    }


}
