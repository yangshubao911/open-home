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
    @BaseInfo(desc = "查询归属城市所有分类商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String list(@Context RequestContext rc,
                       @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                       @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                       @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId) {
        return clientService.listGoods(serviceId, userId, groupId);
    }


    @Path("/goods/detail")
    @GET
    @BaseInfo(desc = "查询大类下所有商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String detail(@Context RequestContext rc,
                         @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                         @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                         @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                         @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                         @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId) {
        return clientService.detail(serviceId, userId, groupId, categoryId, goodsId);
    }

    @Path("/order/confrim")
    @GET
    @BaseInfo(desc = "查询大类下所有商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String orderConfirm(@Context RequestContext rc,
                               @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                               @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                               @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                               @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                               @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                               @ParamDesc(desc = "是否使用实惠现金", isRequired = true) @QueryParam("costSh") int costSh) {
        return clientService.orderConfirm(serviceId, userId, groupId, categoryId, goodsId, costSh);
    }

    @Path("/area/time")
    @GET
    @BaseInfo(desc = "查询时间接口", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String queryTime(@Context RequestContext rc,
                            @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                            @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                            @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                            @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                            @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                            @ParamDesc(desc = "谷歌经度", isRequired = true) @QueryParam("longitude") String longitude,
                            @ParamDesc(desc = "谷歌纬度", isRequired = true) @QueryParam("latitude") String latitude) {

        return clientService.queryTime(serviceId, userId, groupId, categoryId, goodsId, longitude, latitude);
    }

    @Path("/order/create")
    @POST
    @BaseInfo(desc = "查询时间接口", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String orderCreate(@Context RequestContext rc,
                              @ParamDesc(desc = "业务Id", isRequired = true) @QueryParam("serviceId") int serviceId,
                              @ParamDesc(desc = "小区Id", isRequired = true) @QueryParam("groupId") long groupId,
                              @ParamDesc(desc = "用户Id", isRequired = true) @QueryParam("userId") long userId,
                              @ParamDesc(desc = "商品分类Id", isRequired = true) @QueryParam("categoryId") int categoryId,
                              @ParamDesc(desc = "商品Id", isRequired = true) @QueryParam("goodsId") int goodsId,
                              @ParamDesc(desc = "谷歌经度", isRequired = true) @QueryParam("longitude") String longitude,
                              @ParamDesc(desc = "谷歌纬度", isRequired = true) @QueryParam("latitude") String latitude,
                              @ParamDesc(desc = "是否使用实惠现金", isRequired = true) @QueryParam("costSh") int costSh,
                              @ParamDesc(desc = "实际支付金额", isRequired = true) @QueryParam("actPay") String actPay,
                              @ParamDesc(desc = "实际实惠现金抵扣", isRequired = true) @QueryParam("actOffset") String actOffset,
                              @ParamDesc(desc = "服务开始时间", isRequired = true) @QueryParam("serviceTime") String serviceTime,
                              @ParamDesc(desc = "联系人姓名", isRequired = true) @QueryParam("contactName") String contactName,
                              @ParamDesc(desc = "服务地址", isRequired = true) @QueryParam("serviceAddress") String serviceAddress,
                              @ParamDesc(desc = "详细地址", isRequired = true) @QueryParam("detailAddress") String detailAddress,
                              @ParamDesc(desc = "联系电话", isRequired = true) @QueryParam("servicePhone") String servicePhone,
                              @ParamDesc(desc = "支付类型", isRequired = true) @QueryParam("payType") int payType,
                              @ParamDesc(desc = "可选商户", isRequired = true) @QueryParam("merchants") String merchants,
                              @ParamDesc(desc = "备注信息", isRequired = false) @QueryParam("remark") String remark) {

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
        orderForm.setPayType(payType);
        orderForm.setMerchants(merchants);
        orderForm.setRemark(remark);
        return clientService.orderCreate(orderForm);
    }


}
