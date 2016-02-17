package com.shihui.openpf.home.resource;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.model.MerchantCategory;
import com.shihui.openpf.home.model.MerchantGoods;
import com.shihui.openpf.home.service.api.MerchantGoodsService;
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
 * Created by zhoutc on 2016/2/1.
 */
@Controller
@Path("/v2/openpf/home/merchant/goods")
public class MerchantGoodsResource {

    @Resource
    MerchantGoodsService merchantGoodsService;

    @Path("/list")
    @GET
    @BaseInfo(desc = "查询商户商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String list(
            @Context RequestContext rc,
            @ParamDesc(desc = "商户业务关联ID", isRequired = true) @QueryParam("m_s_c_id") int m_s_c_id
    ){
        MerchantGoods merchantGoods = new MerchantGoods();
        merchantGoods.setM_s_c_id(m_s_c_id);
        return JSON.toJSONString(merchantGoodsService.queryMerchantGoodsList(merchantGoods));
    }


    @Path("/update")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String update(
            @Context RequestContext rc,
            @ParamDesc(desc = "商户id", isRequired = false) @FormParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "商品id", isRequired = false) @FormParam("goods_id") Integer goods_id,
            @ParamDesc(desc = "商户商品开通状态", isRequired = false) @FormParam("status") Integer status,
            @ParamDesc(desc = "商户商品开通状态", isRequired = false) @FormParam("settlement") String settlement

    ){
        MerchantGoods merchantGoods = new MerchantGoods();
        merchantGoods.setMerchantId(merchant_id);
        merchantGoods.setGoodsId(goods_id);
        merchantGoods.setM_s_g_status(status);
        merchantGoods.setSettlement(settlement);
        return merchantGoodsService.updateMerchantGoods(merchantGoods);
    }

    @Path("/create")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String create(
            @Context RequestContext rc,
            @ParamDesc(desc = "商户id", isRequired = false) @FormParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "商品id", isRequired = false) @FormParam("goods_id") Integer goods_id,
            @ParamDesc(desc = "分类id", isRequired = false) @FormParam("category_id") Integer category_id,
            @ParamDesc(desc = "业务id", isRequired = false) @FormParam("service_id") Integer service_id,
            @ParamDesc(desc = "商户商品开通状态", isRequired = false) @FormParam("status") Integer status,
            @ParamDesc(desc = "结算价", isRequired = false) @FormParam("settlement") String settlement

    ){
        MerchantGoods merchantGoods = new MerchantGoods();
        merchantGoods.setCategoryId(category_id);
        merchantGoods.setMerchantId(merchant_id);
        merchantGoods.setGoodsId(goods_id);
        merchantGoods.setServiceId(service_id);
        merchantGoods.setM_s_g_status(status);
        merchantGoods.setSettlement(settlement);
        return merchantGoodsService.createMerchantGoods(merchantGoods);
    }
}
