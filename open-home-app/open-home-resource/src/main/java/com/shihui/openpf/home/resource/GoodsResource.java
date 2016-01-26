/**
 * 
 */
package com.shihui.openpf.home.resource;


import me.weimi.api.auth.annotations.AuthType;
import me.weimi.api.commons.context.RequestContext;
import me.weimi.api.swarm.annotations.ApiStatus;
import me.weimi.api.swarm.annotations.BaseInfo;
import me.weimi.api.swarm.annotations.ParamDesc;
import org.springframework.stereotype.Controller;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * @author zhouqisheng
 *
 * @version 1.0 Created at: 2016年1月19日 下午3:09:50
 */
@Controller
@Path("/v2/openpf/home")
public class GoodsResource {

	
    @Path("/create")
    @POST
    @BaseInfo(desc = "创建实惠应用", needAuth = AuthType.REQUIRED, status = ApiStatus.INTERNAL, crossDomain = true)
    @Produces({MediaType.APPLICATION_JSON})
	public long create(
			@Context RequestContext rc,
			@ParamDesc(desc = "商品信息", isRequired = true) @FormParam("goods_info") String goodsInfo
			){
		return 0l;
	}

}
