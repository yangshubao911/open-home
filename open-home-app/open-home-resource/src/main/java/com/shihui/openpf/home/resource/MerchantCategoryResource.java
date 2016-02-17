package com.shihui.openpf.home.resource;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.model.MerchantCategory;
import com.shihui.openpf.home.service.api.MerchantCategoryService;
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
@Path("/v2/openpf/home/merchant/category")
public class MerchantCategoryResource {

    @Resource
    MerchantCategoryService merchantCategoryService;

    @Path("/list")
    @GET
    @BaseInfo(desc = "查询商户商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String list(
            @Context RequestContext rc,
            @ParamDesc(desc = "业务id", isRequired = true) @QueryParam("service_id") int service_id,
            @ParamDesc(desc = "商户id", isRequired = true) @QueryParam("merchant_id") int merchant_id
    ){
        MerchantCategory merchantCategory = new MerchantCategory();
        merchantCategory.setMerchantId(merchant_id);
        return JSON.toJSONString(merchantCategoryService.queryCategoryList(merchantCategory));
    }


    @Path("/update")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String update(
            @Context RequestContext rc,
            @ParamDesc(desc = "服务类型状态", isRequired = true) @FormParam("status") Integer status,
            @ParamDesc(desc = "商户id", isRequired = true) @FormParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "业务id", isRequired = true) @FormParam("service_id") Integer service_id,
            @ParamDesc(desc = "商品分类id", isRequired = true) @FormParam("category_id") Integer category_id

    ){
       MerchantCategory merchantCategory = new MerchantCategory();
        merchantCategory.setMerchantId(merchant_id);
        merchantCategory.setServiceId(service_id);
        merchantCategory.setCategoryId(category_id);
        merchantCategory.setM_s_c_status(status);
        return merchantCategoryService.updateCategory(merchantCategory);
    }

    @Path("/create")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String create(
            @Context RequestContext rc,
            @ParamDesc(desc = "服务类型状态", isRequired = true) @FormParam("status") Integer status,
            @ParamDesc(desc = "商户id", isRequired = true) @FormParam("merchant_id") Integer merchant_id,
            @ParamDesc(desc = "业务id", isRequired = true) @FormParam("service_id") Integer service_id,
            @ParamDesc(desc = "商品分类id", isRequired = true) @FormParam("category_id") Integer category_id

    ){
        MerchantCategory merchantCategory = new MerchantCategory();
        merchantCategory.setMerchantId(merchant_id);
        merchantCategory.setServiceId(service_id);
        merchantCategory.setCategoryId(category_id);
        merchantCategory.setM_s_c_status(status);
        return merchantCategoryService.create(merchantCategory);
    }

}
