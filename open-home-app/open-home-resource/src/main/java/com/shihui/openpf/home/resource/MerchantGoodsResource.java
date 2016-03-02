package com.shihui.openpf.home.resource;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.model.MerchantGoods;
import com.shihui.openpf.home.service.api.MerchantGoodsService;
import com.shihui.openpf.home.util.SimpleResponse;

import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;

/**
 * Created by zhoutc on 2016/2/1.
 */
@Controller
@Path("/v2/openpf/home/merchant/goods")
public class MerchantGoodsResource {
	private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    MerchantGoodsService merchantGoodsService;

    @Path("/list")
    @GET
    @BaseInfo(desc = "查询商户商品", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String list(
            @Context RequestContext rc,
            @ParamDesc(desc = "商户ID", isRequired = true) @QueryParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "业务ID", isRequired = true) @QueryParam("service_id") Integer service_id,
            @ParamDesc(desc = "商品分类ID", isRequired = false) @QueryParam("category_id") Integer category_id
    ){
        List<MerchantGoods> list;
		try {
			list = merchantGoodsService.findByConditions(merchant_id, service_id, category_id);
		} catch (Exception e) {
			log.error("查询商户商品遗产", e);
			return new SimpleResponse(1, "查询失败").toString();
		}
        return JSON.toJSONString(list);
    }


    @Path("/update")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String update(
            @Context RequestContext rc,
            @ParamDesc(desc = "商户id", isRequired = false) @FormParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "商品id", isRequired = false) @FormParam("goods_id") Long goods_id,
            @ParamDesc(desc = "商户商品开通状态", isRequired = false) @FormParam("status") Integer status,
            @ParamDesc(desc = "商户商品开通状态", isRequired = false) @FormParam("settlement") String settlement

    ){
        MerchantGoods merchantGoods = new MerchantGoods();
        merchantGoods.setMerchantId(merchant_id);
        merchantGoods.setGoodsId(goods_id);
        merchantGoods.setStatus(status);
        merchantGoods.setSettlement(settlement);
        return merchantGoodsService.updateMerchantGoods(merchantGoods);
    }

    @Path("/create")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String create(
            @Context RequestContext rc,
            @ParamDesc(desc = "商户id", isRequired = true) @FormParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "商品id", isRequired = true) @FormParam("goods_id") Long goods_id,
            @ParamDesc(desc = "分类id", isRequired = true) @FormParam("category_id") Integer category_id,
            @ParamDesc(desc = "业务id", isRequired = true) @FormParam("service_id") Integer service_id,
            @ParamDesc(desc = "商户商品开通状态", isRequired = true) @FormParam("status") Integer status,
            @ParamDesc(desc = "结算价", isRequired = true) @FormParam("settlement") String settlement

    ){
        MerchantGoods merchantGoods = new MerchantGoods();
        merchantGoods.setCategoryId(category_id);
        merchantGoods.setMerchantId(merchant_id);
        merchantGoods.setGoodsId(goods_id);
        merchantGoods.setServiceId(service_id);
        merchantGoods.setStatus(status);
        merchantGoods.setSettlement(settlement);
        return merchantGoodsService.createMerchantGoods(merchantGoods);
    }
    
    @Path("/batchAdd")
    @POST
    @BaseInfo(desc = "批量绑定商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String batchAdd(
            @Context RequestContext rc,
            @ParamDesc(desc = "data", isRequired = false) @FormParam("data") String json
    ){
        List<MerchantGoods> list;
		try {
			list = JSON.parseArray(json, MerchantGoods.class);
		} catch (Exception e) {
			log.error("批量添加商户商品参数转换异常 param={}", json, e);
			return new SimpleResponse(1, "参数格式错误").toString();
		}
        return merchantGoodsService.batchAddGoods(list);
    }
    
    @Path("/batchUpdate")
    @POST
    @BaseInfo(desc = "批量更新已绑定商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String batchUpdate(
            @Context RequestContext rc,
            @ParamDesc(desc = "data", isRequired = false) @FormParam("data") String json
    ){
        List<MerchantGoods> list;
		try {
			list = JSON.parseArray(json, MerchantGoods.class);
		} catch (Exception e) {
			log.error("批量更新商户商品参数转换异常 param={}", json, e);
			return new SimpleResponse(1, "参数格式错误").toString();
		}
        return merchantGoodsService.batchUpdateAddedGoods(list);
    }
}
