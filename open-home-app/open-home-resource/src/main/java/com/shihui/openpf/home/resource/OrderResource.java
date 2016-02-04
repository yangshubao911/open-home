/**
 * 
 */
package com.shihui.openpf.home.resource;

import com.alibaba.fastjson.JSON;
import com.shihui.openpf.home.api.OrderManage;
import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;
import org.apache.commons.fileupload.RequestContext;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
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
                             @ParamDesc(desc = "商品分类id", isRequired = true) @QueryParam("category_id") int categoryId,
                             @ParamDesc(desc = "城市id", isRequired = true) @QueryParam("city_id") int cityId) {

        //return orderManage.queryOrderList(rc,);
    	return null;

    }


}
