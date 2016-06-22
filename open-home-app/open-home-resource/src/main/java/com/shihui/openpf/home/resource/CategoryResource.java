/**
 * 
 */
package com.shihui.openpf.home.resource;

import javax.annotation.Resource;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.model.Category;
import com.shihui.openpf.home.service.api.CategoryService;

import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月21日 下午5:43:19
 */
@Controller
@Path("/v2/openpf/home/category")
public class CategoryResource {
	
	@Resource
	private CategoryService CategoryService;
	
    @Path("/create")
    @POST
    @BaseInfo(desc = "创建商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
	public String create(
			@Context RequestContext rc,
			@ParamDesc(desc = "商品类型描述", isRequired = true) @FormParam("desc") String desc,
			@ParamDesc(desc = "商品类型名称", isRequired = true) @FormParam("name") String name,
			@ParamDesc(desc = "商品类型图片", isRequired = true) @FormParam("image_id") String image_id,
			@ParamDesc(desc = "服务类型id", isRequired = true) @FormParam("service_id") Integer service_id,
			@ParamDesc(desc = "数量", isRequired = false) @FormParam("amount") @DefaultValue("1") Integer amount,
			@ParamDesc(desc = "产品编号", isRequired = true) @FormParam("product_id") String productId,
			@ParamDesc(desc = "商品类型状态", isRequired = true) @FormParam("status") Integer status
			){
    	Category category = new Category();
    	category.setDesc(desc);
    	category.setImageId(image_id);
    	category.setName(name);
    	category.setServiceId(service_id);
    	category.setStatus(status);
    	category.setAmount(amount);
    	category.setProductId(productId);
		return CategoryService.create(category);
    }
    
    @Path("/update")
    @POST
    @BaseInfo(desc = "更新商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
	public String update(
			@Context RequestContext rc,
			@ParamDesc(desc = "商品类型描述", isRequired = false) @FormParam("desc") String desc,
			@ParamDesc(desc = "商品类型名称", isRequired = false) @FormParam("name") String name,
			@ParamDesc(desc = "商品类型图片", isRequired = false) @FormParam("image_id") String image_id,
			@ParamDesc(desc = "商品类型id", isRequired = true) @FormParam("id") Integer id,
			@ParamDesc(desc = "数量", isRequired = false) @FormParam("amount") Integer amount,
			@ParamDesc(desc = "产品编号", isRequired = false) @FormParam("product_id") String productId,
			@ParamDesc(desc = "服务类型状态", isRequired = false) @FormParam("status") Integer status
			){
    	Category category = new Category();
    	category.setDesc(desc);
    	category.setImageId(image_id);
    	category.setName(name);
    	category.setId(id);
    	category.setStatus(status);
		return CategoryService.update(category);
    }
    
    @Path("/list")
    @GET
    @BaseInfo(desc = "查询商品分类", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
	public String list(
			@Context RequestContext rc,
			@ParamDesc(desc = "服务类型", isRequired = true) @QueryParam("service_id") int service_id
			){
		return JSON.toJSONString(CategoryService.list(service_id));
    }
    
    @Path("/rank/list")
    @GET
    @BaseInfo(desc = "查询商品分类排序", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
	public String rankList(
			@Context RequestContext rc,
			@ParamDesc(desc = "服务类型", isRequired = true) @QueryParam("service_id") int serviceId
			){
		return JSON.toJSONString(CategoryService.rankList(serviceId));
    }
    
    @Path("/rank/update")
    @GET
    @BaseInfo(desc = "更新商品分类排序", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
	public String rankUpdate(
			@Context RequestContext rc,
			@ParamDesc(desc = "服务类型", isRequired = true) @QueryParam("service_id") int serviceId,
			@ParamDesc(desc = "商品类型id数组", isRequired = true) @QueryParam("category_ids") String categoryIds
			){
		return CategoryService.rankUpdate(serviceId, categoryIds);
    }


}
