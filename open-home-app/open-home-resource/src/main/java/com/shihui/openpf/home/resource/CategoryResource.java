/**
 * 
 */
package com.shihui.openpf.home.resource;

import javax.annotation.Resource;
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
			@ParamDesc(desc = "商品类型图片", isRequired = true) @FormParam("img_url") String img_url,
			@ParamDesc(desc = "服务类型id", isRequired = true) @FormParam("service_id") Integer service_id
			){
    	Category category = new Category();
    	category.setDesc(desc);
    	category.setImgUrl(img_url);
    	category.setName(name);
    	category.setServiceId(service_id);
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
			@ParamDesc(desc = "商品类型图片", isRequired = false) @FormParam("img_url") String img_url,
			@ParamDesc(desc = "商品类型id", isRequired = true) @FormParam("id") Integer id,
			@ParamDesc(desc = "服务类型状态", isRequired = false) @FormParam("status") Integer status
			){
    	Category category = new Category();
    	category.setDesc(desc);
    	category.setImgUrl(img_url);
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

}
