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
import com.shihui.openpf.home.model.MerchantCategory;
import com.shihui.openpf.home.service.api.MerchantCategoryService;
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
@Path("/v2/openpf/home/merchant/category")
public class MerchantCategoryResource {

	private Logger log = LoggerFactory.getLogger(getClass());
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
        merchantCategory.setStatus(status);
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
        merchantCategory.setStatus(status);
        return merchantCategoryService.create(merchantCategory);
    }
    
    @Path("/batchAdd")
    @POST
    @BaseInfo(desc = "批量绑定商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
    public String batchAdd(
            @Context RequestContext rc,
            @ParamDesc(desc = "data", isRequired = true) @FormParam("data") String json
    ){
        List<MerchantCategory> merchantCategorys;
		try {
			merchantCategorys = JSON.parseArray(json, MerchantCategory.class);
		} catch (Exception e) {
			log.error("批量绑定商品分类参数错误", e);
			return JSON.toJSONString(new SimpleResponse(1,"参数格式错误"));
		}
        return merchantCategoryService.batchCreate(merchantCategorys);
    }

}
